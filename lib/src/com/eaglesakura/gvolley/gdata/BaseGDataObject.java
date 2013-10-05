package com.eaglesakura.gvolley.gdata;

import java.util.Date;

import com.eaglesakura.gvolley.VolleyUtil;
import com.eaglesakura.lib.io.XmlElement;

public class BaseGDataObject {
    /**
     * オブジェクトタイトル
     */
    final protected String title;

    /**
     * 一意のID
     */
    final protected String id;

    /**
     * 更新日時
     */
    final protected Date updated;

    public BaseGDataObject(XmlElement element) {
        title = element.childToString("title");
        id = element.childToString("id");
        updated = VolleyUtil.toDate(element.childToString("updated"));
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public Date getUpdated() {
        return updated;
    }
}
