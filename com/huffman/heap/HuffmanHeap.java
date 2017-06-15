package com.huffman.heap;

import com.huffman.HuffmanTree;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Min-heap implementation. Used in building a Huffman coding tree.
 * @author Benjamin Vial (29590765)
 * @see HuffmanTree
 */
public class HuffmanHeap {
	private int count;
	private HuffmanTree[] heap;
	private int heapSize;
	
	/**
	 * Entry of a {@code Map}. Used in building character and frequency arrays
	 * to build the min-heap from.
	 */
	private class Entry {
		private char val;
		private int weight;
		
		/**
		 * Constructor. Takes a character and an integer value.
		 * @param c Character value
		 * @param w Character weight
		 */
		public Entry(char c, int w) {
			val = c;
			weight = w;
		}
		
		/**
		 * @return Character value
		 */
		public char val() {
			return val;
		}
		
		/**
		 * @return Character weight
		 */
		public int weight() {
			return weight;
		}
		
		/**
		 * Sets this entry's character weight to the specified value.
		 * @param w New character weight
		 */
		public void setWeight(int w) {
			weight = w;
		}
	}
	
	/**
	 * Simple map implementation. Used in building character and frequency
	 * arrays to build the min-heap from.
	 */
	private class Map {
		private int count;
		private Entry[] map;
		private int mapSize;
		private static final int DEFAULT_SIZE = 35;

		/**
		 * Default constructor. Build a map with the default size.
		 */
		public Map() {
			this(DEFAULT_SIZE);
		}
		
		/**
		 * Parameterized constructor. Builds a map with the specified size.
		 * @param n Map size
		 */
		public Map(int n) {
			mapSize = n;
			map = new Entry[mapSize];
		}
		
		/**
		 * Adds a new {@code Entry} to the map.
		 * @param c Character value
		 * @param w Character weight
		 */
		public void add(char c, int w) {
			add(new Entry(c, w));
		}
		
		/**
		 * Adds a new {@code Entry} to the map.
		 * @param e Entry to add
		 */
		public void add(Entry e) {
			if (count == mapSize)
				expand();
			map[count++] = e;
		}
		
		/**
		 * Checks whether the map contains the specified character.
		 * @param c Character to check
		 * @return {@code true} if the character is found, {@code false}
		 * otherwise
		 */
		public boolean contains(char c) {
			if (indexOf(c) >= 0)
				return true;
			return false;
		}
		
		/**
		 * Finds the map index of the specified character.
		 * @param c Character to check
		 * @return Map index of the character
		 */
		public int indexOf(char c) {
			for (int i = 0; i < count; ++i)
				if (map[i] != null && map[i].val() == c)
					return i;
			return -1;
		}
		
		/**
		 * @return Array of map entries
		 */
		public Entry[] map() {
			return map;
		}
		
		/**
		 * @return Number of elements in the map
		 */
		public int size() {
			return count;
		}
		
		/**
		 * Trims the map to its utilized capacity.
		 */
		public void trim() {
			mapSize = count;
			Entry[] m = new Entry[mapSize];
			for (int i = 0; i < count; ++i)
				m[i] = map[i];
			map = m;
			m = null;
		}
		
		/**
		 * Used to expand the map when full.
		 */
		private void expand() {
			mapSize *= 2;
			Entry[] m = new Entry[mapSize];
			for (int i = 0; i < count; ++i)
				m[i] = map[i];
			map = m;
			m = null;
		}
	}
	
	/**
	 * Quicksort implementation using median-of-three pivot selection. Used to
	 * display the frequency table more neatly. Also because recycling is cool.
	 */
	private static final class QSMedian {
		
		/**
		 * Computes the median-of-three of a partition for pivot selection.
		 * @param a Array of comparables to sort
		 * @param left Start index
		 * @param right End index
		 * @return Index of the median, positioned as the second-to-last element
		 */
		private static int median(Entry[] a, int left, int right) {
			int mid = (left+right)/2;
			if (a[left].val() > a[mid].val())
				swap(a, left, mid);
			if (a[left].val() > a[right].val())
				swap(a, left, right);
			if (a[mid].val() > a[right].val())
				swap(a, mid, right);
			swap(a, mid, right-1);
			return right-1;
		}
		
