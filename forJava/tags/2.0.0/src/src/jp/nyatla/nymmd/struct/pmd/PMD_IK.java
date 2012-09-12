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
import jp.nyatla.nymmd.struct.StructType;

public class PMD_IK implements StructType
{
	public int nTargetNo;	// IKターゲットボーン番号
	public int nEffNo;		// IK先端ボーン番号
	public int	cbNumLink;	// IKを構成するボーンの数
	public int unCount;
	public float fFact;
	public int[] punLinkNo;// IKを構成するボーンの配列(可変長配列)
	
	public void read(DataReader i_reader) throws MmdException
	{
		this.nTargetNo=i_reader.readShort();
		this.nEffNo=i_reader.readShort();
		this.cbNumLink=i_reader.read();
		this.unCount=i_reader.readUnsignedShort();
		this.fFact=i_reader.readFloat();
		//必要な数だけ配列を確保しなおす。
		this.punLinkNo=new int[this.cbNumLink];
		for(int i=0;i<this.cbNumLink;i++){
			this.punLinkNo[i]=i_reader.readUnsignedShort();
		}
		return;
	}	
	
	
	
/*	
	short			nTargetNo;	// IKターゲットボーン番号
	short			nEffNo;		// IK先端ボーン番号

	unsigned char	cbNumLink;	// IKを構成するボーンの数

	unsigned short	unCount;
	float			fFact;

	unsigned short	punLinkNo[1];// IKを構成するボーンの配列
*/
}
