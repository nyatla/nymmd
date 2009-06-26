package jp.nyatla.nymmd;


import jp.nyatla.nymmd.types.Matrix;

public interface IMmdPmdRender
{
	public void dispose();
	public void setPmd(MmdPmdModel i_pmd, MmdDataIo i_io) throws MmdException;
	public void updateSkinning(Matrix[] i_skinning_mat);
	public void render();
}
