package com.parse;

import com.huffman.HuffmanTree;

/**
 * Simple class for feeding command line arguments to the Huffman tree
 * implementation.
 * @author Benjamin Vial (29590765)
 */
public class HuffmanParser implements StdInParser {
	/**
	 * Builds a Huffman tree from the command line arguments which can be used
	 * to encode user input. Also optionally encodes a student-specific string
	 * from a file, if the specified ID is found.
	 */
	@Override
	public void parse(String[] args) {
		HuffmanTree ht = HuffmanTree.build(args[0]);
		if (args.length == 3)
			System.out.println(ht.encodeFromFile(args[1], args[2]));
		ht.getInput();
	}
}
