package com.sankhla.sunshine;

/**
 * Created by Chetan_Sankhla on 19-Jan-17.
 */

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JsonObjectRequest extends Request<JSONObject> {

    // in 3 minutes cache will be hit, but also refreshed on background
    final static long mCacheHitButRefreshed = 3 * 60 * 1000;
    // in 1 hours this cache entry expires completely
    final static long mCacheExpired = 60 * 60 * 1000;

    private Listener<JSONObject> mListener;
    private Map<String, String> mParams;

    private static final String AUTHORIZATION_KEY = "X-Authorization";
    private String mAndroidApiAuthorization;

    public JsonObjectRequest(int method, String url, Map<String, String> params,
                             Listener<JSONObject> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = responseListener;
        this.mParams = params;
        //setShouldCache(false);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    public void setAndroidApiAuthorization(String authorization) {
        mAndroidApiAuthorization = authorization;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (TextUtils.isEmpty(mAndroidApiAuthorization)) {
            return super.getHeaders();
        }
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(AUTHORIZATION_KEY, mAndroidApiAuthorization);
        return headers;
    }

    @Override
    public String getCacheKey() {
        StringBuilder sb = new StringBuilder(super.getCacheKey());
        if (mParams != null) {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
            }
        }
        return sb.toString();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    parseIgnoreCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    /**
     * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
     * Cache-control headers are ignored. SoftTtl == 3 mins, ttl == 1 hours.
     *
     * @param response The network response to parse headers from
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    private Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {
        if (shouldCache()) {
            long now = System.currentTimeMillis();

            Map<String, String> headers = response.headers;
            long serverDate = 0;
            String headerValue = headers.get("Date");
            if (headerValue != null) {
                serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
            }

            long softExpire = now + mCacheHitButRefreshed;
            long ttl = now + mCacheExpired;

            Cache.Entry entry = new Cache.Entry();
            entry.data = response.data;
            entry.softTtl = softExpire;
            entry.ttl = ttl;
            entry.serverDate = serverDate;
            entry.responseHeaders = headers;

            return entry;
        }
        return null;
    }

}

