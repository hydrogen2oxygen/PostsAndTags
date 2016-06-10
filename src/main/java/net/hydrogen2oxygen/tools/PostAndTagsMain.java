package net.hydrogen2oxygen.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class PostAndTagsMain implements IConstants {

   private static final String UTF_8 = "UTF-8";

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
      String targetDirectory = properties.getProperty(IConstants.TARGET_DIRECTORY);

      addFilesFromFolder(sourceDirectory);

      File templateFile = new File(properties.getProperty(IConstants.TEMPLATE_FILE));
      String template = FileUtils.readFileToString(templateFile, UTF_8);

      File tagTtemplateFile = new File(properties.getProperty(IConstants.TAG_TEMPLATE_FILE));
      String tagTemplate = FileUtils.readFileToString(tagTtemplateFile, UTF_8);

      // Extract the tags and store them in memory (provide enough for your
      // instance with VM parameters!)
      for (File file : dataCollector.getListOfFiles()) {

         String content = FileUtils.readFileToString(file, UTF_8);
         Pattern p = Pattern.compile("\\[[A-z]+\\]");
         Matcher m = p.matcher(content);

         while (m.find()) {
            String tagWithSquareBrackets = content.substring(m.start(), m.end());
            String tag = tagWithSquareBrackets.toLowerCase().replaceAll("\\[", "").replaceAll("\\]", "").trim();

            dataCollector.addTag(tag);
         }

      }

      // Start generating the target html files
      for (File file : dataCollector.getListOfFiles()) {

         String content = FileUtils.readFileToString(file, UTF_8);

         for (String tag : dataCollector.getListOfTags()) {
            content = content.replaceAll("\\[" + tag + "]", generateLink(tag));
         }

         content = template.replaceAll("###CONTENT###", content);

         File targetFile = new File(targetDirectory + "/" + file.getName());
         FileUtils.writeStringToFile(targetFile, content, UTF_8);
      }

      // Now in a second run generate the tags html
      for (String tag : dataCollector.getListOfTags()) {

         File tagFile = new File(targetDirectory + "/" + tag + ".html");

         if (!tagFile.exists()) {

            String content = generateTagContent(tag, tagTemplate, template);
            File targetFile = new File(targetDirectory + "/" + tag + ".html");
            FileUtils.writeStringToFile(targetFile, content, UTF_8);
         }
      }

      // Generate a protocol also as a html file inside the folder for
      // protocol
   }

   private String generateTagContent(String tag, String tagTemplate, String template) {

      String tagContent = tagTemplate.replaceAll("###TAG###", tag.toUpperCase());
      String content = template.replaceAll("###CONTENT###", tagContent).replaceAll("###TITLE###", tag.toUpperCase());
      return content;
   }

   private String generateLink(String tag) {

      String link = "<a href=\"" + tag + ".html\">" + tag + "</a>";
      return link;
   }

   private void addFilesFromFolder(File folder) {

      for (File file : folder.listFiles()) {

         if (file.isFile()) {

            dataCollector.addFile(file);
         }

         // Recursively move into sub folders
         if (file.isDirectory()) {
            addFilesFromFolder(file);
         }
      }
   }

   private void initProperties(String propertiesFile) throws FileNotFoundException, IOException {

      FileInputStream in = new FileInputStream(propertiesFile);
      properties.load(in);
   }

   /**
    * This method will create a folder structure with default templates for your convenience
    *
    * @param projectName
    */
   private void createNewProject(String projectName) {

   }
}