		/**
		 * Partitions a given array around a pivot, placing smaller elements to the
		 * left and larger elements to the right
		 * @param a Array of entries to sort
		 * @param left Start index
		 * @param right End index
		 * @return Index of the last sorted element to allow for recursion
		 */
		private static int partition(Entry[] a, int left, int right) {
			char pivot = a[median(a, left, right)].val();
			while (left <= right) {
				while (a[left].val() < pivot)
					++left;
				while (a[right].val() > pivot)
					--right;
				if (left <= right) {
					swap(a, left, right);
					++left;
					--right;
				}
			}
			return left;
		}
		
		/**
		 * Recursive quicksort algorithm.
		 * @param a Array of entries to sort
		 * @param left Start index
		 * @param right End index
		 */
		private static void sort(Entry[] a, int left, int right) {
			int k = partition(a, left, right);
			if (left < k-1)
				sort(a, left, k-1);
			if (k < right)
				sort(a, k, right);
		}
		
		/**
		 * Swaps two elements in an array.
		 * @param a Array of comparables
		 * @param i First index to swap
		 * @param j Second index to swap
		 */
		private static void swap(Entry[] a, int i, int j) {
			Entry temp = a[i];
			a[i] = a[j];
			a[j] = temp;
			temp = null;
		}
	}
	
	/**
	 * Used to build a min-heap from given character and frequency arrays.
	 * @param h Character array
	 * @param w Frequency array
	 */
	public HuffmanHeap(char[] h, int[] w) {
		if (h.length != w.length)
			throw new RuntimeException("Invalid constructor input");
		heapSize = h.length;
		heap = new HuffmanTree[heapSize];
		for (int i = 0; i < heapSize; ++i) {
			heap[i] = new HuffmanTree(h[i], w[i]);
			++count;
		}
		heapify();
	}
	
	/**
	 * Used to build a min-heap from an array of Huffman coding trees.
	 * @param ht Huffman coding tree array
	 */
	public HuffmanHeap(HuffmanTree[] ht) {
		if (heapSize < ht.length)
			expand();
		for (int i = 0; i < ht.length; ++i) {
			heap[i] = ht[i];
			++count;
		}
		heapify();
	}
	
	/**
	 * Used to build a min-heap from a given input file.
	 * @param file The file to build the min-heap from
	 */
	public HuffmanHeap(String file) {
		Map m = process(file);
		QSMedian.sort(m.map(), 0, m.size()-1);
		heapSize = m.size();
		char[] c = new char[heapSize];
		int[] w = new int[heapSize];
		System.out.println("Character\tFrequency\n---------\t---------");
		for (int i = 0; i < heapSize; ++i) {
			c[i] = m.map()[i].val();
			w[i] = m.map()[i].weight();
			System.out.println("" + Character.toUpperCase(c[i]) + "\t\t" + w[i]);
		}
		System.out.println();
		m = null;
		heap = new HuffmanTree[heapSize];
		for (int i = 0; i < heapSize; ++i) {
			heap[i] = new HuffmanTree(c[i], w[i]);
			++count;
		}
		heapify();
	}
	
	/**
	 * Inserts a Huffman coding tree into the min-heap. Used while constructing
	 * a complete Huffman coding tree from smaller ones.
	 * @param ht Huffman coding tree to insert
	 */
	public void insert(HuffmanTree ht) {
		if (count == heapSize)
			expand();
		int pos = count++;
		heap[pos] = ht;
		siftUp(pos);
	}
	
	/**
	 * Checks whether the min-heap is empty.
	 * @return {@code true} if empty, {@code false} otherwise
	 */
	public boolean isEmpty() {
		return count == 0;
	}
	
