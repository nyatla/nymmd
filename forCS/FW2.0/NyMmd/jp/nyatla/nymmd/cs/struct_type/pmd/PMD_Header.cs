using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace jp.nyatla.nymmd.cs.struct_type.pmd
{
    public class PMD_Header : StructType
    {
        public const int SIZE_OF_STRUCT = 3 + 4 + 20 + 256;
        public String szMagic;
        public float fVersion;
        public String szName;
        public String szComment;

        public void read(DataReader i_reader)
        {
            this.szMagic = i_reader.readAscii(3);
            //
            this.fVersion = i_reader.readFloat();
            //szName
            this.szName = i_reader.readAscii(20);

            //szComment
            this.szComment = i_reader.readAscii(256);
            return;
        }

    }
}
