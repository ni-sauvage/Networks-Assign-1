import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;

public class Server extends Node {
	static final int DEFAULT_PORT = 50001;
	static SocketAddress CLIENT_PORT;
	static SocketAddress WORKER_PORT;
	static int WORKERS_WTIHOUT_FILE = 0;
	ArrayList<InetSocketAddress> workerAddresses = new ArrayList<InetSocketAddress>();
	/*
	 *
	 */
	Server(int port) {
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
					WORKER_PORT = null;
					CLIENT_PORT = packet.getSocketAddress();
					WORKERS_WTIHOUT_FILE = 0;
					for(InetSocketAddress dstAddress : workerAddresses){
						packet.setSocketAddress(dstAddress);
						socket.send(packet);
					}
					break;
				case PacketContent.FILEINFO:
					if(WORKER_PORT == null){
						WORKER_PORT = packet.getSocketAddress();
						packet.setSocketAddress(CLIENT_PORT);
						socket.send(packet);
					}
					break;
				case PacketContent.ACKPACKET:
					packet.setSocketAddress(WORKER_PORT);
					socket.send(packet);
					break;
				case PacketContent.FILESEND:
					packet.setSocketAddress(CLIENT_PORT);
					socket.send(packet);
					break;
				case PacketContent.NOFILE:
					WORKERS_WTIHOUT_FILE += 1;
					if(WORKERS_WTIHOUT_FILE == workerAddresses.size()){
						packet.setSocketAddress(CLIENT_PORT);
						socket.send(packet);
					}
					break;
				case PacketContent.REGISTER:
					workerAddresses.add(
						(InetSocketAddress) packet.getSocketAddress()
					);
					break;
			}
		}
		catch(Exception e) {e.printStackTrace();}
	}


	public synchronized void start() throws Exception {
		System.out.println("Waiting for contact");
		this.wait();
	}

	/*
	 *
	 */
	public static void main(String[] args) {
		try {
			(new Server(DEFAULT_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}