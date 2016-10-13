package cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cache.LruCache;

public class LruCacheTest {	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDefaultConstructorDefaultCapacity() {
		LruCache<Object, Object> cache = new LruCache<>();
		assert(cache.getCapacity() == LruCache.DEFAULT_CAPACITY);
	}

	@Test
	public void testConstructorCapacity() {
		final int expectedCapacity = 10;
		LruCache<Object, Object> cache = new LruCache<>(expectedCapacity);
		assert(cache.getCapacity() == expectedCapacity);
	}

	@Test
	public void testCapacityDefaultsToOne() 
	{
		LruCache<Object, Object> cache = new LruCache<>(-1);
		assert(cache.getCapacity() == 1);
	}
	
	@Test
	public void testGetInvalidKeyReturnsNull() 
	{
		LruCache<Object, Object> cache = new LruCache<>(1);
		assert(cache.get("key") == null);
	}
	
	@Test
	public void testPutValue() {
		LruCache<String, String> cache = new LruCache<>(1);
		cache.put("key", "value");
		assert(cache.get("key").equals("value"));
		assert(cache.size() == 1);
	}
	
	@Test
	public void testUpdateValue() 
	{
		LruCache<String, String> cache = new LruCache<>(1);
		cache.put("key", "value");
		assert(cache.get("key").equals("value"));
		cache.put("key", "value2");
		assert(cache.get("key").equals("value2"));
		assert(cache.size() == 1);
	}

	@Test
	public void testEvict() 
	{
		LruCache<String, String> cache = new LruCache<>(1);
		cache.put("key", "value");
		assert(cache.get("key").equals("value"));
		cache.put("key2", "value2");
		assert(cache.get("key2").equals("value2"));
		assert(cache.get("key") == null);
		assert(cache.size() == 1);
	}

	@Test
	public void testGetPreventsEvict() 
	{
		LruCache<String, String> cache = new LruCache<>(2);
		cache.put("key", "value");
		assert(cache.get("key").equals("value"));
		assert(cache.size() == 1);
		cache.put("key2", "value2");
		assert(cache.get("key2").equals("value2"));
		assert(cache.size() == 2);
		cache.get("key");
		cache.put("key3", "value3");
		assert(cache.get("key").equals("value"));
		assert(cache.get("key3").equals("value3"));
		assert(cache.get("key2") == null);
		assert(cache.size() == 2);
	}
	
	@Test
	public void testUpdatePreventsEvict() 
	{
		LruCache<String, String> cache = new LruCache<>(2);
		cache.put("key", "value");
		assert(cache.get("key").equals("value"));
		assert(cache.size() == 1);
		cache.put("key2", "value2");
		assert(cache.get("key2").equals("value2"));
		assert(cache.size() == 2);
		cache.put("key", "newValue");
		cache.put("key3", "value3");
		assert(cache.get("key").equals("newValue"));
		assert(cache.get("key3").equals("value3"));
		assert(cache.get("key2") == null);
		assert(cache.size() == 2);
	}
	
	@Test
	public void testRemoveFromCache() 
	{
		LruCache<String, String> cache = new LruCache<>(2);
		cache.put("key", "value");
		assert(cache.get("key").equals("value"));
		assert(cache.size() == 1);
		cache.put("key2", "value2");
		assert(cache.get("key2").equals("value2"));
		assert(cache.size() == 2);
		cache.remove("key");
		assert(cache.get("key") == null);
		assert(cache.get("key2").equals("value2"));
		assert(cache.size() == 1);
	}
	
	@Test
	public void testRemoveFromCacheofSize1() {
		LruCache<String, String> cache = new LruCache<>(2);
		cache.put("key", "value");
		assert(cache.get("key").equals("value"));
		assert(cache.size() == 1);
		cache.remove("key");
		assert(cache.get("key") == null);
		assert(cache.size() == 0);
	}
}
