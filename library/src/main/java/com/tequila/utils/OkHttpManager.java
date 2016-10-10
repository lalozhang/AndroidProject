package com.tequila.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by admin on 2016/10/10.
 */
public class OkHttpManager {

    private OkHttpClient mOkHttpClient;
    private volatile static OkHttpManager mInstance;
    private OkHttpManager(){
        mOkHttpClient = new OkHttpClient();
    }

    public static OkHttpManager getInstance(){
        if(mInstance==null){
            synchronized (OkHttpManager.class){
                if (mInstance == null) {
                    mInstance = new OkHttpManager();
                }
            }

        }
        return mInstance;
    }

    private RequestBody buildRequestBody(Map<String, String> bodyParams){
        RequestBody body;
        FormBody.Builder formBuilder = new FormBody.Builder();
        if(bodyParams != null){
            Set<Map.Entry<String, String>> entrySet = bodyParams.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> set = iterator.next();
                String key = set.getKey();
                String value = set.getValue();
                formBuilder.add(key,value);
            }
        }
        body=formBuilder.build();
        return body;
    }


    public Response execute(String url,Map<String, String> bodyParams) throws IOException {
        RequestBody formBody = buildRequestBody(bodyParams);
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }




}
