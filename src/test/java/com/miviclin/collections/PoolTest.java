/*   Copyright 2014 Miguel Vicente Linares
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.miviclin.collections;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import junitparams.JUnitParamsRunner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class PoolTest {

	@Test
	public void createEmptyPool_byDefault_sizeIsZero() throws Exception {
		Pool<TestUser> pool = createEmptyPoolOfTestUsers();

		int size = pool.size();

		Assert.assertEquals(0, size);
	}

	@Test
	public void createEmptyPool_byDefault_isEmpty() throws Exception {
		Pool<TestUser> pool = createEmptyPoolOfTestUsers();

		boolean empty = pool.isEmpty();

		Assert.assertTrue(empty);
	}

	@Test
	public void obtain_emptyPool_hasZeroElements() throws Exception {
		Pool<TestUser> pool = createEmptyPoolOfTestUsers();

		pool.obtain();
		int size = pool.size();

		Assert.assertEquals(0, size);
	}

	@Test
	public void obtain_emptyPool_returnsNewObject() throws Exception {
		Pool<TestUser> pool = createEmptyPoolOfTestUsers();

		TestUser testUser = pool.obtain();
		String testUserName = testUser.getName();
		String testUserPassword = testUser.getPassword();

		assertEquals(TestUser.DEFAULT_NAME, testUserName);
		assertEquals(TestUser.DEFAULT_PASSWORD, testUserPassword);
	}

	@Test
	public void recycle_emptyPool_hasOneElement() throws Exception {
		Pool<TestUser> pool = createEmptyPoolOfTestUsers();

		TestUser testUser = new TestUser("testName", "testPassword");
		pool.recycle(testUser);
		int size = pool.size();

		assertEquals(1, size);
	}

	private static Pool<TestUser> createEmptyPoolOfTestUsers() {
		return new TestUserPool();
	}

	public static class EmptyPoolFixture {

		private Pool<TestUser> pool;

		@Before
		public void givenAnEmptyPoolOfTestUsers() {
			pool = new TestUserPool();
		}

		@Test(expected = IllegalArgumentException.class)
		public void whenANullObjectIsRecycled_thenAnIllegalArgumentExceptionShouldBeThrown() throws Exception {
			pool.recycle(null);
		}

		@Test
		public void whenATestUserIsRecycledAndThenObtained_thenTheReturnedElementShouldBeTheRecycledElement()
				throws Exception {

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
		public void givenANonEmptyPoolOfTestUsers() {
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
		public void whenInitialized_thenThePoolShouldHaveTheSameSizeAsTestUsersList() throws Exception {
			assertEquals(testUsersList.size(), pool.size());
		}

		@Test
		public void whenInitialized_thenThePoolShouldNotBeEmpty() throws Exception {
			assertEquals(false, pool.isEmpty());
		}

		@Test
		public void whenThePoolIsCleared_thenThePoolShouldBeEmpty() throws Exception {
			pool.clear();
			assertEquals(true, pool.isEmpty());
		}

		@Test
		public void whenATestUserIsObtained_thenTheReturnedTestUserShouldBeTheLastTestUserOfTestUsersList()
				throws Exception {

			TestUser testUser = pool.obtain();
			assertEquals(testUsersList.getLast(), testUser);
		}

		@Test(expected = IllegalArgumentException.class)
		public void whenANullObjectIsRecycled_thenAnIllegalArgumentExceptionShouldBeThrown() throws Exception {
			pool.recycle(null);
		}

		@Test
		public void whenATestUserIsRecycled_thenThePoolShouldIncrementItsSizeInOne() throws Exception {
			int initialSize = pool.size();
			pool.recycle(new TestUser("testName", "testPassword"));
			int currentSize = pool.size();
			assertEquals(initialSize + 1, currentSize);
		}

		@Test
		public void whenATestUserIsObtained_thenThePoolShouldDecrementItsSizeInOne() throws Exception {
			int initialSize = pool.size();
			pool.obtain();
			int currentSize = pool.size();
			assertEquals(initialSize - 1, currentSize);
		}

		@Test
		public void whenATestUserIsRecycledAndThenObtained_thenTheReturnedElementShouldBeTheRecycledElement()
				throws Exception {

			TestUser testUser = new TestUser("testName", "testPassword");
			pool.recycle(testUser);
			TestUser obtainedTestUser = pool.obtain();
			assertEquals(testUser, obtainedTestUser);
		}

		@Test
		public void whenATestUserIsRecycledAndThenObtained_thenTheReturnedElementShouldBeAnUnresettedElement()
				throws Exception {

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
		public void givenAnEmptyPoolOfPoolableTestUsers() {
			pool = new PoolableTestUserPool();
		}

		@Test
		public void whenInitialized_thenThePoolShouldHaveZeroElements() throws Exception {
			assertEquals(0, pool.size());
		}

		@Test
		public void whenInitialized_thenThePoolShouldBeEmpty() throws Exception {
			assertEquals(true, pool.isEmpty());
		}

		@Test
		public void whenATestUserIsObtained_thenThePoolShouldHaveZeroElements() throws Exception {
			pool.obtain();
			assertEquals(0, pool.size());
		}

		@Test
		public void whenATestUserIsObtained_thenTheReturnedTestUserShouldHaveResettedNameAndPassword() throws Exception {
			PoolableTestUser testUser = pool.obtain();
			assertEquals(PoolableTestUser.DEFAULT_RESETTED_NAME, testUser.getName());
			assertEquals(PoolableTestUser.DEFAULT_RESETTED_PASSWORD, testUser.getPassword());
		}

		@Test
		public void whenATestUserIsRecycled_thenThePoolShouldHaveOneElement() throws Exception {
			PoolableTestUser testUser = new PoolableTestUser("testName", "testPassword");
			pool.recycle(testUser);
			assertEquals(1, pool.size());
		}

		@Test
		public void whenATestUserIsRecycledAndThenObtained_ThenTheReturnedElementShouldBeTheRecycledElement()
				throws Exception {

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
		public void givenANonEmptyPoolOfPoolableTestUsers() {
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
		public void whenInitialized_thenThePoolShouldHaveTheSameSizeAsTestUsersList() throws Exception {
			assertEquals(testUsersList.size(), pool.size());
		}

		@Test
		public void whenInitialized_thenThePoolShouldNotBeEmpty() throws Exception {
			assertEquals(false, pool.isEmpty());
		}

		@Test
		public void whenThePoolIsCleared_thenThePoolShouldBeEmpty() throws Exception {
			pool.clear();
			assertEquals(true, pool.isEmpty());
		}

		@Test
		public void whenATestUserIsObtained_thenTheReturnedElementShouldBeTheLastTestUserOfTestUsersList()
				throws Exception {

			PoolableTestUser testUser = pool.obtain();
			assertEquals(testUsersList.getLast(), testUser);
		}

		@Test
		public void whenATestUserIsRecycled_thenThePoolShouldIncrementItsSizeInOne() throws Exception {
			int initialSize = pool.size();
			pool.recycle(new PoolableTestUser("testName", "testPassword"));
			int currentSize = pool.size();
			assertEquals(initialSize + 1, currentSize);
		}

		@Test
		public void whenATestUserIsObtained_thenThePoolShouldDecrementItsSizeInOne() throws Exception {
			int initialSize = pool.size();
			pool.obtain();
			int currentSize = pool.size();
			assertEquals(initialSize - 1, currentSize);
		}

		@Test
		public void whenATestUserIsRecycledAndThenObtained_thenTheReturnedElementShouldBeTheRecycledElement()
				throws Exception {

			PoolableTestUser testUser = new PoolableTestUser("testName", "testPassword");
			pool.recycle(testUser);
			TestUser obtainedTestUser = pool.obtain();
			assertEquals(testUser, obtainedTestUser);
		}

		@Test
		public void whenATestUserIsRecycledAndThenObtained_thenTheReturnedElementShouldBeAResettedElement()
				throws Exception {

			PoolableTestUser testUser = new PoolableTestUser(TestUser.DEFAULT_NAME, TestUser.DEFAULT_PASSWORD);
			pool.recycle(testUser);
			PoolableTestUser obtainedTestUser = pool.obtain();
			assertEquals(PoolableTestUser.DEFAULT_RESETTED_NAME, obtainedTestUser.getName());
			assertEquals(PoolableTestUser.DEFAULT_RESETTED_PASSWORD, obtainedTestUser.getPassword());
		}
	}

	private static class TestUserPool extends Pool<TestUser> {

		@Override
		public TestUser createObject() {
			return new TestUser(TestUser.DEFAULT_NAME, TestUser.DEFAULT_PASSWORD);
		}

	}

	private static class PoolableTestUserPool extends Pool<PoolableTestUser> {

		@Override
		public PoolableTestUser createObject() {
			return new PoolableTestUser(PoolableTestUser.DEFAULT_RESETTED_NAME,
					PoolableTestUser.DEFAULT_RESETTED_PASSWORD);
		}

	}

	private static class TestUser {

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

	private static class PoolableTestUser extends TestUser implements Pool.Poolable {

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
