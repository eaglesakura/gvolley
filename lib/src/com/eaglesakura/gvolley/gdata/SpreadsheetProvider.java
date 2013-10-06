package com.eaglesakura.gvolley.gdata;

import com.android.volley.Request;
import com.eaglesakura.gvolley.auth.OAuthProvider;
import com.eaglesakura.gvolley.auth.Scopes;
import com.eaglesakura.gvolley.gdata.spreadsheet.CellEntry;
import com.eaglesakura.gvolley.gdata.spreadsheet.SheetCells;
import com.eaglesakura.gvolley.gdata.spreadsheet.SheetEntry;
import com.eaglesakura.gvolley.gdata.spreadsheet.SheetList;
import com.eaglesakura.gvolley.gdata.spreadsheet.SpreadsheetDocumentList;
import com.eaglesakura.gvolley.gdata.spreadsheet.Worksheet;
import com.eaglesakura.gvolley.gdata.spreadsheet.WorksheetEntry;
import com.eaglesakura.gvolley.request.BaseRequest;
import com.eaglesakura.gvolley.request.SimpleXmlRequest;
import com.eaglesakura.gvolley.request.listener.RequestListener;

/**
 * Spreadsheetアクセス用リクエストを生成する
 */
public class SpreadsheetProvider {

    final OAuthProvider provider;

    static final String ENDPOINT = Scopes.Spreadsheet.getEndpoint();

    public SpreadsheetProvider(OAuthProvider provider) {
        this.provider = provider;
    }

    /**
     * トークンをDBからロードし直す
     */
    public void loadToken() {
        provider.load();
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

        provider.authorize(req, Scopes.Spreadsheet); // 認証を行う
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

        provider.authorize(req, Scopes.Spreadsheet); // 認証を行う
        return req;
    }

    /**
     * シートを読み込む
     * @param listener
     * @param entry
     * @return
     */
    public BaseRequest<SheetList> getSheetList(RequestListener<SheetList> listener, SheetEntry entry) {
        BaseRequest<SheetList> req = new SimpleXmlRequest<SheetList>(Request.Method.GET, entry.getListUrl(),
                SheetList.class, listener) {
        };

        provider.authorize(req, Scopes.Spreadsheet); // 認証を行う
        return req;
    }

    /**
     * シートを読み込む
     * @param listener
     * @param entry
     * @return
     */
    public BaseRequest<SheetCells> getSheetCells(RequestListener<SheetCells> listener, SheetEntry entry) {
        BaseRequest<SheetCells> req = new SimpleXmlRequest<SheetCells>(Request.Method.GET, entry.getCellsUrl(),
                SheetCells.class, listener) {
        };

        provider.authorize(req, Scopes.Spreadsheet); // 認証を行う
        return req;
    }

    /**
     * セル内容を最新に交換する
     */
    public BaseRequest<CellEntry> getCell(RequestListener<CellEntry> listener, CellEntry entry) {
        BaseRequest<CellEntry> req = new SimpleXmlRequest<CellEntry>(Request.Method.GET, entry.getSelfLink(),
                CellEntry.class, listener) {
        };

        provider.authorize(req, Scopes.Spreadsheet); // 認証を行う
        return req;
    }
}
