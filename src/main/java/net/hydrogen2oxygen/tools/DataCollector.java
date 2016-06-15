package net.hydrogen2oxygen.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataCollector {

	private List<File> listOfFiles = new ArrayList<File>();

	private HashMap<String, List<String>> tagsForPost = new HashMap<String, List<String>>();

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

	/**
	 * The other way around: return each post where the tag was included.
	 * 
	 * @param tag
	 * @return
	 */
	public List<String> getPostForTag(String tag) {

		List<String> posts = new ArrayList<String>();

		for (String post : tagsForPost.keySet()) {

			if (tagsForPost.get(post).contains(tag)) {
				posts.add(post.replace(".html", ""));
			}
		}

		return posts;
	}

	/**
	 * Get all tags from a specific post.
	 * 
	 * @param post
	 * @return
	 */
	public List<String> getTagsForPost(String post) {

		if (tagsForPost.containsKey(post)) {
			return tagsForPost.get(post);
		}

		return new ArrayList<String>();
	}

	public void addTagForPost(String post, String tag) {

		if (tagsForPost.get(post) != null) {

			List<String> tagList = tagsForPost.get(post);

			if (!tagList.contains(tag)) {
				tagList.add(tag);
				tagsForPost.put(post, tagList);
			}

		} else {
			List<String> newTagList = new ArrayList<String>();
			newTagList.add(tag);
			tagsForPost.put(post, newTagList);
		}
	}

}
