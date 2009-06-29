using System;
using System.Collections.Generic;
using System.Text;
using jp.nyatla.nymmd.cs.types;


namespace jp.nyatla.nymmd.cs.struct_type.vmd
{
    class VMD_Motion : StructType
    {
        public String szBoneName;	// ボーン名
        public long ulFrameNo;		// フレーム番号

        public MmdVector3 vec3Position = new MmdVector3();// 位置
        public MmdVector4 vec4Rotate = new MmdVector4();  // 回転(クォータニオン)

        public int[] cInterpolation1 = new int[16];	// 補間情報
        public int[] cInterpolation2 = new int[16];
        public int[] cInterpolation3 = new int[16];
        public int[] cInterpolation4 = new int[16];

        public void read(DataReader i_reader)
        {
            int i;
            //szName
            this.szBoneName = i_reader.readAscii(15);
            this.ulFrameNo = i_reader.readInt();
            StructReader.read(this.vec3Position, i_reader);
            StructReader.read(this.vec4Rotate, i_reader);
            for (i = 0; i < 16; i++)
            {
                this.cInterpolation1[i] = i_reader.readByte();
            }
            for (i = 0; i < 16; i++)
            {
                this.cInterpolation2[i] = i_reader.readByte();
            }
            for (i = 0; i < 16; i++)
            {
                this.cInterpolation3[i] = i_reader.readByte();
            }
            for (i = 0; i < 16; i++)
            {
                this.cInterpolation4[i] = i_reader.readByte();
            }
            return;
        }
    }
}
