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
using System.Windows.Forms;
using Microsoft.DirectX;
using Microsoft.DirectX.Direct3D;
using System.Diagnostics;
using System.IO;
using System.Drawing;
using NyMmdUtils;

using jp.nyatla.nymmd.cs;

namespace NyMmdTest
{
    class DataIo : IMmdDataIo
    {
        private String _root_dir;
        public DataIo(String i_dir)
        {
            this._root_dir = i_dir;
        }
        public Stream request(String i_name)
        {
            return new StreamReader(_root_dir + i_name).BaseStream;
        }

    }


    class NyMmdTest : IDisposable
    {
        //NyAR
        /// Direct3D デバイス
        private Device _device = null;



        /* Direct3Dデバイスを準備する関数
         */
        private static Device CreateD3dDevice(Control i_window)
        {
            PresentParameters pp = new PresentParameters();
            pp.Windowed = true;
            pp.SwapEffect = SwapEffect.Flip;
            pp.BackBufferFormat = Format.X8R8G8B8;
            pp.BackBufferCount = 1;
            pp.EnableAutoDepthStencil = true;
            pp.AutoDepthStencilFormat = DepthFormat.D16;
            CreateFlags fl_base = CreateFlags.FpuPreserve;


            try
            {
                return new Device(0, DeviceType.Hardware, i_window.Handle, fl_base | CreateFlags.HardwareVertexProcessing, pp);
            }
            catch (Exception ex1)
            {
                Debug.WriteLine(ex1.ToString());
                try
                {
                    return new Device(0, DeviceType.Hardware, i_window.Handle, fl_base | CreateFlags.SoftwareVertexProcessing, pp);
                }
                catch (Exception ex2)
                {
                    // 作成に失敗
                    Debug.WriteLine(ex2.ToString());
                    try
                    {
                        return new Device(0, DeviceType.Reference, i_window.Handle, fl_base | CreateFlags.SoftwareVertexProcessing, pp);
                    }
                    catch (Exception ex3)
                    {
                        throw ex3;
                    }
                }
            }
        }



        public NyMmdTest(Form1 topLevelForm)
        {
            topLevelForm.ClientSize = new Size(640, 480);

            //[d3d]3dデバイスを準備する
            this._device = CreateD3dDevice(topLevelForm);
            this._device.Transform.Projection = Matrix.PerspectiveFovLH((float)Math.PI / 4, 640f / 480f, 1.0f, 100.0f);

            this._device.RenderState.ZBufferEnable = true;
            this._device.RenderState.Lighting = true;

            //[d3d]ビュー変換の設定(左手座標系ビュー行列で設定する)
            //0,0,0から、Z+方向を向いて、上方向がY軸
            this._device.Transform.View = Microsoft.DirectX.Matrix.LookAtLH(
                new Microsoft.DirectX.Vector3(0.0f, 0.0f, 0.0f), new Microsoft.DirectX.Vector3(0.0f, 0.0f, 1.0f), new Microsoft.DirectX.Vector3(0.0f, 1.0f, 0.0f));
            Viewport vp = new Viewport();
            vp.X = 0;
            vp.Y = 0;
            vp.Height = 480;
            vp.Width = 640;
            vp.MaxZ = 1.0f;
            vp.MinZ = 0.0f;
            //[d3d]ビューポート設定
            this._device.Viewport = vp;
            //ライトの設定
            this._device.Lights[0].Direction = Vector3.Normalize(new Vector3(0.45f,0.55f,1.0f));
            this._device.Lights[0].Type = LightType.Directional;

            this._device.Lights[0].Diffuse = Color.FromArgb(255,255,255);
            this._device.Lights[0].Ambient = Color.FromArgb(255,255,255);

            this._device.Lights[0].Enabled = true;
            this._device.Lights[0].Update();

            //StreamReader pmds = new StreamReader("D:\\application.files\\MikuMikuDance_v405\\UserFile\\Model\\初音ミクVer2.pmd");
            //StreamReader vmds = new StreamReader("D:\\application.files\\MikuMikuDance_v405\\UserFile\\Motion\\kisimen.vmd");
            //DataIo pmd_io = new DataIo("D:\\application.files\\MikuMikuDance_v405\\UserFile\\Model\\");
            
            //PMDとVMDを開く
            using (OpenFileDialog ofd = new OpenFileDialog())
            {
                ofd.Title = "Select PMD";
                if (ofd.ShowDialog() != DialogResult.OK)
                {
                    return;
                }
                DataIo pmd_io = new DataIo(System.IO.Path.GetDirectoryName(ofd.FileName) + "\\");
                StreamReader pmds = new StreamReader(ofd.FileName);
            }
            using (OpenFileDialog ofd = new OpenFileDialog())
            {
                ofd.Title = "Select VMD";
                if (ofd.ShowDialog() != DialogResult.OK)
                {
                    return;
                }
                StreamReader vmds = new StreamReader(ofd.FileName);
            }
            //Mmdプレイヤーのオブジェクトを作る
            this._pmd = new MmdPmdModel(pmds);
            this._vmd = new MmdVmdMotion(vmds);
            this._player = new MmdMotionPlayer(this._pmd, this._vmd);
            //player
            this._render = new MmdPmdRenderD3d(this._device);
            this._render.setPmd(this._pmd, pmd_io);
            //
            this.animation_start_time = System.Environment.TickCount;
            this._player.setLoop(true);
            return;
        }
        private int animation_start_time;
        private int prev_time=0;
        private MmdMotionPlayer _player;
        private MmdPmdModel _pmd;
        private MmdVmdMotion _vmd;
        private IMmdPmdRender _render;
        //メインループ処理
        public void MainLoop()
        {
            int iTime = System.Environment.TickCount - this.animation_start_time;
            float fDiffTime = (float)(iTime - prev_time) * (1.0f / 30.0f);
            prev_time = iTime;

            lock (this)
            {
                // 3Dオブジェクトの描画はここから
                this._device.BeginScene();
                this._device.Clear(ClearFlags.ZBuffer | ClearFlags.Target, Color.Blue, 1.0f, 0);

                this._render.updateSkinning(this._player.refSkinningMatrix());
                //変換行列を掛ける
                Matrix tr = Matrix.Translation(0, -10, 30);
                this._device.SetTransform(TransformType.World, tr);
                this._player.updateMotion(fDiffTime);
                this._render.updateSkinning(this._player.refSkinningMatrix());
                this._render.render();

                // 描画はここまで
                this._device.EndScene();

                // 実際のディスプレイに描画
                this._device.Present();
            }
            return;
        }

        // リソースの破棄をするために呼ばれる
        public void Dispose()
        {
            lock (this)
            {
                if (this._render != null)
                {
                    this._render.Dispose();
                }
                // Direct3D デバイスのリソース解放
                if (this._device != null)
                {
                    this._device.Dispose();
                }
            }
        }
    }
}
