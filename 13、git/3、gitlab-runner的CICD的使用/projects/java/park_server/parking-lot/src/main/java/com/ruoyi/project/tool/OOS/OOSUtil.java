package com.ruoyi.project.tool.OOS;

import cn.ctyun.config.DomainConfig;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Slf4j
public class OOSUtil {

    private static String myBucketName = TestConfig.bucketName;

    private static volatile AmazonS3 ossClient;

    public static AmazonS3 getAmazonS3() {

        if (ossClient ==null){
            synchronized (AmazonS3.class){
                if (ossClient==null){
                    ClientConfiguration clientConfig = new ClientConfiguration();
                    clientConfig.setConnectionTimeout(30 * 1000);     //设置连接的超时时间，单位毫秒
                    clientConfig.setSocketTimeout(30 * 1000);        //设置socket超时时间，单位毫秒
                    clientConfig.setProtocol(Protocol.HTTP);        //设置http


                    //负载是否参与签名、设置参与
                    S3ClientOptions options = new S3ClientOptions();
                    options.setPayloadSigningEnabled(true);

                    /* 创建client*/
                    ossClient = new AmazonS3Client(
                            new PropertiesCredentials(TestConfig.OOS_ACCESS_ID, TestConfig.OOS_ACCESS_KEY), clientConfig);

                    // 设置endpoint
                    ossClient.setEndpoint(TestConfig.OOS_ENDPOINT);

                    //设置选项
                    ossClient.setS3ClientOptions(options);
                }
            }

        }

        return ossClient;
    }


    public static void putObject(AmazonS3 ossClient, File pictureFile, String pictureName) {
        System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "false");
        PutObjectResult result;
        PutObjectRequest request = new PutObjectRequest(myBucketName, pictureName, pictureFile);
        request.setStorageClass(StorageClass.ReducedRedundancy);
        result = ossClient.putObject(request);
        System.out.println("put object:" + pictureName + ",md5: " + result.getContentMd5());
        pictureFile.delete();
    }


    public static String getObject(AmazonS3 ossClient, String objectInBucketKey) throws IOException {
        // String objectInBucketKey = "A2.txt";
        //myBucketName = "csharp19";
        GetObjectRequest request = new GetObjectRequest(myBucketName, objectInBucketKey);
        //request.setLimit("2048");
        //request.setLimitrate("2048");
        //request.setRange(0,100);  //Start-End  取部分内容 End不能大于文件长度
        S3Object s3Object = ossClient.getObject(request);


        //S3Object s3Object = ossClient.getObject(myBucketName,objectInBucketKey);  //取全部

        //InputStream input = new ByteArrayInputStream(object.getObjectContent());

//        ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides();
//        headerOverrides.setContentType("");
//        request.setResponseHeaders(headerOverrides);
        S3ObjectInputStream input = s3Object.getObjectContent();

//        FileOutputStream fos = null;
        byte[] data=input.readAllBytes();
        String base64=Base64.encodeBase64String(data);
//        try {
//
//            int bufferSize = 10 * 1024;
//            byte [] bytes = new byte[bufferSize];
//            File file = new File(localFilePath);
//            fos = new FileOutputStream(file);
//
//            byte[] buffer = new byte[bufferSize];
//            int bytesRead= -1;
//            while ((bytesRead = input.read(buffer)) > -1) {
//                fos.write(buffer, 0, bytesRead);
//            }
//
//            fos.flush();
//            //input.reset();
//        } catch (IOException e) {
//            try {
//                input.abort();
//            } catch (IOException abortException) {
//            }
//        } finally {
//            if (fos != null)
//                fos.close();
//            if (input != null)
//                input.close();
//        }

        //如果是S6，查看getObjectMetadata
        if (!DomainConfig.isS5Endpoint(TestConfig.OOS_ENDPOINT)) {
            s3Object.getObjectMetadata();
        }
        return base64;
    }
}
