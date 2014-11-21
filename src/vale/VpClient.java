package vale;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class VpClient implements Runnable {
	private static VpClientGui gui;
	
	// The client socket
	private static Socket clientSocket = null;
	// The output stream
	private static PrintStream os = null;
	// The input stream
	private static BufferedReader is = null;
	
	private static boolean connected = false;
	
	
	public VpClient()
	{
		
	}
	
	
	public VpClient(VpClientGui gui)
	{
		VpClient.gui = gui;
		
		gui.addPelaaListener(new PelaaListener());
		gui.addEpailenListener(new EpailenListener());
		gui.addKaatuuListener(new KaatuuListener());
		gui.addConnectListener(new ConnectListener());
		
		
	}
	
	private class PelaaListener implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0) {
			
			int[] valitutKortit = gui.getValitutKortit();
			
			String valitutIndeksit = "";
			for (int i=0; i<valitutKortit.length; i++)
				valitutIndeksit = valitutIndeksit + valitutKortit[i] + " ";
			
			String s_vaite = gui.getVaite();
			int vaite = Integer.parseInt(s_vaite);
			
			if (os != null)
				os.println("P I " + valitutIndeksit + "V "+vaite);
			
			
			//gui.setPakkaText(""+vp.getNostoKoko());
			//gui.setVuoroText(""+vp.getVuoro());
			//gui.setVaiteText(valitutKortit.length + " " + vaite);
			
		}
		
	}
	
	
	private class EpailenListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if (os != null)
				os.println("Epäilen!");
		}
	}
	
	
	private class KaatuuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			
		}
	}
	
	
	private class ConnectListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if (!connected) {
				// The default port.
				int portNumber = 9999;
				// The default host.
				String host = "localhost";
				try {
					clientSocket = new Socket(host, portNumber);
					os = new PrintStream(clientSocket.getOutputStream());
					is = new BufferedReader(new InputStreamReader(
							clientSocket.getInputStream()));
				} catch (UnknownHostException ex) {
					System.err.println("Don't know about host " + host);
				} catch (IOException ex) {
					System.err
							.println("Couldn't get I/O for the connection to the host "
									+ host);
				}
				if (clientSocket != null && os != null && is != null) {

					/* Create a thread to read from the server. */
					new Thread(new VpClient()).start();

				}
				connected = true;
				gui.setConnectText("Disconnect");
				
			}
			else {
				try {
					os.close();
			        is.close();
			        clientSocket.close();
			        connected = false;
			        gui.setConnectText("Connect");
				} catch (IOException ex) {
					System.err.println("Erorr:"+ex);
				}
			}
		}
	}
	
	
	private void paivita()
	{
		
	}




	public void run() {
		/*
	     * Keep on reading from the socket till we receive "Bye" from the
	     * server. Once we received that then we want to break.
	     */
	    String responseLine;
	    try {
	    	while ((responseLine = is.readLine()) != null) {
	    		String[] serverin_vastaus = responseLine.split(";");
	    		String viesti = serverin_vastaus[1];
	    		if (serverin_vastaus.length > 2)
	    		{
	    			String nostopakka = serverin_vastaus[2];
	    			String[] kortit = serverin_vastaus[3].split("&");
	    			gui.setList1(kortit);
		    		gui.setPakkaText(nostopakka);
	    		}
	    		
	    		gui.addInfoText(viesti);
	    		
	    	}
	      
	    } catch (IOException e) {
	      System.err.println("IOException: LOL " + e);
	    }
	}

}
