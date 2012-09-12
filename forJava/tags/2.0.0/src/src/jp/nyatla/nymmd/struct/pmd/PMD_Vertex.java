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
package jp.nyatla.nymmd.struct.pmd;


import jp.nyatla.nymmd.MmdException;
import jp.nyatla.nymmd.struct.DataReader;
import jp.nyatla.nymmd.struct.StructReader;
import jp.nyatla.nymmd.struct.StructType;
import jp.nyatla.nymmd.types.MmdTexUV;
import jp.nyatla.nymmd.types.MmdVector3;

public class PMD_Vertex implements StructType
{
	public MmdVector3 vec3Pos=new MmdVector3();	// 座標
	public MmdVector3 vec3Normal=new MmdVector3();	// 法線ベクトル
	public MmdTexUV uvTex=new MmdTexUV();		// テクスチャ座標

	public int[] unBoneNo=new int[2];	// ボーン番号
	public int	cbWeight;		// ブレンドの重み (0～100％)
	public int	cbEdge;			// エッジフラグ
/*
	Vector3		vec3Pos;	// 座標
	Vector3		vec3Normal;	// 法線ベクトル
	TexUV		uvTex;		// テクスチャ座標

	unsigned short	unBoneNo[2];	// ボーン番号
	unsigned char	cbWeight;		// ブレンドの重み (0～100％)
	unsigned char	cbEdge;			// エッジフラグ	
*/
	public void read(DataReader i_reader) throws MmdException
	{
		StructReader.read(this.vec3Pos, i_reader);
		StructReader.read(this.vec3Normal, i_reader);
		StructReader.read(this.uvTex, i_reader);
		this.unBoneNo[0]=i_reader.readUnsignedShort();
		this.unBoneNo[1]=i_reader.readUnsignedShort();
		this.cbWeight=i_reader.read();
		this.cbEdge=i_reader.read();
		return;
	}	

}
