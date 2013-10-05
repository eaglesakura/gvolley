package com.eaglesakura.gvolley.gdata.spreadsheet;

import com.android.volley.Request;
import com.eaglesakura.gvolley.auth.OAuthProvider;
import com.eaglesakura.gvolley.auth.Scopes;
import com.eaglesakura.gvolley.request.BaseRequest;
import com.eaglesakura.gvolley.request.SimpleXmlRequest;
import com.eaglesakura.gvolley.request.listener.RequestListener;

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
    public BaseRequest<SpreadsheetDocumentList> listDocuments(RequestListener<SpreadsheetDocumentList> listener) {
        BaseRequest<SpreadsheetDocumentList> req = new SimpleXmlRequest<SpreadsheetDocumentList>(Request.Method.GET,
                ENDPOINT + "spreadsheets/private/full", SpreadsheetDocumentList.class, listener) {
        };

        provider.authorize(req, Scopes.SPREADSHEET); // 認証を行う
        return req;
    }

    /**
     * ワークシート詳細を読み込む
     * @param listener
     * @param entry
     * @return
     */
    public BaseRequest<Worksheet> getWorksheet(RequestListener<Worksheet> listener, final WorksheetEntry entry) {
        BaseRequest<Worksheet> req = new SimpleXmlRequest<Worksheet>(Request.Method.GET, ENDPOINT + "worksheets/"
                + entry.getKey() + "/private/basic", Worksheet.class, listener) {
        };

        provider.authorize(req, Scopes.SPREADSHEET); // 認証を行う
        return req;
    }
}
