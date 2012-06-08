package jp.nyatla.nymmd;


import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import jp.nyatla.nymmd.types.*;





public class MmdMotionPlayerGL extends MmdMotionPlayer
{
	private class TextureList extends ArrayList<TextureList.Item>
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		class Item {
			public int gl_texture_id;
			public String file_name;
		}
		private GL _gl;

		public TextureList(GL i_gl)
		{
			this._gl = i_gl;
		}
		public void clear()
		{
			for (TextureList.Item i : this) {
				final int[] ids = {i.gl_texture_id};
				this._gl.glDeleteTextures(1, ids, 0);
			}
			super.clear();
		}
		private TextureList.Item createTexture(String szFileName, InputStream i_st) throws MmdException
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

			TextureList.Item ret = new TextureList.Item();
			ret.file_name = szFileName;
			ret.gl_texture_id = texid.get(0);
			return ret;
		}
		public int getTexture(String i_name, MmdPmdModel_BasicClass.IResourceProvider i_io) throws MmdException
		{
			try{
				for (TextureList.Item i : this){
					if (i.file_name.equalsIgnoreCase(i_name)) {
						// 読み込み済みのテクスチャを発見
						return i.gl_texture_id;
					}
				}
				// なければファイルを読み込んでテクスチャ作成
				TextureList.Item ret =this.createTexture(i_name, i_io.getTextureStream(i_name));
				if (ret != null) {
					this.add(ret);
					return ret.gl_texture_id;
				}
			}catch(Exception e)
			{
				throw new MmdException(e);
			}
			// テクスチャ読み込みか作成失敗
			throw new MmdException();
		}
	}	
	
	private class Material
	{
		public final float[] color = new float[12];// Diffuse,Specular,Ambientの順
		public float fShininess;
		public ShortBuffer indices;
		public int ulNumIndices;
		public int texture_id;
		public int unknown;
	}	
	public MmdMotionPlayerGL(GL i_gl)
	{
		super();
		this._gl=i_gl;
		this._tex_list=new TextureList(i_gl);
	}
	public void dispose()
	{
		this._tex_list.clear();
	}	
	private GL _gl;
	private MmdVector3[] _position_array;
	private MmdVector3[] _normal_array;
	private TextureList _tex_list;
	private final MmdMatrix __tmp_matrix = new MmdMatrix();
	private Material[] _materials;
	public void setPmd(MmdPmdModel_BasicClass i_pmd_model) throws MmdException
	{
		super.setPmd(i_pmd_model);
		
		//確保済みリソースのリセット
		this._tex_list.clear();
		//OpenGLResourceの生成
		final int number_of_vertex=i_pmd_model.getNumberOfVertex();
		this._position_array = MmdVector3.createArray(number_of_vertex);
		this._normal_array = MmdVector3.createArray(number_of_vertex);
		
		MmdPmdModel_BasicClass.IResourceProvider tp=i_pmd_model.getResourceProvider();
		
		//Material配列の作成
		PmdMaterial[] m = i_pmd_model.getMaterials();// this._ref_materials;
		Vector<Material> materials = new Vector<Material>();
		for (int i = 0; i < m.length; i++){
			final Material new_material = new Material();
			new_material.unknown=m[i].unknown;
			// D,A,S[rgba]
			m[i].col4Diffuse.getValue(new_material.color, 0);
			m[i].col4Ambient.getValue(new_material.color, 4);
			m[i].col4Specular.getValue(new_material.color,8);
			new_material.fShininess = m[i].fShininess;
			if (m[i].texture_name != null)
			{
				new_material.texture_id = this._tex_list.getTexture(m[i].texture_name,tp);
			} else {
				new_material.texture_id = 0;
			}
			new_material.indices=ShortBuffer.wrap(m[i].indices);
			new_material.ulNumIndices = m[i].indices.length;
			materials.add(new_material);
		}
		this._materials = materials.toArray(new Material[materials.size()]);

		return;		
	}
	public void setVmd(MmdVmdMotion_BasicClass i_vmd_model) throws MmdException
	{
		super.setVmd(i_vmd_model);
	}
	/**
	 * この関数はupdateMotionがskinning_matを更新するを呼び出します。
	 */
	protected void onUpdateSkinningMatrix(MmdMatrix[] i_skinning_mat) throws MmdException
	{
		int number_of_vertex = this._ref_pmd_model.getNumberOfVertex();
		MmdVector3[] org_pos_array=this._ref_pmd_model.getPositionArray();
		MmdVector3[] org_normal_array=this._ref_pmd_model.getNormatArray();
		PmdSkinInfo[] org_skin_info=this._ref_pmd_model.getSkinInfoArray();

		for (int i = 0; i < number_of_vertex; i++)
		{
			PmdSkinInfo info_ptr=org_skin_info[i];
			if (info_ptr.fWeight == 0.0f) {
				final MmdMatrix mat = i_skinning_mat[info_ptr.unBoneNo_1];
				this._position_array[i].Vector3Transform(org_pos_array[i], mat);
				this._normal_array[i].Vector3Rotate(org_normal_array[i], mat);
			} else if (info_ptr.fWeight >= 0.9999f) {
				final MmdMatrix mat = i_skinning_mat[info_ptr.unBoneNo_0];
				this._position_array[i].Vector3Transform(org_pos_array[i], mat);
				this._normal_array[i].Vector3Rotate(org_normal_array[i], mat);
			} else {
				final MmdMatrix mat0 = i_skinning_mat[info_ptr.unBoneNo_0];
				final MmdMatrix mat1 = i_skinning_mat[info_ptr.unBoneNo_1];
				final MmdMatrix matTemp = this.__tmp_matrix;
				matTemp.MatrixLerp(mat0, mat1, info_ptr.fWeight);

				this._position_array[i].Vector3Transform(org_pos_array[i], matTemp);
				this._normal_array[i].Vector3Rotate(org_normal_array[i], matTemp);
			}
		}
		return;
	}
	public void render()
	{
		final GL gl = this._gl;
		final MmdTexUV[] texture_uv = this._ref_pmd_model.getUvArray();
		final int number_of_vertex = this._ref_pmd_model.getNumberOfVertex();
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
		for (int i = 0; i < this._materials.length; i++) {
			// マテリアル設定
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, this._materials[i].color, 0);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, this._materials[i].color, 4);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, this._materials[i].color,8);
			gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, this._materials[i].fShininess);

            //カリング判定：何となくうまくいったから
            if ((0x100 & this._materials[i].unknown) == 0x100)
            {
            	gl.glDisable(GL.GL_CULL_FACE);
            }
            else
            {
            	gl.glEnable(GL.GL_CULL_FACE);
            }
            
			if (this._materials[i].texture_id!=0) {
				// テクスチャありならBindする
				gl.glEnable(GL.GL_TEXTURE_2D);
				gl.glBindTexture(GL.GL_TEXTURE_2D, this._materials[i].texture_id);
			} else {
				// テクスチャなし
				gl.glDisable(GL.GL_TEXTURE_2D);
			}
			// 頂点インデックスを指定してポリゴン描画
			gl.glDrawElements(GL.GL_TRIANGLES, this._materials[i].ulNumIndices, GL.GL_UNSIGNED_SHORT, this._materials[i].indices);
			vertex_index += this._materials[i].ulNumIndices;
		}

		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		return;
	}	

}
