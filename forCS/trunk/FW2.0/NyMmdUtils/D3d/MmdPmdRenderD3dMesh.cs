/* 
 * PROJECT: MMD for Java
 * --------------------------------------------------------------------------------
 * This work is based on the ARTK_MMD v0.1 
 *   PY
 * http://ppyy.hp.infoseek.co.jp/
 * py1024<at>gmail.com
 * http://www.nicovideo.jp/watch/sm7398691
 *
 * The MMD for Java is Java version MMD class library.
 * Copyright (C)2009 nyatla
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this framework; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * For further information please contact.
 *	http://nyatla.jp/
 *	<airmail(at)ebony.plala.or.jp>
 * 
 */
using System;
using System.Collections.Generic;
using System.Collections;
using System.Text;
using System.Drawing;
#if NyMmd_FRAMEWORK_CFW
using Microsoft.WindowsMobile.DirectX.Direct3D;
using Microsoft.WindowsMobile.DirectX;
#else
using Microsoft.DirectX;
using Microsoft.DirectX.Direct3D;
#endif
using System.IO;

using jp.nyatla.nymmd.cs;
using jp.nyatla.nymmd.cs.types;

namespace NyMmdUtils
{










    /*  MMDレンダラにちょっと細工をしてですね…。
     *  
     * 
     */
    public class MmdPmdRenderD3dMesh : IMmdPmdRender
    {
        private class NyMmdMeshContainer:IDisposable
        {
            public Mesh mesh;
            public Material[] material;
            public Texture[] texture;

            private D3dTextureList _texture_list;
            public NyMmdMeshContainer(Device i_device, IMmdDataIo i_io, PmdMaterial[] i_pmd_materials)
            {
                this._texture_list = new D3dTextureList(i_device);
                List<Material> mats = new List<Material>();
                List<Texture> texs = new List<Texture>();
                for (int i = 0; i < i_pmd_materials.Length; i++)
                {
                    Material m = new Material();
                    m.DiffuseColor = new ColorValue(i_pmd_materials[i].col4Diffuse.r, i_pmd_materials[i].col4Diffuse.g, i_pmd_materials[i].col4Diffuse.b, i_pmd_materials[i].col4Diffuse.a);
                    m.AmbientColor = new ColorValue(i_pmd_materials[i].col4Ambient.r, i_pmd_materials[i].col4Ambient.g, i_pmd_materials[i].col4Ambient.b, i_pmd_materials[i].col4Ambient.a);
                    m.SpecularColor = new ColorValue(i_pmd_materials[i].col4Specular.r, i_pmd_materials[i].col4Specular.g, i_pmd_materials[i].col4Specular.b, i_pmd_materials[i].col4Specular.a);
                    m.SpecularSharpness = i_pmd_materials[i].fShininess;
                    mats.Add(m);
                    if (i_pmd_materials[i].texture_name != null)
                    {
                        texs.Add(this._texture_list.getTexture(i_pmd_materials[i].texture_name, i_io));
                    }
                    else
                    {
                        texs.Add(null);
                    }
                }
                this.texture = texs.ToArray();
                this.material= mats.ToArray();
            }
            public void Dispose()
            {
                this._texture_list.Dispose();
                this.mesh.Dispose();
            }
        }
        private Device _device;
        public MmdPmdRenderD3dMesh(Device i_device)
        {
            this._device = i_device;
            return;
        }
        public void Dispose()
        {
            this._container.Dispose();
        }


