package com.miviclin.collections;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This queue is implemented as a linked list. The main difference is that this queue has a pool of nodes, so it does
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

	/**
	 * Creates a new PooledLinkedQueue with 10 pooled nodes.
	 */
	public PooledLinkedQueue() {
		this(10);
	}

	/**
	 * Creates a new PooledLinkedQueue with the specified initial number of pooled nodes.
	 * 
	 * @param initialNumPooledNodes Initial number of pooled nodes.
	 */
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

	/**
	 * Creates a new PooledLinkedQueue filled with the specified collection and 0 pooled nodes.
	 * 
	 * @param collection Collection.
	 */
	public PooledLinkedQueue(Collection<E> collection) {
		this(0, collection);
	}

	/**
	 * Creates a new PooledLinkedQueue filled with the specified collection and the specified initial number of pooled
	 * nodes.
	 * 
	 * @param initialNumPooledNodes Initial number of pooled nodes. After the collection is added to the queue, the
	 *            specified amount of pooled nodes will be available for later insertions.
	 * @param collection Collection.
	 */
	public PooledLinkedQueue(int initialNumPooledNodes, Collection<E> collection) {
		this(initialNumPooledNodes + collection.size());
		addAll(collection);
	}

	@Override
	public boolean offer(E e) {
		if (e == null) {
			throw new NullPointerException();
		}

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
		E removedItem = removeHead();
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

	/**
	 * Returns a node from the pool if possible. If the pool is empty, creates a new node and returns it.
	 * 
	 * @return Node
	 */
	private Node<E> obtainNode() {
		if (nodePool.size() == 0) {
			return new Node<E>();
		}
		int index = nodePool.size() - 1;
		return nodePool.remove(index);
	}

	/**
	 * Resets the specified node and stores it in the pool for later use.
	 * 
	 * @param node Node.
	 */
	private void recycleNode(Node<E> node) {
		node.reset();
		nodePool.add(node);
	}

	/**
	 * Removes the head node and recycles it.
	 * 
	 * @return Item of the removed head.
	 */
	private E removeHead() {
		Node<E> previousHead = head;
		head = head.getNextNode();
		if (head != null) {
			head.setPreviousNode(null);
		}

		E item = previousHead.getItem();
		recycleNode(previousHead);
		return item;
	}

	/**
	 * Node.
	 * 
	 * @author Miguel Vicente Linares
	 * 
	 * @param <E>
	 */
	private static class Node<E> {

		private E item;
		private Node<E> nextNode;
		private Node<E> previousNode;

		/**
		 * Creates a new Node.
		 */
		public Node() {
			reset();
		}

		/**
		 * Resets the item, nextNode and previousNode to null.
		 */
		void reset() {
			this.item = null;
			this.nextNode = null;
			this.previousNode = null;
		}

		/**
		 * Returns the item of this Node.
		 * 
		 * @return The item of this Node.
		 */
		public E getItem() {
			return item;
		}

		/**
		 * Sets the item of this Node.
		 * 
		 * @param item New value.
		 */
		public void setItem(E item) {
			this.item = item;
		}

		/**
		 * Returns the next Node.
		 * 
		 * @return Next Node
		 */
		public Node<E> getNextNode() {
			return nextNode;
		}

		/**
		 * Sets the next Node.
		 * 
		 * @param nextNode New value.
		 */
		public void setNextNode(Node<E> nextNode) {
			this.nextNode = nextNode;
		}

		/**
		 * Returns the previous Node.
		 * 
		 * @return Previous Node
		 */
		public Node<E> getPreviousNode() {
			return previousNode;
		}

		/**
		 * Sets the previous Node.
		 * 
		 * @param previousNode New value.
		 */
		public void setPreviousNode(Node<E> previousNode) {
			this.previousNode = previousNode;
		}

	}

	/**
	 * Iterator for PooledLinkedQueue.
	 * 
	 * @author Miguel Vicente Linares
	 */
	private class PooledLinkedQueueIterator implements Iterator<E> {

		private Node<E> lastReturnedNode;
		private Node<E> nextNode;
		private boolean allowRemove;

		/**
		 * Creates a new PooledLinkedQueueIterator.
		 */
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
