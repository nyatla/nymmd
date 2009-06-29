using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace jp.nyatla.nymmd.cs.types
{
    public class MmdTexUV
    {
        public float u, v;
        public static MmdTexUV[] createArray(int i_length)
        {
            MmdTexUV[] ret = new MmdTexUV[i_length];
            for (int i = 0; i < i_length; i++)
            {
                ret[i] = new MmdTexUV();
            }
            return ret;
        }
        public void setValue(MmdTexUV v)
        {
            this.u = v.u;
            this.v = v.v;
            return;
        }
    }
}
