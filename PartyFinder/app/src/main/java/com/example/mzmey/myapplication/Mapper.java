package com.example.mzmey.myapplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MZmey on 25.05.2015.
 */
public class Mapper {

    public static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
