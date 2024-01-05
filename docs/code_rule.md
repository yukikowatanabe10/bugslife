# Code rule

## コントローラー(Controller)

コントローラーの役割は主に入力バリデーション、サービスの呼び出し、そして view への値の受け渡しです。  
ここでは具体的なビジネスロジックを持たず、その代わりにサービスクラスのメソッドを呼び出します。  
また、入力パラメータのバリデーション（たとえばパラメータが null または空でないかどうか）も行います。  
そして、サービスから受け取った結果を view（または次のレイヤー）に渡します。

## サービス(Service)

サービス層は再利用可能なビジネスロジックを持つクラスを含みます。  
これはコントローラーから呼び出されることを想定しています。  
コントローラーからの要求を受けてリポジトリのメソッドを呼び出し、その結果をコントローラーに返します。  
また、リポジトリ間で連携が必要なロジックもここに入れます。

## リポジトリ(Repository)

リポジトリはデータベースへのアクセスを担当します。  
この層では具体的なデータ操作（挿入、削除、更新、取得など）を行い、その結果をサービス層に返します。  
ここで使用されるメソッドは主にデータベースの操作に関連しています。

## モデル(Model)

モデルはアプリケーションのデータ構造を定義します。  
これにはデータベースのテーブル構造と一致するフィールド（変数）、それぞれのフィールドに対するゲッターとセッター（アクセッサ）メソッド、そしてその他の構造に関するロジック（例えばデータ整合性チェックなど）が含まれます。

## その他

- form や varidate などの特定の関心ごとについては、それぞれのディレクトリにまとめ、自身の関心のスコープをはみ出さない様にしてください。
- コントローラーが肥大化しない様にして下さい。  
  そのためには、コントローラーの中でサービスを呼び出すだけでなく、サービスの中でさらにサービスを呼び出すことで、コントローラーの肥大化を防ぐことができます。  
  コントローラーのメソッドは、再利用性がありません。サービスにビジネスロジックを集中することで、メソッドの再利用性が高まります。