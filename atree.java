import com.parse.ATreeParser;

/**
 * Convenience class for parsing tree selection problem arguments.
 * @author Benjamin Vial (29590765)
 * @see ATreeParser
 */
public class atree {
	/**
	 * Passes command line arguments to an {@code ATreeParser}.
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		new ATreeParser().parse(args);
	}
}
