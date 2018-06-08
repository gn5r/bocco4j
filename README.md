# bocco4j

## 更新履歴

- 2018年5月30日(水) initial commit
- 2018年6月1日(金) JSONObjectライブラリを利用し、jsonパース。postメソッド・createSessionsメソッドの修正
- 2018年6月2日(土) コード見直し、コメントを付与。accesstoken取得可能に
- 2018年6月4日(月) 必要機能実装完了。Boccoに喋らせることを確認
----

## 当ライブラリの使い方
Boccoとやり取りするために **APIKey** を申請しておきます。<br>
また、スマートフォンアプリから初期設定を済ませておいてください。

### BoccoAPI(APIKey,Email,Password)
BoccoAPIクラスのコンストラクタには受け取ったAPIKey、アプリで設定したメールアドレスとパスワードを入力

### public boolean createSessions()
コンストラクタで設定したパラメータを使い、セッションを作成しアクセストークンを取得。<br>
問題なくトークンを取得できると **true** を、パラメータのミス等があった場合 **false** を返す。

### public boolen getFirstRoomID()
１件目のroom情報を取得し、roomIDを取り出す。

### public boolean postMessage(String message)
accesstokenとroomIDを使い、テキストメッセージを送信<br>
日本語(漢字・ひらがな・カタカナ)入力でBoccoが日本語を喋ってくれる
