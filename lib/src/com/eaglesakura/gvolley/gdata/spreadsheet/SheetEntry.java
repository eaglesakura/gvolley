package com.eaglesakura.gvolley.gdata.spreadsheet;

import java.util.List;

import com.eaglesakura.gvolley.gdata.BaseGDataObject;
import com.eaglesakura.gvolley.gdata.Link;
import com.google.api.client.util.Key;

/**
 * 1シートの概要を構築する
 */
public class SheetEntry extends BaseGDataObject {

    @Key("link")
    public List<Link> links;

    /**
     * シートのアクセスキーを取得する
     * @return
     */
    public String getKey() {
        String temp = id.substring("https://spreadsheets.google.com/feeds/worksheets/".length() + 1);
        return temp.substring(0, temp.lastIndexOf('/'));
    }
}
