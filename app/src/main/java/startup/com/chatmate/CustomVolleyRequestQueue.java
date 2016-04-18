package startup.com.chatmate;

/**
 * Created by USER on 03/09/2015.
 */
import android.content.Context;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;

import java.io.File;

public class CustomVolleyRequestQueue {

    private static CustomVolleyRequestQueue mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private static final int DEFAULT_DISK_USAGE_BYTES = 25 * 1024 * 1024;
    private static final String DEFAULT_CACHE_DIR = "photos";


    private CustomVolleyRequestQueue(Context context) {
        mCtx = context;
        //mRequestQueue = getRequestQueue();
    }

    public static synchronized CustomVolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CustomVolleyRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        /*if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;*/
            File rootCache = mCtx.getExternalCacheDir();
            if (rootCache == null) {
                rootCache = mCtx.getCacheDir();
            }
            File cacheDir = new File(rootCache, DEFAULT_CACHE_DIR);
            cacheDir.mkdirs();

            HttpStack stack = new HurlStack();
            Network network = new BasicNetwork(stack);
            DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DEFAULT_DISK_USAGE_BYTES);
            mRequestQueue = new RequestQueue(diskBasedCache, network);
            mRequestQueue.start();

            return mRequestQueue;

    }

}