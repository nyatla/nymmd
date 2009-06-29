using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace jp.nyatla.nymmd.cs
{
    public class MmdException :Exception
    {

        public MmdException():base()
        {
        }

        public MmdException(Exception e):base("",e)
        {
        }

        public MmdException(String m):base(m)
        {
        }

        public static void trap(String m)
        {
            throw new MmdException("トラップ:" + m);
        }

        public static void notImplement()
        {
            throw new MmdException("Not Implement!");
        }
    }
}
