package com.eaglesakura.gvolley.gdata.spreadsheet;

import java.util.ArrayList;
import java.util.List;

import com.eaglesakura.gvolley.gdata.BaseGDataObject;
import com.eaglesakura.gvolley.gdata.Link;
import com.google.api.client.util.Key;

public class RowEntry extends BaseGDataObject {
    @Key("content")
    public String content;

    @Key("link")
    public List<Link> links;

    private List<Cell> cellCache = null;

    /**
     * セル分割を行う
     * @return
     */
    public List<Cell> listCells() {
        if (cellCache == null) {
            List<Cell> result = new ArrayList<Cell>();
            String[] split = content.split(", ");
            for (String value : split) {
                String[] kv = value.split(": ");
                Cell cell = new Cell();
                cell.columnId = kv[0];
                cell.value = kv[1];
                result.add(cell);
            }
            cellCache = result;
        }
        return cellCache;
    }
}
