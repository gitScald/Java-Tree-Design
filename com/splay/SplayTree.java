package com.splay;

import com.splay.opcount.OperationCounter;
import com.tree.BinaryTree;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Splay tree implementation.
 * @author Benjamin Vial (29590765)
 */
public class SplayTree implements BinaryTree<Integer> {
	private OperationCounter opcount;
	private SplayNode root;
	
	/**
	 * Splay tree Node class. Contains a pointer to its left and right children
	 * as well as to its parent.
	 */
	private class SplayNode implements BinaryNode<Integer>, Comparable<SplayNode> {
		private SplayNode left;
		private SplayNode parent;
		private SplayNode right;
		private int val;
		
		/**
		 * Constructor. Takes a parent, left and right Nodes, as well as an
		 * integer value.
		 * @param p Parent
		 * @param l Left child
		 * @param r Right child
		 * @param v Node value
		 */
		public SplayNode(SplayNode p, SplayNode l, SplayNode r, int v) {
			parent = p;
			left = l;
			right = r;
			val = v;
		}
		
		/**
		 * {@code Comparable<T>} method override.
		 */
		@Override
		public int compareTo(SplayNode sn) {
			if (val < sn.val())
				return -1;
			if (val == sn.val())
				return 0;
			return 1;
		}
		
		/**
		 * @return Left child
		 */
		public SplayNode left() {
			return left;
		}
		
		/**
		 * @return Parent
		 */
		public SplayNode parent() {
			return parent;
		}
		
		/**
		 * @return Right child
		 */
		public SplayNode right() {
			return right;
		}
		
		/**
		 * Sets the left child to the specified Node, also setting that Node's
		 * parent pointer if necessary.
		 * @param sn New left child
		 */
		public void setLeft(SplayNode sn) {
			left = sn;
			if (sn != null)
				sn.setParent(this);
		}
		
		/**
		 * Sets the parent to the specified Node.
		 * @param sn New parent
		 */
		public void setParent(SplayNode sn) {
			opcount.parentChange();
			parent = sn;
		}
		
		/**
		 * Sets the right child to the specified Node, also setting that Node's
		 * parent pointer if necessary.
		 * @param sn New right child
		 */
		public void setRight(SplayNode sn) {
			right = sn;
			if (sn != null)
				sn.setParent(this);
		}
		
		/**
		 * Used to display the Node's value, followed by its left and right
		 * children (essentially a preorder traversal display).
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			sb.append(val);
			sb.append(") ");
			if (left != null)
				sb.append("L" + left);
			if (right != null)
				sb.append("R" + right);
			return sb.toString();
		}
		
		/**
		 * @return Node value
		 */
		public int val() {
			return val;
		}
	}
	
	/**
	 * Default constructor. Creates an empty splay tree.
	 */
	public SplayTree() {
		opcount = new OperationCounter();
		root = null;
	}
	
	/**
	 * Used to createc an empty splay tree, then builds its structure according
	 * to the specified input file.
	 * @param file The file to build the tree from
	 */
	public SplayTree(String file) {
		this();
		build(file);
	}
	
	/**
	 * Convenience method for {@code add(n, root)}.
	 * @param n The value to add to the tree
	 * @return The created Node
	 */
	public SplayNode add(int n) {
		opcount.add();
		return add(n, root);
	}
	
	/**
	 * Builds the splay tree according to the operations defined in the file.
	 * @param file The file to build the tree from
	 */
	public void build(String file) {
		System.out.print("Building splay tree from '" + file + "'; processing line ");
		try {
			Scanner sc = new Scanner(new FileInputStream(file));
			int line = 1;
			while (sc.hasNextLine()) {
				System.out.print("" + line + "...");
				String s = sc.nextLine();
				String[] args = { s.substring(0, 1), s.substring(1) };
				switch (args[0]) {
				case "a":
					add(Integer.parseInt(args[1]));
					break;
				case "f":
					find(Integer.parseInt(args[1]));
					break;
				case "r":
					remove(Integer.parseInt(args[1]));
					break;
				default:
					System.out.print("Unknown operation '" + args[0] + "'; will ignore");
					break;
				}
				int bkspaces = 3 + new Integer(line++).toString().length();
				for (int i = 0; i < bkspaces; ++i)
					System.out.print('\b');
			}
			sc.close();
			System.out.println("\b\b\b\b\ball done!   \n");
			System.out.println(stats());
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not establish stream with file '" + file + "'");
			System.exit(1);
		}
	}
	
	/**
	 * Convenience method for {@code find(n, root)}.
	 * @param n The value to search for
	 * @return The Node containing the value ({@code null} if not found)
	 */
	@Override
	public SplayNode find(Integer n) {
		opcount.find();
		return find(n, root);
	}
	
	/**
	 * @return {@code true} if the tree is empty, {@code false} otherwise
	 */
	@Override
	public boolean isEmpty() {
		return root == null;
	}
	
	/**
	 * Convenience method for {@code remove(n, root)}.
	 * @param n The value to search for and remove
	 * @return The Node containing the value ({@code null} if not found)
	 */
	public SplayNode remove(int n) {
		opcount.remove();
		return remove(n, root);
	}
	
	/**
	 * @return Output of the {@code OperationCounter}'s {@code stats()} method
	 */
	public String stats() {
		return opcount.stats();
	}
	
	/**
	 * Used to display the tree's contents.
	 */
	@Override
	public String toString() {
		return root.toString();
	}
	
