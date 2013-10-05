package com.eaglesakura.gvolley.request;

import java.io.ByteArrayInputStream;

import org.xmlpull.v1.XmlPullParser;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.eaglesakura.gvolley.request.listener.RequestListener;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;

public class SimpleXmlRequest<T> extends BaseRequest<T> {

    /**
     * 
     */
    protected final Class<? extends T> clazz;

    public SimpleXmlRequest(int method, String url, Class<? extends T> clazz, RequestListener<T> listener) {
        super(method, url, listener);
        this.clazz = clazz;
    }

    /**
     * XMLをModelへ変換する
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            XmlNamespaceDictionary dictionary = new XmlNamespaceDictionary();
            XmlPullParser parser = Xml.createParser();
            parser.setInput(new ByteArrayInputStream(response.data), "UTF-8");

            T instance = newInstance();
            Xml.parseElement(parser, instance, dictionary, null);
            return Response.success(instance, getCacheEntry());
        } catch (Exception e) {
            return Response.error(new VolleyError(e.getMessage()));
        }
    }

    protected T newInstance() {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}
