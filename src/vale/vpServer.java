package vale;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class vpServer {
	
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	
	public static final int maxClients = 2;
	private static ArrayList<ClientThread> threads = new ArrayList<ClientThread>();
	
	private static Valepaska vp;
	
	public static void main(String args[]) {
		
		int serverPort = 9999;
		
		vp = new Valepaska();
		
		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
		      System.out.println(e);
		}
		
		System.out.println("Serveri pystyssä. Odotetaan pelaajia.");
		while (true)
		{
			try
			{
				clientSocket = serverSocket.accept();
				if (threads.size() < maxClients)
				{
					vp.lisaaPelaaja();
					ClientThread c = new ClientThread(clientSocket, threads, vp);
					c.start();
					threads.add(c);
					
					
				}
				else
				{
					PrintStream os = new PrintStream(clientSocket.getOutputStream());
			        os.println("Ei mahdu.");
			        os.close();
			        clientSocket.close();
				}
				
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
	
	
	

}


class ClientThread extends Thread {
	
	private Socket clientSocket;
	private ArrayList<ClientThread> threads;
	private BufferedReader is;
	private PrintStream os;
	private Valepaska vp;
	private int pel_num;
	
	public ClientThread(Socket clientSocket, ArrayList<ClientThread> threads, Valepaska vp)
	{
		this.clientSocket = clientSocket;
		this.threads = threads;
		this.vp = vp;
	}
	
	
	private static void laheta(ClientThread th, String s)
	{
		
			th.os.println(s);
			System.out.println("--> "+th.clientSocket.getInetAddress().toString()+":"+th.clientSocket.getPort()+" --> "+s);
		
	}
	
	
	public void run() {
		try {
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			os = new PrintStream(clientSocket.getOutputStream());
			
			pel_num = vp.getPelaajia() - 1;
			System.out.println("Pelaaja " + pel_num + " liittyi peliin.");
			laheta(this, "100;Tervetuloa valepaskan pariin, olet pelaaja " + pel_num + ".");
			
			
			for (int i=0; i<threads.size(); i++)
			{
				if (threads.get(i) != this)
					laheta(threads.get(i), "101;Pelaaja " + vp.getPelaajia() + " liittyi peliin.");
			}
			
			if (threads.size() == vpServer.maxClients)
			{
				vp.aloitaPeli();
				for (int i=0; i<threads.size(); i++)
				{
					String tiedot = vp.getNostoKoko() + ";" + vp.getKortit(i) + ";";
					String viesti = "Peli alkoi!";
					laheta(threads.get(i), "102;" + viesti + ";" + tiedot);
					
					if (i == vp.getVuoro())
						laheta(threads.get(i), "102;Sinun vuorosi.");
					else
						laheta(threads.get(i), "102;Pelaajan " + vp.getVuoro() + " vuoro.");
				}
			}
			
			while (true)
			{
				String s = is.readLine();
				System.out.println("<-- "+clientSocket.getInetAddress().toString()+":"+clientSocket.getPort()+" <-- "+s);
				// käsitellään pelaajan lähettämä merkkijono
				// pelaa
				if (s.matches("P I [\\d+ ]*V \\d+"))
				{
					// hommataan valittujen korttien indeksit
					String[] s_indeksit = s.substring(s.indexOf("I"), s.indexOf("V")).split(" ");
					int[] indeksit = new int[s_indeksit.length-1];
					for (int i=0; i<s_indeksit.length-1; i++)
						indeksit[i] = Integer.parseInt(s_indeksit[i+1]);
					
					// hommataan väite
					String s_vaite = s.substring(s.indexOf("V")+2);
					int vaite = Integer.parseInt(s_vaite);
					
					int palaute = vp.pelaa(pel_num, indeksit, vaite);
					
					// käsitellään palaute
					// kaikki hyvin, ei kaadu
					if (palaute == 201)
					{
						for (int i=0; i<threads.size(); i++)
						{
							String tiedot = vp.getNostoKoko() + ";" + vp.getKortit(i);
							String viesti;
							if (threads.get(i) != this)
								viesti = "Pelaaja " + pel_num + " pelasi " + indeksit.length + " " + vaite + ".";
							else
								viesti = "Pelasit " + indeksit.length + " " + vaite + ".";
							
							
							laheta(threads.get(i), "201;" + viesti + ";" + tiedot);
							
							if (i == vp.getVuoro())
								laheta(threads.get(i), "201;Sinun vuorosi, pelaa tai epäile.;" + tiedot);
							else
								laheta(threads.get(i), "201;Pelaajan " + vp.getVuoro() + " vuoro.;" + tiedot);
						}
					}
					// kaikki hyvin, on kaatumassa
					else if (palaute == 202)
					{
						for (int i=0; i<threads.size(); i++)
						{
							String tiedot = vp.getNostoKoko() + ";" + vp.getKortit(i);
							String viesti;
							if (threads.get(i) != this)
								viesti = "Pelaaja " + pel_num + " pelasi " + indeksit.length + " " + vaite + ".";
							else
								viesti = "Pelasit " + indeksit.length + " " + vaite + ".";
							
							laheta(threads.get(i), "202;" + viesti + ";" + tiedot);
							
							if (i == vp.getVuoro())
								laheta(threads.get(i), "202;Sinun vuorosi, pelaa, epäile tai kaatuu.;" + tiedot);
							else
								laheta(threads.get(i), "202;Pelaajan " + vp.getVuoro() + " vuoro. Kaatumassa. Paina kaatuu jos hyväksyt.;" + tiedot);
						}
						
					}
					else if (palaute == 401)
					{
						laheta(this, "401;Ei ole vuorosi.");
					}
					else if (palaute == 402)
					{
						laheta(this, "402;Peli on jo ohi!");
					}
					else if (palaute == 403)
					{
						laheta(this, "403;Pakka on kaatumassa! Pelaa samaa tai epäile.");
					}
					else if (palaute == 404)
					{
						laheta(this, "404;Valitse kortteja ennen kuin pelaat.");
					}
					else if (palaute == 405)
					{
						laheta(this, "405;Virhe. Yritä uudestaan.");
					}
					else if (palaute == 406)
					{
						laheta(this, "406;Pelaa isompaa!");
					}
				}
				// epäilee
				else if (s.matches("E.*"))
				{
					int palaute = vp.epailen(pel_num);
					
					if (palaute == 301)
					{
						laheta(this, "301;Älä epäile itseäsi!");
					}
					else if (palaute == 302)
					{
						laheta(this, "302;Pakassa ei ole edes kortteja!");
					}
					else if (palaute == 303)
					{
						for (int i=0; i<threads.size(); i++)
						{
							String tiedot = vp.getNostoKoko() + ";" + vp.getKortit(i);
							if (i == vp.getEdellinen())
								laheta(threads.get(i), "303;Pelaaja " + pel_num + " epäili ja vilunkipelisi paljastui! Huijari (sinä) nostaa kaiken!;" + tiedot);
							else if (threads.get(i) == this)
								laheta(this, "303;Epäilyksesi osui oikeaan ja törkeä huijaus paljastui. Pelaaja " + vp.getEdellinen() + " nostaa kaiken!");
							else
								laheta(threads.get(i), "303;Pelaaja " + pel_num + " epäili ja vilunkipeli paljastui! Pelaaja " + vp.getEdellinen() + " nostaa kaiken!");
							
							if (i == vp.getVuoro())
								laheta(threads.get(i), "303;Sinun vuorosi.");
							else
								laheta(threads.get(i), "303;Pelaajan " + vp.getVuoro() + " vuoro.");
						}
					}
					else if (palaute == 304)
					{
						for (int i=0; i<threads.size(); i++)
						{
							String tiedot = vp.getNostoKoko() + ";" + vp.getKortit(i);
							
							if (threads.get(i) != this)
								laheta(threads.get(i), "304;Pelaaja " + pel_num + " epäili mutta väite olikin totta! Mikä idiootti!");
							else
								laheta(this, "304;Epäilit, mutta väite olikin totta. Nostat koko pakan.;" + tiedot);
							
							if (i == vp.getVuoro())
								laheta(threads.get(i), "304;Sinun vuorosi.");
							else
								laheta(threads.get(i), "304;Pelaajan " + vp.getVuoro() + " vuoro.");
						}
					}
					else if (palaute == 305)
					{
						for (int i=0; i<threads.size(); i++)
						{
							threads.get(i).os.println("Pelaaja " + pel_num + " epäili mutta väite olikin totta! Idiootti hävisi!");
						}
						break;
					}
				}
				// kaatuu
				else if (s.matches("K.*"))
				{
					int palaute = vp.kaatuu(pel_num);
					
					if (palaute == 502)
					{
						this.os.println("Paina sitä kaatuu-nappia vasta kun kysytään.");
					}
					else if (palaute == 503)
					{
						for (int i=0; i<threads.size(); i++)
						{
							threads.get(i).os.println("Pelaaja " + pel_num + " hyväksyi kaatumisen.");
						}
					}
					else if (palaute == 504)
					{
						for (int i=0; i<threads.size(); i++)
						{
							threads.get(i).os.println("Pelaaja " + pel_num + " hyväksyi kaatumisen. Kaatui! Jatkakaa.");
						}
						break;
					}
					else if (palaute == 505)
					{
						this.os.println("Peli on jo ohi!");
					}
					else if (palaute == 506)
					{
						for (int i=0; i<threads.size(); i++)
						{
							threads.get(i).os.println("Pelaaja " + pel_num + " hyväksyi kaatumisen ja täten hävisi pelin! Idiootti!");
						}
						break;
					}
				}
			}
			
			is.close();
			os.close();
			clientSocket.close();
			
		} catch (IOException e) {
			
		}
	}
}
