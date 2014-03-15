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
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class PooledLinkedQueueTest {

	public static class EmptyPooledLinkedQueueFixture {

		private PooledLinkedQueue<String> queue;

		@Before
		public void givenAnEmptyPooledLinkedQueue() {
			queue = new PooledLinkedQueue<>();
		}

		@Test
		public void whenInitialized_thenTheQueueShouldHaveZeroElements() throws Exception {
			assertEquals(0, queue.size());
		}

		@Test
		public void whenInitialized_thenTheHeadOfTheQueueShouldBeNull() throws Exception {
			assertEquals(null, queue.peek());
		}

		@Test
		public void whenAnElementIsPolled_thenTheQueueShouldHaveZeroElements() throws Exception {
			queue.poll();
			assertEquals(0, queue.size());
		}

		@Test
		public void whenAnElementIsPolled_thenTheHeadOfTheQueueShouldBeNull() throws Exception {
			queue.poll();
			assertEquals(null, queue.peek());
		}

		@Test
		public void whenAnElementIsPolled_thenTheReturnedElementShouldBeNull() throws Exception {
			String polledElement = queue.poll();
			assertEquals(null, polledElement);
		}

		@Test
		public void whenAnElementIsOffered_thenTheQueueShouldHaveOneElement() throws Exception {
			queue.offer("A");
			assertEquals(1, queue.size());
		}

		@Test
		public void whenAnElementIsOffered_thenTheHeadOfTheQueueShouldBeTheOfferedElement() throws Exception {
			String element = "A";
			queue.offer(element);
			assertEquals(element, queue.peek());
		}
	}

	public static class PooledLinkedQueueWithOneElementFixture {

		private PooledLinkedQueue<String> queue;

		@Before
		public void givenAPooledLinkedQueueWithOneElement() {
			queue = new PooledLinkedQueue<>();
			queue.offer("E1");
		}

		@Test
		public void whenInitialized_thenTheQueueShouldHaveOneElement() throws Exception {
			assertEquals(1, queue.size());
		}

		@Test
		public void whenInitialized_thenTheHeadOfTheQueueShouldNotBeNull() throws Exception {
			assertNotEquals(null, queue.peek());
		}

		@Test
		public void whenAnElementIsPolled_thenTheReturnedElementShouldNotBeNull() throws Exception {
			assertNotEquals(null, queue.poll());
		}

		@Test
		public void whenAnElementIsPolled_thenTheQueueShouldHaveZeroElements() throws Exception {
			queue.poll();
			assertEquals(0, queue.size());
		}

		@Test
		public void whenAnElementIsPolled_thenTheHeadOfTheQueueShouldBeNull() throws Exception {
			queue.poll();
			assertEquals(null, queue.peek());
		}

		@Test
		public void whenAnElementIsOffered_thenTheQueueShouldHaveTwoElements() throws Exception {
			queue.offer("A");
			assertEquals(2, queue.size());
		}

		@Test
		public void whenAnElementIsOffered_thenTheHeadOfTheQueueShouldNotBeChanged() throws Exception {
			String head = queue.peek();
			String element = "A";
			queue.offer(element);
			assertEquals(head, queue.peek());
		}

		@Test
		public void whenAnElementIsOfferedAndThenItIsPolled_thenTheHeadOfTheQueueShouldBeTheOfferedElement()
				throws Exception {

			String element = "A";
			queue.offer(element);
			queue.poll();
			assertEquals(element, queue.peek());
		}
	}

	public static class NonEmptyPooledLinkedQueueFixture {

		private ArrayList<String> testElementsList;
		private PooledLinkedQueue<String> queue;

		@Before
		public void givenAPooledLinkedQueueMoreThanOneElement() {
			testElementsList = new ArrayList<>();
			testElementsList.add("E1");
			testElementsList.add("E2");
			testElementsList.add("E3");
			queue = new PooledLinkedQueue<>(testElementsList);
		}

		@Test
		public void whenInitialized_thenTheQueueShouldHaveTheSameSizeAsTestElementsList() throws Exception {
			assertEquals(testElementsList.size(), queue.size());
		}

		@Test
		public void whenAnElementIsPolled_thenTheQueueShouldDecrementItsSizeInOne() {
			int initialSize = queue.size();
			queue.poll();
			int currentSize = queue.size();
			assertEquals(initialSize - 1, currentSize);
		}

		@Test
		public void whenAnElementIsPolled_thenTheReturnedElementShouldBeTheHeadOfTheQueue() {
			String head = queue.peek();
			String polledElement = queue.poll();
			assertEquals(head, polledElement);
		}

		@Test
		public void whenAnElementIsOffered_thenTheQueueShouldIncrementItsSizeInOne() throws Exception {
			int initialSize = queue.size();
			queue.offer("A");
			int currentSize = queue.size();
			assertEquals(initialSize + 1, currentSize);
		}

		@Test
		public void whenAnElementIsOffered_TheHeadOfTheQueueShouldNotBeChanged() throws Exception {
			String head = queue.peek();
			String element = "A";
			queue.offer(element);
			assertEquals(head, queue.peek());
		}

		@Test
		public void whenAnElementIsOfferedAndThenAllOtherElementsArePolled_thenTheHeadOfTheQueueShouldBeTheNewElement()
				throws Exception {

			int initialSize = queue.size();
			String element = "A";
			queue.offer(element);
			for (int i = 0; i < initialSize; i++) {
				queue.poll();
			}
			assertEquals(element, queue.peek());
		}

		@Test(expected = NullPointerException.class)
		public void whenANullElementIsOffered_thenANullPointerExceptionShouldBeThrown() {
			queue.offer(null);
		}

		@Test
		public void whenTheQueueIsIterated_thenTheIteratorShouldIterateThroughAllElements() {
			int i = 0;
			Iterator<String> iterator = queue.iterator();
			while (iterator.hasNext()) {
				iterator.next();
				i++;
			}
			assertEquals(testElementsList.size(), i);
		}

		@Test
		public void whenTheQueueIsIterated_thenTheIteratorShouldReturnTheSameElementsTestElementsListHas() {
			int i = 0;
			Iterator<String> iterator = queue.iterator();
			while (iterator.hasNext()) {
				String element = iterator.next();
				assertEquals(testElementsList.get(i), element);
				i++;
			}
		}

		@Test
		public void whenTheIteratorRemoveIsCalledBeforeNext_thenItShouldDoNothing() {
			Iterator<String> iterator = queue.iterator();
			iterator.remove();
			int i = 0;
			iterator = queue.iterator();
			while (iterator.hasNext()) {
				String element = iterator.next();
				assertEquals(testElementsList.get(i), element);
				i++;
			}
		}

		@Test
		public void whenTheIteratorIsUsedToRemoveTheFirstElement_thenTheHeadOfTheQueueShouldBeRemoved() {
			String head = queue.peek();
			Iterator<String> iterator = queue.iterator();
			iterator.next();
			iterator.remove();
			assertEquals(false, queue.contains(head));
		}

		@Test
		public void whenTheIteratorIsUsedToRemoveAnElement_thenTheSizeOfTheQueueShouldBeDecrementedInOne() {
			int initialSize = queue.size();
			Iterator<String> iterator = queue.iterator();
			iterator.next();
			iterator.remove();
			int currentSize = queue.size();
			assertEquals(initialSize - 1, currentSize);
		}

		@Test(expected = IllegalStateException.class)
		public void whenTheIteratorRemoveIsCalledTwice_thenAnIllegalStateExceptionShouldBeCalled() {
			Iterator<String> iterator = queue.iterator();
			iterator.next();
			iterator.next();
			iterator.remove();
			iterator.remove();
		}
	}

}
