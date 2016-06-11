package net.hydrogen2oxygen.tools;

import org.junit.Test;

public class PostsAndTagsTest {

	private static final String TEST_PROPERTIES = "src/test/resources/test.properties";

	@Test
	public void test() throws Exception {

		String arguments[] = { TEST_PROPERTIES };
		PostAndTagsMain.main(arguments);
	}

}
