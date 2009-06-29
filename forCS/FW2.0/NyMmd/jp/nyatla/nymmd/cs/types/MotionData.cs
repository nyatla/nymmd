using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace jp.nyatla.nymmd.cs.types
{
    public class MotionData
    {
        public String szBoneName;			// ボーン名
        public int ulNumKeyFrames;	// キーフレーム数
        public BoneKeyFrame[] pKeyFrames;	// キーフレームデータ配列
    }
}
