package jp.nyatla.nymmd.types;


public class TexUV
{
	public float u, v;
	public static TexUV[] createArray(int i_length)
	{
		TexUV[] ret=new TexUV[i_length];
		for(int i=0;i<i_length;i++)
		{
			ret[i]=new TexUV();
		}
		return ret;
	}
	public void setValue(TexUV v)
	{
		this.u=v.u;
		this.v=v.v;
		return;
	}	
}
