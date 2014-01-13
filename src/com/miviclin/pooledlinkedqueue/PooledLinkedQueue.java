package com.miviclin.pooledlinkedqueue;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This queue is implemented like a linked list. The main difference is that this queue uses a pool of nodes, so it does
 * not need to create new nodes if there are nodes available in the pool.<br>
 * This queue does not allow null objects.
 * 
 * @author Miguel Vicente Linares
 * 
 * @param <E>
 */
public class PooledLinkedQueue<E> extends AbstractQueue<E> {

	private Node<E> head;
	private Node<E> tail;
	private int size;
	private ArrayList<Node<E>> nodePool;

	public PooledLinkedQueue() {
		this(10);
	}

	public PooledLinkedQueue(int initialNumPooledNodes) {
		super();
		this.head = null;
		this.tail = null;
		this.size = 0;
		this.nodePool = new ArrayList<>(initialNumPooledNodes);
		for (int i = 0; i < initialNumPooledNodes; i++) {
			this.nodePool.add(new Node<E>());
		}
	}

	public PooledLinkedQueue(Collection<E> data) {
		this(data.size());
		for (E e : data) {
			offer(e);
		}
	}

	@Override
	public boolean offer(E e) {
		Node<E> node = obtainNode();
		node.setItem(e);
		if (size == 0) {
			head = node;
		}
		if (tail != null) {
			tail.setNextNode(node);
		}
		node.setPreviousNode(tail);
		tail = node;
		size++;
		return true;
	}

	@Override
	public E poll() {
		if (head == null) {
			return null;
		}
		if (head == tail) {
			tail = null;
		}
		E removedItem = recycleHead();
		if (removedItem != null) {
			size--;
		}
		return removedItem;
	}

	@Override
	public E peek() {
		if (head == null) {
			return null;
		}
		return head.getItem();
	}

	@Override
	public Iterator<E> iterator() {
		return new PooledLinkedQueueIterator();
	}

	@Override
	public int size() {
		return size;
	}

	private Node<E> obtainNode() {
		if (nodePool.size() == 0) {
			return new Node<E>();
		}
		int index = nodePool.size() - 1;
		return nodePool.remove(index);
	}

	private void recycleNode(Node<E> node) {
		node.reset();
		nodePool.add(node);
	}

	private E recycleHead() {
		Node<E> previousHead = head;
		head = head.getNextNode();
		if (head != null) {
			head.setPreviousNode(null);
		}

		E item = previousHead.getItem();
		recycleNode(previousHead);
		return item;
	}

	private static class Node<E> {

		private E item;
		private Node<E> nextNode;
		private Node<E> previousNode;

		public Node() {
			reset();
		}

		void reset() {
			this.item = null;
			this.nextNode = null;
			this.previousNode = null;
		}

		public E getItem() {
			return item;
		}

		public void setItem(E item) {
			this.item = item;
		}

		public Node<E> getNextNode() {
			return nextNode;
		}

		public void setNextNode(Node<E> nextNode) {
			this.nextNode = nextNode;
		}

		public Node<E> getPreviousNode() {
			return previousNode;
		}

		public void setPreviousNode(Node<E> previousNode) {
			this.previousNode = previousNode;
		}

	}

	private class PooledLinkedQueueIterator implements Iterator<E> {

		private Node<E> lastReturnedNode;
		private Node<E> nextNode;
		private boolean allowRemove;

		public PooledLinkedQueueIterator() {
			this.lastReturnedNode = null;
			this.nextNode = head;
			this.allowRemove = false;
		}

		@Override
		public boolean hasNext() {
			return nextNode != null;
		}

		@Override
		public E next() {
			E currentItem = nextNode.getItem();
			lastReturnedNode = nextNode;
			nextNode = nextNode.getNextNode();
			allowRemove = true;
			return currentItem;
		}

		@Override
		public void remove() {
			if (lastReturnedNode == null) {
				return;
			}
			if (!allowRemove) {
				throw new IllegalStateException("The next method has not yet been called, or the remove method has " +
						"already been called after the last call to the next method");
			}

			Node<E> previousLastReturnedNode = lastReturnedNode;
			lastReturnedNode = lastReturnedNode.getPreviousNode();
			if (nextNode != null) {
				nextNode.setPreviousNode(lastReturnedNode);
			}
			recycleNode(previousLastReturnedNode);
			size--;
			allowRemove = false;
		}
	}

}
