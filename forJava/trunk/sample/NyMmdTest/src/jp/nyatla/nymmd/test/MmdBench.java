package jp.nyatla.nymmd.test;

import java.io.*;
import jp.nyatla.nymmd.*;
import jp.nyatla.nymmd.types.MmdMatrix;
/**
 * ベンチマーク用のプログラム。{@link MmdMotionPlayer#updateMotion}関数の速度を計測します。
 * @author nyatla
 *
 */
public class MmdBench
{
	private MmdPmdModel_BasicClass _pmd;
	private MmdVmdMotion_BasicClass _vmd;
	private MmdMotionPlayer _player;
	public class MmdMotionPlayerTest extends MmdMotionPlayer
	{
		protected void onUpdateSkinningMatrix(MmdMatrix[] i_skinning_mat) throws MmdException
		{
		}		
	}
	public MmdBench() throws MmdException, IOException
	{
		String vmd_file="D:\\application.files\\MikuMikuDance_v524\\UserFile\\Motion\\初音ミクVer2みなぎる.vmd";
		String pmd_file="D:\\application.files\\MikuMikuDance_v524\\UserFile\\model\\初音ミクVer2.pmd";
		//PMD
		this._pmd = new MmdPmdModel(pmd_file);
		//VMD
		this._vmd = new MmdVmdMotion(vmd_file);
		// Player
		this._player = new MmdMotionPlayerTest();
		this._player.setPmd(_pmd);
		this._player.setVmd(_vmd);
		
		//テクスチャ用のIO
		long st=System.currentTimeMillis();
		for(int i=0;i<1000;i++){
			this._player.updateMotion(i);
//			this._player.updateNeckBone(100.0f,10f,10f);
//			this._player.refSkinningMatrix();
		}
		System.out.println(System.currentTimeMillis()-st);
		
	}

	public static void main(String[] args)
	{
		try {
			new MmdBench();
		} catch (Exception e){
			e.printStackTrace();
		}
		return;
	}
}
