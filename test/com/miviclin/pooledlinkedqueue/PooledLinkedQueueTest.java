package com.miviclin.pooledlinkedqueue;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

public class PooledLinkedQueueTest {

	@Test
	public void offerTest() {
		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(10);
		queue.offer("A");
		assertEquals("A", queue.peek());
		assertEquals(1, queue.size());
		queue.offer("B");
		assertEquals("A", queue.peek());
		assertEquals(2, queue.size());
	}

	@Test
	public void pollTest() {
		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(10);
		queue.offer("A");
		queue.offer("B");
		assertEquals("A", queue.poll());
		assertEquals("B", queue.peek());
		assertEquals(1, queue.size());
		assertEquals("B", queue.poll());
		assertEquals(null, queue.peek());
		assertEquals(0, queue.size());
	}

	@Test
	public void pollFromEmptyQueueTest() {
		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(10);
		String removedElement = queue.poll();
		assertEquals(null, removedElement);
		assertEquals(0, queue.size());
	}

	@Test
	public void peekFromEmptyQueueTest() {
		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(10);
		String headElement = queue.peek();
		assertEquals(null, headElement);
		assertEquals(0, queue.size());
	}

	@Test
	public void iteratorBasicTest() {
		ArrayList<String> testData = new ArrayList<>();
		testData.add("A");
		testData.add("B");
		testData.add("C");

		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(testData);
		assertEquals(testData.size(), queue.size());

		int i = 0;
		Iterator<String> iterator = queue.iterator();
		while (iterator.hasNext() && i < testData.size()) {
			String element = iterator.next();
			assertEquals(testData.get(i), element);
			i++;
		}
		assertEquals(testData.size(), i);
	}

	@Test
	public void iteratorRemoveBasicTest() {
		ArrayList<String> testData = new ArrayList<>();
		testData.add("A");
		testData.add("B");
		testData.add("C");

		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(testData);
		assertEquals(testData.size(), queue.size());

		Iterator<String> iterator = queue.iterator();
		iterator.remove();
		assertEquals(3, queue.size());
		iterator.next();
		iterator.remove();

		ArrayList<String> expectedData = new ArrayList<>();
		testData.add("B");
		testData.add("C");

		int i = 0;
		Iterator<String> it = queue.iterator();
		while (it.hasNext() && i < expectedData.size()) {
			String element = it.next();
			assertEquals(expectedData.get(i), element);
			i++;
		}
		assertEquals(expectedData.size(), i);
	}

	@Test(expected = IllegalStateException.class)
	public void iteratorRemoveSameElementMultipleTimesTest() {
		ArrayList<String> testData = new ArrayList<>();
		testData.add("A");
		testData.add("B");
		testData.add("C");

		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(testData);
		assertEquals(testData.size(), queue.size());

		Iterator<String> iterator = queue.iterator();
		assertEquals(3, queue.size());
		iterator.next();
		iterator.next();
		iterator.remove();
		iterator.remove();
	}

}
