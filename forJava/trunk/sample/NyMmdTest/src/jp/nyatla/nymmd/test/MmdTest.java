package jp.nyatla.nymmd.test;

import java.awt.event.*;
import java.awt.*;
import java.io.*;

import javax.media.opengl.*;

import jp.nyatla.nymmd.*;

import com.sun.opengl.util.*;

class FileIO implements IMmdDataIo
{
	private String _dir;
	public FileIO(String i_dir)
	{
		this._dir=i_dir;
	}
	public InputStream request(String i_name)
	{
		FileInputStream fs2;
		try {
			fs2 = new FileInputStream(this._dir +"\\"+ i_name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return fs2;
	}
}

public class MmdTest implements GLEventListener
{
	private final static int SCREEN_X = 640;

	private final static int SCREEN_Y = 480;

	private Animator _animator;

	private GL _gl;

	private long animation_start_time;

	private MmdPmdModel _pmd;

	private MmdVmdMotion _vmd;

	private MmdMotionPlayer _player;

	private IMmdPmdRender _render;
	
	private IMmdDataIo _data_io;

	public MmdTest() throws MmdException, IOException
	{
		Frame frame = new Frame("MMD for Java");
		frame.setVisible(true);
		Insets ins = frame.getInsets();
		frame.setSize(SCREEN_X + ins.left + ins.right, SCREEN_Y + ins.top + ins.bottom);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				_render.dispose();//忘れないように。
				System.exit(0);
			}
		});	
		
		//PMDとVMDを取得
/*		FileDialog fd;

		fd=new FileDialog(frame, "Select PMD file" , FileDialog.LOAD);
		fd.setVisible(true);
		String pmd_file=fd.getDirectory()+fd.getFile();

		if(fd.getFile()==null){
			System.out.println("failed:please select pmd file.");
			frame.dispose();
			return;
		}
		
		fd=new FileDialog(frame, "Select VMD file" , FileDialog.LOAD);
		fd.setVisible(true);
		String vmd_file=fd.getDirectory()+fd.getFile();
		
		if(fd.getFile()==null){
			System.out.println("failed:please select vmd file.");
			frame.dispose();
			return;
		}
*/		
		String vmd_file="D:\\application.files\\MikuMikuDance_v524\\UserFile\\Motion\\hannyasinkyo.vmd";
		String pmd_file="D:\\application.files\\MikuMikuDance_v524\\UserFile\\model\\初音ミクVer2.pmd";
		//PMD
		FileInputStream fs = new FileInputStream(pmd_file);
		this._pmd = new MmdPmdModel(fs);
		//VMD
		FileInputStream fs2 = new FileInputStream(vmd_file);
		this._vmd = new MmdVmdMotion(fs2);
		// Player
		this._player = new MmdMotionPlayer(this._pmd, this._vmd);
		this._player.setLoop(true);//ループするよ。
		//テクスチャ用のIO
		File f=new File(pmd_file);//pmdのパス
		this._data_io=new FileIO(f.getParentFile().getPath());
		
		//OpenGL
		GLCanvas canvas = new GLCanvas();
		frame.add(canvas);
		canvas.addGLEventListener(this);
	
		canvas.setBounds(ins.left, ins.top, SCREEN_X, SCREEN_Y);		
	}

	public void init(GLAutoDrawable drawable)
	{
		this._gl = drawable.getGL();
		try {

			this._render = new MmdPmdRenderGL(this._gl);
			this._render.setPmd(this._pmd,this._data_io);

		} catch (Exception e) {
			e.printStackTrace();
		}

		this.animation_start_time = System.currentTimeMillis();
		GL gl = this._gl;

		gl.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);

		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);

		gl.glEnable(GL.GL_CULL_FACE);
		gl.glCullFace(GL.GL_FRONT);

		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		gl.glEnable(GL.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL.GL_GEQUAL, 0.05f);

		float[] fLightPos = { 0.45f, 0.55f, 1.0f, 0.0f };
		float[] fLightDif = { 0.9f, 0.9f, 0.9f, 0.0f };
		float[] fLightAmb = { 1.0f, 1.0f, 1.0f, 0.0f };
		float[] fLightSpq = { 0.9f, 0.9f, 0.9f, 0.0f };

		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, fLightPos, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, fLightDif, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, fLightAmb, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, fLightSpq, 0);
		gl.glEnable(GL.GL_LIGHT0);

		gl.glEnable(GL.GL_LIGHTING);
		this._animator = new FPSAnimator(drawable,60);
		this._animator.start();
		return;
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		GL gl = this._gl;
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustum(-1.0f, 1.0f, -(float) height / (float) width, (float) height / (float) width, 1.0f, 100.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

	private long prev_time = 0;

	public void display(GLAutoDrawable drawable)
	{
		long iTime = System.currentTimeMillis() - this.animation_start_time;
		float fDiffTime = (float) (iTime - prev_time) * 30 / 1000.0f;
		prev_time = iTime;
		try {
			this._player.updateMotion(fDiffTime);
			//this._player.updateNeckBone(100.0f,10f,10f);
			this._render.updateSkinning(this._player.refSkinningMatrix());

			this._gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // Clear the buffers for new frame.
			_gl.glEnable(GL.GL_CULL_FACE);
			_gl.glEnable(GL.GL_ALPHA_TEST);
			_gl.glEnable(GL.GL_BLEND);
			_gl.glMatrixMode(GL.GL_MODELVIEW);
			_gl.glPushMatrix();
			_gl.glLoadIdentity();
			_gl.glTranslatef(0, -10, -20);
			_gl.glScalef(1.0f, 1.0f, 1.0f);

			_gl.glScalef(1.0f, 1.0f, -1.0f); // 左手系 → 右手系
			//レンダリング
			this._render.render();
			_gl.glPopMatrix();

			Thread.sleep(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
	{
	}

	public static void main(String[] args)
	{
		try {
			new MmdTest();
		} catch (Exception e){
			e.printStackTrace();
		}
		return;
	}
}
