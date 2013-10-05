package com.eaglesakura.gvolley.gdata.spreadsheet;

import com.eaglesakura.gvolley.gdata.BaseGDataObject;
import com.eaglesakura.lib.io.XmlElement;

/**
 * 1シートの概要を構築する
 */
public class SheetEntry extends BaseGDataObject {

    final String key;

    /**
     * 行数を取得する
     */
    final int rowCount;

    /**
     * 列数を取得する
     */
    final int colCount;

    public SheetEntry(XmlElement element) {
        super(element);

        {
            String temp = id.substring("https://spreadsheets.google.com/feeds/worksheets/".length() + 1);
            key = temp.substring(0, temp.lastIndexOf('/'));
        }

        // 行数・列数を取得する
        {
            rowCount = element.childToInt("gs:rowCount", 0);
            colCount = element.childToInt("gs:colCount", 0);
        }
    }
}
