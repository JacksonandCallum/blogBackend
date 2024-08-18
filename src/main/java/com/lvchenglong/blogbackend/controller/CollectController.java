package com.lvchenglong.blogbackend.controller;

import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.entity.Collect;
import com.lvchenglong.blogbackend.service.CollectService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    /**
     * 收藏和取消
     * @param collect
     * @return
     */
    @PostMapping("/set")
    public Result set(@RequestBody Collect collect){
        collectService.set(collect);
        return Result.success();
    }
}
