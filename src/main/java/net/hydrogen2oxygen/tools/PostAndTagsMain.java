package net.hydrogen2oxygen.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PostAndTagsMain implements IConstants {

	private Properties properties = new Properties();
	private DataCollector dataCollector = new DataCollector();

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.out.println("At least provide a properties file as first parameter!");
			System.exit(0);

		}

		String propertiesFile = args[0];
		IConstants postsAndTags = new PostAndTagsMain(propertiesFile);

	}

	private PostAndTagsMain(String propertiesFile) throws Exception {

		// Init configurations
		initProperties(propertiesFile);

		// First collect all files inside a source folder
		File sourceDirectory = new File(properties.getProperty(IConstants.SOURCE_DIRECTORY));

		for (File file : sourceDirectory.listFiles()) {
			System.out.println(file.getAbsolutePath());
		}

		// Extract the tags and store them in memory (provide enough for your
		// instance with VM parameters!)

		// Start generating the target html files

		// Generate a protocol also as a html file inside the folder for
		// protocol
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
	private void createNewProject(String projectName) {

	}
}
