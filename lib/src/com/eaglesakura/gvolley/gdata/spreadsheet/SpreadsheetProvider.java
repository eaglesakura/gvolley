package com.eaglesakura.gvolley.gdata.spreadsheet;

import com.android.volley.Request;
import com.eaglesakura.gvolley.auth.OAuthProvider;
import com.eaglesakura.gvolley.auth.Scopes;
import com.eaglesakura.gvolley.request.XmlRequest;
import com.eaglesakura.gvolley.request.listener.RequestListener;
import com.eaglesakura.lib.io.XmlElement;

/**
 * Spreadsheetアクセス用リクエストを生成する
 */
public class SpreadsheetProvider {

    final OAuthProvider provider;

    static final String ENDPOINT = Scopes.SPREADSHEET.getEndpoint();

    public SpreadsheetProvider(OAuthProvider provider) {
        this.provider = provider;
    }

    /**
     * ドキュメント一覧を読み込む
     * @param listener
     * @return
     */
    public XmlRequest<SpreadsheetDocumentList> listDocuments(RequestListener<SpreadsheetDocumentList> listener) {
        XmlRequest<SpreadsheetDocumentList> req = new XmlRequest<SpreadsheetDocumentList>(Request.Method.GET, ENDPOINT
                + "spreadsheets/private/full", listener) {
            @Override
            protected SpreadsheetDocumentList convert(XmlElement response) {
                return new SpreadsheetDocumentList(response);
            }
        };

        // 認証を行う
        provider.authorize(req, Scopes.SPREADSHEET);
        return req;
    }
}
