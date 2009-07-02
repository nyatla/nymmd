using System;
using System.Collections.Generic;
using System.Text;
using jp.nyatla.nymmd.cs.types;


namespace jp.nyatla.nymmd.cs.struct_type.pmd
{
    public class PMD_Material : StructType
    {
        public MmdColor4 col4Diffuse = new MmdColor4();
        public float fShininess;
        public MmdColor3 col3Specular = new MmdColor3();
        public MmdColor3 col3Ambient = new MmdColor3();
        public int unknown;
        public int ulNumIndices;		// この材質に対応する頂点数
        public String szTextureFileName;	// テクスチャファイル名
        public void read(DataReader i_reader)
        {
            StructReader.read(this.col4Diffuse, i_reader);
            this.fShininess = i_reader.readFloat();
            StructReader.read(this.col3Specular, i_reader);
            StructReader.read(this.col3Ambient, i_reader);
            this.unknown = i_reader.readUnsignedShort();
            this.ulNumIndices = i_reader.readInt();
            this.szTextureFileName = i_reader.readAscii(20);
            return;
        }
    }
}
