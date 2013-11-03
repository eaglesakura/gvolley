package com.eaglesakura.gvolley;

/**
 * GVolleyで送信するコンテンツの種類を設定する
 */
public enum ContentType {
    /**
     * JSON形式にエンコードされたModel
     */
    Json {
        @Override
        public String getContentType() {
            return "application/json";
        }
    },

    /**
     * プレーンテキスト
     */
    Text {
        @Override
        public String getContentType() {
            return "text/plain";
        }
    },

    /**
     * WebForm形式
     */
    WebFormUTF8 {
        @Override
        public String getContentType() {
            return "application/x-www-form-urlencoded; charset=UTF-8";
        }
    };

    public abstract String getContentType();
}
