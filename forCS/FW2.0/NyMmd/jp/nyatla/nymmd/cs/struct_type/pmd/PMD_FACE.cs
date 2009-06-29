using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace jp.nyatla.nymmd.cs.struct_type.pmd
{
    public class PMD_FACE : StructType
    {
        public String szName;		// 表情名 (0x00 終端，余白は 0xFD)
        public int ulNumVertices;	// 表情頂点数
        public int cbType;			// 分類
        public PMD_FACE_VTX[] pVertices = PMD_FACE_VTX.createArray(64);// 表情頂点データ
        public void read(DataReader i_reader)
        {
            int i;
            //szName
            this.szName = i_reader.readAscii(20);
            this.ulNumVertices = i_reader.readInt();
            this.cbType = i_reader.read();
            //必要な数だけ配列を確保しなおす。
            if (this.ulNumVertices > this.pVertices.Length)
            {
                this.pVertices = PMD_FACE_VTX.createArray(this.ulNumVertices);
            }
            for (i = 0; i < this.ulNumVertices; i++)
            {
                this.pVertices[i].read(i_reader);
            }
            return;
        }

    }

}
