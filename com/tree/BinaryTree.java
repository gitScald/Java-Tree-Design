package com.tree;

/**
 * Abstract, flexible binary tree ADT for solving both the Huffman coding tree
 * and the tree selection problems.
 * @author Benjamin Vial (29590765)
 * @param <T> Node contents
 */
public interface BinaryTree<T> {	
	/**
	 * Binary tree Node interface.
	 * @param <T> Node contents
	 */
	public interface BinaryNode<T> { }
	
	/**
	 * Searches through the tree for a specified value, returning the Node
	 * containing the value.
	 * @param v The value to search for
	 * @return The Node containing the value
	 */
	public BinaryNode<T> find(T v);
	
	/**
	 * Checks whether the tree is empty (i.e., its root is null).
	 * @return {@code true} if the tree is empty, {@code false} otherwise
	 */
	public boolean isEmpty();
}
