package com.parse;

import com.splay.SplayTree;
import java.util.Scanner;

/**
 * Simple class for feeding command line arguments to the splay tree
 * implementation.
 * @author Benjamin Vial (29590765)
 */
public class ATreeParser implements StdInParser {
	private final String prompt = "Do you wish to see the tree structure? (Y) or (N): ";
	
	/**
	 * Builds a splay tree from the command line arguments.
	 */
	@Override
	public void parse(String[] args) {
		SplayTree st = new SplayTree(args[0]);
		Scanner kb = new Scanner(System.in);
		System.out.print(prompt);
		String s = kb.next();
		while (true) {
			if (s.equalsIgnoreCase("Y")) {
				System.out.println("\nFinal tree structure (pre-order traversal):\n"
						+ "-------------------------------------------\n"
						+ st);
				break;
			}
			else if (s.equalsIgnoreCase("N"))
				break;
			System.out.print(prompt);
			s = kb.next();
		}
		kb.close();
	}
}
