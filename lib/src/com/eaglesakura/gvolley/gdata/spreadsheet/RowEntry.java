package com.eaglesakura.gvolley.gdata.spreadsheet;

import java.util.ArrayList;
import java.util.List;

import com.eaglesakura.gvolley.gdata.model.BaseGDataObject;
import com.eaglesakura.gvolley.gdata.model.Link;
import com.google.api.client.util.Key;

public class RowEntry extends BaseGDataObject {
    @Key("content")
    public String content;

    @Key("link")
    public List<Link> links;

    @Key("gs:row")
    public int row;

    @Key("gs:col")
    public int col;

    private List<CellCache> cellCache = null;

    /**
     * セル分割を行う
     * @return
     */
    public List<CellCache> listCells() {
        if (cellCache == null) {
            List<CellCache> result = new ArrayList<CellCache>();
            String[] split = content.split(", ");
            for (String value : split) {
                String[] kv = value.split(": ");
                CellCache cell = new CellCache();
                cell.columnId = kv[0];
                cell.value = kv[1];
                result.add(cell);
            }
            cellCache = result;
        }
        return cellCache;
    }
}
