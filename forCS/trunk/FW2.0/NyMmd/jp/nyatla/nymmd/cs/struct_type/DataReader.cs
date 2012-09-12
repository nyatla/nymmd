/* 
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
using System.IO;

namespace jp.nyatla.nymmd.cs.struct_type
{
    public class DataReader
    {
        private BinaryReader _buf;
        public DataReader(BinaryReader i_reader)
        {
            this._buf = i_reader;
        }
        public int readByte()
        {
            return this._buf.ReadByte();
        }
        public int read()
        {
            return this._buf.ReadSByte();
        }
        public short readShort()
        {
            return this._buf.ReadInt16();
        }
        public ushort readUnsignedShort()
        {
            return this._buf.ReadUInt16();
        }
        public int readInt()
        {
            return this._buf.ReadInt32();
        }
        public float readFloat()
        {
            return this._buf.ReadSingle();
        }
        public double readDouble()
        {
            return this._buf.ReadDouble();
        }
        public String readAscii(int i_length)
        {
            try
            {
                String ret = "";
                int len = 0;
                byte[] tmp = new byte[i_length];
                int i;
                for (i = 0; i < i_length; i++)
                {
                    byte b = this._buf.ReadByte();
                    if (b == 0x00)
                    {
                        i++;
                        break;
                    }
                    tmp[i] = b;
                    len++;
                }
                Encoding enc = Encoding.GetEncoding("shift-jis");
                ret = enc.GetString(tmp, 0, len);
                for (; i < i_length; i++)
                {
                    this._buf.ReadByte();
                }
                return ret;
            }
            catch (Exception e)
            {
                throw new MmdException();
            }
        }
    }
}
