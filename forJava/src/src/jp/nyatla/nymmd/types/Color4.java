package jp.nyatla.nymmd.types;

public class Color4
{
	public float r, g, b, a;
	public void setValue(Color4 v)
	{
		this.r=v.r;
		this.g=v.g;
		this.b=v.b;
		this.a=v.a;
		return;
	}
	public void getValue(float[] v,int i_st)
	{
		v[i_st+0]=this.r;
		v[i_st+1]=this.g;
		v[i_st+2]=this.b;
		v[i_st+3]=this.a;
		return;
	}
}
