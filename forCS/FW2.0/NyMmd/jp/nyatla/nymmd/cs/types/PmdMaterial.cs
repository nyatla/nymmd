using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace jp.nyatla.nymmd.cs.types
{
    public class PmdMaterial
    {
        public MmdColor4 col4Diffuse = new MmdColor4();
        public MmdColor4 col4Specular = new MmdColor4();
        public MmdColor4 col4Ambient = new MmdColor4();
        public float fShininess;
        public String texture_name;
        public short[] indices;
    };
}
