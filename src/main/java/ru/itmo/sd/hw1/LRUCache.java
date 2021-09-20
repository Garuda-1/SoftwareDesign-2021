package ru.itmo.sd.hw1;

import javax.annotation.Nullable;

/**
 * Interface of cache with LRU (Least Recently Used) eviction policy.
 *
 * @param <K> key type
 * @param <V> value type
 */
public interface LRUCache<K, V> {

  /**
   * Creates a new or replaces an already existing association between key and value.
   *
   * @param key key of association
   * @param value value of association
   * @return previously associated with key value or {@code null} otherwise
   */
  @Nullable
  V put(final K key, final V value);

  /**
   * Gets an associated value for the given key.
   *
   * @param key key to look for associated value with
   * @return associated value or {@code null} if key is not present in cache
   */
  @Nullable
  V get(final K key);
}
