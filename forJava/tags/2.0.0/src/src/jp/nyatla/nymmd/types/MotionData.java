/* 
 * PROJECT: MMD for Java
 * --------------------------------------------------------------------------------
 * This work is based on the ARTK_MMD v0.1 
 *   PY
 * http://ppyy.hp.infoseek.co.jp/
 * py1024<at>gmail.com
 * http://www.nicovideo.jp/watch/sm7398691
 *
 * The MMD for Java is Java version MMD class library.
 * Copyright (C)2009 nyatla
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this framework; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * For further information please contact.
 *	http://nyatla.jp/
 *	<airmail(at)ebony.plala.or.jp>
 * 
 */
package jp.nyatla.nymmd.types;

import jp.nyatla.nymmd.core.PmdBone;


public class MotionData
{
	public String szBoneName;			// ボーン名
	public int ulNumKeyFrames;	// キーフレーム数
	public BoneKeyFrame[] pKeyFrames;	// キーフレームデータ配列
	/**
	 * 
	 * @param fFrame
	 * @param i_pmd_bone
	 * 出力先オブジェクト
	 */
	public void getMotionPosRot(float fFrame,PmdBone i_pmd_bone)
	{
		int	ulNumKeyFrame = this.ulNumKeyFrames;
		BoneKeyFrame[] bone_key_frame=this.pKeyFrames;
		

		// 最終フレームを過ぎていた場合
		if( fFrame > bone_key_frame[ulNumKeyFrame - 1].fFrameNo )
		{
			fFrame = bone_key_frame[ulNumKeyFrame - 1].fFrameNo;
		}
		
		// 現在の時間がどのキー近辺にあるか
		int lKey0=findByBinarySearch(bone_key_frame,fFrame,0,ulNumKeyFrame-1)-1;
		int lKey1=lKey0+1;
		if( lKey1 == ulNumKeyFrame )
		{
			lKey1 = ulNumKeyFrame - 1;
		}
		if(lKey0<0){
			lKey0=0;
		}
		// 前後のキーの時間
		float fTime0 = bone_key_frame[lKey0].fFrameNo;
		float fTime1 = bone_key_frame[lKey1].fFrameNo;
		
		MmdVector3 pvec3Pos= i_pmd_bone.m_vec3Position;
		MmdVector4 pvec4Rot= i_pmd_bone.m_vec4Rotate;

		// 前後のキーの間でどの位置にいるか
		if( lKey0 != lKey1 )
		{
			float fLerpValue = (fFrame - fTime0) / (fTime1 - fTime0);
			pvec3Pos.Vector3Lerp(bone_key_frame[lKey0].vec3Position,bone_key_frame[lKey1].vec3Position, fLerpValue);
			pvec4Rot.QuaternionSlerp(bone_key_frame[lKey0].vec4Rotate,bone_key_frame[lKey1].vec4Rotate, fLerpValue);
			pvec4Rot.QuaternionNormalize(pvec4Rot);//これほんとにいるの？
		}else{
			pvec3Pos.setValue(bone_key_frame[lKey0].vec3Position);
			pvec4Rot.setValue(bone_key_frame[lKey0].vec4Rotate);
		}		
	}
	/**
	 * @author やねうらお さん
	 * @param pKeyFrames
	 * @param fFrame
	 * @param start
	 * @param end
	 * @return
	 */
	private static int findByBinarySearch(BoneKeyFrame[] pKeyFrames,float fFrame, int start, int end)
	{
		int diff = end - start;
		if (diff < 8) {
			// ある程度小さくなったら逐次サーチ。このな かに見つかるはずなんだ。
			for (int i = start; i < end; i++) {
				if (fFrame < pKeyFrames[i].fFrameNo) {
					return i;
				}
			}
			return end;
		}

		// 再帰的に調べる
		int mid = (start + end) / 2;
		if (fFrame < pKeyFrames[mid].fFrameNo){
			return findByBinarySearch(pKeyFrames, fFrame, start, mid);
		}
		else{
			return findByBinarySearch(pKeyFrames, fFrame, mid, end);
		}
	}
}
