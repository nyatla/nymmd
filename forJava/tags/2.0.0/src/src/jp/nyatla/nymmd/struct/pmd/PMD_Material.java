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
import jp.nyatla.nymmd.types.MmdColor3;
import jp.nyatla.nymmd.types.MmdColor4;

public class PMD_Material implements StructType
{
	public final MmdColor4		col4Diffuse=new MmdColor4();
	public float				fShininess;
	public final MmdColor3		col3Specular=new MmdColor3();
	public final MmdColor3		col3Ambient=new MmdColor3();
	public int			unknown;
	public int			ulNumIndices;		// この材質に対応する頂点数
	public String		szTextureFileName;	// テクスチャファイル名
	public void read(DataReader i_reader) throws MmdException
	{
		StructReader.read(this.col4Diffuse, i_reader);
		this.fShininess=i_reader.readFloat();
		StructReader.read(this.col3Specular, i_reader);
		StructReader.read(this.col3Ambient, i_reader);
		this.unknown=i_reader.readUnsignedShort();
		this.ulNumIndices=i_reader.readInt();
		this.szTextureFileName=i_reader.readAscii(20);
		return;
	}	
/*
	Color4		col4Diffuse;
	float		fShininess;
	Color3		col3Specular,
				col3Ambient;

	unsigned short	unknown;
	unsigned long	ulNumIndices;			// この材質に対応する頂点数
	char			szTextureFileName[20];	// テクスチャファイル名
*/
	
}
