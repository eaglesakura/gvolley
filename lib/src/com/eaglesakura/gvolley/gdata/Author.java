package com.eaglesakura.gvolley.gdata;

import com.eaglesakura.gvolley.json.Model;
import com.google.api.client.util.Key;

/**
 * Driveの編集者情報
 */
public class Author extends Model {

    @Key
    public String name;

    @Key
    public String email;

    public Author() {

    }
}
