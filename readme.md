# Readme

## GVolleyについて

GVolleyはGoogleが公開している各種APIをVolley経由で利用しやすくするためのラッパーライブラリです。

個人的な用途で作成しているため、ドキュメント等は整備されていません。

機能は必要最低限のみを整備しています。

## GVolleyの機能

1. OAuth2認証用Activity
1. OAuth2トークン保持
	* トークンの暗号化は行いません
1. OAuth2トークンリフレッシュ
	* 汎用Requestを利用した場合、自動的にトークンリフレッシュを行います
1. APIを呼び出すための汎用的な各種Volleyリクエスト
	* XMLリクエスト
	* JSONリクエスト

## 依存関係

* GVolleyは下記のjarライブラリを利用しています
	1. android-query-full.0.25.10.jar
	1. androidannotations-2.7.1.jar
	1. androidannotations-api-2.7.1.jar
	1. google-http-client-1.17.0-rc.jar
	1. google-http-client-xml-1.17.0-rc.jar
	1. eglibrary-android-api8.jar
	1. jackson-core-2.2.3.jar
	1. jackson-databind-2.2.3.jar
* GVolleyは下記のAndroidライブラリプロジェクトをリンクしています
	1. volley
	1. google-play-services_lib


=============


## ソースコードライセンス

* ソースコードは自由に利用してもらって構いません。
* NYSLに従います。

<pre>
A. 本ソフトウェアは Everyone'sWare です。このソフトを手にした一人一人が、
   ご自分の作ったものを扱うのと同じように、自由に利用することが出来ます。

  A-1. フリーウェアです。作者からは使用料等を要求しません。
  A-2. 有料無料や媒体の如何を問わず、自由に転載・再配布できます。
  A-3. いかなる種類の 改変・他プログラムでの利用 を行っても構いません。
  A-4. 変更したものや部分的に使用したものは、あなたのものになります。
       公開する場合は、あなたの名前の下で行って下さい。

B. このソフトを利用することによって生じた損害等について、作者は
   責任を負わないものとします。各自の責任においてご利用下さい。

C. 著作者人格権は @eaglesakura に帰属します。著作権は放棄します。

D. 以上の３項は、ソース・実行バイナリの双方に適用されます。
</pre>

### LICENSE(en)

<pre>

A. This software is "Everyone'sWare". It means:
  Anybody who has this software can use it as if he/she is
  the author.

  A-1. Freeware. No fee is required.
  A-2. You can freely redistribute this software.
  A-3. You can freely modify this software. And the source
      may be used in any software with no limitation.
  A-4. When you release a modified version to public, you
      must publish it with your name.

B. The author is not responsible for any kind of damages or loss
  while using or misusing this software, which is distributed
  "AS IS". No warranty of any kind is expressed or implied.
  You use AT YOUR OWN RISK.

C. Copyrighted to @eaglesakura

D. Above three clauses are applied both to source and binary
  form of this software.

</pre>