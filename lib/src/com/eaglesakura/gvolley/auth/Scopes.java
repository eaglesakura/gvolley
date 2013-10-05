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
        public String getVersion() {
            return "3.0";
        }

        @Override
        public String getEndpoint() {
            return "https://spreadsheets.google.com/feeds/";
        }
    },

    Drive {
        @Override
        public String getURL() {
            return "https://www.googleapis.com/auth/drive";
        }

        @Override
        public String getVersion() {
            return "2.0";
        }

        @Override
        public String getEndpoint() {
            return null;
        }
    },

    UserInfoEmail {
        @Override
        public String getURL() {
            return "https://www.googleapis.com/auth/userinfo.email";
        }

        @Override
        public String getVersion() {
            return "1.0";
        }

        @Override
        public String getEndpoint() {
            return null;
        }
    };

    public abstract String getURL();

    public abstract String getVersion();

    public abstract String getEndpoint();
}
