using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace jp.nyatla.nymmd.cs
{
    public interface IMmdDataIo
    {
        /**
         * i_nameを読みだすinputstreamを要求する。
         * 関数は、i_nameに対応するリソースを読みだすストリームを返すこと。
         * @param i_name
         * @return
         */
        Stream request(String i_name);
    }
}
