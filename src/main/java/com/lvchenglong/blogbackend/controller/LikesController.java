package com.lvchenglong.blogbackend.controller;

import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.entity.Likes;
import com.lvchenglong.blogbackend.service.LikesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/likes")
public class LikesController {

    @Resource
    private LikesService likesService;

    /**
     * 点赞和取消
     * @param likes
     * @return
     */
    @PostMapping("/set")
    public Result set(@RequestBody Likes likes){
        likesService.set(likes);
        return Result.success();
    }
}
