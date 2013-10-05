package com.eaglesakura.gvolley.gdata;

import com.eaglesakura.gvolley.json.Model;
import com.eaglesakura.lib.io.XmlElement;

public class Author extends Model {

    public String name;

    public String email;

    public Author() {

    }

    public Author(XmlElement element) {
        name = element.childToString("name");
        email = element.childToString("email");
    }
}
