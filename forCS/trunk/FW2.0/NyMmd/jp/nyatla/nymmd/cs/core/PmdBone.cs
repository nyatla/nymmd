﻿/* 
 * PROJECT: NyMmd for C#
 * --------------------------------------------------------------------------------
 * The MMD for C# is C# version MMD Motion player class library.
 * NyMmd is modules which removed the ARToolKit origin codes from ARTK_MMD,
 * and was ported to C#. 
 *
 * This is based on the ARTK_MMD v0.1 by PY.
 * http://ppyy.if.land.to/artk_mmd.html
 * py1024<at>gmail.com
 * http://www.nicovideo.jp/watch/sm7398691
 *
 * 
 * The MIT License
 * Copyright (C)2008-2012 nyatla
 * nyatla39<at>gmail.com
 * http://nyatla.jp
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */
using System;
using System.Collections.Generic;
using System.Text;
using jp.nyatla.nymmd.cs.types;
using jp.nyatla.nymmd.cs.struct_type.pmd;

namespace jp.nyatla.nymmd.cs.core
{
    public class PmdBone
    {
	private String _name;
	private MmdVector3 _pmd_bone_position=new MmdVector3();
	private MmdVector3 m_vec3Offset=new MmdVector3();
	private MmdMatrix m_matInvTransform=new MmdMatrix();	// 初期値のボーンを原点に移動させるような行列

	private PmdBone	_parent_bone;
	private PmdBone	m_pChildBone;

	// 以下は現在の値
	public MmdMatrix m_matLocal=new MmdMatrix();


	//強制public
	public MmdVector3 m_vec3Position=new MmdVector3();
	public MmdVector4 m_vec4Rotate=new MmdVector4();
	public bool m_bIKLimitAngle;	// IK時に角度制限をするかどうか
	//強制public/
	
	public String getName()
	{
		return this._name;
	}
	
	public PmdBone(PMD_Bone pPMDBoneData,PmdBone[] pBoneArray)
	{
		// ボーン名のコピー
		this._name=pPMDBoneData.szName;

		// 位置のコピー
		this._pmd_bone_position.setValue(pPMDBoneData.vec3Position);

		// 親ボーンの設定
		if( pPMDBoneData.nParentNo != -1 )
		{
			this._parent_bone = pBoneArray[pPMDBoneData.nParentNo];
			m_vec3Offset.Vector3Sub(this._pmd_bone_position,this._parent_bone._pmd_bone_position);
		}else{
			// 親なし
			this._parent_bone=null;
			this.m_vec3Offset.setValue(this._pmd_bone_position);
		}

		// 子ボーンの設定
		if( pPMDBoneData.nChildNo != -1 )
		{
			this.m_pChildBone =pBoneArray[pPMDBoneData.nChildNo];
		}

		this.m_matInvTransform.MatrixIdentity();
		this.m_matInvTransform.m[3,0] = -this._pmd_bone_position.x; 
		this.m_matInvTransform.m[3,1] = -this._pmd_bone_position.y; 
		this.m_matInvTransform.m[3,2] = -this._pmd_bone_position.z; 

		this.m_bIKLimitAngle = false;

		// 各変数の初期値を設定
		reset();		
	}
	public void recalcOffset()
	{
		if(this._parent_bone!=null){
			m_vec3Offset.Vector3Sub(this._pmd_bone_position,this._parent_bone._pmd_bone_position);
		}
		return;	
	}

