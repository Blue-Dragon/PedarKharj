package com.example.pedarkharj.profile;


import android.annotation.SuppressLint;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    /*      My  code        */
    private static VolleySingleton mInstance;
    private  Context mContext;
    private RequestQueue requestQueue;

    private VolleySingleton (Context context){
        mContext = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext);
        }
        return  requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> tRequest){
        getRequestQueue().add(tRequest);
    }


    /*      their code      */
//    private static VolleySingleton mInstance;
//    private RequestQueue mRequestQueue;
//    private static Context mCtx;
//
//    private VolleySingleton(Context context) {
//        mCtx = context;
//        mRequestQueue = getRequestQueue();
//    }
//
//    //creating an instance of our Constructor (not able to call constructor directly from out of this class)
//    public static synchronized VolleySingleton getInstance(Context context) {
//        if (mInstance == null) {
//            mInstance = new VolleySingleton(context);
//        }
//        return mInstance;
//    }
//
//    public RequestQueue getRequestQueue() {
//        if (mRequestQueue == null) {
//            // getApplicationContext() is key, it keeps you from leaking the
//            // Activity or BroadcastReceiver if someone passes one in.
//            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
//        }
//        return mRequestQueue;
//    }
//
//    public <T> void addToRequestQueue(Request<T> req) {
//        getRequestQueue().add(req);
//    }
}