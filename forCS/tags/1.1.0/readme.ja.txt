NyMmd
Copyright (C)2009 nyatla

version 1.1.0 (Java版の1.1.0と同機能)

http://nyatla.jp/
wm(at)nyatla.jp
--------------------------------------------------


・NyMmd for C#
NyMmdは、PYさん作のmmd再生+ARToolKitライブラリARTK_MMDから、
ARToolKit依存のコードを取り除いて、他言語対応に向けてAPIを
整備し直したクラスライブリです。

ARTK_MMDはこちらで公開されています。
http://ppyy.hp.infoseek.co.jp/


NyMmd　for C#は、MikuMikuDanceのVMD/PMDファイルの読み込みと、再生機能を持ちます。

現在対応しているレンダラは、ManagedDirect3Dのみです。







・コンパイルの仕方

実行には、.Net framework 3.5以上が必要です（2.0でも調整すれば動きます。）

1.エントリポイントはNyMmdtestプロジェクトにあります。

2.後はサンプル読んで頑張ってください（無責任）



・使い方

1.MmdTestを起動してください。

2.ファイルオープンダイアログが２回表示されます。PMD,VMDの順に指定してください。

3.モデルが読み込まれて再生されます。


・ライセンス
NyMmdは、ARTK_MMDのGPLv2ライセンスを継承しています。

GPLについては、LICENCE.txtをお読みください。

ソースコード毎のライセンスについては、ソースコード先頭の署名をご確認ください。




・謝辞

PY さん
 ARTK_MMDを開発し、ソースコードを公開してくださいました。
 有難うございます。
 http://ppyy.hp.infoseek.co.jp/

樋口M さん
 MikuMikuDanceを開発してくださいました。
 有難うございます。
 http://www.geocities.jp/higuchuu4/index.htm
