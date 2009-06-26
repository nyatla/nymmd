package jp.nyatla.nymmd.types;

public class FaceKeyFrame
{
	public float	fFrameNo;		// フレーム番号
	public float	fRate;			// フレンド率
	public static FaceKeyFrame[] createArray(int i_length)
	{
		FaceKeyFrame[] ret=new FaceKeyFrame[i_length];
		for(int i=0;i<i_length;i++)
		{
			ret[i]=new FaceKeyFrame();
		}
		return ret;
	}	
}
