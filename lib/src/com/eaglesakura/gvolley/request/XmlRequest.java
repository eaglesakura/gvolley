package com.eaglesakura.gvolley.request;

import java.io.ByteArrayInputStream;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.eaglesakura.gvolley.request.listener.RequestListener;
import com.eaglesakura.lib.android.game.util.LogUtil;
import com.eaglesakura.lib.io.XmlElement;

public abstract class XmlRequest<T> extends BaseRequest<T, XmlElement> {

    public XmlRequest(int method, String url, RequestListener<T> listener) {
        super(method, url, listener);
    }

    @Override
    protected Response<XmlElement> parseNetworkResponse(NetworkResponse response) {
        try {
            XmlElement element = XmlElement.parse(new ByteArrayInputStream(response.data));

            return Response.success(element, getCacheEntry());
        } catch (Exception e) {
            LogUtil.log(e);
        }
        return Response.error(new VolleyError("Xml Parse error"));
    }

    @Override
    protected abstract T convert(XmlElement response);
}
