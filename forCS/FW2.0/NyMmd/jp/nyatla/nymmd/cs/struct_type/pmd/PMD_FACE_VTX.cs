using System;
using System.Collections.Generic;
using System.Text;
using jp.nyatla.nymmd.cs.types;

namespace jp.nyatla.nymmd.cs.struct_type.pmd
{
    public class PMD_FACE_VTX : StructType
    {
        public int ulIndex;
        public MmdVector3 vec3Pos = new MmdVector3();
        public void read(DataReader i_reader)
        {
            this.ulIndex = i_reader.readInt();
            StructReader.read(this.vec3Pos, i_reader);
            return;
        }
        public static PMD_FACE_VTX[] createArray(int i_length)
        {
            PMD_FACE_VTX[] ret = new PMD_FACE_VTX[i_length];
            for (int i = 0; i < i_length; i++)
            {
                ret[i] = new PMD_FACE_VTX();
            }
            return ret;
        }
        public void setValue(PMD_FACE_VTX i_value)
        {
            this.ulIndex = i_value.ulIndex;
            this.vec3Pos.setValue(i_value.vec3Pos);
            return;
        }
    }

}
