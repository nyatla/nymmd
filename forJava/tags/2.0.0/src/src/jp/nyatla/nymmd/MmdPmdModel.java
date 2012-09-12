package jp.nyatla.nymmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;



/**
 * ファイルシステムからPMDファイルを読み込むようにラップした{@link MmdPmdModel_BasicClass}
 */
public class MmdPmdModel extends MmdPmdModel_BasicClass
{
	public MmdPmdModel(String i_pmd_file_path) throws FileNotFoundException, MmdException
	{
		super(new FileInputStream(i_pmd_file_path),new FileResourceProvider(i_pmd_file_path));
	}
	protected MmdPmdModel(InputStream i_stream,IResourceProvider i_res_provider) throws FileNotFoundException, MmdException
	{
		super(i_stream,i_res_provider);
	}
	
	protected static class FileResourceProvider implements IResourceProvider
	{
		String _dir;
		public FileResourceProvider(String i_pmd_file_path)
		{
			File f=new File(i_pmd_file_path);//pmdのパス
			this._dir=(f.getParentFile().getPath());
		}
		public InputStream getTextureStream(String i_name) throws MmdException
		{
			try{
				return new FileInputStream(this._dir +"\\"+ i_name);
			}catch(Exception e){
				throw new MmdException(e);
			}
		}
	}
}
