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
import jp.nyatla.nymmd.struct.*;

public class PMD_FACE implements StructType
{
	public String szName;		// 表情名 (0x00 終端，余白は 0xFD)
	public int	ulNumVertices;	// 表情頂点数
	public int cbType;			// 分類
	public PMD_FACE_VTX	[] pVertices=PMD_FACE_VTX.createArray(64);// 表情頂点データ
	public void read(DataReader i_reader) throws MmdException
	{
		int i;
		//szName
		this.szName=i_reader.readAscii(20);
		this.ulNumVertices=i_reader.readInt();
		this.cbType=i_reader.read();
		//必要な数だけ配列を確保しなおす。
		if(this.ulNumVertices>this.pVertices.length){
			this.pVertices=PMD_FACE_VTX.createArray(this.ulNumVertices);
		}
		for(i=0;i<this.ulNumVertices;i++){
			this.pVertices[i].read(i_reader);
		}
		return;
	}	
/*	
	char			szName[20];		// 表情名 (0x00 終端，余白は 0xFD)

	unsigned long	ulNumVertices;	// 表情頂点数
	unsigned char	cbType;			// 分類

	PMD_FACE_VTX	pVertices[1];	// 表情頂点データ
*/
}
