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
using System;
using System.Collections.Generic;
using System.Text;
using jp.nyatla.nymmd.cs.types;
using jp.nyatla.nymmd.cs.struct_type.pmd;

namespace jp.nyatla.nymmd.cs.core
{
    public class PmdIK
    {
        private PmdBone m_pTargetBone;	// IKターゲットボーン
        private PmdBone m_pEffBone;	// IK先端ボーン

        private int m_unCount;
        private double m_fFact;
        private int m_nSortVal;

        private PmdBone[] m_ppBoneList;	// IKを構成するボーンの配列

        private MmdVector4 _work_vector4 = new MmdVector4();
        public PmdIK(PMD_IK pPMDIKData, PmdBone[] i_ref_bone_array)
        {
            // IKターゲットボーン
            this.m_pTargetBone = i_ref_bone_array[pPMDIKData.nTargetNo];

            // IK先端ボーン
            this.m_pEffBone = i_ref_bone_array[pPMDIKData.nEffNo];

            this.m_unCount = pPMDIKData.unCount;
            this.m_fFact = pPMDIKData.fFact * Math.PI;
            this.m_nSortVal = pPMDIKData.punLinkNo[0];

            // IKリンク配列の作成
            int number_of_ik_link = pPMDIKData.cbNumLink;

            this.m_ppBoneList = new PmdBone[number_of_ik_link];//参照
            for (int i = 0; i < number_of_ik_link; i++)
            {
                this.m_ppBoneList[i] = i_ref_bone_array[pPMDIKData.punLinkNo[i]];	// ボーン番号は降順で格納されている
                if (this.m_ppBoneList[i].getName().Equals("左ひざ") || this.m_ppBoneList[i].getName().Equals("右ひざ"))
                {
                    this.m_ppBoneList[i].setIKLimitAngle(true);
                }
            }
        }
        private void limitAngle(MmdVector4 pvec4Out, MmdVector4 pvec4Src)
        {
            TMmdVector3 vec3Angle;

            // XYZ軸回転の取得
            TMmdVector3.QuaternionToEuler(out vec3Angle, pvec4Src);

            // 角度制限
            if (vec3Angle.x < -Math.PI)
            {
                vec3Angle.x = (float)-Math.PI;
            }
            if (-0.002f < vec3Angle.x)
            {
                vec3Angle.x = -0.002f;
            }
            vec3Angle.y = 0.0f;
            vec3Angle.z = 0.0f;

            // XYZ軸回転からクォータニオンへ
            pvec4Out.QuaternionCreateEuler(ref vec3Angle);
            return;
        }

        public int getSortVal()
        {
            return this.m_nSortVal;
        }
        private TMmdVector3 __update_vec3OrgTargetPos;
        private MmdMatrix __update_matInvBone = new MmdMatrix();
        public void update()
        {
            TMmdVector3 vec3OrgTargetPos;// = this.__update_vec3OrgTargetPos;
            MmdMatrix matInvBone = this.__update_matInvBone;

            vec3OrgTargetPos.x = (float)m_pTargetBone.m_matLocal.m[3,0];
            vec3OrgTargetPos.y = (float)m_pTargetBone.m_matLocal.m[3,1];
            vec3OrgTargetPos.z = (float)m_pTargetBone.m_matLocal.m[3,2];

            TMmdVector3 vec3EffPos;// = this._work_vector3[0];
            TMmdVector3 vec3TargetPos;// = this._work_vector3[1];
            TMmdVector3 vec3Diff;// = this._work_vector3[2];
            TMmdVector3 vec3RotAxis;// = this._work_vector3[3];
            MmdVector4 vec4RotQuat = this._work_vector4;

            for (int i = this.m_ppBoneList.Length - 1; i >= 0; i--)
            {
                this.m_ppBoneList[i].updateMatrix();
            }
            m_pEffBone.updateMatrix();

            for (int it = 0; it < m_unCount; it++)
            {
                for (int cbLinkIdx = 0; cbLinkIdx < this.m_ppBoneList.Length; cbLinkIdx++)
                {
                    // エフェクタの位置の取得
                    vec3EffPos.x = (float)m_pEffBone.m_matLocal.m[3,0];
                    vec3EffPos.y = (float)m_pEffBone.m_matLocal.m[3,1];
                    vec3EffPos.z = (float)m_pEffBone.m_matLocal.m[3,2];

                    // ワールド座標系から注目ノードの局所(ローカル)座標系への変換
                    matInvBone.MatrixInverse(m_ppBoneList[cbLinkIdx].m_matLocal);

                    // エフェクタ，到達目標のローカル位置
                    TMmdVector3.Vector3Transform(out vec3EffPos, ref vec3EffPos, matInvBone);
                    TMmdVector3.Vector3Transform(out vec3TargetPos, ref vec3OrgTargetPos, matInvBone);

                    // 十分近ければ終了

                    TMmdVector3.Vector3Sub(out vec3Diff, ref vec3EffPos, ref vec3TargetPos);
                    if (vec3Diff.Vector3DotProduct(ref vec3Diff) < 0.0000001f)
                    {
                        return;
                    }

                    // (1) 基準関節→エフェクタ位置への方向ベクトル
                    vec3EffPos.Vector3Normalize(ref vec3EffPos);

                    // (2) 基準関節→目標位置への方向ベクトル
                    vec3TargetPos.Vector3Normalize(ref vec3TargetPos);

                    // ベクトル (1) を (2) に一致させるための最短回転量（Axis-Angle）
                    //
                    // 回転角
                    double fRotAngle = Math.Acos(vec3EffPos.Vector3DotProduct(ref vec3TargetPos));

                    if (0.00000001 < Math.Abs(fRotAngle))
                    {
                        if (fRotAngle < -m_fFact)
                        {
                            fRotAngle = -m_fFact;
                        }
                        else if (m_fFact < fRotAngle)
                        {
                            fRotAngle = m_fFact;
                        }

                        // 回転軸

                        TMmdVector3.Vector3CrossProduct(out vec3RotAxis,ref vec3EffPos,ref vec3TargetPos);
                        if (vec3RotAxis.Vector3DotProduct(ref vec3RotAxis) < 0.0000001)
                        {
                            continue;
                        }

                        vec3RotAxis.Vector3Normalize(ref vec3RotAxis);

                        // 関節回転量の補正
                        vec4RotQuat.QuaternionCreateAxis(ref vec3RotAxis,fRotAngle);

                        if (m_ppBoneList[cbLinkIdx].m_bIKLimitAngle)
                        {
                            limitAngle(vec4RotQuat, vec4RotQuat);
                        }

                        vec4RotQuat.QuaternionNormalize(vec4RotQuat);

                        m_ppBoneList[cbLinkIdx].m_vec4Rotate.QuaternionMultiply(m_ppBoneList[cbLinkIdx].m_vec4Rotate, vec4RotQuat);
                        m_ppBoneList[cbLinkIdx].m_vec4Rotate.QuaternionNormalize(m_ppBoneList[cbLinkIdx].m_vec4Rotate);

                        for (int i = cbLinkIdx; i >= 0; i--)
                        {
                            m_ppBoneList[i].updateMatrix();
                        }
                        m_pEffBone.updateMatrix();
                    }
                }
            }
            return;
        }
    }
}
