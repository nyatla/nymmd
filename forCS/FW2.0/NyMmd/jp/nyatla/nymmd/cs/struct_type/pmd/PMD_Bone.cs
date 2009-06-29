using System;
using System.Text;
using jp.nyatla.nymmd.cs.types;

namespace jp.nyatla.nymmd.cs.struct_type.pmd
{
    public class PMD_Bone : StructType
    {
        public String szName;			// ボーン名 (0x00 終端，余白は 0xFD)
        public int nParentNo;			// 親ボーン番号 (なければ -1)
        public int nChildNo;			// 子ボーン番号
        public int cbKind;		// ボーンの種類
        public int unIKTarget;	// IK時のターゲットボーン
        public MmdVector3 vec3Position = new MmdVector3();	// モデル原点からの位置

        public void read(DataReader i_reader)
        {
            //szName
            this.szName = i_reader.readAscii(20);
            this.nParentNo = i_reader.readShort();
            this.nChildNo = i_reader.readShort();
            this.cbKind = i_reader.readByte();
            this.unIKTarget = i_reader.readShort();
            StructReader.read(this.vec3Position, i_reader);
            return;
        }
    }

}
