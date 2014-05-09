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

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class PoolTests {

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

		Assert.assertEquals(TestUser.DEFAULT_NAME, testUserName);
		Assert.assertEquals(TestUser.DEFAULT_PASSWORD, testUserPassword);
	}

	@Test
	@Parameters({ "1", "3" })
	public void obtain_poolWithOneOrMoreElements_sizeIsDecrementedByOne(int poolSize) throws Exception {
		Pool<TestUser> pool = createPoolOfTestUsers(poolSize);

		int sizeBeforeObtain = pool.size();
		pool.obtain();
		int sizeDecrement = sizeBeforeObtain - pool.size();

		Assert.assertEquals(1, sizeDecrement);
	}

	@Test
	@Parameters({ "0", "1", "3" })
	public void obtain_afterRecyclingElement_returnsTheRecycledElement(int poolSize) throws Exception {
		Pool<TestUser> pool = createPoolOfTestUsers(poolSize);

		TestUser testUser = new TestUser("testName", "testPassword");
		pool.recycle(testUser);
		TestUser obtainedTestUser = pool.obtain();

		Assert.assertEquals(testUser, obtainedTestUser);
	}

	@Test
	@Parameters({ "1", "3" })
	public void obtain_nonPoolableObjectsPoolWithOneOrMoreElements_returnsAnUnresettedElement(int poolSize)
			throws Exception {

		Pool<TestUser> pool = createPoolOfTestUsers(poolSize);

		String testUserName = "testName";
		String testUserPassword = "testPassword";
		TestUser testUser = new TestUser(testUserName, testUserPassword);
		pool.recycle(testUser);
		TestUser obtainedTestUser = pool.obtain();
		String obtainedTestUserName = obtainedTestUser.getName();
		String obtainedTestUserPassword = obtainedTestUser.getPassword();

		Assert.assertEquals(testUserName, obtainedTestUserName);
		Assert.assertEquals(testUserPassword, obtainedTestUserPassword);
	}

	@Test
	@Parameters({ "1", "3" })
	public void obtain_poolableObjectsPoolWithOneOrMoreElements_returnsResettedElement(int poolSize) throws Exception {
		Pool<PoolableTestUser> pool = createPoolOfPoolableTestUsers(poolSize);

		String testUserName = "testName";
		String testUserPassword = "testPassword";
		PoolableTestUser testUser = new PoolableTestUser(testUserName, testUserPassword);
		pool.recycle(testUser);
		TestUser obtainedTestUser = pool.obtain();
		String obtainedTestUserName = obtainedTestUser.getName();
		String obtainedTestUserPassword = obtainedTestUser.getPassword();

		Assert.assertEquals(PoolableTestUser.DEFAULT_RESETTED_NAME, obtainedTestUserName);
		Assert.assertEquals(PoolableTestUser.DEFAULT_RESETTED_PASSWORD, obtainedTestUserPassword);
	}

	@Test
	@Parameters({ "0", "1", "3" })
	public void obtain_afterRecyclingPoolableElement_returnsTheRecycledElement(int poolSize) throws Exception {
		Pool<PoolableTestUser> pool = createPoolOfPoolableTestUsers(poolSize);

		PoolableTestUser testUser = new PoolableTestUser("testName", "testPassword");
		pool.recycle(testUser);
		PoolableTestUser obtainedPoolableTestUser = pool.obtain();
		boolean returnsTheSameObject = testUser == obtainedPoolableTestUser;

		Assert.assertTrue(returnsTheSameObject);
	}

	@Test
	public void recycle_emptyPool_hasOneElement() throws Exception {
		Pool<TestUser> pool = createEmptyPoolOfTestUsers();

		TestUser testUser = new TestUser("testName", "testPassword");
		pool.recycle(testUser);
		int size = pool.size();

		Assert.assertEquals(1, size);
	}

	@Test
	public void recycle_emptyPool_isNotEmpty() throws Exception {
		Pool<TestUser> pool = createEmptyPoolOfTestUsers();

		TestUser testUser = new TestUser("testName", "testPassword");
		pool.recycle(testUser);
		boolean empty = pool.isEmpty();

		Assert.assertFalse(empty);
	}

	@Test
	@Parameters({ "0", "1", "3" })
	public void recycle_poolWithAnyNumberOfElements_sizeIsIncrementedByOne(int poolSize) throws Exception {
		Pool<TestUser> pool = createPoolOfTestUsers(poolSize);

		int sizeBeforeRecycle = pool.size();
		pool.recycle(new TestUser("testName", "testPassword"));
		int sizeIncrement = pool.size() - sizeBeforeRecycle;

		Assert.assertEquals(1, sizeIncrement);
	}

	@Test
	@Parameters({ "0", "1", "3" })
	public void clear_poolWithAnyNumberOfElements_isEmpty(int poolSize) throws Exception {
		Pool<TestUser> pool = createPoolOfTestUsers(poolSize);

		pool.clear();
		boolean empty = pool.isEmpty();

		Assert.assertTrue(empty);
	}

	@Test(expected = IllegalArgumentException.class)
	public void recycle_nullObject_throwsIllegalArgumentException() throws Exception {
		Pool<TestUser> pool = createEmptyPoolOfTestUsers();

		pool.recycle(null);
	}

	private static Pool<TestUser> createEmptyPoolOfTestUsers() {
		return new TestUserPool();
	}

	private static Pool<TestUser> createPoolOfTestUsers(int numElements) {
		Pool<TestUser> pool = new TestUserPool();
		for (int i = 0; i < numElements; i++) {
			pool.recycle(new TestUser("Name " + i, "Password " + i));
		}
		return pool;
	}

	private static Pool<PoolableTestUser> createPoolOfPoolableTestUsers(int numElements) {
		Pool<PoolableTestUser> pool = new PoolableTestUserPool();
		for (int i = 0; i < numElements; i++) {
			pool.recycle(new PoolableTestUser("Name " + i, "Password " + i));
		}
		return pool;
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
