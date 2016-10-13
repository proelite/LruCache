package cache;
import java.util.HashMap;

/**
 * Implementation of Least Recently Used Cache. Cache size can be specified. Items are stored using key and value. 
 * Items are added to a FIFO queue. Updates and retrieval will shift item to the front of the queue. Items are evicted from the ended of the queue.
 * Item will remain in cache unless it hasn't been updated or retrieved since an number of items equivalent to the cache size has been added to the cache.
 * Get, update, and evict operations have constant complexity. 
 * @author Xiaodi
 *
 * @param <K> Key
 * @param <V> Value
 */
public class LruCache<K, V> {
	
	/**
	 * Default cache size.
	 */
	public static final int DEFAULT_CAPACITY = 10000;
	
	/**
	 * The internal node to store key and value in the doubly linked list.
	 */
	class CacheNode
	{
		CacheNode previous;
		CacheNode next;
		K key;
		V value;
		
		CacheNode (K key, V value) 
		{
			this.key = key;
			this.value = value;
		}
	}

	//The map from key to {@link CacheNode}.
	private final HashMap<K, CacheNode> map;
	
	//The initial capacity.
	private final int capacity;
	
	//The head of the doubly linked list.
	private CacheNode head;
	
	//The tail of the doubly linked list.
	private CacheNode tail;
	
	/**
	 * Constructs a {@link LruCache} with default capacity of {@link LruCache#DEFAUL_CAPACITY}.
	 */
	public LruCache()
	{
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructs a {@link LruCache} with the supplied capacity.
	 * @param capacity The number of items that cache can hold before it starts evicting the oldest items.
	 * Will default to 1 if capacity less than 1 is used. 
	 */
	public LruCache(int capacity) 
	{
		this.capacity = capacity > 0 ? capacity : 1;
		this.map = new HashMap<>(this.capacity);
	}
	
	/**
	 * Puts a key value pair into the cache. If after adding the new key and value the size of the cache exceeds the capacity, the oldest key value pair will be evicted.
	 * @param key The key. 
	 * @param value The value.
	 */
	public void put(K key, V value) 
	{
		CacheNode node;
		
		if (this.map.containsKey(key)) 
		{
			node = this.map.get(key);
			node.value = value;
			removeNode(node);
		}
		else
		{
			node = new CacheNode(key, value);
			this.map.put(key, node);
		}
		
		addToFront(node);
		checkSizeAndDropLastNode();
	}
	
	/**
	 * Removes the key value pair from the cache. 
	 * @param key The key to remove.
	 * @return The value removed. Returns {@code null} if key doesn't exist in cache.
	 */
	public V remove(K key) 
	{
		if (!this.map.containsKey(key))
		{
			return null;
		}
		
		CacheNode node = this.map.remove(key);
		removeNode(node);
		return node.value;
	}
	
	/**
	 * Checks whether or not key value pair is in the cache.
	 * @param key The key.
	 * @return {@code true} if key value pair exists. {@code false}.
	 */
	public boolean contains(K key) 
	{
		return this.map.containsKey(key);
	}
	
	/**
	 * Retrieves the value. Key value pair will be shifted to the front of the FILO queue.
	 * @param key The key.
	 * @return The value. Returns {@code null} if key doesn't exist in cache.
	 */
	public V get(K key) 
	{
		if (!this.map.containsKey(key)) 
		{
			return null;
		}
		
		CacheNode node = this.map.get(key);
		
		if (this.head != node)
		{
			removeNode(node);
			addToFront(node);
		}
				
		return node.value;
	}
	
	/**
	 * Gets the capacity of this cache as specified in the constructor, or {@link LruCache#DEFAULT_CAPACITY} if default constructor was used.
	 * @return The capacity.
	 */
	public int getCapacity() 
	{
		return this.capacity;
	}
	
	/** 
	 * The current number of items in the cache.
	 * @return The size.
	 */
	public int size() 
	{
		return this.map.size();
	}
	
	private void addToFront(CacheNode node)
	{
		if (node == null) 
		{
			return;
		}
		
		if (this.head != null)
		{
			this.head.previous = node;
		}
		
		node.next = this.head;
		node.previous = null;
		this.head = node;
	
		if (this.tail == null) 
		{
			this.tail = node;
		}
	}
	
	private void removeNode(CacheNode node) 
	{
		if (node == null || this.map.size() == 0)
		{
			return;
		}
		
		CacheNode previous = node.previous;
		CacheNode next = node.next;
		
		if (previous != null) 
		{
			previous.next = next;
		}
		else
		{
			this.head = next;
		}
		
		if (next != null) 
		{
			next.previous = previous;
		} else
		{
			this.tail = previous;
		}
		
		if (this.head != null) 
		{
			this.head.previous = null;
		}
		
		if (this.tail != null) 
		{
			this.tail.next = null;
		}
		
		node.next = null;
		node.previous = null;
	}
	
	private void checkSizeAndDropLastNode() 
	{
		if (this.size() > this.capacity)
		{
			this.map.remove(this.tail.key);
			removeNode(this.tail);
		}
	}
}
  