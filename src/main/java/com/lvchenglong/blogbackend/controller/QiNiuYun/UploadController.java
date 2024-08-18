package com.lvchenglong.blogbackend.controller.QiNiuYun;

import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.common.enums.ResultCodeEnum;
import com.lvchenglong.blogbackend.common.vo.QiniuVo;
import com.lvchenglong.blogbackend.service.QiNiuYun.UploadImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/qiniu")
public class UploadController {

    @Autowired
    private UploadImageService uploadImageService;

    @PostMapping("/image")
    public Result upLoadImage(@RequestParam("file") MultipartFile file){
        String result = "失败";
        QiniuVo qiniuVo = new QiniuVo();
        if(!file.isEmpty()){
            String path = uploadImageService.uploadQNImg(file);
            if(path.equals(result)){
                return Result.error(ResultCodeEnum.UPLOAD_ERROR);
            }else{
                System.out.println("七牛云返回的图片链接是：" + path);
                qiniuVo.setPath(path);
                return Result.success(qiniuVo);
            }
        }
        return Result.error(ResultCodeEnum.UPLOAD_ERROR);
    }
}
