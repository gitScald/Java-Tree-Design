package com.splay.opcount;

import java.text.NumberFormat;

/**
 * Simple operation counter for the splay tree implementation. Keeps a running
 * count of certain operations for easy output.
 * @author Benjamin Vial (29590765)
 * @see SplayTree
 */
public class OperationCounter {
	private int countAdd,
				countCompare,
				countFind,
				countParentChange,
				countRemove = 0;
	private final String HEADER = "Total number of tree operations:\n"
			+ "--------------------------------\n";
	private final NumberFormat NF = NumberFormat.getNumberInstance();
	private StringBuilder stats = new StringBuilder();
	
	/**
	 * Constructor.
	 */
	public OperationCounter() {
		stats.append(HEADER);
	}
	
	/**
	 * Increments the counter for the add operations.
	 */
	public void add() {
		++countAdd;
	}
	
	/**
	 * Increments the counter for the compare operations.
	 */
	public void compare() {
		++countCompare;
	}
	
	/**
	 * Increments the counter for the find operations.
	 */
	public void find() {
		++countFind;
	}
	
	/**
	 * Increments the counter for the parent change operations.
	 */
	public void parentChange() {
		++countParentChange;
	}
	
	/**
	 * Increments the counter for the remove operations.
	 */
	public void remove() {
		++countRemove;
	}
	
	/**
	 * @return Operation count for display purposes
	 */
	public String stats() {
		stats.append("Add\t\t\t" + NF.format(countAdd) + "\n");
		stats.append("Compare\t\t\t" + NF.format(countCompare) + "\n");
		stats.append("Find\t\t\t" + NF.format(countFind) + "\n");
		stats.append("Parent change\t\t" + NF.format(countParentChange) +"\n");
		stats.append("Remove\t\t\t" + NF.format(countRemove) + "\n");
		return stats.toString();
	}
}
