package com.nonobank.testcase.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by tangrubei on 2017/7/11.
 */
public class SecurityUtil {
    /**
     * 生成16位的随机字符串
     * @param length
     * @return
     */
    private static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 拼接字符串
     * @param stringBuffer
     * @param key
     * @param value
     * @throws UnsupportedEncodingException
     */
    private static void esc(StringBuffer stringBuffer,String key,String value) throws UnsupportedEncodingException {
        if(value.length()>0&&!"undefined".equals(value) &&!"null".equals(value)){
            if(stringBuffer.length()>0){
                stringBuffer.append("&");
            }
            stringBuffer.append(key+"="+ URLEncoder.encode(String.valueOf(value),"utf-8"));
        }else if(value.length()==0&&!"null".equals(value)){
            if(stringBuffer.length()>0){
                stringBuffer.append("&");
            }
            stringBuffer.append(key+"=");
        }
    }


    /**
     * json object 转字符串
     * @param stringBuffer
     * @param pkey
     * @param jobj
     * @throws UnsupportedEncodingException
     */
    private static void e2s(StringBuffer stringBuffer,String pkey,JSONObject jobj) throws UnsupportedEncodingException {
        List<String> keys = new ArrayList<String>(jobj.keySet());
        Collections.sort(keys,(s1, s2)-> s1.toString().compareTo(s2.toString()));
        for(String key:keys){
            Object object = jobj.get(key);
            String currentKey = pkey!=null&&pkey.length()>0?pkey+"."+key:key;
            if(!(object instanceof JSONObject)&&!(object instanceof JSONArray)){
                esc(stringBuffer,currentKey,String.valueOf(object));
            }else{
                j2s(stringBuffer,currentKey,object);
            }
        }
    }

    /**
     * jsonarray 转字符串
     * @param stringBuffer
     * @param pkey
     * @param ljobj
     * @throws UnsupportedEncodingException
     */
    private static void l2s(StringBuffer stringBuffer,String pkey,JSONArray ljobj) throws UnsupportedEncodingException {
        for(int i=0;i<ljobj.size();i++){
            Object object = ljobj.get(i);
            if(!(object instanceof JSONObject)&&!(object instanceof JSONArray)){
                esc(stringBuffer,pkey,String.valueOf(object));
            }else{
                j2s(stringBuffer,pkey,object);
            }
        }
    }

    /**
     * object 转字符串
     * @param stringBuffer
     * @param pkey
     * @param object
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String j2s(StringBuffer stringBuffer,String pkey,Object object) throws UnsupportedEncodingException {
        if(object instanceof JSONObject){
            e2s(stringBuffer,pkey, (JSONObject) object);
        }else if(object instanceof JSONArray){
            l2s(stringBuffer,pkey, (JSONArray) object);
        }
        return stringBuffer.toString();
    }


    /**
     * jsonobject 转字符串
     * @param jsonObject
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String json2Str(JSONObject jsonObject) throws UnsupportedEncodingException {
        StringBuffer stringBuffer = new StringBuffer();
        return j2s(stringBuffer,"",jsonObject);
    }


    /**
     * 转md5字数组
     * @param source
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static byte[] encode2bytes(String source) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] result = null;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(source.getBytes("UTF-8"));
        result = md.digest();
        return result;
    }

    /**
     * md5数组转16进制
     * @param source
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private static String encode2hex(String source) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] data = encode2bytes(source);

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(0xff & data[i]);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * 对外公共函数
     * @param requestBody
     * @param appid
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static JSONObject doSecurity(String requestBody,String appid) throws UnsupportedEncodingException, NoSuchAlgorithmException {

//        if(appid==null||appid.length()==0){
//            String[] appids= {"nono","mxd","bld","unifi"};
//            Random intRandom = new Random();
//            int i = intRandom.nextInt(3);
//            appid =appids[i];
//        }
    	
    	if(null == requestBody){
    		requestBody = "{}";
    	}
    	
        String appkey = encode2hex(appid).substring(7,23);

        JSONObject jsonObject = JSON.parseObject(requestBody);
        if(jsonObject.containsKey("appId")){
            jsonObject.remove("appId");
        }
        jsonObject.put("timestamp",System.currentTimeMillis());
        jsonObject.put("noncestr",getRandomString(23));
        String jsonStr = json2Str(jsonObject)+appkey;
        System.out.println("对下面的数据进行加密");
        System.out.println(jsonStr);
        String signature = encode2hex(jsonStr);
        jsonObject.put("appId",appid);
        jsonObject.put("signature",signature);
        return jsonObject;

    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        JSONObject jsonObject = JSONObject.parseObject("{\n" +
                "    \"contacts\": [{\n" +
                "        \"addressArr\": [],\n" +
                "        \"emailArr\": [],\n" +
                "        \"firstName\": \"几个\",\n" +
                "        \"lastName\": \".\",\n" +
                "        \"middleName\": \"\",\n" +
                "        \"phoneArr\": [\"13987469004\"]\n" +
                "    }, {\n" +
                "        \"addressArr\": [],\n" +
                "        \"emailArr\": [],\n" +
                "        \"firstName\": \"俺修图忽悠\",\n" +
                "        \"lastName\": \"\",\n" +
                "        \"middleName\": \"\",\n" +
                "        \"phoneArr\": [\"13949484994\"]\n" +
                "    }],\n" +
                "    \"bizCode\": 7\n" +
                "}");
        String jsonStr = json2Str(jsonObject);
        System.out.println(jsonStr);

    }
}
