using System;
using System.Collections.Generic;
using System.Text;
using jp.nyatla.nymmd.cs.types;

namespace jp.nyatla.nymmd.cs.struct_type.pmd
{
    public class PMD_Vertex : StructType
    {
        public TMmdVector3 vec3Pos;// 座標
        public TMmdVector3 vec3Normal;	// 法線ベクトル
        public MmdTexUV uvTex = new MmdTexUV();		// テクスチャ座標

        public int[] unBoneNo = new int[2];	// ボーン番号
        public int cbWeight;		// ブレンドの重み (0～100％)
        public int cbEdge;			// エッジフラグ

        public void read(DataReader i_reader)
        {
            StructReader.read(ref this.vec3Pos, i_reader);
            StructReader.read(ref this.vec3Normal, i_reader);
            StructReader.read(this.uvTex, i_reader);
            this.unBoneNo[0] = i_reader.readUnsignedShort();
            this.unBoneNo[1] = i_reader.readUnsignedShort();
            this.cbWeight = i_reader.read();
            this.cbEdge = i_reader.read();
            return;
        }

    }

}
