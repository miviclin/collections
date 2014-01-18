package com.miviclin.collections;

import static org.junit.Assert.assertEquals;

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
		public void initialize() {
			queue = new PooledLinkedQueue<>();
		}

		@Test
		public void initialized_shouldHaveZeroElements() throws Exception {
			assertEquals(0, queue.size());
		}

		@Test
		public void initialized_headShouldBeNull() throws Exception {
			assertEquals(null, queue.peek());
		}

		@Test
		public void elementPolled_shouldHaveZeroElements() throws Exception {
			queue.poll();
			assertEquals(0, queue.size());
		}

		@Test
		public void elementPolled_headShouldBeNull() throws Exception {
			queue.poll();
			assertEquals(null, queue.peek());
		}

		@Test
		public void elementPolled_shouldReturnNull() throws Exception {
			String polledElement = queue.poll();
			assertEquals(null, polledElement);
		}

		@Test
		public void elementOffered_shouldHaveOneElement() throws Exception {
			queue.offer("A");
			assertEquals(1, queue.size());
		}

		@Test
		public void elementOffered_headShouldBeTheOfferedElement() throws Exception {
			String element = "A";
			queue.offer(element);
			assertEquals(element, queue.peek());
		}
	}

	public static class PooledLinkedQueueWithOneElementFixture {

		private static final String ELEMENT = "element";
		private PooledLinkedQueue<String> queue;

		@Before
		public void initialize() {
			queue = new PooledLinkedQueue<>();
			queue.offer(ELEMENT);
		}

		@Test
		public void initialized_shouldHaveOneElement() throws Exception {
			assertEquals(1, queue.size());
		}

		@Test
		public void initialized_shouldReturnElementOnPeek() throws Exception {
			assertEquals(ELEMENT, queue.peek());
		}

		@Test
		public void elementPolled_shouldReturnElement() throws Exception {
			assertEquals(ELEMENT, queue.poll());
		}

		@Test
		public void elementPolled_shouldHaveZeroElements() throws Exception {
			queue.poll();
			assertEquals(0, queue.size());
		}

		@Test
		public void elementPolled_headShouldBeNull() throws Exception {
			queue.poll();
			assertEquals(null, queue.peek());
		}

		@Test
		public void elementOffered_shouldHaveTwoElements() throws Exception {
			queue.offer("A");
			assertEquals(2, queue.size());
		}

		@Test
		public void elementOffered_headShouldNotHaveChanged() throws Exception {
			String head = queue.peek();
			String element = "A";
			queue.offer(element);
			assertEquals(head, queue.peek());
		}

		@Test
		public void elementOfferedAndThenElementPolled_headShouldBeTheOfferedElement() throws Exception {
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
		public void initialize() {
			testElementsList = new ArrayList<>();
			testElementsList.add("E1");
			testElementsList.add("E2");
			testElementsList.add("E3");
			queue = new PooledLinkedQueue<>(testElementsList);
		}

		@Test
		public void initialized_shouldHaveTheSameSizeAsTestElementsList() throws Exception {
			assertEquals(testElementsList.size(), queue.size());
		}

		@Test
		public void elementPolled_shouldDecrementSizeInOne() {
			int initialSize = queue.size();
			queue.poll();
			int currentSize = queue.size();
			assertEquals(initialSize - 1, currentSize);
		}

		@Test
		public void elementPolled_shouldReturnTheHeadOfTheQueue() {
			String head = queue.peek();
			String polledElement = queue.poll();
			assertEquals(head, polledElement);
		}

		@Test
		public void elementOffered_shouldIncrementSizeInOne() throws Exception {
			int initialSize = queue.size();
			queue.offer("A");
			int currentSize = queue.size();
			assertEquals(initialSize + 1, currentSize);
		}

		@Test
		public void elementOffered_headShouldNotHaveChanged() throws Exception {
			String head = queue.peek();
			String element = "A";
			queue.offer(element);
			assertEquals(head, queue.peek());
		}

		@Test
		public void elementOfferedAndThenAllPreviousElementsPolled_headShouldBeTheOfferedElement() throws Exception {
			int initialSize = queue.size();
			String element = "A";
			queue.offer(element);
			for (int i = 0; i < initialSize; i++) {
				queue.poll();
			}
			assertEquals(element, queue.peek());
		}

		@Test(expected = NullPointerException.class)
		public void nullElementOffered_shouldThrowNullPointerException() {
			queue.offer(null);
		}

		@Test
		public void iterated_shouldIterateThroughAllElements() {
			int i = 0;
			Iterator<String> iterator = queue.iterator();
			while (iterator.hasNext()) {
				iterator.next();
				i++;
			}
			assertEquals(testElementsList.size(), i);
		}

		@Test
		public void iterated_shouldHaveTheSameElementsTestElementsListHas() {
			int i = 0;
			Iterator<String> iterator = queue.iterator();
			while (iterator.hasNext()) {
				String element = iterator.next();
				assertEquals(testElementsList.get(i), element);
				i++;
			}
		}

		@Test
		public void iteratorRemoveBeforeCallingNext_shouldDoNothing() {
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
		public void iteratorRemoveFirstElement_shouldRemoveTheHeadOfTheQueue() {
			String head = queue.peek();
			Iterator<String> iterator = queue.iterator();
			iterator.next();
			iterator.remove();
			assertEquals(false, queue.contains(head));

		}

		@Test
		public void iteratorRemove_shouldDecrementSizeInOne() {
			int initialSize = queue.size();
			Iterator<String> iterator = queue.iterator();
			iterator.next();
			iterator.remove();
			int currentSize = queue.size();
			assertEquals(initialSize - 1, currentSize);
		}

		@Test(expected = IllegalStateException.class)
		public void iteratorRemove_shouldThrowIllegalStateExceptionWhenCallingRemoveTwiceBeforeCallingNext() {
			Iterator<String> iterator = queue.iterator();
			iterator.next();
			iterator.next();
			iterator.remove();
			iterator.remove();
		}
	}

}
