package com.eaglesakura.gvolley.gdata.model;

import java.util.Date;
import java.util.List;

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

    /**
     * リンク一覧から所定のリンクを取得する
     * @param links
     * @param rel
     * @return
     */
    public static String getLink(List<Link> links, String rel) {
        for (Link link : links) {
            if (rel.equals(link.rel)) {
                return link.href;
            }
        }
        return null;
    }
}
