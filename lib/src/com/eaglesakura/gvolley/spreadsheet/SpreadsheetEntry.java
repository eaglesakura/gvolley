package com.eaglesakura.gvolley.spreadsheet;

import java.util.ArrayList;
import java.util.List;

import com.eaglesakura.gvolley.auth.Scopes;
import com.eaglesakura.gvolley.gdata.Author;
import com.eaglesakura.gvolley.json.Model;
import com.eaglesakura.lib.io.XmlElement;

/**
 * Spreadsheetの1ファイルの概要を扱う
 */
public class SpreadsheetEntry extends Model {

    String id;

    String title;

    /**
     * ファイルを示す一意のID
     */
    String fileId;

    List<Author> author = new ArrayList<Author>();

    public SpreadsheetEntry(XmlElement elenent) {
        id = elenent.childToString("id");
        title = elenent.childToString("title");
        fileId = id.substring(id.lastIndexOf('/') + 1);

        // 編集者リストを設定する
        {
            List<XmlElement> list = elenent.listChilds("author");
            for (XmlElement author : list) {
                Author a = new Author(author);
                this.author.add(a);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    /**
     * ワークシート一覧へアクセスするためのURL
     * @return
     */
    public String getWorksheetsUrl() {
        return Scopes.SPREADSHEET.getEndpoint() + "worksheets/" + fileId + "/private/full";
    }

    /**
     * 編集者一覧を取得する
     * @return
     */
    public List<Author> getAuthors() {
        return author;
    }
}
