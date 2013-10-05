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

    /**
     * 1シート詳細用のURLを取得する
     * @return
     */
    public String getDetailUrl() {
        return "https://spreadsheets.google.com/feeds/list/" + getWorksheetId() + "/" + getSheetId() + "/private/full";
    }
}
