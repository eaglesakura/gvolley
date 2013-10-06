package com.eaglesakura.gvolley.gdata.spreadsheet;

public class CellCache {
    /**
     * カラムID
     */
    public String columnId;

    /**
     * 値
     */
    public String value;

    @Override
    public String toString() {
        return "" + columnId + "=" + value;
    }
}
