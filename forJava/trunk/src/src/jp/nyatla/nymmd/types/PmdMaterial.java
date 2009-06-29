package jp.nyatla.nymmd.types;


public class PmdMaterial
{
	public final Color4 col4Diffuse=new Color4();
	public final Color4 col4Specular=new Color4();
	public final Color4 col4Ambient=new Color4();
	public float fShininess;
	public String texture_name;
	public short[] indices;
};
