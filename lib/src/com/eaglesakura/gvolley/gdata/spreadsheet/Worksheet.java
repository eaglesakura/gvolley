package com.eaglesakura.gvolley.gdata.spreadsheet;

import java.util.List;

import com.google.api.client.util.Key;

/**
 * Spreadsheetの1ワークシートを管理する
 * ワークシートは複数のシートの集合になる
 */
public class Worksheet {
    /**
     * ファイルキー
     */
    public String key;

    @Key
    public String title;

    @Key
    public String id;

    @Key("entry")
    public List<SheetEntry> entries;

    public Worksheet() {
    }

}
