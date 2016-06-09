package net.hydrogen2oxygen.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataCollector {

	private List<File> listOfFiles = new ArrayList<File>();

	private List<String> listOfTags = new ArrayList<String>();

	public List<File> getListOfFiles() {
		return listOfFiles;
	}

	public void addFile(File file) {

		if (!listOfFiles.contains(file)) {
			listOfFiles.add(file);
			System.out.println("adding file " + file.getName());
		}
	}

	public List<String> getListOfTags() {
		return listOfTags;
	}

	public String addTag(String tag) {

		if (!listOfTags.contains(tag)) {
			listOfTags.add(tag);
			System.out.println("adding tag " + tag);

			return tag;
		}

		return null;
	}

}
