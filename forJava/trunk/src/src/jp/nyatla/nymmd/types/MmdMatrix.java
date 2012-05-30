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

import jp.nyatla.nyartoolkit.NyARDoubleMatrix44;

public class MmdMatrix extends NyARDoubleMatrix44
{
	//NyARToolkitと統合かな。
//	public final double[][] m=new double[4][4];
	public static MmdMatrix[] createArray(int i_length)
	{
		MmdMatrix[] ret=new MmdMatrix[i_length];
		for(int i=0;i<i_length;i++)
		{
			ret[i]=new MmdMatrix();
		}
		return ret;
	}
//	public void MatrixIdentity()
//	{
//		this.m[0][1] = this.m[0][2] = this.m[0][3] =this.m[1][0] = this.m[1][2] = this.m[1][3] =this.m[2][0] = this.m[2][1] = this.m[2][3] =this.m[3][0] = this.m[3][1] = this.m[3][2] = 0.0f;
//		this.m[0][0] = this.m[1][1] = this.m[2][2] = this.m[3][3] = 1.0f;
//		return;
//	}
//	private final double[][] _array_temp=new double[4][4];
//	public void MatrixMultiply(MmdMatrix matSrc1,MmdMatrix matSrc2 )
//	{
//		final double[][] matTemp=this._array_temp;
//		int		i;
//		
//		for( i = 0 ; i < 4 ; i++ )
//		{
//			matTemp[i][0] =	matSrc1.m[i][0] * matSrc2.m[0][0] + matSrc1.m[i][1] * matSrc2.m[1][0] + matSrc1.m[i][2] * matSrc2.m[2][0] + matSrc1.m[i][3] * matSrc2.m[3][0];
//			matTemp[i][1] =	matSrc1.m[i][0] * matSrc2.m[0][1] + matSrc1.m[i][1] * matSrc2.m[1][1] + matSrc1.m[i][2] * matSrc2.m[2][1] + matSrc1.m[i][3] * matSrc2.m[3][1];
//			matTemp[i][2] =	matSrc1.m[i][0] * matSrc2.m[0][2] + matSrc1.m[i][1] * matSrc2.m[1][2] + matSrc1.m[i][2] * matSrc2.m[2][2] + matSrc1.m[i][3] * matSrc2.m[3][2];
//			matTemp[i][3] =	matSrc1.m[i][0] * matSrc2.m[0][3] + matSrc1.m[i][1] * matSrc2.m[1][3] + matSrc1.m[i][2] * matSrc2.m[2][3] + matSrc1.m[i][3] * matSrc2.m[3][3];
//		}
//
//		for( i = 0 ; i < 4 ; i++ )
//		{
//			this.m[i][0] = matTemp[i][0];
//			this.m[i][1] = matTemp[i][1];
//			this.m[i][2] = matTemp[i][2];
//			this.m[i][3] = matTemp[i][3];
//		}
//	}
/*
	public void MatrixInverse(MmdMatrix matSrc)
	{
		final double[][] matTemp=this._array_temp;
		for(int i=0;i<4;i++){
			for(int i2=0;i2<4;i2++){
				matTemp[i][i2]=matSrc.m[i][i2];
			}
		}
		this.identity();

		//掃き出し法
		for( int i = 0 ; i < 4 ; i++ )
		{
			double	buf = 1.0 / matTemp[i][i];
			for( int j = 0 ; j < 4 ; j++ )
			{
				matTemp[i][j] *= buf;
				this.m[i][j] *= buf;
			}
			for( int j = 0 ; j < 4 ; j++ )
			{
				if( i != j )
				{
					buf = matTemp[j][i];
					for( int k = 0 ; k < 4 ; k++ )
					{
						matTemp[j][k] -= matTemp[i][k] * buf;
						this.m[j][k] -= this.m[i][k] * buf;
					}
				}
			}
		}
		return;
	}
*/
	public void MatrixLerp(MmdMatrix sm1, MmdMatrix sm2, float fLerpValue )
	{
		double fT = 1.0 - fLerpValue;
		this.m00 = sm1.m00 * fLerpValue + sm2.m00 * fT;
		this.m01 = sm1.m01 * fLerpValue + sm2.m01 * fT;
		this.m02 = sm1.m02 * fLerpValue + sm2.m02 * fT;
		this.m03 = sm1.m03 * fLerpValue + sm2.m03 * fT;
		this.m10 = sm1.m10 * fLerpValue + sm2.m10 * fT;
		this.m11 = sm1.m11 * fLerpValue + sm2.m11 * fT;
		this.m12 = sm1.m12 * fLerpValue + sm2.m12 * fT;
		this.m13 = sm1.m13 * fLerpValue + sm2.m13 * fT;
		this.m20 = sm1.m20 * fLerpValue + sm2.m20 * fT;
		this.m21 = sm1.m21 * fLerpValue + sm2.m21 * fT;
		this.m22 = sm1.m22 * fLerpValue + sm2.m22 * fT;
		this.m23 = sm1.m23 * fLerpValue + sm2.m23 * fT;
		this.m30 = sm1.m30 * fLerpValue + sm2.m30 * fT;
		this.m31 = sm1.m31 * fLerpValue + sm2.m31 * fT;
		this.m32 = sm1.m32 * fLerpValue + sm2.m32 * fT;
		this.m33 = sm1.m33 * fLerpValue + sm2.m33 * fT;
		return;
	}
	public void QuaternionToMatrix(MmdVector4 pvec4Quat)
	{
		double	x2 = pvec4Quat.x * pvec4Quat.x * 2.0f;
		double	y2 = pvec4Quat.y * pvec4Quat.y * 2.0f;
		double	z2 = pvec4Quat.z * pvec4Quat.z * 2.0f;
		double	xy = pvec4Quat.x * pvec4Quat.y * 2.0f;
		double	yz = pvec4Quat.y * pvec4Quat.z * 2.0f;
		double	zx = pvec4Quat.z * pvec4Quat.x * 2.0f;
		double	xw = pvec4Quat.x * pvec4Quat.w * 2.0f;
		double	yw = pvec4Quat.y * pvec4Quat.w * 2.0f;
		double	zw = pvec4Quat.z * pvec4Quat.w * 2.0f;

		this.m00 = 1.0f - y2 - z2;
		this.m01 = xy + zw;
		this.m02 = zx - yw;
		this.m10 = xy - zw;
		this.m11 = 1.0f - z2 - x2;
		this.m12 = yz + xw;
		this.m20 = zx + yw;
		this.m21 = yz - xw;
		this.m22 = 1.0f - x2 - y2;

		this.m03 = this.m13 = this.m23 = this.m30 = this.m31 = this.m32 = 0.0f;
		this.m33 = 1.0f;
		return;
	}

}