	/**
	 * Adds the specified value by searching down the given subtree for a
	 * suitable location, then splays it to the top of the tree.
	 * @param n The value to add to the tree
	 * @param sn The subtree to search through
	 * @return The created Node
	 */
	private SplayNode add(int n, SplayNode sn) {
		if (isEmpty()) {
			root = new SplayNode(null, null, null, n);
			return root;
		}
		SplayNode rt;
		// Iterative version, since Java does not optimize tail recursion
		while (true) {
			opcount.compare();
			// Keep going left
			if (n < sn.val()) {
				if (sn.left() != null)
					sn = sn.left();
				// Add as left child
				else {
					rt = new SplayNode(sn, null, null, n);
					splay(rt);
					return rt;
				}
			}
			// Keep going right
			else {
				if (sn.right() != null)
					sn = sn.right();
				// Add as right child
				else {
					rt = new SplayNode(sn, null, null, n);
					splay(rt);
					return rt;
				}
			}
		}
	}
	
	/**
	 * Finds the specified value by searching down the given subtree, then
	 * splays it to the top of the tree. If it cannot be found, the last
	 * visited node is splayed instead.
	 * @param n The value to search for
	 * @param sn The subtree to search through
	 * @return The Node containing the value ({@code null} if not found)
	 */
	private SplayNode find(int n, SplayNode sn) {
		if (sn == null)
			return null;
		// Iterative version, since Java does not optimize tail recursion
		while (true) {
			opcount.compare();
			// Key was found, splay and return
			if (sn.val() == n) {
				splay(sn);
				return sn;
			}
			// Keep going left
			if (n < sn.val()) {
				if (sn.left() != null)
					sn = sn.left();
				else {
					splay(sn);
					return null;
				}
			}
			// Keep going right
			else {
				if (sn.right() != null)
					sn = sn.right();
				else {
					splay(sn);
					return null;
				}
			}
		}
	}
	
	/**
	 * Finds, but does not splay, the maximum value in a given subtree.
	 * @param sn The subtree to search through
	 * @return The Node containing the value
	 */
	private SplayNode findMax(SplayNode sn) {
		if (sn == null)
			return null;
		while (sn.right() != null) {
			sn = sn.right();
		}
		return sn;
	}
	
	/**
	 * Finds and remove the specified value after splaying it to the top of the
	 * tree, searching through the given subtree.
	 * @param n The value to remove
	 * @param sn The subtree to search through
	 * @return The Node containing the value ({@code null} if not found)
	 */
	private SplayNode remove(int n, SplayNode sn) {
		if (sn == null)
			return null;
		SplayNode rt = find(n, sn);
		if (rt == null)
			return null;
		else if (rt.val() == n) {
			SplayNode left = rt.left();
			SplayNode right = rt.right();
			// Tree only had one element
			if (left == null && right == null)
				root = null;
			// Left subtree only
			if (right == null) {
				left.setParent(null);
				root = left;
			}
			// Right subtree only
			else if (left == null) {
				right.setParent(null);
				root = right;
			}
			// Left and right subtrees
			else {
				rt.setLeft(null);
				rt.setRight(null);
				SplayNode snm = findMax(left);
				splay(snm);
				root.setRight(right);
			}
		}
		return rt;
	}
	
	/**
	 * Brings the specified Node to the top of the tree by performing a chain
	 * of rotations.
	 * @param sn The Node to splay
	 * @return The splayed Node
	 */
	private SplayNode splay(SplayNode sn) {
		if (sn.parent() == null)
			return sn;
		SplayNode parent = sn.parent();
		if (root == parent) {
			opcount.compare();
			// Zig right
			if (sn.val() < parent.val()) {
				parent.setLeft(sn.right());
				sn.setRight(parent);
				sn.setParent(null);
			}
			// Zig left
			else {
				parent.setRight(sn.left());
				sn.setLeft(parent);
				sn.setParent(null);
			}
			// Update the root
			root = sn;
		}
		else {
			SplayNode grandparent = sn.parent().parent();
			SplayNode greatgrandparent = sn.parent().parent().parent();
			opcount.compare();
			opcount.compare();
			// Zig-zig right
			if (sn.val() < parent.val() && parent.val() < grandparent.val()) {
				grandparent.setLeft(parent.right());
				parent.setLeft(sn.right());
				parent.setRight(grandparent);
				sn.setRight(parent);
			}
			// Zig-zig left
			else if (sn.val() >= parent.val() && parent.val() >= grandparent.val()) {
				grandparent.setRight(parent.left());
				parent.setLeft(grandparent);
				parent.setRight(sn.left());
				sn.setLeft(parent);
			}
			// Zig-zag left
			else if (sn.val() < parent.val() && parent.val() >= grandparent.val()) {
				grandparent.setRight(sn.left());
				parent.setLeft(sn.right());
				sn.setLeft(grandparent);
				sn.setRight(parent);
			}
			// Zig-zag right
			else {
				grandparent.setLeft(sn.right());
				parent.setRight(sn.left());
				sn.setLeft(parent);
				sn.setRight(grandparent);
			}
			// Update root, or great-grandparent
			if (root == grandparent)
				root = sn;
			else {
				opcount.compare();
				if (grandparent.val() < greatgrandparent.val())
					greatgrandparent.setLeft(sn);
				else
					greatgrandparent.setRight(sn);
				splay(sn);
			}
		}
		return sn;
	}
}