	public void reset()
	{
		m_vec3Position.x = m_vec3Position.y = m_vec3Position.z = 0.0f;
		m_vec4Rotate.x = m_vec4Rotate.y = m_vec4Rotate.z = 0.0f; m_vec4Rotate.w = 1.0f;

		this.m_matLocal.MatrixIdentity();
		this.m_matLocal.m[3,0] = _pmd_bone_position.x; 
		this.m_matLocal.m[3,1] = _pmd_bone_position.y; 
		this.m_matLocal.m[3,2] = _pmd_bone_position.z; 		
	}
	public void setIKLimitAngle(bool i_value)
	{
		this.m_bIKLimitAngle=i_value;
		return;
	}

	
	public void updateSkinningMat(MmdMatrix o_matrix)
	{
		o_matrix.MatrixMultiply(this.m_matInvTransform,this.m_matLocal);		
		return;
	}
	public 	void updateMatrix()
	{
		// クォータニオンと移動値からボーンのローカルマトリックスを作成
		this.m_matLocal.QuaternionToMatrix(this.m_vec4Rotate );
		this.m_matLocal.m[3,0] = m_vec3Position.x + m_vec3Offset.x; 
		this.m_matLocal.m[3,1] = m_vec3Position.y + m_vec3Offset.y; 
		this.m_matLocal.m[3,2] = m_vec3Position.z + m_vec3Offset.z; 

		// 親があるなら親の回転を受け継ぐ
		if(this._parent_bone!=null){
			m_matLocal.MatrixMultiply(m_matLocal,this._parent_bone.m_matLocal);
		}
		return;
	}
	private MmdMatrix _lookAt_matTemp=new MmdMatrix();
	private MmdMatrix _lookAt_matInvTemp=new MmdMatrix();
	private MmdVector3 _lookAt_vec3LocalTgtPosZY=new MmdVector3();
	private MmdVector3 _lookAt_vec3LocalTgtPosXZ=new MmdVector3();
	private MmdVector3 _lookAt_vec3Angle=new MmdVector3();
	
	
	
	public 	void lookAt(MmdVector3 pvecTargetPos )
	{
		// どうもおかしいので要調整
		MmdMatrix matTemp=this._lookAt_matTemp;
		MmdMatrix matInvTemp=this._lookAt_matInvTemp;
		MmdVector3		vec3LocalTgtPosZY=this._lookAt_vec3LocalTgtPosZY;
        MmdVector3 vec3LocalTgtPosXZ = this._lookAt_vec3LocalTgtPosXZ;

		matTemp.MatrixIdentity();
		matTemp.m[3,0] = m_vec3Position.x + m_vec3Offset.x; 
		matTemp.m[3,1] = m_vec3Position.y + m_vec3Offset.y; 
		matTemp.m[3,2] = m_vec3Position.z + m_vec3Offset.z;

		if(this._parent_bone!=null)
		{
			matInvTemp.MatrixInverse(_parent_bone.m_matLocal );
			matTemp.MatrixMultiply(matTemp, matInvTemp );
		}
		matTemp.MatrixInverse(matTemp);


		vec3LocalTgtPosZY.Vector3Transform(pvecTargetPos, matTemp );

		vec3LocalTgtPosXZ.setValue(vec3LocalTgtPosZY);
		vec3LocalTgtPosXZ.y = 0.0f;
		vec3LocalTgtPosXZ.Vector3Normalize(vec3LocalTgtPosXZ);

		vec3LocalTgtPosZY.x = 0.0f;
		vec3LocalTgtPosZY.Vector3Normalize(vec3LocalTgtPosZY);

		MmdVector3 vec3Angle = this._lookAt_vec3Angle;
		vec3Angle.x=vec3Angle.y=vec3Angle.z=0;

		if( vec3LocalTgtPosZY.z > 0.0f ){
            vec3Angle.x =  (float)(Math.Asin(vec3LocalTgtPosZY.y ) - (20.0*Math.PI/180.0));
		}
		if( vec3LocalTgtPosXZ.x < 0.0f ){
			vec3Angle.y =  (float)Math.Acos(vec3LocalTgtPosXZ.z );
		}else{
			vec3Angle.y = (float)-Math.Acos(vec3LocalTgtPosXZ.z );
		}

		if( vec3Angle.x < (-25.0*Math.PI/180.0) ){
			vec3Angle.x = (float)(-25.0*Math.PI/180.0);
		}
		if( (45.0f*Math.PI/180.0) < vec3Angle.x  )	{
			vec3Angle.x = (float)( 45.0*Math.PI/180.0);
		}
		if( vec3Angle.y < (-80.0*Math.PI/180.0) ){
			vec3Angle.y = (float)(-80.0*Math.PI/180.0);
		}
		if((80.0*Math.PI/180.0) < vec3Angle.y  ){
			vec3Angle.y =(float)( 80.0*Math.PI/180.0);
		}

		m_vec4Rotate.QuaternionCreateEuler(vec3Angle);
	}
    }
}
