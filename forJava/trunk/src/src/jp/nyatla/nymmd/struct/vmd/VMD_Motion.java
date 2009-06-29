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
package jp.nyatla.nymmd.struct.vmd;


import jp.nyatla.nymmd.MmdException;
import jp.nyatla.nymmd.struct.*;
import jp.nyatla.nymmd.types.MmdVector3;
import jp.nyatla.nymmd.types.MmdVector4;
public class VMD_Motion implements StructType
{
	public String szBoneName;	// ボーン名
	public long ulFrameNo;		// フレーム番号

	public final MmdVector3 vec3Position=new MmdVector3();// 位置
	public final MmdVector4 vec4Rotate=new MmdVector4();  // 回転(クォータニオン)

	public final int[] cInterpolation1=new int[16];	// 補間情報
	public final int[] cInterpolation2=new int[16];
	public final int[] cInterpolation3=new int[16];
	public final int[] cInterpolation4=new int[16];
	
	public void read(DataReader i_reader) throws MmdException
	{
		int i;
		//szName
		this.szBoneName=i_reader.readAscii(15);
		this.ulFrameNo=i_reader.readInt();
		StructReader.read(this.vec3Position, i_reader);
		StructReader.read(this.vec4Rotate,i_reader);
		for(i=0;i<16;i++){
			this.cInterpolation1[i]=i_reader.readByte();
		}
		for(i=0;i<16;i++){
			this.cInterpolation2[i]=i_reader.readByte();
		}
		for(i=0;i<16;i++){
			this.cInterpolation3[i]=i_reader.readByte();
		}
		for(i=0;i<16;i++){
			this.cInterpolation4[i]=i_reader.readByte();
		}
		return;
	}	
	
/*	
	char	szBoneName[15];			// ボーン名

	unsigned long	ulFrameNo;		// フレーム番号

	Vector3	vec3Position;			// 位置
	Vector4	vec4Rotate;				// 回転(クォータニオン)

	char	cInterpolation1[16];	// 補間情報
	char	cInterpolation2[16];
	char	cInterpolation3[16];
	char	cInterpolation4[16];	
*/
}
