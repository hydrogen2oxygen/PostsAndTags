package net.hydrogen2oxygen.tools;

import java.io.File;

import org.junit.Test;

public class PostsAndTagsTest {

	private static final String TEST_PROPERTIES = "src/test/resources/test.properties";
	private static final String TARGET = "src/test/resources/target";

	@Test
	public void test() throws Exception {

		File target = new File(TARGET);

		// Clean target directory first
		cleanTargetFolder(target);

		String arguments[] = { TEST_PROPERTIES };
		PostAndTagsMain.main(arguments);
	}

	private void cleanTargetFolder(File target) {
		if (target.exists()) {
			System.out.println("Target exist. I will clean it first.");

			for (File file : target.listFiles()) {
				if (file.isFile()) {

					System.out.println("... deleting " + file.getName());
					file.delete();
				}
			}
		}
	}
}
