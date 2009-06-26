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
package jp.nyatla.nymmd.struct;

import java.io.*;
import java.nio.*;

import jp.nyatla.nymmd.*;

public class DataReader
{
	private ByteBuffer _buf; 
	public DataReader(InputStream i_stream) throws MmdException
	{
		try{
			int file_len=i_stream.available();
			if(file_len<1){
				file_len=2*1024*1024;
			}
			byte[] buf=new byte[file_len];
			int buf_len=i_stream.read(buf,0,file_len);
			this._buf=ByteBuffer.wrap(buf,0,buf_len);
			this._buf.order(ByteOrder.LITTLE_ENDIAN);
			return;
		}catch(Exception e){
			throw new MmdException();
		}
	}
	public int readByte()
	{
		return this._buf.get();
	}
	public int read()
	{
		int v=this._buf.get();
		return (v>=0)?v:0xff+v;//unsignedに戻す
	}
	public short readShort()
	{
		return this._buf.getShort();
	}
	public int readUnsignedShort()
	{
		int v=this._buf.getShort();
		return (v>=0)?v:0xffff+v;//unsignedに戻す
	}
	public int readInt()
	{
		return this._buf.getInt();
	}
	public float readFloat()
	{
		return this._buf.getFloat();
	}
	public double readDouble()
	{
		return this._buf.getDouble();
	}
	public void mark()
	{
		this._buf.mark();
	}
	public void reset()
	{
		this._buf.reset();
	}
	public String readAscii(int i_length) throws MmdException
	{
		try{
		String ret="";
		int len=0;
		byte[] tmp=new byte[i_length];
		int i;
		for(i=0;i<i_length;i++){
			byte b=this._buf.get();
			if(b==0x00){
				i++;
				break;
			}
			tmp[i]=b;
			len++;
		}
		ret=new String(tmp,0,len,"Shift_JIS");
		for(;i<i_length;i++){
			this._buf.get();
		}
		return ret;
		}catch(Exception e){
			throw new MmdException();
		}
	}	
}
