package com.eaglesakura.gvolley.gdata.model;

import java.util.Date;

import com.google.api.client.util.Key;

public class BaseGDataObject {
    /**
     * オブジェクトタイトル
     */
    @Key
    public String title;

    /**
     * 一意のID
     */
    @Key
    public String id;

    /**
     * 更新日時
     */
    @Key
    public Date updated;

    public BaseGDataObject() {
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
