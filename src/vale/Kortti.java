package vale;

public class Kortti {

private int num;
private int maa;
private static final String[] MAAT = new String[]{"Ri", "Ru", "Pa", "He"};

	public Kortti(int num, int maa)
	{
		this.num = num;
		this.maa = maa%4;
	}
	
	
	public int Num()
	{
		return num;
	}
	
	
	public int Maa()
	{
		return maa;
	}
	
	
	@Override
	public String toString()
	{
		return num + " " + MAAT[maa];
	}
}
