/* 
 * PROJECT: MMD for Java
 * --------------------------------------------------------------------------------
 * This work is based on the ARTK_MMD v0.1 
 *   PY
 * http://ppyy.web.fc2.com/
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

import java.util.*;
import java.io.*;

import jp.nyatla.nymmd.core.*;
import jp.nyatla.nymmd.struct.*;
import jp.nyatla.nymmd.struct.pmd.*;
import jp.nyatla.nymmd.types.*;


class PmdSkinInfo
{
	public float fWeight;// ウェイト
	public int[] unBoneNo=new int[2];	// ボーン番号
	public static PmdSkinInfo[] createArray(int i_length)
	{
		PmdSkinInfo[] ret=new PmdSkinInfo[i_length];
		for(int i=0;i<i_length;i++)
		{
			ret[i]=new PmdSkinInfo();
		}
		return ret;
	}		
}

class MmdPmdMaterial
{
	public final Color4 col4Diffuse=new Color4();
	public final Color4 col4Specular=new Color4();
	public final Color4 col4Ambient=new Color4();
	public float fShininess;
	public int ulNumIndices;
	public String texture_name;
	public static MmdPmdMaterial[] createArray(int i_length)
	{
		MmdPmdMaterial[] ret=new MmdPmdMaterial[i_length];
		for(int i=0;i<i_length;i++)
		{
			ret[i]=new MmdPmdMaterial();
		}
		return ret;
	}	
};

class DataComparator implements java.util.Comparator<PmdIK>
{
	public int compare(PmdIK o1, PmdIK o2)
	{
		return (int)(o1.getSortVal() - o2.getSortVal());		  
	}
}

public class MmdPmdModel
{
	private String _name;	// モデル名
	private int _number_of_vertex;	// 頂点数
	
	private PmdFace[] m_pFaceArray; // 表情配列
	private PmdBone[] m_pBoneArray; // ボーン配列
	private PmdIK[] m_pIKArray;    // IK配列
	
	private short[] _indices;
	private Vector3[] _position_array;	// 座標配列	
	private Vector3[] _normal_array;		// 法線配列
	private TexUV[] _texture_uv;		// テクスチャ座標配列
	private PmdSkinInfo[] _skin_info_array;
	private MmdPmdMaterial[] _materials;		// マテリアル配列

	public MmdPmdModel(InputStream i_stream) throws MmdException
	{
		try{
			if(!initialize(i_stream)){
				throw new MmdException();
			}
		}catch(Exception e){
			throw new MmdException(e);
		}
		return;
	}
	
	public int getNumberOfVertex()
	{
		return this._number_of_vertex;
	}
	public MmdPmdMaterial[] getMaterials()
	{
		return this._materials;
	}
	public short[] getIndices()
	{
		return this._indices;
	}
	public TexUV[] getUvArray()
	{
		return this._texture_uv;
	}
	public Vector3[] getPositionArray()
	{
		return this._position_array;
	}
	public Vector3[] getNormatArray()
	{
		return this._normal_array;
	}
	public PmdSkinInfo[] getSkinInfoArray()
	{
		return this._skin_info_array;
	}
	public PmdFace[] getFaceArray()
	{
		return this.m_pFaceArray;
	}	
	public PmdBone[] getBoneArray()
	{
		return this.m_pBoneArray;
	}	
	public PmdIK[] getIKArray()
	{
		return this.m_pIKArray;
	}	
	
	
	public PmdBone getBoneByName(String i_name)
	{
		final PmdBone[] bone_array=this.m_pBoneArray;
		for(int i = 0 ; i < bone_array.length ; i++)
		{
			final PmdBone bone=bone_array[i];
			if(bone.getName().equals(i_name))
				return bone;
		}
		return null;
	}
	public PmdFace getFaceByName(String i_name)
	{
		final PmdFace[] face_array=this.m_pFaceArray;
		for(int i = 0 ; i < face_array.length ; i++)
		{
			final PmdFace face=face_array[i];
			if(face.getName().equals(i_name))
				return face;
		}
		return null;		
	}	



	private boolean initialize(InputStream i_stream) throws IOException,MmdException
	{
		DataReader reader=new DataReader(i_stream);
		

		
		PMD_Header pPMDHeader = new PMD_Header();
		pPMDHeader.read(reader);
		if(!pPMDHeader.szMagic.equalsIgnoreCase("PMD")){
			return false;
		}		

		this._name=pPMDHeader.szName;
		
		// -----------------------------------------------------
		// 頂点数取得
		this._number_of_vertex=reader.readInt();//
		if(this._number_of_vertex<0){
			throw new MmdException();
		}
		
		// 頂点配列をコピー
		this._position_array=Vector3.createArray(this._number_of_vertex); 
		this._normal_array=Vector3.createArray(this._number_of_vertex);
		this._texture_uv=TexUV.createArray(this._number_of_vertex);
		this._skin_info_array=PmdSkinInfo.createArray(this._number_of_vertex);

		PMD_Vertex tmp_pmd_vertex=new PMD_Vertex();
		for(int i = 0 ; i < _number_of_vertex ; i++)
		{
			tmp_pmd_vertex.read(reader);
			_position_array[i].setValue(tmp_pmd_vertex.vec3Pos);
			_normal_array[i].setValue(tmp_pmd_vertex.vec3Normal);
			_texture_uv[i].setValue(tmp_pmd_vertex.uvTex);

			_skin_info_array[i].fWeight     = tmp_pmd_vertex.cbWeight / 100.0f; 
			_skin_info_array[i].unBoneNo[0] = tmp_pmd_vertex.unBoneNo[0]; 
			_skin_info_array[i].unBoneNo[1] = tmp_pmd_vertex.unBoneNo[1]; 
		}
		// -----------------------------------------------------
		// 頂点インデックス数取得
		this._indices=createIndicesArray(reader);

		
		// -----------------------------------------------------
		// マテリアル数取得
		int number_of_materials=reader.readInt();

		// マテリアル配列をコピー
		this._materials = MmdPmdMaterial.createArray(number_of_materials);

		PMD_Material tmp_pmd_material=new PMD_Material();
		
		for(int i = 0 ; i < number_of_materials; i++ )
		{
			tmp_pmd_material.read(reader);
			_materials[i].col4Diffuse.setValue(tmp_pmd_material.col4Diffuse);

			this._materials[i].col4Specular.r = tmp_pmd_material.col3Specular.r;
			this._materials[i].col4Specular.g = tmp_pmd_material.col3Specular.g;
			this._materials[i].col4Specular.b = tmp_pmd_material.col3Specular.b;
			this._materials[i].col4Specular.a = 1.0f;

			this._materials[i].col4Ambient.r = tmp_pmd_material.col3Ambient.r;
			this._materials[i].col4Ambient.g = tmp_pmd_material.col3Ambient.g;
			this._materials[i].col4Ambient.b = tmp_pmd_material.col3Ambient.b;
			this._materials[i].col4Ambient.a = 1.0f;

			this._materials[i].fShininess = tmp_pmd_material.fShininess;
			this._materials[i].ulNumIndices = tmp_pmd_material.ulNumIndices;

			this._materials[i].texture_name = tmp_pmd_material.szTextureFileName;
			if(this._materials[i].texture_name.length()<1){
				this._materials[i].texture_name=null;
			}
		}

		//Boneの読み出し
		this.m_pBoneArray=createBoneArray(reader);		
		//IK配列の読み出し
		this.m_pIKArray=createIKArray(reader,this.m_pBoneArray);
		//Face配列の読み出し
		this.m_pFaceArray=createFaceArray(reader);
		return true;		
	}
	private static short[] createIndicesArray(DataReader i_reader) throws MmdException
	{
		int num_of_indeces=i_reader.readInt();
		short[] result=new short[num_of_indeces];
		result=new short[num_of_indeces];

		// 頂点インデックス配列をコピー
		for(int i=0;i<num_of_indeces;i++){
			result[i]=i_reader.readShort();
		}
		return result;
	}
	private static PmdBone[] createBoneArray(DataReader i_reader) throws MmdException
	{
		final int number_of_bone = i_reader.readShort();
		PMD_Bone tmp_pmd_bone=new PMD_Bone();
		
		PmdBone[] result=new PmdBone[number_of_bone];
		for(int i = 0 ; i < number_of_bone ; i++ )
		{
			tmp_pmd_bone.read(i_reader);
			//ボーンの親子関係を一緒に読みだすので。
			result[i]=new PmdBone(tmp_pmd_bone,result);
		}	
		for(int i = 0 ; i <number_of_bone ; i++ ){
			result[i].recalcOffset();
		}
		return result;
	}
	
	private static PmdIK[] createIKArray(DataReader i_reader,PmdBone[] i_ref_bone_array) throws MmdException
	{
		final int number_of_ik = i_reader.readShort();
		PMD_IK tmp_pmd_ik=new PMD_IK();
		PmdIK[] result=new PmdIK[number_of_ik];
		// IK配列を作成
		if(number_of_ik>0)
		{

			for(int i = 0 ; i < number_of_ik ; i++ )
			{
				tmp_pmd_ik.read(i_reader);
				result[i]=new PmdIK(tmp_pmd_ik,i_ref_bone_array);
			}
			Arrays.sort(result, new DataComparator());
		}
		return result;
	}
	
	private static PmdFace[] createFaceArray(DataReader i_reader) throws MmdException
	{
		final int number_of_face=i_reader.readShort();
		PMD_FACE tmp_pmd_face=new PMD_FACE();
		PmdFace[] result=new PmdFace[number_of_face];		

		// 表情配列を作成
		if(number_of_face>0)
		{

			for(int i = 0 ; i <number_of_face ; i++ )
			{
				tmp_pmd_face.read(i_reader);
				result[i]=new PmdFace(tmp_pmd_face,result[0]);
			}
		}
		return result;	
	}


	

	public String getModelName()
	{
		return this._name;
	}

}
