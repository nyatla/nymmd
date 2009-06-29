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
