using System;
using System.Collections.Generic;
using System.Text;
using jp.nyatla.nymmd.cs.types;

namespace jp.nyatla.nymmd.cs
{
    public interface IMmdPmdRender:IDisposable
    {
        void setPmd(MmdPmdModel i_pmd, IMmdDataIo i_io);
        void updateSkinning(MmdMatrix[] i_skinning_mat);
        void render();
    }
}
