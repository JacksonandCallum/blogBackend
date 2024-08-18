package com.lvchenglong.blogbackend.controller.TencentYun;

import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.utils.TencentCosUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/tencent")
public class UploadFileController {

    @Autowired
    private TencentCosUtils tencentCosUtils;

    //腾讯云存储
    @PostMapping("/image")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传，文件名：{}",file.getOriginalFilename());
        String url = tencentCosUtils.upload(file);
        log.info("文件上传完成，文件访问的url为：{}",url);
        return Result.success(url);
    }
}
