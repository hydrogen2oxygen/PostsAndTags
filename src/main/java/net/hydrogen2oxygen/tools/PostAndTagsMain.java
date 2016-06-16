package net.hydrogen2oxygen.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class PostAndTagsMain implements IConstants {

	private static final String UTF_8 = "UTF-8";

	private Properties properties = new Properties();

	private DataCollector dataCollector = new DataCollector();

	private String template;

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.out.println("At least provide a properties file as first parameter!");
			System.exit(0);

		}

		if (args.length == 1) {
			String propertiesFile = args[0];
			new PostAndTagsMain(propertiesFile);
			return;
		}

		System.err.println("I don't know this syntax. Too many params.");

	}

	private PostAndTagsMain(String propertiesFile) throws Exception {

		// Init configurations
		initProperties(propertiesFile);

		// First collect all files inside a source folder
		File sourceDirectory = new File(properties.getProperty(SOURCE_DIRECTORY));
		String targetDirectory = properties.getProperty(TARGET_DIRECTORY);
		File targetDirectoryFile = new File(targetDirectory);

		// Clean target directory first
		PostAndTagsMain.cleanFolder(targetDirectoryFile);

		addFilesFromFolder(sourceDirectory);

		File templateFile = new File(properties.getProperty(TEMPLATE_FILE));
		template = FileUtils.readFileToString(templateFile, UTF_8);

		File staticResourcesDirectory = new File(properties.getProperty(STATIC_RESOURCES));

		// Extract the tags and store them in memory (provide enough for your
		// instance with VM parameters!)
		for (File file : dataCollector.getListOfFiles()) {

			String content = FileUtils.readFileToString(file, UTF_8);
			extractTagsFromContent(file, content, "\\[\\s?[\\sA-Za-z]+]");
			extractTagsFromContent(file, content, "\\[\\s?[\\sA-Za-z][0-9]+]");
		}

		// Start generating the target html files
		for (File file : dataCollector.getListOfFiles()) {

			String content = FileUtils.readFileToString(file, UTF_8);
			String title = FileUtils.readLines(file, UTF_8).get(0);

			content = content.replace(title, "");

			for (String tag : dataCollector.getListOfTags()) {
				content = content.replaceAll("\\[" + tag + "]", generateLink(tag));
			}

			StringBuffer tags = new StringBuffer();
			List<String> tagListForFile = dataCollector.getTagsForPost(file.getName());

			for (String tag : tagListForFile) {

				if (tags.length() > 0) {
					tags.append(", ");
				}

				tags.append(generateLink(tag.toLowerCase()));
			}

			String fileContent = generateTagContent(title, content, tags.toString());

			File targetFile = new File(targetDirectory + "/" + file.getName());
			FileUtils.writeStringToFile(targetFile, fileContent, UTF_8);
		}

		// Now in a second run generate the tags html
		for (String tag : dataCollector.getListOfTags()) {

			File tagFile = new File(targetDirectory + "/" + tag + ".html");

			if (!tagFile.exists()) {

				StringBuffer posts = new StringBuffer();
				List<String> tagListForFile = dataCollector.getPostForTag(tag);

				for (String post : tagListForFile) {

					if (posts.length() > 0) {
						posts.append(", ");
					}

					posts.append(generateLink(post.toLowerCase()));
				}

				String content = generateTagContent(tag, "", posts.toString());
				File targetFile = new File(targetDirectory + "/" + tag.toLowerCase() + ".html");
				FileUtils.writeStringToFile(targetFile, content, UTF_8);
			}
		}

		// Copy the content of the provided staticResources to the target folder
		for (File file : staticResourcesDirectory.listFiles()) {

			if (file.isDirectory()) {
				FileUtils.copyDirectoryToDirectory(file, targetDirectoryFile);
			}
		}

		// Generate a protocol also as a html file inside the folder for
		// protocol
	}

	private void extractTagsFromContent(File file, String content, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);

		while (m.find()) {
			String tagWithSquareBrackets = content.substring(m.start(), m.end());

			if (StringUtils.countMatches(tagWithSquareBrackets, "[") > 1) {
				System.err.println("ERROR: Regular did miss a specific pattern: " + tagWithSquareBrackets);
				System.exit(0);
			}

			String tag = tagWithSquareBrackets.replaceAll("\\[", "").replaceAll("\\]", "").trim();

			dataCollector.addTag(tag);
			dataCollector.addTagForPost(file.getName(), tag);
		}
	}

	private String generateTagContent(String title, String content, String tags) {

		if (title.equals(title.toLowerCase())) {
			title = title.substring(0, 1).toUpperCase() + title.substring(1);
		}

		content = template.replaceAll("###CONTENT###", content).replaceAll("###TAGS###", tags.toString())
		        .replaceAll("###TITLE###", title).replaceAll("###PROJECTNAME###", properties.getProperty(PROJECT_NAME))
		        .replaceAll("###ABOUT###", properties.getProperty(ABOUT_LINK))
		        .replaceAll("###CONTACT###", properties.getProperty(CONTACT_LINK));

		return content;
	}

	private String generateLink(String tag) {

		String link = "<a href=\"" + tag.toLowerCase() + ".html\">" + tag + "</a>";
		return link;
	}

	private void addFilesFromFolder(File folder) {

		for (File file : folder.listFiles()) {

			if (file.isFile()) {

				dataCollector.addFile(file);
			}

			// Recursively move into sub folders
			if (file.isDirectory()) {
				// TODO: not yet, we need a clear idea why we should allow
				// sub-folders
				// addFilesFromFolder(file);
			}
		}
	}

	private void initProperties(String propertiesFile) throws FileNotFoundException, IOException {

		FileInputStream in = new FileInputStream(propertiesFile);
		properties.load(in);
	}

	/**
	 * This method will create a folder structure with default templates for
	 * your convenience
	 *
	 * @param projectName
	 */
	public static void createNewProject(String projectName) {
		// TODO: this is a nice feature
	}

	public static void cleanFolder(File folder) throws IOException {
		if (folder.exists() && folder.isDirectory()) {
			System.out.println("Target exist. I will clean it first.");

			for (File file : folder.listFiles()) {
				if (file.isFile()) {

					System.out.println("... deleting " + file.getName());
					file.delete();
				} else {
					System.out.println("... deleting " + file.getName());
					FileUtils.deleteDirectory(file);
				}
			}
		}
	}
}
