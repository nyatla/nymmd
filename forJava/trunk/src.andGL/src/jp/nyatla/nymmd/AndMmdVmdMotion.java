package jp.nyatla.nymmd;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.res.AssetManager;

import jp.nyatla.nymmd.MmdException;
import jp.nyatla.nymmd.MmdVmdMotion;

public class AndMmdVmdMotion extends MmdVmdMotion
{

	public AndMmdVmdMotion(String i_vmd_file_path) throws FileNotFoundException,MmdException
	{
		super(i_vmd_file_path);
	}
	public AndMmdVmdMotion(AssetManager i_asm,String i_vmd_file) throws MmdException, IOException
	{
		super(i_asm.open(i_vmd_file));
	}

}
