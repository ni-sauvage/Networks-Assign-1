/**
 *
 */
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

/**
 *
 * Client class
 *
 * An instance accepts user input
 *
 */
public class Client extends Node {
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "server";
	File reqFile;
	byte[] fileData;
	int bytesRec = 0;
	InetSocketAddress dstAddress;
	String fname;

	/**
	 * Constructor
	 *
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	Client(String dstHost, int dstPort, int srcPort) {
		try {
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			socket= new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}


	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		DatagramPacket response;
		System.out.println("Recieved packet: " + content.toString());
		switch (content.type) {
			case PacketContent.FILEINFO:
				System.out.println("It was a FILEINFO CONTENT");
				fileData = new byte [((FileInfoContent) content).size];
				response= new AckPacketContent("OK - Received this", 0).toDatagramPacket();
				response.setSocketAddress(packet.getSocketAddress());
				try {
					socket.send(response);
					System.out.println("Send ACKPACKET");
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case PacketContent.FILESEND:
				for(int i = 0; i < ((FileContent)content).file.length; i++){
					fileData[i + bytesRec] = ((FileContent)content).file[i];
				}
				bytesRec += ((FileContent)content).file.length;
				if(bytesRec == fileData.length){
					try {
						reqFile.createNewFile();
						Files.write(reqFile.toPath(), fileData);
					} catch (Exception e){
						e.printStackTrace();
						System.out.println("An exception occurred creating new file: " + e);
					}
					reqFile = null;
					fileData = null;
					fname = null;
					bytesRec = 0;
					this.notify();
				}
				response= new AckPacketContent("OK - Received this", ((FileContent)content).file.length).toDatagramPacket();
				response.setSocketAddress(packet.getSocketAddress());
				try {
					socket.send(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case PacketContent.NOFILE:
				System.out.println("No file with that name could be found");
				reqFile = null;
				fileData = null;
				fname = null;
				bytesRec = 0;
				this.notify();
				break;
		}
	}


	/**
	 * Sender Method
	 *
	 */
	public synchronized void start() throws Exception {

		GetFileContent fcontent;
		DatagramPacket packet= null;

		fname= System.console().readLine("Name of file: ");

		reqFile= new File(fname);				// Reserve buffer for length of file and read file

		fcontent= new GetFileContent(fname);

		System.out.println("Sending packet w/ name" + fname); // Send packet with file name
		packet= fcontent.toDatagramPacket();
		packet.setSocketAddress(dstAddress);
		socket.send(packet);
		System.out.println("Packet sent");
		this.wait();
	}


	/**
	 * Test method
	 *
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {
			(new Client(DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
