package vale;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class VpSwing {
	private Valepaska vp;
	private VpGUI gui;
	
	
	public VpSwing(Valepaska vp, VpGUI gui)
	{
		this.vp = vp;
		this.gui = gui;
		
		gui.addPelaaListener(new PelaaListener());
		gui.addEpailenListener(new EpailenListener());
		gui.addKaatuuListener(new KaatuuListener());
		
		gui.addPelaaListener2(new PelaaListener2());
		gui.addEpailenListener2(new EpailenListener2());
		gui.addKaatuuListener2(new KaatuuListener2());
		
		vp.aloitaPeli();
		paivita();
	}
	
	private class PelaaListener implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0) {
			
			int[] valitutKortit = gui.getValitutKortit();
			String s_vaite = gui.getVaite();
			int vaite = Integer.parseInt(s_vaite);
			int palaute = vp.pelaa(0, valitutKortit, vaite);
			
			if (palaute == 201)
			{
				gui.setLblInfoText("H�rski v�ite! Putki: " + vp.getPutki());
			}
			else if (palaute == 202)
			{
				gui.setLblInfoText("Kaatuuko? Putki: " + vp.getPutki());
			}
			else if (palaute == 401)
			{
				gui.setLblInfoText("Ei ole vuorosi! Idiootti. Putki: " + vp.getPutki());
				return;
			}
			else if (palaute == 402)
			{
				gui.setLblInfoText("Peli ohi!");
				return;
			}
			else if (palaute == 403)
			{
				gui.setLblInfoText("Laita samaa numeroa tai kaatuu!");
				return;
			}
			else if (palaute == 404)
			{
				gui.setLblInfoText("Yrit�p�s nyt edes jotain pelata.");
				return;
			}
			else if (palaute == 405)
			{
				gui.setLblInfoText("405 error: bad parameters, bad you");
				return;
			}
			else if (palaute == 406)
			{
				gui.setLblInfoText("Pelaa isompaa!");
				return;
			}
			
			paivita();
			gui.setPakkaText(""+vp.getNostoKoko());
			gui.setVuoroText(""+vp.getVuoro());
			gui.setVaiteText(valitutKortit.length + " " + vaite);
			
		}
		
	}
	
	
	private class EpailenListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			int palaute = vp.epailen(0);			
			
			if (palaute == 301)
				gui.setLblInfoText("�l� ep�ile itse�si!");
			else if (palaute == 302)
				gui.setLblInfoText("Pakassa ei ole edes kortteja!");
			else if (palaute == 303)
				gui.setLblInfoText("K�ry k�vi!");
			else if (palaute == 304)
				gui.setLblInfoText("V�ite oli rehti! Idiootti!");
			else if (palaute == 305)
				gui.setLblInfoText("V�ite oli rehti! H�visit!");
			
			paivita();
			gui.setVuoroText(""+vp.getVuoro());
		}
	}
	
	
	private class KaatuuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			int palaute = vp.kaatuu(0);

			if (palaute == 502)
				gui.setLblInfoText("�l� painele sit� nappia turhaan!");
			else if (palaute == 503)
				gui.setLblInfoText("Ok, odotetaan muiden mielipiteit�.");
			else if (palaute == 504)
				gui.setLblInfoText("Kaatui! Jatkakaa.");
			else if (palaute == 505)
				gui.setLblInfoText("Peli on ohi!");
			else if (palaute == 506)
				gui.setLblInfoText("H�visit! Olisit ep�illyt!");
			
			paivita();
			gui.setVuoroText(""+vp.getVuoro());
		}
	}
	
	
	private class PelaaListener2 implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0) {
			
			int[] valitutKortit = gui.getValitutKortit2();
			String s_vaite = gui.getVaite();
			int vaite = Integer.parseInt(s_vaite);
			int palaute = vp.pelaa(1, valitutKortit, vaite);
			
			if (palaute == 201)
			{
				gui.setLblInfoText("H�rski v�ite! Putki: " + vp.getPutki());
			}
			else if (palaute == 202)
			{
				gui.setLblInfoText("Kaatuuko? Putki: " + vp.getPutki());
			}
			else if (palaute == 401)
			{
				gui.setLblInfoText("Ei ole vuorosi! Idiootti. " + vp.getPutki());
				return;
			}
			else if (palaute == 402)
			{
				gui.setLblInfoText("Peli ohi!");
				return;
			}
			else if (palaute == 403)
			{
				gui.setLblInfoText("T�ytyy pelata samaa numeroa!");
				return;
			}
			else if (palaute == 404)
			{
				gui.setLblInfoText("Yrit�p�s nyt edes jotain pelata.");
				return;
			}
			else if (palaute == 405)
			{
				gui.setLblInfoText("405 error: bad parameters, bad you");
				return;
			}
			else if (palaute == 406)
			{
				gui.setLblInfoText("Pelaa isompaa!");
				return;
			}
			paivita();
			
			gui.setPakkaText(""+vp.getNostoKoko());
			gui.setVuoroText(""+vp.getVuoro());
			gui.setVaiteText(valitutKortit.length + " " + vaite);
		}
		
	}
	
	
	private class EpailenListener2 implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			int palaute = vp.epailen(1);			
			
			if (palaute == 301)
				gui.setLblInfoText("�l� ep�ile itse�si!");
			else if (palaute == 302)
				gui.setLblInfoText("Pakassa ei ole edes kortteja!");
			else if (palaute == 303)
				gui.setLblInfoText("K�ry k�vi!");
			else if (palaute == 304)
				gui.setLblInfoText("V�ite oli rehti! Idiootti!");
			else if (palaute == 305)
				gui.setLblInfoText("V�ite oli rehti! H�visit!");
			
			paivita();
			gui.setVuoroText(""+vp.getVuoro());
		}
	}
	
	
	private class KaatuuListener2 implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			int palaute = vp.kaatuu(1);

			if (palaute == 502)
				gui.setLblInfoText("�l� painele sit� nappia turhaan!");
			else if (palaute == 503)
				gui.setLblInfoText("Ok, odotetaan muiden mielipiteit�.");
			else if (palaute == 504)
				gui.setLblInfoText("Kaatui! Jatkakaa.");
			else if (palaute == 505)
				gui.setLblInfoText("Peli on ohi!");
			else if (palaute == 506)
				gui.setLblInfoText("H�visit! Olisit ep�illyt!");
			
			paivita();
			gui.setVuoroText(""+vp.getVuoro());
		}
	}
	
	
	private void paivita()
	{
		gui.setList1(vp.getKortitT(0));
		gui.setList2(vp.getKortitT(1));
		gui.setList3(vp.getAvo());
	}

}
