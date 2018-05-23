import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 
 */

/**
 * @author jsuit
 * @param <V>
 * 
 */
public class HashMultiMapImplementation<K extends Comparable<? super K>, V extends Comparable<? super V>>
		implements HashMultiMap<K, V> {

	private int capacity;
	private double MaxloadFactor;
	private Bucket<K, V>[] list;
	private Set<K> keys;
	private int size;

	public HashMultiMapImplementation() {
		capacity = 89;
		MaxloadFactor = .5;
		list = (Bucket<K, V>[]) new Bucket[89];
		keys = new HashSet<K>();
	}

	public HashMultiMapImplementation(int capacity, double loadFactor) {

		this.capacity = capacity;
		if (capacity <= 0) {
			System.out.println("Invalid capacity. Resetting capacity to 89");
			this.capacity = 89;
		}
		this.MaxloadFactor = loadFactor;
		if (this.MaxloadFactor <= 0 || this.MaxloadFactor > 1) {
			System.out.println("Invalid loadFactor. Resetting to default .5");
			this.MaxloadFactor = .5;
		}

		list = (Bucket<K, V>[]) new Bucket[this.capacity];
		keys = new HashSet<K>();
	}

	/**
	 * rehash function: sets new capacity to 2*oldCapacity +1. Goes through the
	 * keys and rehashes them according to new capacity.
	 * 
	 * @return void
	 */
	public void rehash() {
		int oldCapacity = capacity;
		capacity = (2 * capacity) + 1; // newCapacity
		// temp array of buckets
		Bucket<K, V>[] temp = (Bucket<K, V>[]) new Bucket[capacity];

		for (K aKey : keys) {
			int newHash = aKey.hashCode() % capacity;
			int oldHash = aKey.hashCode() % oldCapacity;
			// check to make sure there are no null nodes we cross over
			// and check to see if we are at the right place
			while (list[oldHash] == null || !list[oldHash].key.equals(aKey)) {
				// loop circularly
				if (++oldHash == oldCapacity) {
					oldHash = 0;
				}
			}
			// find the new spot in the new array
			while (temp[newHash] != null) {
				if (++newHash == capacity) {
					newHash = 0;
				}
			}
			// put the value in new array
			temp[newHash] = list[oldHash];
		}
		// make the list point at the new array
		list = temp;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public void clear() {
		for (K aKey : keys) {
			int hash = findIndex(aKey);
			list[hash].key = null;
			list[hash].list = null;
			list[hash] = null;
		}
		size = 0;
		keys.clear();
	}

	@Override
	public List<V> values() {
		List<V> listOfValues = new LinkedList<V>();

		for (K key : keys) {
			int hash = findIndex(key);
			listOfValues.addAll(list[hash].list);
		}

		return listOfValues;
	}

	@Override
	public List<V> values(K key) {
		if (keys.contains(key)) {
			int hash = findIndex(key);
			return list[hash].list;
		} else
			return new LinkedList<V>();
	}

	@Override
	public Set<K> keys() {
		// TODO Auto-generated method stub
		return keys;

	}

	/**
	 * looks for the first null place to put new value and key Only to be called
	 * when you know the key doesn't exist. Otherwise, you could insert
	 * duplicate keys.
	 * 
	 * @return void
	 */
	public void putInPlace(K key, V value, int hash) {
		if (list[hash] == null) {
			list[hash] = new Bucket<K, V>(key, value);
		} else {
			while (list[hash] != null) {
				if (++hash == capacity) {
					hash = 0;
				}
			}
			list[hash] = new Bucket<K, V>(key, value);
		}
	}

	@Override
	public boolean put(K key, V value) {
		// what we think the index(i.e., hash) value is
		int hash = key.hashCode() % capacity;
		boolean truthvalue = true;
		// no keys
		if (keys.isEmpty()) {
			list[hash] = new Bucket<K, V>(key, value);
			keys.add(key);
			// test in case we have already gone over the Maxloadfactor
			if (loadfactor() >= MaxloadFactor) {
				rehash();
			}
			++size; // increase the size by one
			// we have a new key
		} else if (!keys.contains(key)) {
			// function looks for newest null value to insert key, value
			putInPlace(key, value, hash);
			keys.add(key); // add the keys to the set
			// too big? then rehash.
			if (loadfactor() >= MaxloadFactor) {
				rehash();
			}
			++size;
		} else {
			// already have key; check to see if we can add a new value;
			while (list[hash] == null || !list[hash].key.equals(key)) {
				if (++hash == capacity) {
					hash = 0;
				}
			}
			// if it doesn't contain the value add it; otherwise return false
			if (!list[hash].list.contains(value)) {
				list[hash].list.addFirst(value);
				++size;
			} else {
				truthvalue = false;
			}
		}
		// return true if we put something; false otherwise.
		return truthvalue;
	}

	@Override
	public boolean removeAll(K key) {

		if (keys.contains(key)) {
			int hash = findIndex(key);
			// decrease the size by the number of values deleted.
			size -= list[hash].list.size();
			keys.remove(key);
			list[hash] = null;
			return true;
		} else
			return false; // didn't have key
	}

	/**
	 * finds the index of the key. Only to be called if if we know the key is in
	 * the array. Otherwise, we get an infinite loop.
	 * 
	 * @return the total key->value pairs in the table.
	 */
	private int findIndex(K key) {
		// key must be in the array
		int hash = key.hashCode() % capacity;
		while (list[hash] == null || !list[hash].key.equals(key)) {
			if (++hash == list.length) {
				hash = 0;
			}
		}
		return hash;
	}

	@Override
	public boolean remove(K key, V value) {
		if (keys.contains(key)) {
			int hash = findIndex(key);
			if (list[hash].list.size() == 1 && list[hash].list.contains(value)) {
				list[hash].list.remove(value);
				keys.remove(key);
				list[hash] = null;
				--size;

			} else if (list[hash].list.contains(value)) {
				list[hash].list.remove(value);
				--size;

			}
			return true;
		} else
			return false;
	}

	@Override
	public double loadfactor() {
		// TODO Auto-generated method stub
		return ((double) keys.size() / capacity);
	}

	@Override
	public int capacity() {
		// TODO Auto-generated method stub
		return capacity;
	}

	@Override
	public String toString() {
		String s = null;
		StringBuilder j = new StringBuilder("[ ");

		for (K aKey : keys) {
			j.append("<");
			int hash = findIndex(aKey);
			j.append(list[hash].key + ", ");
			for (V element : list[hash].list) {
				j.append(element + ",");
			}
			int index = j.lastIndexOf(",");
			j.replace(index, index + 1, "");
			j.append("> ");

		}
		j.append("]");
		s = j.toString();

		return s;

	}

	private class Bucket<K, V> {
		// start stepping through the array from the beginning
		private K key;
		private LinkedList<V> list = new LinkedList<V>();

		public Bucket(K key, V value) {
			this.key = key;
			list.addLast(value);
		}

	}

}