package com.pinyougou.common.utils;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.util.Map;
import java.util.UUID;

public class FileUploadUtil {

    public static String upload(Map<String, String> config, byte[] data) {
        Configuration cfg = new Configuration(Zone.zone1());//zong1() 代表华北地区
        UploadManager uploadManager = new UploadManager(cfg);

        String accessKey = config.get("accessKey"); //AccessKey的值
        String secretKey = config.get("secretKey"); //SecretKey的值
        String bucket = config.get("bucket");                                          //存储空间名

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;                                              //在七牛云中图片的命名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(data, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
            }
            return null;
        }
    }

    private static String suffixFileName(String fileName) {
        int index = fileName.lastIndexOf('.');
        return fileName.substring(index + 1);
    }

    private static String randomFileName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Map<String,Object> kindEditorSuccess(String url) {
        return ImmutableMap.of(
                "error",0,
                "url",url
        );
    }
    public static Map<String,Object> kindEditorError(String message) {
        return ImmutableMap.of(
                "error",1,
                "message",message
        );
    }
}
