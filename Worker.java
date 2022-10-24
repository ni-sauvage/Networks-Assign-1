import java.io.File;
import java.nio.file.Files;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Worker extends Node {
	static final InetSocketAddress SERVER_PORT = new InetSocketAddress("server", 50001);
	File reqFile;
	byte[] fileContent;
	/*
	 *
	 */
	Worker(int port) {
		try {
			socket= new DatagramSocket(port);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public void onReceipt(DatagramPacket packet) {
		try {

			PacketContent content= PacketContent.fromDatagramPacket(packet);
			System.out.println("Received packet" + content.toString());
			switch (content.getType()) {
				case PacketContent.GETFILE:
					String fname = ((GetFileContent)content).filename;
                    DatagramPacket response;
                    try {
					    reqFile = new File(fname);
					    fileContent = Files.readAllBytes(reqFile.toPath());
                        response = new FileInfoContent(fileContent.length).toDatagramPacket();
                    } catch (Exception e) {
                        response = new NoFile().toDatagramPacket();
                    }
					response.setSocketAddress(packet.getSocketAddress());
					socket.send(response);
					break;
				case PacketContent.ACKPACKET:
					if (fileContent != null) {
						byte[] newData = new byte[fileContent.length - ((AckPacketContent)content).size];
						for(int i = 0; i < newData.length; i++){
							newData[i] = fileContent[((AckPacketContent)content).size + i];
						}
						fileContent = newData;
						if (fileContent.length > PACKETSIZE){
							byte[] slice;
							slice = new byte[PACKETSIZE];
							for(int i = 0; i < PACKETSIZE; i++){
								slice[i] = fileContent[i];
							}
							response = new FileContent(slice).toDatagramPacket();
							
						} else{
							response = new FileContent(fileContent).toDatagramPacket();
							fileContent = null;
						}
						response.setSocketAddress(packet.getSocketAddress());
						socket.send(response);
					}
					break;
			}
		}
		catch(Exception e) {e.printStackTrace();}
	}


	public synchronized void start() throws Exception {
		System.out.println("Waiting for contact");
		DatagramPacket packet;
		packet= new Register().toDatagramPacket();
		packet.setSocketAddress(SERVER_PORT);
		socket.send(packet);
		this.wait();
	}

	/*
	 *
	 */
	public static void main(String[] args) {
		try {
            System.out.println("Please enter the desired port number: ");
            int portNum= Integer.parseInt(System.console().readLine());
			(new Worker(portNum)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
