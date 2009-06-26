package jp.nyatla.nymmd.types;



public class BoneKeyFrame
{
	public float fFrameNo;		// フレーム番号
	public final Vector3	vec3Position=new Vector3();	// 位置
	public final Vector4	vec4Rotate=new Vector4();		// 回転(クォータニオン)
	public static BoneKeyFrame[] createArray(int i_length)
	{
		BoneKeyFrame[] ret=new BoneKeyFrame[i_length];
		for(int i=0;i<i_length;i++)
		{
			ret[i]=new BoneKeyFrame();
		}
		return ret;
	}	
/*	
	float	fFrameNo;		// フレーム番号

	Vector3	vec3Position;	// 位置
	Vector4	vec4Rotate;		// 回転(クォータニオン)
*/
}
