package net.hydrogen2oxygen.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataCollector {

	private List<File> listOfFiles = new ArrayList<File>();

	public List<File> getListOfFiles() {
		return listOfFiles;
	}

	public void addFile(File file) {
		listOfFiles.add(file);
	}

}
