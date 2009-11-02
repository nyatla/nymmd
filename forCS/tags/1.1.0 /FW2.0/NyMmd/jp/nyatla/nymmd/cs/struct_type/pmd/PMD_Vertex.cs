using System;
using System.Collections.Generic;
using System.Text;
using jp.nyatla.nymmd.cs.types;

namespace jp.nyatla.nymmd.cs.struct_type.pmd
{
    public class PMD_Vertex : StructType
    {
        public MmdVector3 vec3Pos = new MmdVector3();	// 座標
        public MmdVector3 vec3Normal = new MmdVector3();	// 法線ベクトル
        public MmdTexUV uvTex = new MmdTexUV();		// テクスチャ座標

        public int[] unBoneNo = new int[2];	// ボーン番号
        public int cbWeight;		// ブレンドの重み (0～100％)
        public int cbEdge;			// エッジフラグ

        public void read(DataReader i_reader)
        {
            StructReader.read(this.vec3Pos, i_reader);
            StructReader.read(this.vec3Normal, i_reader);
            StructReader.read(this.uvTex, i_reader);
            this.unBoneNo[0] = i_reader.readUnsignedShort();
            this.unBoneNo[1] = i_reader.readUnsignedShort();
            this.cbWeight = i_reader.read();
            this.cbEdge = i_reader.read();
            return;
        }

    }

}
