package com.eaglesakura.gvolley.spreadsheet;

import java.util.Date;

import com.eaglesakura.gvolley.VolleyUtil;
import com.eaglesakura.gvolley.json.Model;
import com.eaglesakura.lib.io.XmlElement;

/**
 * Spreadsheetのファイル一覧
 */
public class SpreadsheetDocumentList extends Model {

    /**
     * ID
     */
    public String id;

    /**
     * updated
     */
    public Date updated;

    /**
     * 
     */
    public Entry[] entries;

    public static class Entry {
        /**
         * unique id
         */
        public String id;

        /**
         * タイトル
         */
        public String title;
        public String tablesLink;
    }

    public SpreadsheetDocumentList(XmlElement root) {
        this.id = root.childToString("id");
        this.updated = VolleyUtil.toDate(root.childToString("updated"));
    }
}
