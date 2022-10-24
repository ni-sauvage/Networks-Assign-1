import java.io.ObjectOutputStream;

/**
 * Class for packet content that indicates file is not on machine
 *
 */
public class NoFile extends PacketContent {

	/**
	 * Constructor which does not take in parameters
	 */
NoFile() {
	super.type = PacketContent.NOFILE;
	/*No Content to display, simply signals file not found on machine */
}

	protected void toObjectOutputStream(ObjectOutputStream out) {}

	public String toString() {
		return "No File";
	}
}
