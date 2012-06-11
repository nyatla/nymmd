package jp.nyatla.nymmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;

import jp.nyatla.nymmd.MmdException;
import jp.nyatla.nymmd.MmdPmdModel;


public class AndMmdPmdModel extends MmdPmdModel
{
	public AndMmdPmdModel(String i_pmd_file_path) throws FileNotFoundException, MmdException
	{
		super(new FileInputStream(i_pmd_file_path),new MmdPmdModel.FileResourceProvider(i_pmd_file_path));
	}
	public AndMmdPmdModel(AssetManager i_assetmgr,String i_pmd_file_name) throws MmdException, IOException
	{
		super(i_assetmgr.open(i_pmd_file_name),new AssetResourceProvider(i_assetmgr,i_pmd_file_name));
	}
	
	private static class AssetResourceProvider implements IResourceProvider
	{
		String _dir;
		AssetManager _asm;
		AssetResourceProvider(AssetManager i_assetmgr,String i_pmd_file_path)
		{
			File f=new File(i_pmd_file_path);//pmdのパス
			this._dir=(f.getParentFile().getPath());
			this._asm=i_assetmgr;
		}
		public InputStream getTextureStream(String i_name) throws MmdException
		{
			try{
				return this._asm.open(this._dir +"/"+ i_name);
			}catch(Exception e){
				throw new MmdException(e);
			}
		}
	}
}
