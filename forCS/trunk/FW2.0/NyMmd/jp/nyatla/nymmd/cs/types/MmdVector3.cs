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


namespace jp.nyatla.nymmd.cs.types
{
    public class MmdVector3
    {
        public float x, y, z;
        public static MmdVector3[] createArray(int i_length)
        {
            MmdVector3[] ret = new MmdVector3[i_length];
            for (int i = 0; i < i_length; i++)
            {
                ret[i] = new MmdVector3();
            }
            return ret;
        }
        public void setValue(MmdVector3 v)
        {
            this.x = v.x;
            this.y = v.y;
            this.z = v.z;
            return;
        }
        public void Vector3Add(MmdVector3 pvec3Add1, MmdVector3 pvec3Add2)
        {
            this.x = pvec3Add1.x + pvec3Add2.x;
            this.y = pvec3Add1.y + pvec3Add2.y;
            this.z = pvec3Add1.z + pvec3Add2.z;
            return;
        }
        public void Vector3Sub(MmdVector3 pvec3Sub1, MmdVector3 pvec3Sub2)
        {
            this.x = pvec3Sub1.x - pvec3Sub2.x;
            this.y = pvec3Sub1.y - pvec3Sub2.y;
            this.z = pvec3Sub1.z - pvec3Sub2.z;
            return;
        }
        public void Vector3MulAdd(MmdVector3 pvec3Add1, MmdVector3 pvec3Add2, float fRate)
        {
            this.x = pvec3Add1.x + pvec3Add2.x * fRate;
            this.y = pvec3Add1.y + pvec3Add2.y * fRate;
            this.z = pvec3Add1.z + pvec3Add2.z * fRate;
        }
        public void Vector3Normalize(MmdVector3 pvec3Src)
        {
            double fSqr = (1.0f / Math.Sqrt(pvec3Src.x * pvec3Src.x + pvec3Src.y * pvec3Src.y + pvec3Src.z * pvec3Src.z));
            this.x = (float)(pvec3Src.x * fSqr);
            this.y = (float)(pvec3Src.y * fSqr);
            this.z = (float)(pvec3Src.z * fSqr);
            return;
        }


        public double Vector3DotProduct(MmdVector3 pvec3Src2)
        {
            return (this.x * pvec3Src2.x + this.y * pvec3Src2.y + this.z * pvec3Src2.z);
        }

        public void Vector3CrossProduct(MmdVector3 pvec3Src1, MmdVector3 pvec3Src2)
        {
            float vx1 = pvec3Src1.x;
            float vy1 = pvec3Src1.y;
            float vz1 = pvec3Src1.z;
            float vx2 = pvec3Src2.x;
            float vy2 = pvec3Src2.y;
            float vz2 = pvec3Src2.z;
            this.x = vy1 * vz2 - vz1 * vy2;
            this.y = vz1 * vx2 - vx1 * vz2;
            this.z = vx1 * vy2 - vy1 * vx2;
        }
        public void Vector3Lerp(MmdVector3 pvec3Src1, MmdVector3 pvec3Src2, float fLerpValue)
        {
            float t0 = 1.0f - fLerpValue;

            this.x = pvec3Src1.x * t0 + pvec3Src2.x * fLerpValue;
            this.y = pvec3Src1.y * t0 + pvec3Src2.y * fLerpValue;
            this.z = pvec3Src1.z * t0 + pvec3Src2.z * fLerpValue;
            return;
        }

        public void Vector3Transform(MmdVector3 pVec3In, MmdMatrix matTransform)
        {
            double vx = pVec3In.x;
            double vy = pVec3In.y;
            double vz = pVec3In.z;
            this.x = (float)(vx * matTransform.m[0,0] + vy * matTransform.m[1,0] + vz * matTransform.m[2,0] + matTransform.m[3,0]);
            this.y = (float)(vx * matTransform.m[0,1] + vy * matTransform.m[1,1] + vz * matTransform.m[2,1] + matTransform.m[3,1]);
            this.z = (float)(vx * matTransform.m[0,2] + vy * matTransform.m[1,2] + vz * matTransform.m[2,2] + matTransform.m[3,2]);
            return;
        }

        public void Vector3Rotate(MmdVector3 pVec3In, MmdMatrix matRotate)
        {
            double vx = pVec3In.x;
            double vy = pVec3In.y;
            double vz = pVec3In.z;
            this.x = (float)(vx * matRotate.m[0,0] + vy * matRotate.m[1,0] + vz * matRotate.m[2,0]);
            this.y = (float)(vx * matRotate.m[0,1] + vy * matRotate.m[1,1] + vz * matRotate.m[2,1]);
            this.z = (float)(vx * matRotate.m[0,2] + vy * matRotate.m[1,2] + vz * matRotate.m[2,2]);
            return;
        }
        public void QuaternionToEuler(MmdVector4 pvec4Quat)
        {
            // XYZ軸回転の取得
            // Y回転を求める
            double x2 = pvec4Quat.x + pvec4Quat.x;
            double y2 = pvec4Quat.y + pvec4Quat.y;
            double z2 = pvec4Quat.z + pvec4Quat.z;
            double xz2 = pvec4Quat.x * z2;
            double wy2 = pvec4Quat.w * y2;
            double temp = -(xz2 - wy2);

            // 誤差対策
            if (temp >= 1.0)
            {
                temp = 1.0;
            }
            else if (temp <= -1.0)
            {
                temp = -1.0;
            }

            double yRadian = Math.Sin(temp);

            // 他の回転を求める
            double xx2 = pvec4Quat.x * x2;
            double xy2 = pvec4Quat.x * y2;
            double zz2 = pvec4Quat.z * z2;
            double wz2 = pvec4Quat.w * z2;

            if (yRadian < 3.1415926f * 0.5f)
            {
                if (yRadian > -3.1415926f * 0.5f)
                {
                    double yz2 = pvec4Quat.y * z2;
                    double wx2 = pvec4Quat.w * x2;
                    double yy2 = pvec4Quat.y * y2;
                    this.x = (float)Math.Atan2(yz2 + wx2, (1.0f - (xx2 + yy2)));
                    this.y = (float)yRadian;
                    this.z = (float)Math.Atan2((xy2 + wz2), (1.0f - (yy2 + zz2)));
                }
                else
                {
                    this.x = (float)-Math.Atan2((xy2 - wz2), (1.0f - (xx2 + zz2)));
                    this.y = (float)yRadian;
                    this.z = 0.0f;
                }
            }
            else
            {
                this.x = (float)Math.Atan2((xy2 - wz2), (1.0f - (xx2 + zz2)));
                this.y = (float)yRadian;
                this.z = 0.0f;
            }
        }
    }

}
