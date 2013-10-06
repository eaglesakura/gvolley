package com.eaglesakura.gvolley.gdata.spreadsheet;

import com.google.api.client.util.Key;

public class CellData {

    @Key("@row")
    public int row;

    @Key("@col")
    public int col;

    @Key("@inputValue")
    public String inputValue;

    @Key("text()")
    public String content;

    @Override
    public String toString() {
        return String.format("<gs:cell row=%d col=%d inputValue=%s>%s</gs:cell>", row, col, inputValue, content);
    }
}
