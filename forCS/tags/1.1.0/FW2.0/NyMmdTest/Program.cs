using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using System.Threading;

namespace NyMmdTest
{
    static class Program
    {
        /// <summary>
        /// アプリケーションのメイン エントリ ポイントです。
        /// </summary>
        [STAThread]
        static void Main()
        {
            // フォームとメインサンプルクラスを作成
            using (Form1 frm = new Form1())
            {
                using (NyMmdTest sample = new NyMmdTest(frm))
                {
                    // アプリケーションの初期化
                    // メインフォームを表示
                    frm.Show();
                    // フォームが作成されている間はループし続ける
                    while (frm.Created)
                    {
                        // メインループ処理を行う
                        sample.MainLoop();

                        //スレッドスイッチ
                        Thread.Sleep(1);

                        // イベントがある場合はその処理する
                        Application.DoEvents();
                    }
                }
            }
        }
    }
}