        public static void makeSubsetIndex(PmdMaterial[] i_material,ref int[] attrBuf)
        {
            int index = 0;
            for (int i = 0; i < i_material.Length; i++)
            {
                for (int j = 0; j < i_material[i].number_of_indics; j += 3)
                {
                    attrBuf[index] = i;
                    index++;
                }
            }
        }
        public static VertexElement[] createVertexElements()
        {
            return new VertexElement[] {
                    new VertexElement(0, 0, DeclarationType.Float3, DeclarationMethod.Default, DeclarationUsage.Position, 0),
                    new VertexElement(0, sizeof(float) * 3, DeclarationType.Float3, DeclarationMethod.Default, DeclarationUsage.Normal, 0),
                    new VertexElement(0, sizeof(float) * 6, DeclarationType.Float2, DeclarationMethod.Default, DeclarationUsage.TextureCoordinate, 0),
                    VertexElement.VertexDeclarationEnd
                };
        }
        public void setPmd(MmdPmdModel i_pmd, IMmdDataIo i_io)
        {
            this._ref_pmd = i_pmd;

            Device dev = this._device;
  
            int number_of_vertex= i_pmd.getNumberOfVertex();
            this._vertex_array = new CustomVertex.PositionNormalTextured[number_of_vertex];

            MmdTexUV[] uv_array = i_pmd.getUvArray();
            //先にセットできるものはセットしておく
            for (int i = 0; i < number_of_vertex; i++)
            {
                this._vertex_array[i].Tu = uv_array[i].u;
                this._vertex_array[i].Tv = uv_array[i].v;
            }
 
            short[] indics_array = i_pmd.getIndicsArray();

            // メッシュを作成
            // 参考：http://msdn.microsoft.com/ja-jp/library/ms229646%28VS.80%29.aspx
            Mesh mesh = new Mesh(indics_array.Length / 3, number_of_vertex, MeshFlags.Managed, createVertexElements(), dev);
            mesh.VertexBuffer.SetData(this._vertex_array,0,LockFlags.None);
            mesh.IndexBuffer.SetData(indics_array, 0, LockFlags.None);
            dev.VertexDeclaration = new VertexDeclaration(dev, createVertexElements());

            
            // DrawSubsetのインデックスを設定
            int[] attrBuf = mesh.LockAttributeBufferArray(LockFlags.None);
            makeSubsetIndex(i_pmd.getMaterials(), ref attrBuf);
            mesh.UnlockAttributeBuffer(attrBuf);

            //コンテナを一回解放
            if (this._container != null)
            {
                this._container.Dispose();
                this._container = null;
            }
            // モデルをメッシュコンテナに格納
            NyMmdMeshContainer container = new NyMmdMeshContainer(dev, i_io, i_pmd.getMaterials());
            container.mesh=mesh;
            this._container = container;
        }
        private CustomVertex.PositionNormalTextured[] _vertex_array;
        private MmdPmdModel _ref_pmd;
        private NyMmdMeshContainer _container;

        //この関数でthis._vertex_arrayを更新する。
        public void updateSkinning(MmdMatrix[] i_skinning_mat)
        {
            MmdPmdModel pmd = this._ref_pmd;
            int number_of_vertex = pmd.getNumberOfVertex();
            MmdVector3[] org_pos_array = pmd.getPositionArray();
            MmdVector3[] org_normal_array = pmd.getNormatArray();
            PmdSkinInfo[] org_skin_info = pmd.getSkinInfoArray();
            CustomVertex.PositionNormalTextured[] vertex_array = this._vertex_array;
            // 頂点スキニング
            MmdMatrix matTemp = new MmdMatrix();
            MmdVector3 position = new MmdVector3();
            MmdVector3 normal = new MmdVector3();
            for (int i = 0; i < number_of_vertex; i++)
            {
                PmdSkinInfo si = org_skin_info[i];
                if (si.fWeight == 0.0f)
                {
                    MmdMatrix mat = i_skinning_mat[si.unBoneNo_1];
                    position.Vector3Transform(org_pos_array[i], mat);
                    normal.Vector3Rotate(org_normal_array[i], mat);
                }
                else if (si.fWeight >= 0.9999f)
                {
                    MmdMatrix mat = i_skinning_mat[si.unBoneNo_0];
                    position.Vector3Transform(org_pos_array[i], mat);
                    normal.Vector3Rotate(org_normal_array[i], mat);
                }
                else
                {
                    matTemp.MatrixLerp(i_skinning_mat[si.unBoneNo_0], i_skinning_mat[si.unBoneNo_1], si.fWeight);
                    position.Vector3Transform(org_pos_array[i], matTemp);
                    normal.Vector3Rotate(org_normal_array[i], matTemp);
                }
                //ここの転写は少し考える。
                vertex_array[i].X = position.x;
                vertex_array[i].Y = position.y;
                vertex_array[i].Z = position.z;
                vertex_array[i].Nx = normal.x;
                vertex_array[i].Ny = normal.y;
                vertex_array[i].Nz = normal.z;
            }
            
            return;
        }
        public void render()
        {
            Device dev = this._device;
            //頂点データをセット
            dev.RenderState.CullMode = Cull.None;
            GraphicsStream gs = this._container.mesh.LockVertexBuffer(LockFlags.Discard);
            gs.Write(this._vertex_array);
            gs.Dispose();
            this._container.mesh.UnlockVertexBuffer();

            for (int i = 0; i < this._container.material.Length; i++)
            {
                if (this._container.texture[i] != null)
                {
                    dev.SetTexture(0, this._container.texture[i]);
                }
                else
                {
                    dev.SetTexture(0, null);
                }
                dev.Material = this._container.material[i];
                this._container.mesh.DrawSubset(i);
            }
            return;
        }
    }


}
