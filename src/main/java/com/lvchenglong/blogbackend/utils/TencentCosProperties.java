package com.lvchenglong.blogbackend.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@Data
@Component
@ConfigurationProperties(prefix = "tencent.cos")
public class TencentCosProperties {
    // https://mr-lv-blog-1326677053.cos.ap-nanjing.myqcloud.com
    private String rootSrc ;
    // ap-nanjing
    private String bucketAddr;
    // AKIDra6P6jAOblob8PQBgwwg9JL8fwyxpVx9
    private String secretId;
    // 12tVqvfDVXR8i7KI2YptjKix60fNxJRK
    private String secretKey;
    // mr-lv-blog-1326677053
    private String bucketName;

    public String getRootSrc() {
        return rootSrc;
    }

    public String getBucketAddr() {
        return bucketAddr;
    }

    public void setBucketAddr(String bucketAddr) {
        this.bucketAddr = bucketAddr;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setRootSrc(String rootSrc) {
        this.rootSrc = rootSrc;
    }
}
