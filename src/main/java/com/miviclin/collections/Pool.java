package com.miviclin.collections;

import java.util.ArrayList;

/**
 * Object Pool.
 * 
 * @author Miguel Vicente Linares
 * 
 * @param <T>
 */
public abstract class Pool<T> {

	private ArrayList<T> objects;

	/**
	 * Creates a new empty Pool with ititial capacity for 10 elements.
	 */
	public Pool() {
		this(10);
	}

	/**
	 * Creates a new empty Pool.
	 * 
	 * @param initialCapacity Initial capacity.
	 */
	public Pool(int initialCapacity) {
		this.objects = new ArrayList<T>(initialCapacity);
	}

	/**
	 * Retuns an object from this Pool or creates and returns a new object if the Pool is empty.<br>
	 * The returned object is removed from this Pool.
	 * 
	 * @return An object from this Pool or a new object if the Pool is empty
	 * @see #createObject()
	 */
	public T obtain() {
		if (objects.size() > 0) {
			int index = objects.size() - 1;
			return objects.remove(index);
		}
		return createObject();
	}

	/**
	 * Creates a new object.<br>
	 * This method is called from {@link #obtain()} when this pool is empty.
	 * 
	 * @return New object
	 */
	public abstract T createObject();

	/**
	 * Adds the specified object to this Pool so it can be reused later.<br>
	 * If the specified object is a {@link Poolable} object, its {@link Poolable#reset()} method will be called.
	 * 
	 * @param object Object to be added.
	 * @throws IllegalArgumentException if the specified object is null
	 */
	public void recycle(T object) {
		if (object == null) {
			throw new IllegalArgumentException("The specified object can not be null.");
		}
		if (object instanceof Poolable) {
			((Poolable) object).reset();
		}
		objects.add(object);
	}

	/**
	 * Returns the number of objects in this Pool.
	 * 
	 * @return Number of objects in this Pool
	 */
	public int size() {
		return objects.size();
	}

	/**
	 * Returns true if this Pool is empty (size() == 0).
	 * 
	 * @return true if this Pool is empty, false otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Removes all objects from this Pool, leaving it empty.
	 */
	public void clear() {
		objects.clear();
	}

	/**
	 * Poolable interface.
	 * 
	 * @author Miguel Vicente Linares
	 */
	public interface Poolable {

		/**
		 * Resets the object to its default values. This method is called from {@link Pool#recycle(Object)} when the
		 * object is stored in the {@link Pool} for later reuse.
		 */
		public void reset();

	}

}
