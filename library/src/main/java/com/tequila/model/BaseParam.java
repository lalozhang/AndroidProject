package com.tequila.model;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by admin on 2016/10/2.
 */
public class BaseParam {


    public String newCacheKey(){
        String cacheKey = null;
        try{
            cacheKey = JSONObject.toJSONString(this);
        }catch(Exception e){

        }
        return cacheKey;
    }
}
