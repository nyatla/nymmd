package jp.nyatla.nymmd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MmdVmdMotion extends MmdVmdMotion_BasicClass
{
	public MmdVmdMotion(String i_vmd_file_path) throws FileNotFoundException, MmdException
	{
		super(new FileInputStream(i_vmd_file_path));
	}

	protected MmdVmdMotion(InputStream i_stream) throws MmdException {
		super(i_stream);
	}

}
