package com.sankhla.sunshine;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
public class NetworkManager {
    private static NetworkManager mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mAppContext;
    private static final int REQUEST_TIMEOUT = 10000;

    private NetworkManager(Context context) {
        // getApplicationContext() is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        mAppContext = context.getApplicationContext();
        mRequestQueue = getRequestQueue();

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        final int cacheSize = maxMemory / 8;

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount() / 1024;
                }
            };

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        }) {
            @Override
            public ImageLoader.ImageContainer get(String requestUrl,
                                                  ImageLoader.ImageListener imageListener,
                                                  int maxWidth, int maxHeight,
                                                  ImageView.ScaleType scaleType) {

                    android.util.Log.d("GT", "image load request URL:" + requestUrl);

                return super.get(requestUrl, imageListener, maxWidth, maxHeight, scaleType);
            }

            @Override
            protected void onGetImageError(String cacheKey, VolleyError error) {

                    android.util.Log.e("MT", "image load error cacheKey:" + cacheKey + ", error:"
                            + error.toString());

                super.onGetImageError(cacheKey, error);
            }

            @Override
            protected void onGetImageSuccess(String cacheKey, Bitmap response) {

                    android.util.Log.d("MT", "image load success cacheKey:" + cacheKey +
                            ", response:" + response);

                super.onGetImageSuccess(cacheKey, response);
            }
        };
    }

    public static NetworkManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (NetworkManager.class) {
                // Double check
                if (mInstance == null) {
                    mInstance = new NetworkManager(context);
                }
            }
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mAppContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {

            android.util.Log.d("MT", "add request URL:" + req.getOriginUrl());

        //changed
        req.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        //changed
        getRequestQueue().add(req);
        //clearCache();
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void clearCache() {
        getRequestQueue().getCache().clear();
    }
}
