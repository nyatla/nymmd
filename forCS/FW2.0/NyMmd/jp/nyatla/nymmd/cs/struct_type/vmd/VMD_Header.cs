using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace jp.nyatla.nymmd.cs.struct_type.vmd
{
    public class VMD_Header : StructType
    {
        public String szHeader;
        public String szModelName;
        public void read(DataReader i_reader)
        {
            //szHeader
            this.szHeader = i_reader.readAscii(30);
            //szModelName
            this.szModelName = i_reader.readAscii(20);
            return;
        }
    }
}
