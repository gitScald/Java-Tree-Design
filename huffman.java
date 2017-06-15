import com.parse.HuffmanParser;

/**
 * Convenience class for parsing Huffman coding tree arguments.
 * @author Benjamin Vial (29590765)
 * @see HuffmanParser
 */
public class huffman {
	/**
	 * Passes command line arguments to a {@code HuffmanParser}.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		new HuffmanParser().parse(args);
	}
}
