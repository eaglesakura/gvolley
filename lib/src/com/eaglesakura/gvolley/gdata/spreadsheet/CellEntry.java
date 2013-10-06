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
}
