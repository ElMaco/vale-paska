package vale;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class vpServer {
	
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	
	private static final int maxClients = 4;
	
	public static void main(String args[]) {
		
		int serverPort = 9999;
		
		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
		      System.out.println(e);
		}
		
		
		while (true)
		{
			try
			{
				clientSocket = serverSocket.accept();
				
				
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

}
