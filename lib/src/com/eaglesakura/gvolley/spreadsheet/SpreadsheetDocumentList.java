package com.eaglesakura.gvolley.spreadsheet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    String id;

    /**
     * updated
     */
    Date updated;

    /**
     * 
     */
    List<SpreadsheetEntry> files = new ArrayList<SpreadsheetEntry>();

    public SpreadsheetDocumentList(XmlElement root) {
        this.id = root.childToString("id");
        this.updated = VolleyUtil.toDate(root.childToString("updated"));
        initializeEntries(root);

    }

    void initializeEntries(XmlElement root) {
        List<XmlElement> childs = root.listChilds("entry");
        for (XmlElement element : childs) {
            SpreadsheetEntry entry = new SpreadsheetEntry(element);
            files.add(entry);
        }
    }

    public String getId() {
        return id;
    }

    public Date getUpdated() {
        return updated;
    }

    public List<SpreadsheetEntry> getFiles() {
        return files;
    }

    /**
     * 指定したファイルを取得する
     * @param name
     * @return
     */
    public SpreadsheetEntry getFile(String name) {
        for (SpreadsheetEntry entry : files) {
            if (entry.getTitle().equals(name)) {
                return entry;
            }
        }
        return null;
    }
}
