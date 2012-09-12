/* 
 * PROJECT: NyMmd for C#
 * --------------------------------------------------------------------------------
 * The MMD for C# is C# version MMD Motion player class library.
 * NyMmd is modules which removed the ARToolKit origin codes from ARTK_MMD,
 * and was ported to C#. 
 *
 * This is based on the ARTK_MMD v0.1 by PY.
 * http://ppyy.if.land.to/artk_mmd.html
 * py1024<at>gmail.com
 * http://www.nicovideo.jp/watch/sm7398691
 *
 * 
 * The MIT License
 * Copyright (C)2008-2012 nyatla
 * nyatla39<at>gmail.com
 * http://nyatla.jp
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */
using System;
using System.Collections.Generic;
using System.Text;
using jp.nyatla.nymmd.cs.types;
using jp.nyatla.nymmd.cs.struct_type.pmd;

namespace jp.nyatla.nymmd.cs.core
{
    public class PmdFace
    {
        private String _name;

        private PMD_FACE_VTX[] _face_vertex; // 表情頂点データ

        public PmdFace(PMD_FACE pPMDFaceData, PmdFace pPMDFaceBase)
        {
            // 表情名のコピー
            this._name = pPMDFaceData.szName;

            // 表情頂点データのコピー
            int number_of_vertex = pPMDFaceData.ulNumVertices;

            this._face_vertex = PMD_FACE_VTX.createArray(number_of_vertex);
            for (int i = 0; i < this._face_vertex.Length; i++)
            {
                this._face_vertex[i].setValue(pPMDFaceData.pVertices[i]);
            }
            // baseとの相対インデックスを絶対インデックスに変換
            if (pPMDFaceBase != null)
            {
                PMD_FACE_VTX[] vertex_array = this._face_vertex;
                for (int i = 0; i < this._face_vertex.Length; i++)
                {
                    PMD_FACE_VTX vertex = vertex_array[i];
                    vertex.vec3Pos.Vector3Add(pPMDFaceBase._face_vertex[vertex.ulIndex].vec3Pos, vertex.vec3Pos);
                    vertex.ulIndex = pPMDFaceBase._face_vertex[vertex.ulIndex].ulIndex;
                }
            }
            return;
        }

        public void setFace(MmdVector3[] pvec3Vertices)
        {
            if (this._face_vertex == null)
            {
                throw new MmdException();
            }

            PMD_FACE_VTX[] vertex_array = this._face_vertex;
            for (int i = 0; i < vertex_array.Length; i++)
            {
                PMD_FACE_VTX vertex = vertex_array[i];
                pvec3Vertices[vertex.ulIndex].setValue(vertex.vec3Pos);
            }
            return;
        }

        public void blendFace(MmdVector3[] pvec3Vertices, float fRate)
        {
            if (this._face_vertex == null)
            {
                throw new MmdException();
            }

            PMD_FACE_VTX[] vertex_array = this._face_vertex;
            for (int i = 0; i < vertex_array.Length; i++)
            {
                PMD_FACE_VTX vertex = vertex_array[i];
                int ulIndex = vertex.ulIndex;
                pvec3Vertices[ulIndex].Vector3Lerp(pvec3Vertices[ulIndex], vertex.vec3Pos, fRate);
            }
            return;
        }

        public String getName()
        {
            return this._name;
        }
    }
}
