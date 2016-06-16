package net.hydrogen2oxygen.tools;

import org.junit.Test;

public class PostsAndTagsTest {

	private static final String TEST_PROPERTIES = "src/test/resources/test.properties";

	/**
	 * TODO: before you allow collaboration within this project add a lot of
	 * asserts in order that the basic functionality is not changed
	 * 
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {

		String arguments[] = { TEST_PROPERTIES };
		PostAndTagsMain.main(arguments);
	}

}
