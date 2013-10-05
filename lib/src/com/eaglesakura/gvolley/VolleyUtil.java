package com.eaglesakura.gvolley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class VolleyUtil {

    public VolleyUtil() {
        // TODO Auto-generated constructor stub
    }

    /**
     * クエリ用URLを生成する
     * @param url
     * @param queries
     * @return
     */
    public static String makeQueryUrl(String url, Map<String, String> queries) {
        try {
            if (!queries.isEmpty()) {
                StringBuilder sb = new StringBuilder(url);
                sb.append("?");
    
                Iterator<Entry<String, String>> iterator = queries.entrySet().iterator();
                // URLにクエリを追加する
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "utf-8"));
                    if (iterator.hasNext()) {
                        sb.append('&');
                    }
                }
                return sb.toString();
            } else {
                return url;
            }
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

}
