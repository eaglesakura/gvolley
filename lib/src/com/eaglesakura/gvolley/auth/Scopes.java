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
    },

    Drive {
        @Override
        public String getURL() {
            return "https://www.googleapis.com/auth/drive";
        }

        @Override
        public int getVersion() {
            return 2;
        }
    },

    UserInfoEmail {
        @Override
        public String getURL() {
            return "https://www.googleapis.com/auth/userinfo.email";
        }

        @Override
        public int getVersion() {
            return 1;
        }
    };

    public abstract String getURL();

    public abstract int getVersion();
}
