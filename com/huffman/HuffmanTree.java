package com.huffman;

import com.huffman.heap.HuffmanHeap;
import com.tree.BinaryTree;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Huffman coding tree implementation. Uses an auxiliary min-heap to build
 * itself.
 * @author Benjamin Vial (29590765)
 * @see HuffmanHeap
 */
public class HuffmanTree implements BinaryTree<Character>, Comparable<HuffmanTree> {
	private HuffmanNode root;
	
	/**
	 * Huffman internal Node. Contains a pointer to its left and right children
	 * as well as their combined weight.
	 */
	private class HuffmanInternal extends HuffmanNode {
		private HuffmanNode left;
		private HuffmanNode right;
		
		/**
		 * Constructor. Takes a left and a right Node, as well as their
		 * combined weight as parameters.
		 * @param l Left child
		 * @param r Right child
		 * @param w Combined weight of the children
		 */
		public HuffmanInternal(HuffmanNode l, HuffmanNode r, int w) {
			super(w);
			left = l;
			right = r;
		}
		
		/**
		 * @return Left child
		 */
		public HuffmanNode left() {
			return left;
		}
		
		/**
		 * @return Right child
		 */
		public HuffmanNode right() {
			return right;
		}
		
		/**
		 * Used to display the Node's weight, followed by its left and right
		 * children (essentially a preorder traversal display).
		 */
		@Override
		public String toString() {
			return "(" + weight + ") " + "L" + left + "R" + right;
		}
	}
	
	/**
	 * Huffman leaf Node. Contains a character value as well as its weight.
	 */
	private class HuffmanLeaf extends HuffmanNode {
		private char val;
		
		/**
		 * Constructor. Takes a character and an integer value.
		 * @param c Encoded character
		 * @param w Character weight
		 */
		public HuffmanLeaf(char c, int w) {
			super(w);
			val = c;
		}
		
		/**
		 * Used to display the Node's weight followed by its character value.
		 */
		@Override
		public String toString() {
			return "(" + weight + "-" + val + ") ";
		}
		
		/**
		 * @return Encoded character
		 */
		public char val() {
			return val;
		}
	}
	
	/**
	 * Abstract HuffmanNode class. Extended by {@code HuffmanInternal} and
	 * {@code HuffmanLeaf}.
	 * @see HuffmanInternal, HuffmanLeaf
	 */
	abstract private class HuffmanNode implements BinaryNode<Character>, Comparable<HuffmanNode> {
		protected int weight;
		
		/**
		 * Constructor.
		 * @param w Node weight
		 */
		public HuffmanNode(int w) {
			weight = w;
		}
		
		/**
		 * {@code Comparable<T>} method override.
		 */
		@Override
		public int compareTo(HuffmanNode hn) {
			if (weight < hn.weight())
				return -1;
			if (weight == hn.weight())
				return 0;
			return 1;
		}
		
		/**
		 * @return Node weight
		 */
		public int weight() {
			return weight;
		}
	}
	
	/**
	 * Used to build a Huffman coding tree from given character and frequency
	 * arrays.
	 * @param c Character array
	 * @param w Frequency array
	 * @return Resulting Huffman coding tree
	 */
	public static HuffmanTree build(char[] c, int[] w) {
		return build(new HuffmanHeap(c, w));
	}
	
	/**
	 * Used to build a Huffman coding tree from a given input file.
	 * @param file The file to build the tree from
	 * @return Resulting Huffman coding tree
	 */
	public static HuffmanTree build(String file) {
		System.out.println("Building Huffman coding tree from '" + file + "'\n");
		return build(new HuffmanHeap(file));
	}
	
	/**
	 * Used to build a Huffman coding tree from an auxiliary min-heap.
	 * @param hh Custom min-heap
	 * @return Resulting Huffman coding tree
	 */
	private static HuffmanTree build(HuffmanHeap hh) {
		HuffmanTree ht1, ht2, ht3 = null;
		while (hh.size() > 1) {
			ht1 = hh.removeMin();
			ht2 = hh.removeMin();
			ht3 = new HuffmanTree(ht1, ht2);
			hh.insert(ht3);
		}
		hh = null;
		ht1 = ht2 = null;
		return ht3;
	}
	
	/**
	 * Used to construct a Huffman coding tree from a single character entry.
	 * @param c Encoded character
	 * @param w Character weight
	 */
	public HuffmanTree(char c, int w) {
		root = new HuffmanLeaf(c, w);
	}
	
	/**
	 * Used to construct a Huffman coding tree by joining two Nodes under the
	 * same root.
	 * @param l Left child
	 * @param r Right child
	 * @param w Combined weight of the children
	 */
	public HuffmanTree(HuffmanNode l, HuffmanNode r, int w) {
		root = new HuffmanInternal(l, r, w);
	}
	
	/**
	 * Used to construct a Huffman coding tree by merging two smaller trees.
	 * @param ht1 Left subtree
	 * @param ht2 Right subtree
	 */
	public HuffmanTree(HuffmanTree ht1, HuffmanTree ht2) {
		root = new HuffmanInternal(ht1.root(), ht2.root(), ht1.weight() + ht2.weight());
	}
	
	/**
	 * {@code Comparable<T>} method override.
	 */
	@Override
	public int compareTo(HuffmanTree ht) {
		if (root.weight < ht.root().weight())
			return -1;
		if (root.weight == ht.root().weight())
			return 0;
		return 1;
	}
	
	/**
	 * Checks whether the specified subtree contains the specified character.
	 * More explicit form of: {@code find(c)}.
	 * @param c Character to check for
	 * @return {@code true} if the character is found, {@code false} otherwise
	 */
	public boolean contains(char c) {
		if (find(c, root) != null)
			return true;
		return false;
	}
	
