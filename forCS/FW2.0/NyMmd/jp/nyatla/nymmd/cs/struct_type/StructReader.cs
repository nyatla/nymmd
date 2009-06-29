using System;
using System.Collections.Generic;
using System.Text;
using jp.nyatla.nymmd.cs.types;


namespace jp.nyatla.nymmd.cs.struct_type
{
    public class StructReader
    {
        public static void read(MmdColor4 i_dest, DataReader i_reader)
        {
            i_dest.r = i_reader.readFloat();
            i_dest.g = i_reader.readFloat();
            i_dest.b = i_reader.readFloat();
            i_dest.a = i_reader.readFloat();
            return;
        }
        public static void read(MmdColor3 i_dest, DataReader i_reader)
        {
            i_dest.r = i_reader.readFloat();
            i_dest.g = i_reader.readFloat();
            i_dest.b = i_reader.readFloat();
            return;
        }
        public static void read(MmdTexUV i_dest, DataReader i_reader)
        {
            i_dest.u = i_reader.readFloat();
            i_dest.v = i_reader.readFloat();
            return;
        }
        public static void read(MmdVector3 i_dest, DataReader i_reader)
        {
            i_dest.x = i_reader.readFloat();
            i_dest.y = i_reader.readFloat();
            i_dest.z = i_reader.readFloat();
            return;
        }
        public static void read(MmdVector4 i_dest, DataReader i_reader)
        {
            i_dest.x = i_reader.readFloat();
            i_dest.y = i_reader.readFloat();
            i_dest.z = i_reader.readFloat();
            i_dest.w = i_reader.readFloat();
            return;
        }
    }
}
