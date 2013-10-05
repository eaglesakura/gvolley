package com.eaglesakura.gvolley.gdata.spreadsheet;

import java.util.List;

import com.eaglesakura.gvolley.gdata.model.Author;
import com.eaglesakura.gvolley.gdata.model.BaseGDataObject;
import com.eaglesakura.gvolley.gdata.model.Link;
import com.google.api.client.util.Key;

public class Sheet extends BaseGDataObject {

    /**
     * link
     */
    @Key("link")
    public List<Link> links;

    /**
     * 編集者一覧
     */
    @Key("author")
    public List<Author> authors;

    @Key("openSearch:totalResults")
    public int totalResult;

    @Key("openSearch:startIndex")
    public int startIndex;

    @Key("entry")
    public List<RowEntry> rows;
}