	/**
	 * Decodes the specified string of {@code 0}'s and {@code 1}'s.
	 * @param s String to decode
	 * @return Decoded string
	 */
	public String decode(String s) {
		if (isEmpty() || s.length() == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		HuffmanNode hn = root;
		for (char c : s.toCharArray()) {
			if (hn instanceof HuffmanInternal) {
				HuffmanInternal hi = (HuffmanInternal) hn;
				if (c == '0') {
					hn = hi.left();
					if (hn instanceof HuffmanLeaf) {
						HuffmanLeaf hl = (HuffmanLeaf) hn;
						sb.append(hl.val());
						hn = root;
					}
				}
				else if (c == '1') {
					hn = hi.right();
					if (hn instanceof HuffmanLeaf) {
						HuffmanLeaf hl = (HuffmanLeaf) hn;
						sb.append(hl.val());
						hn = root;
					}
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * Encodes the specified string.
	 * @param s String to encode
	 */
	public String encode(String s) {
		if (isEmpty() || s.length() == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		for (char c : s.toCharArray()) {
			c = Character.toLowerCase(c);
			if (!contains(c))
				throw new NoSuchElementException("Character not found in coding tree: " + c);
			encode(c, root, sb);
		}
		return sb.toString();
	}
	
	/**
	 * Searches through the specified file for a student-specific string, which
	 * will then be encoded and output to the user.
	 * @param file File to search through
	 * @return Encoded string
	 */
	public String encodeFromFile(String file, String id) {
		String s = null;
		try {
			Scanner sc = new Scanner(new FileInputStream(file));
			sc.useDelimiter("\\|");
			while (sc.hasNextLine()) {
				s = sc.nextLine();
				if (s.substring(0, 8).equals(id)) {
					s = s.substring(9);
					System.out.println("Encoding \"" + s + "\"");
					s = encode(s) + "\n";
					sc.close();
					return s;
				}
			}
			sc.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not establish stream with file '" + file + "'");
			System.exit(1);
		}
		s = "Could not find student ID '" + id + "' in file '" + file + "'";
		return s;
	}
	
	/**
	 * Finds the specified character within the whole tree.
	 * Shorthand form of: {@code find(c, root)}.
	 * @param c Character to look for
	 * @return {@code HuffmanLeaf} Node containing the character ({@code null}
	 * if not found)
	 */
	@Override
	public HuffmanLeaf find(Character c) {
		return find(c, root);
	}
	
	/**
	 * Reads from stdin to encode user input.
	 */
	public void getInput() {
		System.out.print("Enter a string to encode using Huffman coding: ");
		Scanner kb = new Scanner(System.in);
		String s = kb.nextLine();
		while (!s.equals("/exit")) {
			System.out.println("\"" + s + "\"\n" + encode(s) + "\n");
			System.out.print("Enter another string, or '/exit' to quit: ");
			s = kb.nextLine();
		}
		System.out.println("Terminating Huffman coding program");
		kb.close();
	}
	
	/**
	 * Checks whether the tree is empty (i.e., its root is null).
	 * @return {@code true} if empty, {@code false} otherwise.
	 */
	@Override
	public boolean isEmpty() {
		return root == null;
	}
	
	/**
	 * @return Root of the tree
	 */
	public HuffmanNode root() {
		return root;
	}
	
	/**
	 * Used to display the tree's contents.
	 */
	@Override
	public String toString() {
		return root.toString();
	}
	
	/**
	 * @return Weight of the root
	 */
	public int weight() {
		return root.weight();
	}
	
	/**
	 * Checks whether the specified subtree contains the specified character.
	 * More explicit form of: {@code find(c, hn)}.
	 * @param c Character to check for
	 * @param hn Subtree to check in
	 * @return {@code true} if the character is found, {@code false} otherwise
	 */
	private boolean contains(char c, HuffmanNode hn) {
		if (find(c, hn) != null)
			return true;
		return false;
	}
	
	/**
	 * Encodes the specified character by going down the specified subtree.
	 * @param c Character to encode
	 * @param hn Subtree to traverse
	 * @param sb StringBuilder used to build the Huffman code
	 */
	private void encode(char c, HuffmanNode hn, StringBuilder sb) {
		if (hn == null)
			return;
		if (contains(c, hn)) {
			if (hn instanceof HuffmanLeaf)
				return;
			HuffmanInternal hi = (HuffmanInternal) hn;
			if (contains(c, hi.left())) {
				sb.append('0');
				encode(c, hi.left(), sb);
			}
			else if (contains(c, hi.right())) {
				sb.append('1');
				encode(c, hi.right(), sb);
			}
		}
	}
	
	/**
	 * Finds the specified character within the specified subtree.
	 * @param c Character to look for
	 * @param hn Subtree to look in
	 * @return {@code HuffmanLeaf} Node containing the character ({@code null}
	 * if not found)
	 */
	private HuffmanLeaf find(char c, HuffmanNode hn) {
		if (hn == null)
			return null;
		if (hn instanceof HuffmanInternal) {
			HuffmanInternal hi = (HuffmanInternal) hn;
			HuffmanLeaf hl1 = find(c, hi.left());
			if (hl1 != null && hl1.val() == c)
				return hl1;
			HuffmanLeaf hl2 = find(c, hi.right());
			if (hl2 != null && hl2.val() == c)
				return hl2;
			else
				return null;
		}
		else {
			HuffmanLeaf hl = (HuffmanLeaf) hn;
			if (hl.val() == c)
				return hl;
			else
				return null;
		}
	}
}
