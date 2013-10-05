package com.eaglesakura.gvolley.gdata.spreadsheet;

import com.eaglesakura.gvolley.gdata.BaseGDataObject;
import com.eaglesakura.lib.io.XmlElement;

/**
 * Spreadsheetの1ワークシートを管理する
 * ワークシートは複数のシートの集合になる
 */
public class Worksheet extends BaseGDataObject {
    /**
     * ファイルキー
     */
    final String key;

    public Worksheet(XmlElement root, String key) {
        super(root);
        this.key = key;
    }
}
