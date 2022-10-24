import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 *
 */
public class GetFileContent extends PacketContent {

	String filename;

	/**
	 * Constructor that takes in information about a file.
	 * @param filename Initial filename.
	 */
	GetFileContent(String filename) {
		type= GETFILE;
		this.filename = filename;
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected GetFileContent(ObjectInputStream oin) {
		try {
			type= GETFILE;
			filename= oin.readUTF();
		}
		catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeUTF(filename);
		}
		catch(Exception e) {e.printStackTrace();}
	}


	/**
	 * Returns the content of the packet as String.
	 *
	 * @return Returns the content of the packet as String.
	 */
	public String toString() {
		return "Filename: " + filename;
	}

	/**
	 * Returns the file name contained in the packet.
	 *
	 * @return Returns the file name contained in the packet.
	 */
	public String getFileName() {
		return filename;
	}
}
