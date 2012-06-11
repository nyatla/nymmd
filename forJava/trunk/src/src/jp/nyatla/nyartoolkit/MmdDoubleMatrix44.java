/* 
 * PROJECT: NyARToolkit(Extension)
 * --------------------------------------------------------------------------------
 * The NyARToolkit is Java edition ARToolKit class library.
 * Copyright (C)2008-2009 Ryo Iizuka
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * For further information please contact.
 *	http://nyatla.jp/nyatoolkit/
 *	<airmail(at)ebony.plala.or.jp> or <nyatla(at)nyatla.jp>
 * 
 */
package jp.nyatla.nyartoolkit;




public class MmdDoubleMatrix44
{
	/** 行列の要素値です。*/
	public double m00;
	/** 行列の要素値です。*/
	public double m01;
	/** 行列の要素値です。*/
	public double m02;
	/** 行列の要素値です。*/
	public double m03;
	/** 行列の要素値です。*/
	public double m10;
	/** 行列の要素値です。*/
	public double m11;
	/** 行列の要素値です。*/
	public double m12;
	/** 行列の要素値です。*/
	public double m13;
	/** 行列の要素値です。*/
	public double m20;
	/** 行列の要素値です。*/
	public double m21;
	/** 行列の要素値です。*/
	public double m22;
	/** 行列の要素値です。*/
	public double m23;
	/** 行列の要素値です。*/
	public double m30;
	/** 行列の要素値です。*/
	public double m31;
	/** 行列の要素値です。*/
	public double m32;
	/** 行列の要素値です。*/
	public double m33;
	/**
	 * この関数は、オブジェクトの配列を生成して返します。
	 * @param i_number
	 * 配列の長さ
	 * @return
	 * 新しいオブジェクト配列
	 */	
	public static MmdDoubleMatrix44[] createArray(int i_number)
	{
		MmdDoubleMatrix44[] ret=new MmdDoubleMatrix44[i_number];
		for(int i=0;i<i_number;i++)
		{
			ret[i]=new MmdDoubleMatrix44();
		}
		return ret;
	}
	/**
	 * この関数は、要素数16の配列を、行列にセットします。
	 * 00,01,02,03,10...の順です。
	 */
	public void setValue(double[] i_value)
	{
		this.m00=i_value[ 0];
		this.m01=i_value[ 1];
		this.m02=i_value[ 2];
		this.m03=i_value[ 3];
		this.m10=i_value[ 4];
		this.m11=i_value[ 5];
		this.m12=i_value[ 6];
		this.m13=i_value[ 7];
		this.m20=i_value[ 8];
		this.m21=i_value[ 9];
		this.m22=i_value[10];
		this.m23=i_value[11];
		this.m30=i_value[12];
		this.m31=i_value[13];
		this.m32=i_value[14];
		this.m33=i_value[15];
		return;
	}
	/**
	 * この関数は、オブジェクトの内容をインスタンスにコピーします。
	 * @param i_value
	 * コピー元のオブジェクト
	 */
	public void setValue(MmdDoubleMatrix44 i_value)
	{
		this.m00=i_value.m00;
		this.m01=i_value.m01;
		this.m02=i_value.m02;
		this.m03=i_value.m03;
		this.m10=i_value.m10;
		this.m11=i_value.m11;
		this.m12=i_value.m12;
		this.m13=i_value.m13;
		this.m20=i_value.m20;
		this.m21=i_value.m21;
		this.m22=i_value.m22;
		this.m23=i_value.m23;
		this.m30=i_value.m30;
		this.m31=i_value.m31;
		this.m32=i_value.m32;
		this.m33=i_value.m33;
		return;
	}
	/**
	 * この関数は、要素数16の配列に、行列の内容をコピーします。
	 * 順番は、00,01,02,03,10...の順です。
	 */	
	public void getValue(double[] o_value)
	{
		o_value[ 0]=this.m00;
		o_value[ 1]=this.m01;
		o_value[ 2]=this.m02;
		o_value[ 3]=this.m03;
		o_value[ 4]=this.m10;
		o_value[ 5]=this.m11;
		o_value[ 6]=this.m12;
		o_value[ 7]=this.m13;
		o_value[ 8]=this.m20;
		o_value[ 9]=this.m21;
		o_value[10]=this.m22;
		o_value[11]=this.m23;
		o_value[12]=this.m30;
		o_value[13]=this.m31;
		o_value[14]=this.m32;
		o_value[15]=this.m33;
		return;
	}
	/**
	 * この関数は、要素数16の配列に、行列の内容を転置してからコピーします。
	 * 順番は、00,10,20,30,01...の順です。
	 * @param o_value
	 * 値を受け取る配列
	 */	
	public void getValueT(double[] o_value)
	{
		o_value[ 0]=this.m00;
		o_value[ 1]=this.m10;
		o_value[ 2]=this.m20;
		o_value[ 3]=this.m30;
		o_value[ 4]=this.m01;
		o_value[ 5]=this.m11;
		o_value[ 6]=this.m21;
		o_value[ 7]=this.m31;
		o_value[ 8]=this.m02;
		o_value[ 9]=this.m12;
		o_value[10]=this.m22;
		o_value[11]=this.m32;
		o_value[12]=this.m03;
		o_value[13]=this.m13;
		o_value[14]=this.m23;
		o_value[15]=this.m33;
		return;
	}
	/**
	 * この関数は、逆行列を計算して、インスタンスにセットします。
	 * @param i_src
	 * 逆行列を計算するオブジェクト。thisを指定できます。
	 * @return
	 * 逆行列を得られると、trueを返します。
	 */
	public boolean inverse(MmdDoubleMatrix44 i_src)
	{
		final double a11,a12,a13,a14,a21,a22,a23,a24,a31,a32,a33,a34,a41,a42,a43,a44;
		final double b11,b12,b13,b14,b21,b22,b23,b24,b31,b32,b33,b34,b41,b42,b43,b44;	
		double t1,t2,t3,t4,t5,t6;
		a11=i_src.m00;a12=i_src.m01;a13=i_src.m02;a14=i_src.m03;
		a21=i_src.m10;a22=i_src.m11;a23=i_src.m12;a24=i_src.m13;
		a31=i_src.m20;a32=i_src.m21;a33=i_src.m22;a34=i_src.m23;
		a41=i_src.m30;a42=i_src.m31;a43=i_src.m32;a44=i_src.m33;
		
		t1=a33*a44-a34*a43;
		t2=a34*a42-a32*a44;
		t3=a32*a43-a33*a42;
		t4=a34*a41-a31*a44;
		t5=a31*a43-a33*a41;
		t6=a31*a42-a32*a41;
		
		b11=a22*t1+a23*t2+a24*t3;
		b21=-(a23*t4+a24*t5+a21*t1);
		b31=a24*t6-a21*t2+a22*t4;
		b41=-(a21*t3-a22*t5+a23*t6);
		
		t1=a43*a14-a44*a13;
		t2=a44*a12-a42*a14;
		t3=a42*a13-a43*a12;
		t4=a44*a11-a41*a14;
		t5=a41*a13-a43*a11;
		t6=a41*a12-a42*a11;

		b12=-(a32*t1+a33*t2+a34*t3);
		b22=a33*t4+a34*t5+a31*t1;
		b32=-(a34*t6-a31*t2+a32*t4);
		b42=a31*t3-a32*t5+a33*t6;
		
		t1=a13*a24-a14*a23;
		t2=a14*a22-a12*a24;
		t3=a12*a23-a13*a22;
		t4=a14*a21-a11*a24;
		t5=a11*a23-a13*a21;
		t6=a11*a22-a12*a21;

		b13=a42*t1+a43*t2+a44*t3;
		b23=-(a43*t4+a44*t5+a41*t1);
		b33=a44*t6-a41*t2+a42*t4;
		b43=-(a41*t3-a42*t5+a43*t6);

		t1=a23*a34-a24*a33;
		t2=a24*a32-a22*a34;
		t3=a22*a33-a23*a32;
		t4=a24*a31-a21*a34;		
		t5=a21*a33-a23*a31;
		t6=a21*a32-a22*a31;

		b14=-(a12*t1+a13*t2+a14*t3);
		b24=a13*t4+a14*t5+a11*t1;
		b34=-(a14*t6-a11*t2+a12*t4);
		b44=a11*t3-a12*t5+a13*t6;
		
		double det_1=(a11*b11+a21*b12+a31*b13+a41*b14);
		if(det_1==0){
			return false;
		}
		det_1=1/det_1;

		this.m00=b11*det_1;
		this.m01=b12*det_1;
		this.m02=b13*det_1;
		this.m03=b14*det_1;
		
		this.m10=b21*det_1;
		this.m11=b22*det_1;
		this.m12=b23*det_1;
		this.m13=b24*det_1;
		
		this.m20=b31*det_1;
		this.m21=b32*det_1;
		this.m22=b33*det_1;
		this.m23=b34*det_1;
		
		this.m30=b41*det_1;
		this.m31=b42*det_1;
		this.m32=b43*det_1;
		this.m33=b44*det_1;
		
		return true;
	}
	/**
	 * この関数は、行列同士の掛け算をして、インスタンスに格納します。
	 *　i_mat_lとi_mat_rには、thisを指定しないでください。
	 * @param i_mat_l
	 * 左成分の行列
	 * @param i_mat_r
	 * 右成分の行列
	 */
	public final void mul(MmdDoubleMatrix44 i_mat_l,MmdDoubleMatrix44 i_mat_r)
	{
		assert(this!=i_mat_l);
		assert(this!=i_mat_r);
		this.m00=i_mat_l.m00*i_mat_r.m00 + i_mat_l.m01*i_mat_r.m10 + i_mat_l.m02*i_mat_r.m20 + i_mat_l.m03*i_mat_r.m30;
		this.m01=i_mat_l.m00*i_mat_r.m01 + i_mat_l.m01*i_mat_r.m11 + i_mat_l.m02*i_mat_r.m21 + i_mat_l.m03*i_mat_r.m31;
		this.m02=i_mat_l.m00*i_mat_r.m02 + i_mat_l.m01*i_mat_r.m12 + i_mat_l.m02*i_mat_r.m22 + i_mat_l.m03*i_mat_r.m32;
		this.m03=i_mat_l.m00*i_mat_r.m03 + i_mat_l.m01*i_mat_r.m13 + i_mat_l.m02*i_mat_r.m23 + i_mat_l.m03*i_mat_r.m33;

		this.m10=i_mat_l.m10*i_mat_r.m00 + i_mat_l.m11*i_mat_r.m10 + i_mat_l.m12*i_mat_r.m20 + i_mat_l.m13*i_mat_r.m30;
		this.m11=i_mat_l.m10*i_mat_r.m01 + i_mat_l.m11*i_mat_r.m11 + i_mat_l.m12*i_mat_r.m21 + i_mat_l.m13*i_mat_r.m31;
		this.m12=i_mat_l.m10*i_mat_r.m02 + i_mat_l.m11*i_mat_r.m12 + i_mat_l.m12*i_mat_r.m22 + i_mat_l.m13*i_mat_r.m32;
		this.m13=i_mat_l.m10*i_mat_r.m03 + i_mat_l.m11*i_mat_r.m13 + i_mat_l.m12*i_mat_r.m23 + i_mat_l.m13*i_mat_r.m33;

		this.m20=i_mat_l.m20*i_mat_r.m00 + i_mat_l.m21*i_mat_r.m10 + i_mat_l.m22*i_mat_r.m20 + i_mat_l.m23*i_mat_r.m30;
		this.m21=i_mat_l.m20*i_mat_r.m01 + i_mat_l.m21*i_mat_r.m11 + i_mat_l.m22*i_mat_r.m21 + i_mat_l.m23*i_mat_r.m31;
		this.m22=i_mat_l.m20*i_mat_r.m02 + i_mat_l.m21*i_mat_r.m12 + i_mat_l.m22*i_mat_r.m22 + i_mat_l.m23*i_mat_r.m32;
		this.m23=i_mat_l.m20*i_mat_r.m03 + i_mat_l.m21*i_mat_r.m13 + i_mat_l.m22*i_mat_r.m23 + i_mat_l.m23*i_mat_r.m33;

		this.m30=i_mat_l.m30*i_mat_r.m00 + i_mat_l.m31*i_mat_r.m10 + i_mat_l.m32*i_mat_r.m20 + i_mat_l.m33*i_mat_r.m30;
		this.m31=i_mat_l.m30*i_mat_r.m01 + i_mat_l.m31*i_mat_r.m11 + i_mat_l.m32*i_mat_r.m21 + i_mat_l.m33*i_mat_r.m31;
		this.m32=i_mat_l.m30*i_mat_r.m02 + i_mat_l.m31*i_mat_r.m12 + i_mat_l.m32*i_mat_r.m22 + i_mat_l.m33*i_mat_r.m32;
		this.m33=i_mat_l.m30*i_mat_r.m03 + i_mat_l.m31*i_mat_r.m13 + i_mat_l.m32*i_mat_r.m23 + i_mat_l.m33*i_mat_r.m33;	
		return;
	}
	/**
	 * この関数は、行列を単位行列にします。
	 */
	public final void identity()
	{
		this.m00=this.m11=this.m22=this.m33=1;
		this.m01=this.m02=this.m03=this.m10=this.m12=this.m13=this.m20=this.m21=this.m23=this.m30=this.m31=this.m32=0;
		return;
	}
}
