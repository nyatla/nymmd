NyMmd for Java
Copyright (C)2009-2012 nyatla

version 2.0.1

http://nyatla.jp/
wm(at)nyatla.jp
--------------------------------------------------


・NyMmd
NyMmdは、PYさん作のmmd再生+ARToolKitライブラリARTK_MMDから、
ARToolKit依存のコードを取り除いて、他言語対応に向けてAPIを
整備し直したクラスライブリです。

ARTK_MMDはこちらで公開されています。
http://ppyy.if.land.to/artk_mmd.html


NyMmdは、MikuMikuDanceのVMD/PMDファイルの読み込みと、再生機能を持ちます。

現在対応しているレンダラは、OpenGL/AndroidのOpenGLESです。







・コンパイルの仕方

1.Java版の動作には、別途JOGLが必要になります。
  下記URLから適切なものをダウンロードして、インストールします。

2.eclipseにNyMmdのトップレベルディレクトリをインポートします。

3.NyMmdとNyMmdTestのプロジェクトがインポートできたら、文字コード
  をUTF8にして、JVMのバージョンを1.5以上に設定します。

4.エントリポイントはNyMmdTestプロジェクトのjp.nyatla.mmd.testにある、
  MmdTest.javaです。実行してください。

5.後はサンプル読んで頑張ってください（無責任）



・使い方

1.MmdTestを起動してください。

2.ファイルオープンダイアログが２回表示されます。PMD,VMDの順に指定してください。

3.モデルが読み込まれて再生されます。


・ライセンス
NyMmd for Javaは、2.0.1よりMITライセンスで配布しています。
ソースコード毎のライセンスについては、ソースコード先頭の署名をご確認ください。




・謝辞

PY さん
 ARTK_MMDを開発し、ソースコードを公開してくださいました。
 また、MMDプレイヤー部分のソースコードについて、MITライセンスでの配布を許可
 していただきました。有難うございます。
 http://ppyy.if.land.to/artk_mmd.html

樋口M さん
 MikuMikuDanceを開発してくださいました。
 有難うございます。
 http://www.geocities.jp/higuchuu4/index.htm
