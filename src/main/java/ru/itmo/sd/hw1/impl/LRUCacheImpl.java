package ru.itmo.sd.hw1.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import ru.itmo.sd.hw1.LRUCache;

/** Standard implementation of {@link ru.itmo.sd.hw1.LRUCache}. */
public class LRUCacheImpl<K, V> implements LRUCache<K, V> {

  private static final int CACHE_DEFAULT_SIZE = 1024;

  private final Map<K, Node<K, V>> map;
  private final HistoryList<K, V> history;
  private final int capacity;

  /** Constructs cache with default cache size {@link this#CACHE_DEFAULT_SIZE}. */
  public LRUCacheImpl() {
    this(CACHE_DEFAULT_SIZE);
  }

  /**
   * Constructs cache with custom cache size.
   *
   * @param capacity max number of entries stored in cache
   */
  public LRUCacheImpl(final int capacity) {
    this.map = new HashMap<>();
    this.history = new HistoryList<>();
    this.capacity = capacity;
  }

  @Override
  @Nullable
  public V put(final K key, final V value) {
    if (Objects.isNull(key)) {
      throw new IllegalArgumentException("Null keys are not supported");
    }
    if (Objects.isNull(value)) {
      throw new IllegalArgumentException("Null values are not supported");
    }
    assert map.size() <= capacity;

    if (map.containsKey(key)) {
      final Node<K, V> oldNode = map.get(key);
      final Node<K, V> newNode = history.update(oldNode, value);
      map.put(key, newNode);
      return oldNode.getValue();
    } else {
      if (size() == capacity) {
        final Node<K, V> evicted = history.evict();
        map.remove(evicted.getKey());
        assert size() == capacity - 1;
      }
      final Node<K, V> newNode = history.add(key, value);
      map.put(key, newNode);
      return null;
    }
  }

  @Override
  @Nullable
  public V get(final K key) {
    if (Objects.isNull(key)) {
      throw new IllegalArgumentException("Null keys are not supported");
    }

    if (!map.containsKey(key)) {
      return null;
    }
    final Node<K, V> oldNode = map.get(key);
    final Node<K, V> newNode = history.update(oldNode, oldNode.getValue());
    map.put(key, newNode);
    return oldNode.getValue();
  }

  private int size() {
    assert map.size() == history.size();
    return map.size();
  }

  /** Usages history one-directional linked list of {@link Node(K, V)}. */
  private static class HistoryList<K, V> {
    @Nullable private Node<K, V> listHead;
    @Nullable private Node<K, V> listTail;
    private int size;

    public HistoryList() {
      this.listHead = this.listTail = null;
      this.size = 0;
    }

    /**
     * Add new node to list head.
     *
     * @param key node key
     * @param value node value
     * @return newly created node
     */
    public Node<K, V> add(final K key, final V value) {
      final Node<K, V> newHead = new Node<>(key, value);
      if (size > 0) {
        assert Objects.nonNull(listHead);

        listHead.setNext(newHead);
        newHead.setPrevious(listHead);
        listHead = newHead;
      } else {
        listHead = listTail = newHead;
      }
      size++;
      return newHead;
    }

    /**
     * Moves node to the history head.
     *
     * @param target node to move
     * @param newValue new associated value
     * @return updated node
     */
    public Node<K, V> update(final Node<K, V> target, final V newValue) {
      assert size > 0;
      assert Objects.nonNull(listHead);
      assert Objects.nonNull(listTail);

      if (listHead == listTail) {
        assert listHead.getKey() == target.getKey();

        listHead = listTail = new Node<>(target.getKey(), newValue);
        return listHead;
      }

      assert target == listHead || Objects.nonNull(target.getNext());
      assert target == listTail || Objects.nonNull(target.getPrevious());

      if (target != listHead) {
        target.getNext().setPrevious(target.getPrevious());
      }
      if (target != listTail) {
        target.getPrevious().setNext(target.getNext());
      }
      if (target == listTail) {
        listTail = target.getNext();
      }
      if (target == listHead) {
        listHead = target.getPrevious();
      }
      size--;

      return add(target.getKey(), newValue);
    }

    /**
     * Remove node in compliance with LRU policy.
     *
     * @return removed node
     */
    public Node<K, V> evict() {
      assert size > 0;
      assert Objects.nonNull(listTail);

      final Node<K, V> evictedNode = listTail;
      if (size == 1) {
        listHead = listTail = null;
      } else {
        assert Objects.nonNull(listTail.getNext());

        listTail = listTail.getNext();
        listTail.setPrevious(null);
      }
      size--;
      return evictedNode;
    }

    public int size() {
      return size;
    }
  }

  /** Node of usages history list. */
  private static class Node<K, V> {
    private final K key;
    private final V value;
    @Nullable private Node<K, V> next;
    @Nullable private Node<K, V> previous;

    public Node(final K key, final V value) {
      assert Objects.nonNull(key);
      assert Objects.nonNull(value);
      this.key = key;
      this.value = value;
      this.next = null;
      this.previous = null;
    }

    public K getKey() {
      return key;
    }

    public V getValue() {
      return value;
    }

    @Nullable
    public Node<K, V> getNext() {
      return next;
    }

    public void setNext(@Nullable Node<K, V> next) {
      this.next = next;
    }

    @Nullable
    public Node<K, V> getPrevious() {
      return previous;
    }

    public void setPrevious(@Nullable Node<K, V> previous) {
      this.previous = previous;
    }
  }
}
