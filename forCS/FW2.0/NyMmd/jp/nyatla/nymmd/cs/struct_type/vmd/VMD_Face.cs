using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace jp.nyatla.nymmd.cs.struct_type.vmd
{
    class VMD_Face : StructType
    {
        public String szFaceName;
        public long ulFrameNo;
        public float fFactor;
        public void read(DataReader i_reader)
        {
            //szFaceName
            this.szFaceName = i_reader.readAscii(15);
            this.ulFrameNo = i_reader.readInt();
            this.fFactor = i_reader.readFloat();
            return;
        }
    }
}
