package vale;

import java.util.*;

public class Pelaaja {

private List<Kortti> kasi;
private boolean pois_pelista = false;
private boolean kaatuu = false;
	

	public Pelaaja()
	{
		kasi = new ArrayList<Kortti>();
	}
	
	
	public Pelaaja(ArrayList<Kortti> l)
	{
		kasi = l;
	}
	
	
	public void lisaa(Kortti k)
	{
		kasi.add(k);
	}
	
	
	public Kortti poista(int i)
	{
		if (i<0 || i>= kasi.size()) return null;
		
		return kasi.remove(i);
	}
	
	
	public int kortteja()
	{
		return kasi.size();
	}
	
	
	public Kortti getKortti(int i)
	{
		return kasi.get(i);
	}
	
	
	public boolean onkoPoisPelista()
	{
		return pois_pelista;
	}
	
	
	public void poisPelista(boolean pois)
	{
		pois_pelista = pois;
	}
	
	
	public void setKaatuu(boolean k)
	{
		kaatuu = k;
	}
	
	
	public boolean getKaatuu()
	{
		return kaatuu;
	}
	
	
	//palauttaa pelaajan kortit merkkijonotaulukkona
	public String[] kortitString()
	{
		String[] s = new String[kasi.size()];
		for (int i=0; i<kasi.size(); i++)
			s[i] = kasi.get(i).toString();
		return s;
	}
}
