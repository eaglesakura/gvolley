package com.eaglesakura.gvolley.gdata.model;

import com.google.api.client.util.Key;

public class Link {

    @Key("@rel")
    public String rel;

    @Key("@type")
    public String type;

    @Key("@href")
    public String href;
}
