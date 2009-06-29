using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace jp.nyatla.nymmd.cs.struct_type.pmd
{
    public class PMD_IK : StructType
    {
        public int nTargetNo;	// IKターゲットボーン番号
        public int nEffNo;		// IK先端ボーン番号
        public int cbNumLink;	// IKを構成するボーンの数
        public int unCount;
        public float fFact;
        public int[] punLinkNo = new int[128];// IKを構成するボーンの配列(可変長配列)

        public void read(DataReader i_reader)
        {
            this.nTargetNo = i_reader.readShort();
            this.nEffNo = i_reader.readShort();
            this.cbNumLink = i_reader.read();
            this.unCount = i_reader.readUnsignedShort();
            this.fFact = i_reader.readFloat();
            //必要な数だけ配列を確保しなおす。
            if (this.cbNumLink > this.punLinkNo.Length)
            {
                this.punLinkNo = new int[this.cbNumLink];
            }
            for (int i = 0; i < this.cbNumLink; i++)
            {
                this.punLinkNo[i] = i_reader.readUnsignedShort();
            }
            return;
        }
    }
}
