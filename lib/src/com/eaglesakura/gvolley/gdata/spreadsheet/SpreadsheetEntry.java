package com.eaglesakura.gvolley.gdata.spreadsheet;

import java.util.ArrayList;
import java.util.List;

import com.eaglesakura.gvolley.auth.Scopes;
import com.eaglesakura.gvolley.gdata.Author;
import com.eaglesakura.gvolley.gdata.BaseGDataObject;
import com.eaglesakura.lib.io.XmlElement;

/**
 * Spreadsheetの1ファイルの概要を扱う
 */
public class SpreadsheetEntry extends BaseGDataObject {

    /**
     * ファイルを示す一意のID
     */
    String key;

    List<Author> author = new ArrayList<Author>();

    public SpreadsheetEntry(XmlElement elenent) {
        super(elenent);
        key = id.substring(id.lastIndexOf('/') + 1);

        // 編集者リストを設定する
        {
            List<XmlElement> list = elenent.listChilds("author");
            for (XmlElement author : list) {
                Author a = new Author(author);
                this.author.add(a);
            }
        }
    }

    /**
     * ワークシート等へアクセスするためのファイルごとの一意のキー
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * ワークシート一覧へアクセスするためのURL
     * @return
     */
    public String getWorksheetsUrl() {
        return Scopes.SPREADSHEET.getEndpoint() + "worksheets/" + key + "/private/full";
    }

    /**
     * 編集者一覧を取得する
     * @return
     */
    public List<Author> getAuthors() {
        return author;
    }
}
