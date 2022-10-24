import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 *
 */
public class FileInfoContent extends PacketContent {

	int size;

	/**
	 * Constructor that takes in information about a file.
	 * @param filename Initial filename.
	 * @param size Size of filename.
	 */
	FileInfoContent(int size) {
		type= FILEINFO;
		this.size= size;
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected FileInfoContent(ObjectInputStream oin) {
		try {
			type= FILEINFO;
			size= oin.readInt();
		}
		catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeInt(size);
		}
		catch(Exception e) {e.printStackTrace();}
	}


	/**
	 * Returns the content of the packet as String.
	 *
	 * @return Returns the content of the packet as String.
	 */
	public String toString() {
		return "Size: " + size;
	}

	/**
	 * Returns the file size contained in the packet.
	 *
	 * @return Returns the file size contained in the packet.
	 */
	public int getFileSize() {
		return size;
	}
}
