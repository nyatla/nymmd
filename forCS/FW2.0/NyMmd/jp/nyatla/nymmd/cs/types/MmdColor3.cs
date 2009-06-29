using System;
using System.Collections.Generic;
using System.Text;

namespace jp.nyatla.nymmd.cs.types
{
    public class MmdColor3
    {
        public float r, g, b;

        public void setValue(MmdColor4 v)
        {
            this.r = v.r;
            this.g = v.g;
            this.b = v.b;
            return;
        }
    }
}
