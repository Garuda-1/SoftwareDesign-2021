package ru.itmo.sd.hw1.impl;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.itmo.sd.hw1.LRUCache;

@RunWith(JUnit4.class)
public class LRUCacheImplUnitTests {

  @Test
  public void putReplace() {
    final int cacheCapacity = 4;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);
    final int key0 = 1;
    final int value0 = 2;
    final int key1 = 3;
    final int value1 = 4;

    assertThat(cache.put(key0, value0)).isNull();
    assertThat(cache.put(key1, value1)).isNull();
    assertThat(cache.put(key0, value1)).isEqualTo(value0);
  }

  @Test
  public void getEmpty() {
    final int cacheCapacity = 4;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);
    final int key = 1;

    assertThat(cache.get(key)).isNull();
  }

  @Test
  public void putGet() {
    final int cacheCapacity = 4;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);
    final int key = 1;
    final int value = 2;

    cache.put(key, value);
    assertThat(cache.get(key)).isEqualTo(value);
  }

  @Test
  public void putReplaceGet() {
    final int cacheCapacity = 4;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);
    final int key = 1;
    final int value0 = 2;
    final int value1 = 3;

    cache.put(key, value0);
    cache.get(key);
    cache.put(key, value1);
    assertThat(cache.get(key)).isEqualTo(value1);
  }

  @Test
  public void testEviction_noHistoryUpdates() {
    final int cacheCapacity = 2;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);
    final int key0 = 1;
    final int value0 = 2;
    final int key1 = 3;
    final int value1 = 4;
    final int key2 = 5;
    final int value2 = 6;

    cache.put(key0, value0);
    cache.put(key1, value1);
    cache.put(key2, value2);
    assertThat(cache.get(key0)).isNull();
    assertThat(cache.get(key1)).isEqualTo(value1);
    assertThat(cache.get(key2)).isEqualTo(value2);
  }

  @Test
  public void testEviction_historyUpdated_1() {
    final int cacheCapacity = 2;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);
    final int key0 = 1;
    final int value0 = 2;
    final int key1 = 3;
    final int value1 = 4;
    final int key2 = 5;
    final int value2 = 6;

    cache.put(key0, value0);
    cache.put(key1, value1);
    cache.get(key0);
    cache.put(key2, value2);
    assertThat(cache.get(key0)).isEqualTo(value0);
    assertThat(cache.get(key1)).isNull();
    assertThat(cache.get(key2)).isEqualTo(value2);
  }

  @Test
  public void testEviction_historyUpdated_2() {
    final int cacheCapacity = 2;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);
    final int key0 = 1;
    final int value0 = 2;
    final int key1 = 3;
    final int value1 = 4;
    final int key2 = 5;
    final int value2 = 6;

    cache.put(key0, value0);
    cache.put(key1, value1);
    cache.get(key1);
    cache.put(key2, value2);
    assertThat(cache.get(key0)).isNull();
    assertThat(cache.get(key1)).isEqualTo(value1);
    assertThat(cache.get(key2)).isEqualTo(value2);
  }

  @Test
  public void singletonCache() {
    final int cacheCapacity = 1;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);
    final int key0 = 1;
    final int value0 = 2;
    final int key1 = 3;
    final int value1 = 4;

    cache.put(key0, value0);
    cache.put(key1, value1);
    assertThat(cache.get(key0)).isNull();
    assertThat(cache.get(key1)).isEqualTo(value1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void putNullKey() {
    final int cacheCapacity = 4;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);
    final int value = 1;

    cache.put(null, value);
  }

  @Test(expected = IllegalArgumentException.class)
  public void putNullValue() {
    final int cacheCapacity = 4;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);
    final int key = 1;

    cache.put(key, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNullKey() {
    final int cacheCapacity = 4;
    final LRUCache<Integer, Integer> cache = new LRUCacheImpl<>(cacheCapacity);

    cache.get(null);
  }
}
