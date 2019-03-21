package com.pinyougou.seller.controller;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.common.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
public class UploadController {

    @Value("${QINIUYUN_ACCESSKEY}")
    private String accessKey;
    @Value("${QINIUYUN_SECRETKEY}")
    private String secretKey;
    @Value("${QINIUYUN_BUCKET}")
    private String bucket;
    @Value("${UINIUYUN_BASE_URL}")
    private String baseUrl;

    @RequestMapping("/upload")
    public ResponseResult<String> upload(MultipartFile file) {
        try {
            Map<String, String> config = ImmutableMap.of(
                    "accessKey", accessKey,
                    "secretKey", secretKey,
                    "bucket", bucket
            );
            byte[] data = file.getBytes();
            String newFileName = FileUploadUtil.upload(config, data);
            if (Strings.isNullOrEmpty(newFileName)) {
                return ResponseResult.error("图片上传出错");
            }
            return ResponseResult.ok(baseUrl+newFileName);
        }catch (Exception e) {
            return ResponseResult.error("图片上传出错");
        }
    }

    @RequestMapping("/kindEditorUpload")
    public Map<String,Object> kindEditorUpload(MultipartFile file) {
        try {
            Map<String, String> config = ImmutableMap.of(
                    "accessKey", accessKey,
                    "secretKey", secretKey,
                    "bucket", bucket
            );
            byte[] data = file.getBytes();
            String newFileName = FileUploadUtil.upload(config, data);
            if (Strings.isNullOrEmpty(newFileName)) {
                return FileUploadUtil.kindEditorError("图片上传出错");
            }
            return FileUploadUtil.kindEditorSuccess(baseUrl+newFileName);
        }catch (Exception e) {
            return FileUploadUtil.kindEditorError("图片上传出错");
        }
    }

}
