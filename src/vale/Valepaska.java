package vale;

import java.util.*;


public class Valepaska {
	
private List<Kortti> nostopakka;
private List<Kortti> avopakka;
private List<Pelaaja> pelaajat;
private int vuoro = 0;
private int edellinen = 0;
private int putki = 0;
private int[] vaite = new int[]{0, 0}; // vaite[0] = kortin numero, vaite[1] = montako
private boolean on_kaatumassa = false;
private boolean peli_ohi = false;


	public Valepaska()
	{
		pelaajat = new ArrayList<Pelaaja>();
	}


	public static void main(String[] args)
	{
		Valepaska vp = new Valepaska();
		vp.aloitaPeli();
		
		tulostaKortit(vp.pelaajat.get(0));
		System.out.println();
		tulostaKortit(vp.pelaajat.get(1));
		System.out.println();
		tulostaLista(vp.avopakka);
		System.out.println();
		
		vp.pelaa(0, new int[]{0,2}, 2);
		vp.pelaa(1, new int[]{0}, 2);
		tulostaKortit(vp.pelaajat.get(0));
		System.out.println();
		tulostaKortit(vp.pelaajat.get(1));
		System.out.println();
		tulostaLista(vp.avopakka);
		System.out.println();
		vp.epailen(0);
		
		tulostaKortit(vp.pelaajat.get(0));
		System.out.println();
		tulostaKortit(vp.pelaajat.get(1));
		System.out.println();
		tulostaLista(vp.avopakka);
		System.out.println();
	}
	
	
	public void lisaaPelaaja()
	{
		pelaajat.add(new Pelaaja());
	}
	
	
	public void aloitaPeli()
	{
		vuoro = 0;
		
		nostopakka = new ArrayList<Kortti>(52);
		avopakka = new ArrayList<Kortti>();
		
		
		for (int i=2; i<15; i++)
			for (int j=0; j<4; j++)
				nostopakka.add(new Kortti(i,j));
		Collections.shuffle(nostopakka);
		jaa(5);
	}
	
	
	// kutsutaan kun pelaaja pelaa kortteja
	// HUOM kortit[] pit‰‰ olla suuruusj‰rjestyksess‰
	// poistetaan pelaajan k‰dest‰ ja lis‰t‰‰n avopakkaan
	// vaite = mit‰ pelaaja v‰itt‰‰ korttien olevan
	// palauttaa:
	// 401 = ei vuoro
	// 402 = peli ohi
	// 403 = ei pelannut oikeaa numeroa vaikka pakka on kaatumassa
	// 404 = jos yritt‰‰ pelata 0 korttia
	// 405 = paskoja parametreja
	// 406 = yritti pelata liian pieni‰ kortteja
	// 201 = kaikki hyvin, ei kaadu
	// 202 = kaikki hyvin, pakka kaatumassa
	public int pelaa(int pelaaja, int[] kortit, int vaite)
	{
		if (peli_ohi) return 402;
		Pelaaja p = pelaajat.get(pelaaja);
		
		if (vaite == 2) vaite = 15;
		
		for (int i=0; i<kortit.length-1; i++)
			if (kortit[i] > kortit[i+1]) return 405;
		if (pelaaja != vuoro) return 401;
		if (kortit.length <= 0) return 404;		
		if (pelaaja < 0 || pelaaja >= pelaajat.size()) return 405;		
		if (on_kaatumassa && vaite != this.vaite[0]) return 403; //kun on kaatumassa niin pit‰‰ laittaa samaa num
		if (vaite < this.vaite[0]) return 406;
		
		if (this.vaite[0] == vaite) putki += kortit.length;
		else putki = kortit.length;
		
		this.vaite[0] = vaite;
		this.vaite[1] = 0;
		
		if (!on_kaatumassa && (vaite == 10 || vaite == 14 || (putki >= 4 && vaite != 15))) on_kaatumassa = true;
		
		for (int i=kortit.length-1; i>=0; i--)
		{
			Kortti k = p.poista(kortit[i]);
			if (k != null)
			{
				avopakka.add(k);
				this.vaite[1]++;
				
			}
			
		
		}
		
		int montako_nostetaan = 5 - p.kortteja();
		for (int i=0; i<montako_nostetaan; i++)
			if (!nostopakka.isEmpty())
				p.lisaa(nostopakka.remove(nostopakka.size()-1));		
		

		edellinen = vuoro;
		vuoro = (vuoro+1)%pelaajat.size();
		
		if (p.kortteja() == 0)
			p.poisPelista(true);
		
		
		
		if (!on_kaatumassa)
			return 201;
		return 202;
	}
	
	
	// kutsutaan kun joku ep‰ilee vilunkipeli‰
	// palauttaa true jos vilunkia oli tapahtunut
	// palauttaa:
	// 301 = jos ep‰ilee omaa v‰itett‰‰n
	// 302 = avopakassa ei ole kortteja
	// 303 = k‰ry k‰vi
	// 304 = vilunkia ei ollut
	// 305 = vilunkia ei ollut + peli ohi
	public int epailen(int pelaaja)
	{
		if (peli_ohi) return 305;
		//int edellinen = (vuoro+pelaajat.size()-1)%pelaajat.size();
		Pelaaja edel = pelaajat.get(edellinen);
		if (pelaaja == edellinen) return 301;
		if (avopakka.size() == 0) return 302;
		
		on_kaatumassa = false;
		
		for (int i=avopakka.size()-1; i>avopakka.size()-1-vaite[1]; i--)
		{
			Kortti k = avopakka.get(i);
			if (k.Num() != vaite[0])
			{
				nostaPakka(edellinen);
				edel.poisPelista(false);
				vaite[1] = 0;
				vaite[0] = 0;
				putki = 0;
				for (int j=0; j<pelaajat.size(); j++)
					pelaajat.get(j).setKaatuu(false);
				return 303;
			}
		}
		
		nostaPakka(pelaaja);
		if (edel.onkoPoisPelista())
			pelaajat.remove(edellinen);
		if (pelaajat.size() == 1)
		{
			peli_ohi = true;
			return 305;
		}
		
		vuoro = (pelaaja+1)%pelaajat.size();
		vaite[1] = 0;
		vaite[0] = 0;
		putki = 0;
		for (int i=0; i<pelaajat.size(); i++)
			pelaajat.get(i).setKaatuu(false);
		
		return 304;
	}
	
	
	// kutsutaan kun jonkun mielest‰ kaatuu
	// palauttaa:
	// 502 = jos ei edes ole kaatumassa
	// 503 = ok, mutta ei viel‰ kaadu (kaikki pelaajat eiv‰t ole viel‰ hyv‰ksyneet kaatoa)
	// 504 = ok, kaatuu
	// 505 = peli on jo ohi
	// 506 = idiootti pelaaja h‰visi pelin koska antoi kaatua
	public int kaatuu(int pelaaja)
	{
		if (peli_ohi) return 505;
		//int edellinen = (vuoro+pelaajat.size()-1)%pelaajat.size();
		if (pelaaja == edellinen) return 503;
		if (!on_kaatumassa) return 502;
		
		pelaajat.get(pelaaja).setKaatuu(true);
		for (int i=0; i<pelaajat.size(); i++)
			if (i != edellinen && !pelaajat.get(i).getKaatuu()) return 503;
		
		if (pelaajat.get(edellinen).onkoPoisPelista())
			pelaajat.remove(edellinen);
		if (pelaajat.size() == 1)
		{
			peli_ohi = true;
			return 506;
		}
		
		avopakka.clear();
		vaite[1] = 0;
		putki = 0;
		vuoro = edellinen;
		on_kaatumassa = false;
		
		for (int i=0; i<pelaajat.size(); i++)
			pelaajat.get(i).setKaatuu(false);
		
		return 504;
	}
	
	
	// pelaaja nostaa koko avopakan
	public void nostaPakka(int pelaaja)
	{
		for (int i=0; i<avopakka.size(); i++)
			pelaajat.get(pelaaja).lisaa(avopakka.get(i));
		avopakka.clear();
	}
	
	
	// jakaa halutun m‰‰r‰n kortteja pelaajille
	public void jaa(int kortteja)
	{
		for (int i=0; i<pelaajat.size(); i++)
			for (int j=0; j<kortteja; j++)
			{
				if (nostopakka.isEmpty()) return;
					pelaajat.get(i).lisaa(nostopakka.remove(nostopakka.size()-1));
			}
	}
	
	
	public static void tulostaKortit(Pelaaja p)
	{
			for (int j=0; j<p.kortteja(); j++)
				System.out.println(p.getKortti(j).Num() + " " + p.getKortti(j).Maa());
	}
	
	
	public static void tulostaLista(List<Kortti> l)
	{
			for (int j=0; j<l.size(); j++)
				System.out.println(l.get(j).Num() + " " + l.get(j).Maa());
	}
	
	
	public int getVuoro()
	{
		return vuoro;
	}
	
	
	public int getEdellinen()
	{
		return edellinen;
	}
	
	
	public int getPelaajia()
	{
		return pelaajat.size();
	}
	
	
	//palauttaa pelaajan i kortit merkkijonotaulukkona
	public String[] getKortitT(int i)
	{
		if (i < 0 || i >= pelaajat.size()) return new String[]{""};
		return pelaajat.get(i).kortitStringT();
	}
	
	
	//palauttaa pelaajan i kortit merkkijonona
	public String getKortit(int i)
	{
		if (i < 0 || i >= pelaajat.size()) return "";
		return pelaajat.get(i).kortitString();
	}
	
	
	public String[] getAvo()
	{
		String[] s = new String[avopakka.size()];
		for (int i=0; i<avopakka.size(); i++)
			s[i] = avopakka.get(i).toString();
		return s;
	}
	
	
	public int getAvoKoko()
	{
		return avopakka.size();
	}
	
	
	public int getNostoKoko()
	{
		return nostopakka.size();
	}
	
	
	public int getPutki()
	{
		return putki;
	}
	
	
	public boolean onkoKaatumassa()
	{
		return on_kaatumassa;
	}
	
}
