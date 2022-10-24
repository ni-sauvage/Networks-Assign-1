import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 *
 */
public class NoFile extends PacketContent {

	/**
	 * Constructor that takes in information about a file.
	 * @param filename Initial filename.
	 * @param size Size of filename.
	 */
NoFile() {
	super.type = PacketContent.NOFILE;
	/*No Content to display, simply signals end of file. */
}

	protected void toObjectOutputStream(ObjectOutputStream out) {}

	public String toString() {
		return "No File";
	}
}
