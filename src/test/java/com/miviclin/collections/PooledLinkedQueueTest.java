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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class PooledLinkedQueueTest {

	@Test
	public void createEmptyQueue_byDefault_sizeIsZero() throws Exception {
		PooledLinkedQueue<String> queue = createEmptyPooledLinkedQueue();

		int size = queue.size();

		Assert.assertEquals(0, size);
	}

	@Test
	public void createEmptyQueue_byDefault_headIsNull() throws Exception {
		PooledLinkedQueue<String> queue = createEmptyPooledLinkedQueue();

		String head = queue.peek();

		Assert.assertNull(head);
	}

	@Test
	public void createQueueFromEmptyCollection_byDefault_sizeIsZero() throws Exception {
		List<String> list = new ArrayList<>();
		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(list);

		int size = queue.size();

		Assert.assertEquals(0, size);
	}

	@Test
	public void createQueueFromEmptyCollection_byDefault_headIsNull() throws Exception {
		List<String> list = new ArrayList<>();
		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(list);

		String head = queue.peek();

		Assert.assertNull(head);
	}

	@Test
	@Parameters({ "1", "3" })
	public void createQueueFromCollection_byDefault_sizeIsEqualToSizeOfCollection(int collectionSize) throws Exception {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < collectionSize; i++) {
			list.add("E" + i);
		}
		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(list);

		int sizeOfCollection = list.size();
		int size = queue.size();

		Assert.assertEquals(sizeOfCollection, size);
	}

	@Test
	@Parameters({ "1", "3" })
	public void createQueueFromCollection_byDefault_headIsEqualToFirstElementOfCollection(int collectionSize)
			throws Exception {

		List<String> list = new ArrayList<>();
		for (int i = 0; i < collectionSize; i++) {
			list.add("E" + i);
		}
		PooledLinkedQueue<String> queue = createQueueFromCollection(list);

		String firstElementOfCollection = list.get(0);
		String head = queue.peek();

		Assert.assertEquals(firstElementOfCollection, head);
	}

	@Test
	public void poll_emptyQueue_hasZeroElements() throws Exception {
		PooledLinkedQueue<String> queue = createEmptyPooledLinkedQueue();

		queue.poll();
		int size = queue.size();

		Assert.assertEquals(0, size);
	}

	@Test
	public void poll_emptyQueue_headIsNull() throws Exception {
		PooledLinkedQueue<String> queue = createEmptyPooledLinkedQueue();

		queue.poll();
		String head = queue.peek();

		Assert.assertNull(head);
	}

	@Test
	public void poll_emptyQueue_returnsNull() throws Exception {
		PooledLinkedQueue<String> queue = createEmptyPooledLinkedQueue();

		String returnedElement = queue.poll();

		Assert.assertNull(returnedElement);
	}

	@Test
	public void poll_queueWithOneElement_hasZeroElements() throws Exception {
		PooledLinkedQueue<String> queue = createPooledLinkedQueue(1);

		queue.poll();
		int size = queue.size();

		Assert.assertEquals(0, size);
	}

	@Test
	public void poll_queueWithOneElement_headIsNull() throws Exception {
		PooledLinkedQueue<String> queue = createPooledLinkedQueue(1);

		queue.poll();
		String head = queue.peek();

		Assert.assertEquals(null, head);
	}

	@Test
	public void pollAfterOffer_queueWithOneElement_headIsTheOfferedElement() throws Exception {
		PooledLinkedQueue<String> queue = createPooledLinkedQueue(1);

		String element = "New Element";
		queue.offer(element);
		queue.poll();
		String head = queue.peek();

		Assert.assertEquals(element, head);
	}

	@Test
	@Parameters({ "1", "3" })
	public void poll_queueWithOneOrMoreElements_returnsThePreviousHead(int queueSize) throws Exception {
		PooledLinkedQueue<String> queue = createPooledLinkedQueue(queueSize);

		String previousHead = queue.peek();
		String returnedElement = queue.poll();

		Assert.assertEquals(previousHead, returnedElement);
	}

	@Test
	@Parameters({ "1", "3" })
	public void poll_queueWithOneOrMoreElements_sizeIsDecrementedByOne(int queueSize) throws Exception {
		PooledLinkedQueue<String> queue = createPooledLinkedQueue(queueSize);

		int sizeBeforePoll = queue.size();
		queue.poll();
		int sizeDecrement = sizeBeforePoll - queue.size();

		Assert.assertEquals(1, sizeDecrement);
	}

	@Test
	@Parameters({ "1", "3" })
	public void poll_queueWithOneOrMoreElements_returnsTheHeadOfTheQueue(int queueSize) throws Exception {
		PooledLinkedQueue<String> queue = createPooledLinkedQueue(queueSize);

		String head = queue.peek();
		String polledElement = queue.poll();

		Assert.assertEquals(head, polledElement);
	}

	@Test
	public void offer_emptyQueue_sizeIsOne() throws Exception {
		PooledLinkedQueue<String> queue = createEmptyPooledLinkedQueue();

		queue.offer("New Element");
		int size = queue.size();

		Assert.assertEquals(1, size);
	}

	@Test
	public void offer_emptyQueue_headIsTheOfferedElement() throws Exception {
		PooledLinkedQueue<String> queue = createEmptyPooledLinkedQueue();

		String element = "New Element";
		queue.offer(element);
		String head = queue.peek();

		Assert.assertEquals(element, head);
	}

	@Test
	@Parameters({ "1", "3" })
	public void offer_queueWithOneOrMoreElements_sizeIsIncrementedByOne(int queueSize) throws Exception {
		PooledLinkedQueue<String> queue = createPooledLinkedQueue(queueSize);

		int sizeBeforeOffer = queue.size();
		queue.offer("New Element");
		int sizeIncrement = queue.size() - sizeBeforeOffer;

		Assert.assertEquals(1, sizeIncrement);
	}

	@Test
	@Parameters({
			"1, 1",
			"1, 3",
			"3, 1",
			"3, 3" })
	public void offer_queueWithOneOrMoreElements_headIsNotChanged(int queueSize, int numInsertions) throws Exception {
		PooledLinkedQueue<String> queue = createPooledLinkedQueue(queueSize);

		String headBeforeInsertions = queue.peek();
		for (int i = 0; i < numInsertions; i++) {
			queue.offer("New Element " + i);
		}
		String headAfterInsertions = queue.peek();

		Assert.assertEquals(headBeforeInsertions, headAfterInsertions);
	}

	@Test(expected = NullPointerException.class)
	public void offer_nullObject_throwsNullPointerException() throws Exception {
		PooledLinkedQueue<String> queue = createEmptyPooledLinkedQueue();

		queue.offer(null);
	}

	@Test
	public void iteratorNext_emptyQueue_iteratesZeroTimes() throws Exception {
		PooledLinkedQueue<String> queue = createEmptyPooledLinkedQueue();

		int numIterations = 0;
		Iterator<String> iterator = queue.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			numIterations++;
		}

		Assert.assertEquals(0, numIterations);
	}

	@Test
	@Parameters({ "1", "3" })
	public void iteratorNext_queueWithOneOrMoreElements_iteratesOncePerElement(int queueSize) throws Exception {
		PooledLinkedQueue<String> queue = createPooledLinkedQueue(queueSize);

		int numIterations = 0;
		Iterator<String> iterator = queue.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			numIterations++;
		}
		int size = queue.size();

		Assert.assertEquals(size, numIterations);
	}

	@Test
	@Parameters({ "1", "3" })
	public void iteratorNext_queueWithOneOrMoreElements_iteratesThroughAllElementsFromFirstToLast(int queueSize)
			throws Exception {

		String[] originalElements = new String[queueSize];
		for (int i = 0; i < queueSize; i++) {
			originalElements[i] = "E" + i;
		}
		PooledLinkedQueue<String> queue = createQueueFromCollection(Arrays.asList(originalElements));

		List<String> elementsReturnedByIterator = new ArrayList<>();
		Iterator<String> iterator = queue.iterator();
		while (iterator.hasNext()) {
			String element = iterator.next();
			elementsReturnedByIterator.add(element);
		}
		String[] elementsReturnedByIteratorArray = new String[elementsReturnedByIterator.size()];
		elementsReturnedByIterator.toArray(elementsReturnedByIteratorArray);

		Assert.assertArrayEquals(originalElements, elementsReturnedByIteratorArray);
	}

	@Test
	@Parameters({ "0", "1", "3" })
	public void iteratorRemove_calledBeforeNext_nothingIsRemoved(int queueSize) throws Exception {
		String[] originalElements = new String[queueSize];
		for (int i = 0; i < queueSize; i++) {
			originalElements[i] = "E" + i;
		}
		PooledLinkedQueue<String> queue = createQueueFromCollection(Arrays.asList(originalElements));

		List<String> elementsReturnedByIterator = new ArrayList<>();
		Iterator<String> iterator = queue.iterator();
		iterator.remove();
		iterator = queue.iterator();
		while (iterator.hasNext()) {
			String element = iterator.next();
			elementsReturnedByIterator.add(element);
		}
		String[] elementsReturnedByIteratorArray = new String[elementsReturnedByIterator.size()];
		elementsReturnedByIterator.toArray(elementsReturnedByIteratorArray);

		Assert.assertArrayEquals(originalElements, elementsReturnedByIteratorArray);
	}

	@Test
	@Parameters({ "1", "3" })
	public void iteratorRemove_removeFirstElementFromQueueWithOneOrMoreElements_headRemoved(int queueSize)
			throws Exception {

		PooledLinkedQueue<String> queue = createPooledLinkedQueue(queueSize);

		String previousHead = queue.peek();
		Iterator<String> iterator = queue.iterator();
		iterator.next();
		iterator.remove();
		String head = queue.peek();

		Assert.assertNotEquals(previousHead, head);
	}

	@Test
	@Parameters({ "1", "3" })
	public void iteratorRemove_queueWithOneOrMoreElements_sizeIsDecrementedByOne(int queueSize) throws Exception {
		PooledLinkedQueue<String> queue = createPooledLinkedQueue(queueSize);

		int sizeBeforeRemove = queue.size();
		Iterator<String> iterator = queue.iterator();
		iterator.next();
		iterator.remove();
		int sizeDecrement = sizeBeforeRemove - queue.size();

		Assert.assertEquals(1, sizeDecrement);
	}

	@Test(expected = IllegalStateException.class)
	public void iteratorRemove_calledTwiceOnQueueWithMoreThanTwoElements_throwsIllegalStateException()
			throws Exception {

		PooledLinkedQueue<String> queue = createPooledLinkedQueue(3);

		Iterator<String> iterator = queue.iterator();
		iterator.next();
		iterator.next();
		iterator.remove();
		iterator.remove();
	}

	private static PooledLinkedQueue<String> createEmptyPooledLinkedQueue() {
		return new PooledLinkedQueue<>();
	}

	private static PooledLinkedQueue<String> createPooledLinkedQueue(int numElements) {
		PooledLinkedQueue<String> queue = new PooledLinkedQueue<>(numElements);
		for (int i = 0; i < numElements; i++) {
			queue.offer("Element " + i);
		}
		return queue;
	}

	private static PooledLinkedQueue<String> createQueueFromCollection(Collection<String> collection) {
		return new PooledLinkedQueue<>(collection);
	}

}
