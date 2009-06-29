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
package jp.nyatla.nymmd;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;

import jp.nyatla.nymmd.types.*;

import java.util.*;

class GLTextureData
{
	public int gl_texture_id;

	public String file_name;
}

class GLTextureList
{
	private final ArrayList<GLTextureData> m_pTextureList = new ArrayList<GLTextureData>();

	private GL _gl;

	public GLTextureList(GL i_gl)
	{
		this._gl = i_gl;
	}

	public void reset()
	{
		for (int i = 0; i < m_pTextureList.size(); i++) {
			final int[] ids = new int[1];
			ids[0] = this.m_pTextureList.get(i).gl_texture_id;
			this._gl.glDeleteTextures(1, ids, 0);
		}
		this.m_pTextureList.clear();
		return;
	}

	private GLTextureData createTexture(String szFileName, InputStream i_st) throws MmdException
	{
		IntBuffer texid = IntBuffer.allocate(1);
		BufferedImage img;
		try {
			img = ImageIO.read(i_st);
		} catch (Exception e) {
			throw new MmdException();
		}
		_gl.glGenTextures(1, texid);
		_gl.glBindTexture(GL.GL_TEXTURE_2D, texid.get(0));
		_gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 4);

		_gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		_gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		_gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		_gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		// 転写

		int[] rgb_array = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		_gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, img.getWidth(), img.getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, IntBuffer.wrap(rgb_array));
		FloatBuffer prio = FloatBuffer.allocate(1);
		prio.put(0, 1.0f);
		_gl.glPrioritizeTextures(1, texid, prio);

		GLTextureData ret = new GLTextureData();
		ret.file_name = szFileName;
		ret.gl_texture_id = texid.get(0);
		return ret;
	}

	public GLTextureData getTexture(String i_filename, IMmdDataIo i_io) throws MmdException
	{
		GLTextureData ret;

		final int len = this.m_pTextureList.size();
		for (int i = 0; i < len; i++) {
			ret = this.m_pTextureList.get(i);
			if (ret.file_name.equalsIgnoreCase(i_filename)) {
				// 読み込み済みのテクスチャを発見
				return ret;
			}
		}

		// なければファイルを読み込んでテクスチャ作成
		ret = createTexture(i_filename, i_io.request(i_filename));
		if (ret != null) {
			this.m_pTextureList.add(ret);
			return ret;
		}

		return null;// テクスチャ読み込みか作成失敗

	}
}

class GLMaterial
{
	public final float[] color = new float[12];// Diffuse,Specular,Ambientの順
	public float fShininess;
	public ShortBuffer indices_size;
	public int ulNumIndices;
	public GLTextureData texture;
}

public class MmdPmdRenderGL implements IMmdPmdRender
{
	private MmdPmdModel _ref_pmd;

	private GLMaterial[] _gl_materials;
	//レンダリング時の計算用
	private Vector3[] _position_array;
	private Vector3[] _normal_array;

	private GL _gl;

	private GLTextureList _textures;

	public MmdPmdRenderGL(GL i_gl)
	{
		this._gl = i_gl;
		this._textures = new GLTextureList(i_gl);
		return;
	}

	public void dispose()
	{
		this._textures.reset();
	}

	/**
	 * レンダリング対象のPMDを設定する。
	 * @param i_pmd
	 */
	public void setPmd(MmdPmdModel i_pmd, IMmdDataIo i_io) throws MmdException
	{
		// テクスチャリストのリセット
		this._textures.reset();
		//PMD内部を参照（サボってるので、i_pmdを消さないこと）
		this._ref_pmd=i_pmd;
		//内部用
		final int number_of_vertex=i_pmd.getNumberOfVertex();
		this._position_array = Vector3.createArray(number_of_vertex);
		this._normal_array = Vector3.createArray(number_of_vertex);

		//Material配列の作成
		PmdMaterial[] m = i_pmd.getMaterials();// this._ref_materials;
		Vector<GLMaterial> gl_materials = new Vector<GLMaterial>();
		for (int i = 0; i < m.length; i++) {
			final GLMaterial new_material = new GLMaterial();
			// D,A,S[rgba]
			m[i].col4Diffuse.getValue(new_material.color, 0);
			m[i].col4Ambient.getValue(new_material.color, 4);
			m[i].col4Specular.getValue(new_material.color,8);
			new_material.fShininess = m[i].fShininess;
			if (m[i].texture_name != null) {
				new_material.texture = this._textures.getTexture(m[i].texture_name, i_io);
			} else {
				new_material.texture = null;
			}
			new_material.indices_size=ShortBuffer.wrap(m[i].indices);
			new_material.ulNumIndices = m[i].indices.length;
			gl_materials.add(new_material);
		}
		this._gl_materials = gl_materials.toArray(new GLMaterial[gl_materials.size()]);

		return;
	}

