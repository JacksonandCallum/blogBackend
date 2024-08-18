package com.lvchenglong.blogbackend.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云配置类  绑定配置文件
 */
@Data
@Configuration
public class QiNiuYunConfig {
    @Value("${oss.qiniu.url}")
    private String url;

    @Value("${oss.qiniu.accessKey}")
    private String AccessKey;

    @Value("${oss.qiniu.secretKey}")
    private String SecretKey;

    @Value("${oss.qiniu.bucketName}")
    private String BucketName;
}
