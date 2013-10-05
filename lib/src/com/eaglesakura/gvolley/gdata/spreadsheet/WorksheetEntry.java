package com.eaglesakura.gvolley.gdata.spreadsheet;

import java.util.ArrayList;
import java.util.List;

import com.eaglesakura.gvolley.auth.Scopes;
import com.eaglesakura.gvolley.gdata.Author;
import com.eaglesakura.gvolley.gdata.BaseGDataObject;
import com.google.api.client.util.Key;

/**
 * Spreadsheetの1ファイルの概要を扱う
 */
public class WorksheetEntry extends BaseGDataObject {

    @Key("author")
    public List<Author> authors = new ArrayList<Author>();

    /**
     * ワークシート等へアクセスするためのファイルごとの一意のキー
     * @return
     */
    public String getKey() {
        return id.substring(id.lastIndexOf('/') + 1);
    }

    /**
     * ワークシート一覧へアクセスするためのURL
     * @return
     */
    public String getWorksheetsUrl() {
        return Scopes.SPREADSHEET.getEndpoint() + "worksheets/" + getKey() + "/private/full";
    }
}
