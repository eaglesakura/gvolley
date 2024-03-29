package com.eaglesakura.gvolley.gdata.spreadsheet;

import java.util.List;

import com.eaglesakura.gvolley.gdata.model.BaseGDataObject;
import com.eaglesakura.gvolley.gdata.model.Link;
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
    public String getSheetId() {
        String temp = id.substring("https://spreadsheets.google.com/feeds/worksheets/".length());
        temp = temp.substring(temp.indexOf('/') + 1);
        return temp;
    }

    /**
     * ワークシート全体のキーを取得する
     * @return
     */
    public String getWorksheetId() {
        String temp = id.substring("https://spreadsheets.google.com/feeds/worksheets/".length());
        return temp.substring(0, temp.lastIndexOf('/'));
    }
}
