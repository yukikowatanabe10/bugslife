# taxAmount 設計

## 概要

税額を登録する

### アクション

- 一覧（検索）
- 参照
- 新規作成
- 削除

### 要件

- taxamount は name、ID、rate、ceil、floor、round、taxincome、を持つ
- name は税名
- rate は税額（％）
- ceil は切り上げ
- Round は四捨五入
- floor は切り捨て
- taxincome は税込み
- ショップ照会画面に紐づく商品一覧画面へ遷移する動線を設ける
- 一覧には名前と税額のみを表示
- 六パターンのデータを一度に登録

## モデル

- TaxAmount

| `taxamount` | Type    | Required | memo                     |
| ----------- | ------- | :------: | ------------------------ |
| name        | String  |    ○     | 税名前 　                |
| rate        | double  |    ○     | 税額                     |
| ceil        | boolean |    ○     | 切り上げ　　　　　　　   |
| floor       | boolean |  ❍ 　　  | 切り捨て　　　　　　　   |
| taxincome   | boolean |    ❍     | 税込み                   |
| round       | boolean |  ❍ 　　  | 四捨五入　　　　　　　　 |
