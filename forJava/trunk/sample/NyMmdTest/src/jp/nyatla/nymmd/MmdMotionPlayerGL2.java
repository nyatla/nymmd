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





public class MmdMotionPlayerGL2 extends MmdMotionPlayer
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
	public MmdMotionPlayerGL2(GL i_gl)
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
	private TextureList _tex_list;
	private final MmdMatrix __tmp_matrix = new MmdMatrix();
	private Material[] _materials;
	private float[] _fbuf;
	private FloatBuffer _pos_array;
	private FloatBuffer _nom_array;
	private FloatBuffer _tex_array;	
	
	
	public void setPmd(MmdPmdModel_BasicClass i_pmd_model) throws MmdException
	{
		super.setPmd(i_pmd_model);
		
		//確保済みリソースのリセット
		this._tex_list.clear();
		//OpenGLResourceの生成
		final int number_of_vertex=i_pmd_model.getNumberOfVertex();
		this._pos_array=makeFloatBuffer(number_of_vertex* 3);
		this._nom_array=makeFloatBuffer(number_of_vertex * 3);
		this._tex_array=makeFloatBuffer(this._ref_pmd_model.getUvArray().length*2);
		this._fbuf=new float[number_of_vertex*3*2];
		
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

		FloatBuffer tex_array = this._tex_array;
		tex_array.position(0);
		final MmdTexUV[] texture_uv = this._ref_pmd_model.getUvArray();
		for (int i = 0; i < number_of_vertex; i++) {
			tex_array.put(texture_uv[i].u);
			tex_array.put(texture_uv[i].v);
		}		
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
		MmdVector3 vp;
		MmdMatrix mat;
		MmdVector3[] org_pos_array=this._ref_pmd_model.getPositionArray();
		MmdVector3[] org_normal_array=this._ref_pmd_model.getNormatArray();
		PmdSkinInfo[] org_skin_info=this._ref_pmd_model.getSkinInfoArray();
		
		int number_of_vertex=this._ref_pmd_model.getNumberOfVertex();
		float[] ft=this._fbuf;
		int p1=0;
		int p2=number_of_vertex*3;
		for (int i = 0; i<this._ref_pmd_model.getNumberOfVertex() ; i++)
		{
			PmdSkinInfo info_ptr=org_skin_info[i];
			if (info_ptr.fWeight == 0.0f)
			{
				mat = i_skinning_mat[info_ptr.unBoneNo_1];
			} else if (info_ptr.fWeight >= 0.9999f) {
				mat = i_skinning_mat[info_ptr.unBoneNo_0];
			} else {
				final MmdMatrix mat0 = i_skinning_mat[info_ptr.unBoneNo_0];
				final MmdMatrix mat1 = i_skinning_mat[info_ptr.unBoneNo_1];
				mat = this.__tmp_matrix;
				mat.MatrixLerp(mat0, mat1, info_ptr.fWeight);
			}
			vp=org_pos_array[i];
			ft[p1++]=((float)(vp.x * mat.m00 + vp.y * mat.m10 + vp.z * mat.m20 + mat.m30));
			ft[p1++]=((float)(vp.x * mat.m01 + vp.y * mat.m11 + vp.z * mat.m21 + mat.m31));
			ft[p1++]=((float)(vp.x * mat.m02 + vp.y * mat.m12 + vp.z * mat.m22 + mat.m32));			
			
			vp=org_normal_array[i];
			ft[p2++]=((float)(vp.x * mat.m00 + vp.y * mat.m10 + vp.z * mat.m20));
			ft[p2++]=((float)(vp.x * mat.m01 + vp.y * mat.m11 + vp.z * mat.m21));
			ft[p2++]=((float)(vp.x * mat.m02 + vp.y * mat.m12 + vp.z * mat.m22));
		}
		this._pos_array.position(0);
		this._pos_array.put(ft,0,number_of_vertex*3);
		this._nom_array.position(0);
		this._nom_array.put(ft,number_of_vertex*3,number_of_vertex*3);
		return;
	}
	public void render()
	{	
		final GL gl = this._gl;
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glCullFace(GL.GL_FRONT);
		gl.glEnable(GL.GL_NORMALIZE);
		gl.glEnableClientState(GL.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);		
		
		
		// とりあえずbufferに変換しよう
		this._pos_array.position(0);
		this._nom_array.position(0);
		this._tex_array.position(0);
		// とりあえず転写用
		

		// 頂点座標、法線、テクスチャ座標の各配列をセット
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, this._pos_array);
		gl.glNormalPointer(GL.GL_FLOAT, 0, this._nom_array);
		gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, this._tex_array);
		for (int i = this._materials.length-1; i>=0 ; i--)
		{
			final Material mt_ptr=this._materials[i];
			// マテリアル設定
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, mt_ptr.color, 0);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, mt_ptr.color, 4);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, mt_ptr.color,8);
			gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, mt_ptr.fShininess);

            //カリング判定：何となくうまくいったから
            if ((0x100 & mt_ptr.unknown) == 0x100)
            {
            	gl.glDisable(GL.GL_CULL_FACE);
            }
            else
            {
            	gl.glEnable(GL.GL_CULL_FACE);
            }
            
			if (mt_ptr.texture_id!=0) {
				// テクスチャありならBindする
				gl.glEnable(GL.GL_TEXTURE_2D);
				gl.glBindTexture(GL.GL_TEXTURE_2D, mt_ptr.texture_id);
			} else {
				// テクスチャなし
				gl.glDisable(GL.GL_TEXTURE_2D);
			}
			// 頂点インデックスを指定してポリゴン描画
			gl.glDrawElements(GL.GL_TRIANGLES, mt_ptr.ulNumIndices, GL.GL_UNSIGNED_SHORT, mt_ptr.indices);
		}
		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		return;
	}

    private static FloatBuffer makeFloatBuffer(int i_size)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(i_size*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        return fb;
    }
}