	/**
	 * Removes the first element from the min-heap and returns it. Ensures that
	 * the heap property is satisfied after the removal.
	 * @return First element of the min-heap
	 */
	public HuffmanTree removeMin() {
		if (isEmpty())
			throw new NoSuchElementException("Heap is empty");
		HuffmanTree ht = heap[0];
		heap[0] = heap[--count];
		heap[count] = null;
		if (!isEmpty())
			siftDown(0);
		return ht;
	}
	
	/**
	 * @return Number of elements in the heap
	 */
	public int size() {
		return count;
	}
	
	/**
	 * Used to display the min-heap's contents.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; ++i)
			sb.append(heap[i]);
		return sb.toString();
	}
	
	/**
	 * Used to expand the heap when full.
	 */
	private void expand() {
		heapSize *= 2;
		HuffmanTree[] h = new HuffmanTree[heapSize];
		for (int i = 0; i < heapSize; ++i)
			h[i] = heap[i];
		heap = h;
		h = null;
	}
	
	/**
	 * Sifts down elements starting from the middle of the heap up to maintain
	 * the heap property.
	 */
	private void heapify() {
		for (int i = (count-1)/2; i >= 0; --i)
			siftDown(i);
	}
	
	/**
	 * Checks whether the specified heap position is a leaf.
	 * @param pos Position to check
	 * @return {@code true} if the position is a leaf, {@code false} otherwise
	 */
	private boolean isLeaf(int pos) {
		return (pos >= count/2) && (pos < count);
	}
	
	/**
	 * @param pos Position to get the left child from
	 * @return Left child position
	 */
	private int left(int pos) {
		if (pos >= count/2)
			throw new NoSuchElementException("Position has no left child");
		return 2*pos+1;
	}
	
	/**
	 * @param pos Position to get the parent from
	 * @return Parent position
	 */
	private int parent(int pos) {
		if (pos == 0)
			throw new NoSuchElementException("Position has no parent");
		return (pos-1)/2;
	}
	
	/**
	 * Processes the specified file to build a map of characters and their
	 * frequencies, later used to build the min-heap from.
	 * @param file The file to build the map from
	 * @return Resulting map
	 */
	private Map process(String file) {
		Map m = null;
		try {
			Scanner sc = new Scanner(new FileInputStream(file));
			sc.useDelimiter("\\|");
			m = new Map();
			String s = null;
			while (sc.hasNext()) {
				s = sc.next();
				for (char c : s.toCharArray()) {
					if (m.contains(c)) {
						int i = m.indexOf(c);
						m.map()[i].setWeight(m.map()[i].weight()+1);
					}
					else if (c != '\n')
						m.add(c, 1);
				}
			}
			m.trim();
			sc.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not establish stream with file '" + file + "'");
			System.exit(1);
		}
		return m;
	}
	
	/**
	 * @param pos Position to get the right child from
	 * @return Right child position
	 */
	@SuppressWarnings("unused")
	private int right(int pos) {
		if (pos >= (count-1)/2)
			throw new NoSuchElementException("Position has no right child");
		return 2*pos+2;
	}
	
	/**
	 * Sifts down a heap element if it does not respect the heap property.
	 * @param pos Element position
	 */
	private void siftDown(int pos) {
		while (!isLeaf(pos)) {
			int small = left(pos);
			if (small < (count-1) && heap[small].compareTo(heap[small+1]) > 0)
				++small;
			if (heap[pos].compareTo(heap[small]) <= 0)
				return;
			swap(pos, small);
			pos = small;
		}
	}
	
	/**
	 * Sifts up a heap element if it does not respect the heap property.
	 * @param pos Element position
	 */
	private void siftUp(int pos) {
		while (pos != 0 && heap[pos].compareTo(heap[parent(pos)]) < 0) {
			swap(pos, parent(pos));
			pos = parent(pos);
		}
	}
	
	/**
	 * Swaps two elements within the heap.
	 * @param i First position
	 * @param j Second position
	 */
	private void swap(int i, int j) {
		HuffmanTree n = heap[j];
		heap[j] = heap[i];
		heap[i] = n;
		n = null;
	}
}
