package com.eaglesakura.gvolley.gdata.spreadsheet;

import java.util.ArrayList;
import java.util.List;

import com.eaglesakura.gvolley.gdata.model.BaseGDataObject;
import com.eaglesakura.gvolley.gdata.model.Link;
import com.google.api.client.util.Key;

public class CellEntry extends BaseGDataObject {
    /**
     * 表示内容
     */
    @Key("content")
    public String content;

    /**
     * 接続情報
     */
    @Key("link")
    public List<Link> links = new ArrayList<Link>();

    @Key("gs:cell")
    public CellData value;

    /**
     * 自分自身のリンクを取得する
     * 最新に値を更新する際に利用する
     */
    public String selfLink() {
        return getLink(links, "self");
    }

    public String cellId() {
        return id.substring(id.lastIndexOf('/' + 1));
    }

    /**
     * シートのアクセスキーを取得する
     * @return
     */
    public String sheetId() {
        String temp = id.substring("https://spreadsheets.google.com/feeds/cells/".length());
        temp = temp.substring(temp.indexOf('/') + 1);
        return temp;
    }

    /**
     * ワークシート全体のキーを取得する
     * @return
     */
    public String worksheetId() {
        String temp = id.substring("https://spreadsheets.google.com/feeds/cells/".length());
        return temp.substring(0, temp.lastIndexOf('/'));
    }
}
