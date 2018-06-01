# bocco4j

## 更新履歴

- 2018年5月30日(水) initial commit
- 2018年6月1日(金) JSONObjectライブラリを利用し、jsonパース。postメソッド・createSessionsメソッドの修正
----

## 当ライブラリの使い方
Boccoとやり取りするために **APIKey** を申請しておきます。<br>
また、スマートフォンアプリから初期設定を済ませておいてください。

### BoccoAPI(APIKey,Email,Password)
BoccoAPIクラスのコンストラクタには受け取ったAPIKey、アプリで設定したメールアドレスとパスワードを入力

### public boolean createSessions()
コンストラクタで設定したパラメータを使い、セッションを作成しアクセストークンを取得します。<br>
問題なくトークンを取得できると **true** を返し、パラメータのミス等があった場合 **false** を返します。
