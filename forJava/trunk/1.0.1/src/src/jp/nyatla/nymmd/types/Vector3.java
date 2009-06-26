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



public class Vector3
{
	public float x, y, z;
	public static Vector3[] createArray(int i_length)
	{
		Vector3[] ret=new Vector3[i_length];
		for(int i=0;i<i_length;i++)
		{
			ret[i]=new Vector3();
		}
		return ret;
	}	
	public void setValue(Vector3 v)
	{
		this.x=v.x;
		this.y=v.y;
		this.z=v.z;
		return;
	}
	public void Vector3Add(Vector3 pvec3Add1,Vector3 pvec3Add2)
	{
		this.x = pvec3Add1.x + pvec3Add2.x;
		this.y = pvec3Add1.y + pvec3Add2.y;
		this.z = pvec3Add1.z + pvec3Add2.z;
		return;
	}
	public void Vector3Sub(Vector3 pvec3Sub1,Vector3 pvec3Sub2)
	{
		this.x = pvec3Sub1.x - pvec3Sub2.x;
		this.y = pvec3Sub1.y - pvec3Sub2.y;
		this.z = pvec3Sub1.z - pvec3Sub2.z;
		return;
	}
	public void Vector3MulAdd(Vector3 pvec3Add1,Vector3 pvec3Add2, float fRate )
	{
		this.x = pvec3Add1.x + pvec3Add2.x * fRate;
		this.y = pvec3Add1.y + pvec3Add2.y * fRate;
		this.z = pvec3Add1.z + pvec3Add2.z * fRate;
	}
	public void Vector3Normalize(Vector3 pvec3Src)
	{
		double fSqr =(1.0f / Math.sqrt( pvec3Src.x * pvec3Src.x + pvec3Src.y * pvec3Src.y + pvec3Src.z * pvec3Src.z ));
		this.x =(float)(pvec3Src.x * fSqr);
		this.y =(float)(pvec3Src.y * fSqr);
		this.z =(float)(pvec3Src.z * fSqr);
		return;
	}


	public double Vector3DotProduct(Vector3 pvec3Src2)
	{
		return (this.x * pvec3Src2.x + this.y * pvec3Src2.y + this.z * pvec3Src2.z);
	}

	public void Vector3CrossProduct(Vector3 pvec3Src1, Vector3 pvec3Src2 )
	{
		final float vx1=pvec3Src1.x;
		final float vy1=pvec3Src1.y;
		final float vz1=pvec3Src1.z;
		final float vx2=pvec3Src2.x;
		final float vy2=pvec3Src2.y;
		final float vz2=pvec3Src2.z;
		this.x = vy1 * vz2 - vz1 * vy2;
		this.y = vz1 * vx2 - vx1 * vz2;
		this.z = vx1 * vy2 - vy1 * vx2;
	}
	public void Vector3Lerp(Vector3 pvec3Src1,Vector3 pvec3Src2, float fLerpValue )
	{
		float	t0 = 1.0f - fLerpValue;

		this.x = pvec3Src1.x * t0 + pvec3Src2.x * fLerpValue;
		this.y = pvec3Src1.y * t0 + pvec3Src2.y * fLerpValue;
		this.z = pvec3Src1.z * t0 + pvec3Src2.z * fLerpValue;
		return;
	}

	public void Vector3Transform(Vector3 pVec3In,Matrix matTransform)
	{
		final double vx=pVec3In.x;
		final double vy=pVec3In.y;
		final double vz=pVec3In.z;
		this.x =(float)(vx * matTransform.m[0][0] + vy * matTransform.m[1][0] + vz * matTransform.m[2][0] + matTransform.m[3][0]);
		this.y =(float)(vx * matTransform.m[0][1] + vy * matTransform.m[1][1] + vz * matTransform.m[2][1] + matTransform.m[3][1]);
		this.z =(float)(vx * matTransform.m[0][2] + vy * matTransform.m[1][2] + vz * matTransform.m[2][2] + matTransform.m[3][2]);
		return;
	}

	public void Vector3Rotate(Vector3 pVec3In,Matrix matRotate)
	{
		final double vx=pVec3In.x;
		final double vy=pVec3In.y;
		final double vz=pVec3In.z;		
		this.x =(float)(vx * matRotate.m[0][0] + vy * matRotate.m[1][0] + vz * matRotate.m[2][0]);
		this.y =(float)(vx * matRotate.m[0][1] + vy * matRotate.m[1][1] + vz * matRotate.m[2][1]);
		this.z =(float)(vx * matRotate.m[0][2] + vy * matRotate.m[1][2] + vz * matRotate.m[2][2]);
		return;
	}
	public void QuaternionToEuler(Vector4 pvec4Quat )
	{
		// XYZ軸回転の取得
		// Y回転を求める
		final double	x2 = pvec4Quat.x + pvec4Quat.x;
		final double	y2 = pvec4Quat.y + pvec4Quat.y;
		final double	z2 = pvec4Quat.z + pvec4Quat.z;
		final double	xz2 = pvec4Quat.x * z2;
		final double	wy2 = pvec4Quat.w * y2;
		double	temp = -(xz2 - wy2);

		// 誤差対策
		if( temp >= 1.0 ){
			temp = 1.0;
		}else if( temp <= -1.0 ){
			temp = -1.0;
		}

		double	yRadian = Math.sin(temp);

		// 他の回転を求める
		double	xx2 = pvec4Quat.x * x2;
		double	xy2 = pvec4Quat.x * y2;
		double	zz2 = pvec4Quat.z * z2;
		double	wz2 = pvec4Quat.w * z2;

		if( yRadian < 3.1415926f * 0.5f )
		{
			if( yRadian > -3.1415926f * 0.5f )
			{
				double	yz2 = pvec4Quat.y * z2;
				double	wx2 = pvec4Quat.w * x2;
				double	yy2 = pvec4Quat.y * y2;
				this.x = (float)Math.atan2(yz2 + wx2, (1.f - (xx2 + yy2)) );
				this.y = (float)yRadian;
				this.z = (float)Math.atan2( (xy2 + wz2), (1.f - (yy2 + zz2)) );
			}
			else
			{
				this.x = (float)-Math.atan2( (xy2 - wz2), (1.f - (xx2 + zz2)) );
				this.y = (float)yRadian;
				this.z = 0.f;
			}
		}
		else
		{
			this.x = (float)Math.atan2( (xy2 - wz2), (1.f - (xx2 + zz2)) );
			this.y = (float)yRadian;
			this.z = 0.f;
		}
	}	
	
	
}
