package com.eaglesakura.gvolley.auth;

public enum Scopes {

    /**
     * Spreadsheetアクセス
     */
    SPREADSHEET {
        @Override
        public String getURL() {
            return "https://spreadsheets.google.com/feeds/";
        }

        @Override
        public int getVersion() {
            return 3;
        }
    };

    public abstract String getURL();

    public abstract int getVersion();
}
