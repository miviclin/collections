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

/**
 * A simple object pool. If an object is not needed anymore, it can be stored in the pool instead of being freed by the
 * garbage collector. And when a new object of the same type is needed, the stored object can be obtained from the pool
 * and re-initialized instead of creating a new object. This collection may be useful in applications such as games,
 * where preventing the garbage collector from being triggered while the game is running is usually required.
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
