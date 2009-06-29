using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace jp.nyatla.nymmd.cs.types
{
    public class FaceData
    {
        public String szFaceName;	// 表情名
        public int ulNumKeyFrames;	// キーフレーム数
        public FaceKeyFrame[] pKeyFrames;	// キーフレームデータ配列
    }
}
