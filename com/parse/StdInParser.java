package com.parse;

/**
 * Interface for parsing command line arguments.
 * @author Benjamin Vial (29590765)
 * @see ATreeParser, HuffmanParser
 */
public interface StdInParser {
	/**
	 * Parse command line arguments
	 * @param args Command line arguments
	 */
	public void parse(String[] args);
}