	private final Matrix __tmp_matrix = new Matrix();
	/**
	 * i_skinning_matでPMDを更新した頂点データを取得する。
	 * @param i_skinning_mat
	 * @param i_pos_array
	 * @param i_normal_array
	 * @param i_skin_info
	 */
	public void updateSkinning(Matrix[] i_skinning_mat)
	{
		int number_of_vertex = this._ref_pmd.getNumberOfVertex();
		Vector3[] org_pos_array=this._ref_pmd.getPositionArray();
		Vector3[] org_normal_array=this._ref_pmd.getNormatArray();
		PmdSkinInfo[] org_skin_info=this._ref_pmd.getSkinInfoArray();

		// 頂点スキニング
		final Matrix matTemp = this.__tmp_matrix;
		for (int i = 0; i < number_of_vertex; i++) {
			if (org_skin_info[i].fWeight == 0.0f) {
				final Matrix mat = i_skinning_mat[org_skin_info[i].unBoneNo[1]];
				this._position_array[i].Vector3Transform(org_pos_array[i], mat);
				this._normal_array[i].Vector3Rotate(org_normal_array[i], mat);
			} else if (org_skin_info[i].fWeight >= 0.9999f) {
				final Matrix mat = i_skinning_mat[org_skin_info[i].unBoneNo[0]];
				this._position_array[i].Vector3Transform(org_pos_array[i], mat);
				this._normal_array[i].Vector3Rotate(org_normal_array[i], mat);
			} else {
				final Matrix mat0 = i_skinning_mat[org_skin_info[i].unBoneNo[0]];
				final Matrix mat1 = i_skinning_mat[org_skin_info[i].unBoneNo[1]];

				matTemp.MatrixLerp(mat0, mat1, org_skin_info[i].fWeight);

				this._position_array[i].Vector3Transform(org_pos_array[i], matTemp);
				this._normal_array[i].Vector3Rotate(org_normal_array[i], matTemp);
			}
		}
		return;
	}

	public void render()
	{
		final TexUV[] texture_uv = this._ref_pmd.getUvArray();
		final int number_of_vertex = this._ref_pmd.getNumberOfVertex();
		
		final GL gl = this._gl;
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		// とりあえずbufferに変換しよう
		ByteBuffer pos_buf = ByteBuffer.allocateDirect(_position_array.length * 3 * 4);
		pos_buf.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < number_of_vertex; i++) {
			pos_buf.putFloat(_position_array[i].x);
			pos_buf.putFloat(_position_array[i].y);
			pos_buf.putFloat(_position_array[i].z);
		}
		ByteBuffer nom_array = ByteBuffer.allocateDirect(_position_array.length * 3 * 4);
		nom_array.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < number_of_vertex; i++) {
			nom_array.putFloat(_normal_array[i].x);
			nom_array.putFloat(_normal_array[i].y);
			nom_array.putFloat(_normal_array[i].z);
		}
		ByteBuffer tex_array = ByteBuffer.allocateDirect(texture_uv.length * 2 * 4);
		tex_array.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < number_of_vertex; i++) {
			tex_array.putFloat(texture_uv[i].u);
			tex_array.putFloat(texture_uv[i].v);
		}
		pos_buf.position(0);
		nom_array.position(0);
		tex_array.position(0);
		// とりあえず転写用

		// 頂点座標、法線、テクスチャ座標の各配列をセット
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, pos_buf);
		gl.glNormalPointer(GL.GL_FLOAT, 0, nom_array);
		gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, tex_array);
		int vertex_index = 0;
		for (int i = 0; i < this._gl_materials.length; i++) {
			// マテリアル設定
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, this._gl_materials[i].color, 0);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, this._gl_materials[i].color, 4);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, this._gl_materials[i].color,8);
			gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, this._gl_materials[i].fShininess);
			if (this._gl_materials[i].color[3] < 1.0f) {// col4Diffuse.a
				gl.glDisable(GL.GL_CULL_FACE);
			} else {
				gl.glEnable(GL.GL_CULL_FACE);
			}

			if (this._gl_materials[i].texture != null) {
				// テクスチャありならBindする
				gl.glEnable(GL.GL_TEXTURE_2D);
				gl.glBindTexture(GL.GL_TEXTURE_2D, this._gl_materials[i].texture.gl_texture_id);
			} else {
				// テクスチャなし
				gl.glDisable(GL.GL_TEXTURE_2D);
			}
			// 頂点インデックスを指定してポリゴン描画
			gl.glDrawElements(GL.GL_TRIANGLES, this._gl_materials[i].ulNumIndices, GL.GL_UNSIGNED_SHORT, this._gl_materials[i].indices_size);
			vertex_index += this._gl_materials[i].ulNumIndices;
		}

		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		return;
	}
}
