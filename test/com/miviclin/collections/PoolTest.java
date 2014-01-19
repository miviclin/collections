package com.miviclin.collections;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class PoolTest {

	public static class EmptyPoolFixture {

		private Pool<TestUser> pool;

		@Before
		public void initialize() {
			pool = new TestUserPool();
		}

		@Test
		public void initialized_shouldHaveZeroElements() throws Exception {
			assertEquals(0, pool.size());
		}

		@Test
		public void testUserObtained_shouldHaveZeroElements() throws Exception {
			pool.obtain();
			assertEquals(0, pool.size());
		}

		@Test
		public void testUserObtained_shouldReturnTestUserWithDefaultNameAndPassword() throws Exception {
			TestUser testUser = pool.obtain();
			assertEquals(TestUser.DEFAULT_NAME, testUser.getName());
			assertEquals(TestUser.DEFAULT_PASSWORD, testUser.getPassword());
		}

		@Test
		public void testUserRecycled_shouldHaveOneElement() throws Exception {
			TestUser testUser = new TestUser("testName", "testPassword");
			pool.recycle(testUser);
			assertEquals(1, pool.size());
		}

		@Test
		public void testUserRecycledAndThenObtained_shouldReturnTheRecycledElement() throws Exception {
			TestUser testUser = new TestUser("testName", "testPassword");
			pool.recycle(testUser);
			TestUser obtainedTestUser = pool.obtain();
			assertEquals(testUser, obtainedTestUser);
		}
	}

	public static class NonEmptyPoolFixture {

		private LinkedList<TestUser> testUsersList;
		private Pool<TestUser> pool;

		@Before
		public void initialize() {
			testUsersList = new LinkedList<>();
			testUsersList.add(new TestUser("name1", "password1"));
			testUsersList.add(new TestUser("name2", "password2"));
			testUsersList.add(new TestUser("name3", "password3"));
			pool = new TestUserPool();
			for (TestUser testUser : testUsersList) {
				pool.recycle(testUser);
			}
		}

		@Test
		public void initialized_shouldHaveTheSameSizeAsTestUsersList() throws Exception {
			assertEquals(testUsersList.size(), pool.size());
		}

		@Test
		public void testUserObtained_shouldReturnLastTestUserOfTestUsersList() throws Exception {
			TestUser testUser = pool.obtain();
			assertEquals(testUsersList.getLast(), testUser);
		}

		@Test
		public void testUserRecycled_shouldIncrementSizeInOne() throws Exception {
			int initialSize = pool.size();
			pool.recycle(new TestUser("testName", "testPassword"));
			int currentSize = pool.size();
			assertEquals(initialSize + 1, currentSize);
		}

		@Test
		public void testUserObtained_shouldDecrementSizeInOne() throws Exception {
			int initialSize = pool.size();
			pool.obtain();
			int currentSize = pool.size();
			assertEquals(initialSize - 1, currentSize);
		}

		@Test
		public void testUserRecycledAndThenObtained_shouldReturnTheRecycledElement() throws Exception {
			TestUser testUser = new TestUser("testName", "testPassword");
			pool.recycle(testUser);
			TestUser obtainedTestUser = pool.obtain();
			assertEquals(testUser, obtainedTestUser);
		}

		@Test
		public void testUserRecycledAndThenObtained_shouldReturnAnUnresettedElement() throws Exception {
			TestUser testUser = new TestUser("testName", "testPassword");
			pool.recycle(testUser);
			TestUser obtainedTestUser = pool.obtain();
			assertEquals(testUser.getName(), obtainedTestUser.getName());
			assertEquals(testUser.getPassword(), obtainedTestUser.getPassword());
		}
	}

	public static class EmptyPoolOfPoolableElementsFixture {

		private Pool<PoolableTestUser> pool;

		@Before
		public void initialize() {
			pool = new PoolableTestUserPool();
		}

		@Test
		public void initialized_shouldHaveZeroElements() throws Exception {
			assertEquals(0, pool.size());
		}

		@Test
		public void testUserObtained_shouldHaveZeroElements() throws Exception {
			pool.obtain();
			assertEquals(0, pool.size());
		}

		@Test
		public void testUserObtained_shouldReturnTestUserWithResettedNameAndPassword() throws Exception {
			PoolableTestUser testUser = pool.obtain();
			assertEquals(PoolableTestUser.DEFAULT_RESETTED_NAME, testUser.getName());
			assertEquals(PoolableTestUser.DEFAULT_RESETTED_PASSWORD, testUser.getPassword());
		}

		@Test
		public void testUserRecycled_shouldHaveOneElement() throws Exception {
			PoolableTestUser testUser = new PoolableTestUser("testName", "testPassword");
			pool.recycle(testUser);
			assertEquals(1, pool.size());
		}

		@Test
		public void testUserRecycledAndThenObtained_shouldReturnTheRecycledElement() throws Exception {
			PoolableTestUser testUser = new PoolableTestUser("testName", "testPassword");
			pool.recycle(testUser);
			TestUser obtainedTestUser = pool.obtain();
			assertEquals(testUser, obtainedTestUser);
		}
	}

	public static class NonEmptyPoolOfPoolableElementsFixture {

		private LinkedList<PoolableTestUser> testUsersList;
		private Pool<PoolableTestUser> pool;

		@Before
		public void initialize() {
			testUsersList = new LinkedList<>();
			testUsersList.add(new PoolableTestUser("name1", "password1"));
			testUsersList.add(new PoolableTestUser("name2", "password2"));
			testUsersList.add(new PoolableTestUser("name3", "password3"));
			pool = new PoolableTestUserPool();
			for (PoolableTestUser testUser : testUsersList) {
				pool.recycle(testUser);
			}
		}

		@Test
		public void initialized_shouldHaveTheSameSizeAsTestUsersList() throws Exception {
			assertEquals(testUsersList.size(), pool.size());
		}

		@Test
		public void testUserObtained_shouldReturnLastTestUserOfTestUsersList() throws Exception {
			PoolableTestUser testUser = pool.obtain();
			assertEquals(testUsersList.getLast(), testUser);
		}

		@Test
		public void testUserRecycled_shouldIncrementSizeInOne() throws Exception {
			int initialSize = pool.size();
			pool.recycle(new PoolableTestUser("testName", "testPassword"));
			int currentSize = pool.size();
			assertEquals(initialSize + 1, currentSize);
		}

		@Test
		public void testUserObtained_shouldDecrementSizeInOne() throws Exception {
			int initialSize = pool.size();
			pool.obtain();
			int currentSize = pool.size();
			assertEquals(initialSize - 1, currentSize);
		}

		@Test
		public void testUserRecycledAndThenObtained_shouldReturnTheRecycledElement() throws Exception {
			PoolableTestUser testUser = new PoolableTestUser("testName", "testPassword");
			pool.recycle(testUser);
			TestUser obtainedTestUser = pool.obtain();
			assertEquals(testUser, obtainedTestUser);
		}

		@Test
		public void testUserRecycledAndThenObtained_shouldReturnAResettedElement() throws Exception {
			PoolableTestUser testUser = new PoolableTestUser(TestUser.DEFAULT_NAME, TestUser.DEFAULT_PASSWORD);
			pool.recycle(testUser);
			PoolableTestUser obtainedTestUser = pool.obtain();
			assertEquals(PoolableTestUser.DEFAULT_RESETTED_NAME, obtainedTestUser.getName());
			assertEquals(PoolableTestUser.DEFAULT_RESETTED_PASSWORD, obtainedTestUser.getPassword());
		}
	}

	static class TestUserPool extends Pool<TestUser> {

		@Override
		public TestUser createObject() {
			return new TestUser(TestUser.DEFAULT_NAME, TestUser.DEFAULT_PASSWORD);
		}

	}

	static class PoolableTestUserPool extends Pool<PoolableTestUser> {

		@Override
		public PoolableTestUser createObject() {
			return new PoolableTestUser(PoolableTestUser.DEFAULT_RESETTED_NAME,
					PoolableTestUser.DEFAULT_RESETTED_PASSWORD);
		}

	}

	static class TestUser {

		public static String DEFAULT_NAME = "username";
		public static String DEFAULT_PASSWORD = "password";

		private String name;
		private String password;

		public TestUser(String name, String password) {
			super();
			this.name = name;
			this.password = password;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

	static class PoolableTestUser extends TestUser implements Pool.Poolable {

		public static String DEFAULT_RESETTED_NAME = "resetted name";
		public static String DEFAULT_RESETTED_PASSWORD = "resetted password";

		public PoolableTestUser(String name, String password) {
			super(name, password);
		}

		@Override
		public void reset() {
			setName(DEFAULT_RESETTED_NAME);
			setPassword(DEFAULT_RESETTED_PASSWORD);
		}

	}

}
