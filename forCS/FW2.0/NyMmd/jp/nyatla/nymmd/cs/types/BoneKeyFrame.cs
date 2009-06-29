using System;
using System.Collections.Generic;
using System.Text;

namespace jp.nyatla.nymmd.cs.types
{
    public class BoneKeyFrame
    {
        public float fFrameNo;		// フレーム番号
        public MmdVector3 vec3Position = new MmdVector3();	// 位置
        public MmdVector4 vec4Rotate = new MmdVector4();		// 回転(クォータニオン)
        public static BoneKeyFrame[] createArray(int i_length)
        {
            BoneKeyFrame[] ret = new BoneKeyFrame[i_length];
            for (int i = 0; i < i_length; i++)
            {
                ret[i] = new BoneKeyFrame();
            }
            return ret;
        }
    }
}
