package com.eaglesakura.gvolley.gdata.spreadsheet;

import java.util.ArrayList;
import java.util.List;

import com.eaglesakura.gvolley.gdata.BaseGDataObject;
import com.google.api.client.util.Key;

/**
 * Spreadsheetのファイル一覧
 */
public class SpreadsheetDocumentList extends BaseGDataObject {

    /**
     * 
     */
    @Key("entry")
    public List<WorksheetEntry> entries = new ArrayList<WorksheetEntry>();

    public SpreadsheetDocumentList() {
    }

    /**
     * 指定したファイルを取得する
     * @param name
     * @return
     */
    public WorksheetEntry getEntry(String name) {
        for (WorksheetEntry entry : entries) {
            if (entry.getTitle().equals(name)) {
                return entry;
            }
        }
        return null;
    }
}
