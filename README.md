#Collections
This library provides the implementation of some collections that are not included in the Java Collections Framework.


####List of collections

- **Pool:** A simple object pool. If an object is not needed anymore, it can be stored in the pool instead of being freed by the garbage collector. And when a new object of the same type is needed, the stored object can be obtained from the pool and re-initialized instead of creating a new object. This collection may be useful in applications such as games, where preventing the garbage collector from being triggered while the game is running is usually required.
- **PooledLinkedQueue:** This queue is implemented as a linked list that holds an internal pool of nodes. The main difference between this queue and a LinkedList is that this queue has a pool of nodes, so it does not need to create new nodes if there are nodes available in the pool. This collection may be useful in applications such as games, where preventing the garbage collector from being triggered while the game is running is usually required.
