package com.jellyfish.sell.support.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AliyunStorageService extends CloudStorageService {
    private OSSClient client;
    private static final String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    private static final String ACCESSKEYID = "LTAIczHAmyNenC4n";
    private static final String ACCESSKEYSECRET = "1ykgsuCw00zA4UT5uwHUpZk69T6S6s";
    private static String BUCKETNAME = "oss-jellyfish";
    private static String DOMAIN = "https://static.coffee99.cn";

    public AliyunStorageService() {
        init();
    }

    private void init() {
        client = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
    }

    @Override
    public String upload(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return upload(file.getBytes(), getPath("upload") + "." + prefix);
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
           // OSSClient client = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
            client.putObject(BUCKETNAME, path, inputStream);
        } catch (Exception e) {
        }

        return DOMAIN + "/" + path;
    }

    @Override
    public  boolean isHaveFile(String fileKey){
        try {
            //OSSClient client = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
            return client.doesObjectExist(BUCKETNAME, fileKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String[] uploadAcode2OSS(InputStream is, String remotePath, long length) {

        String resultStr = null;
        String[] fo = new String[] { "", "" };
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(length);
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
          //  OSSClient client = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
            PutObjectResult putResult = client.putObject(BUCKETNAME,
                    remotePath, is, metadata);
            resultStr = putResult.getETag();
            fo[1] = remotePath;
            fo[0] = resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fo;
    }
}
