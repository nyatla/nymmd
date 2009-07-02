package jp.nyatla.nymmd.types;


public class PmdMaterial
{
	public final MmdColor4 col4Diffuse=new MmdColor4();
	public final MmdColor4 col4Specular=new MmdColor4();
	public final MmdColor4 col4Ambient=new MmdColor4();
	public float fShininess;
	public String texture_name;
	public short[] indices;
	public int unknown;
};
