package com.lvchenglong.blogbackend.controller;

import com.github.pagehelper.PageInfo;
import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.entity.ActivitySign;
import com.lvchenglong.blogbackend.service.ActivitySignService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/activitySign")
public class ActivitySignController {
    @Resource
    ActivitySignService activitySignService;

    @PostMapping("/add")
    public Result add(@RequestBody ActivitySign activitySign) {
        activitySignService.add(activitySign);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        activitySignService.deleteById(id);
        return Result.success();
    }

    /**
     * 用户取消报名
     */
    @DeleteMapping("/delete/user/{activityId}/{userId}")
    public Result userCancel(@PathVariable Integer activityId,@PathVariable Integer userId) {
        activitySignService.userCancel(activityId,userId);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        activitySignService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ActivitySign activitySign,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ActivitySign> page = activitySignService.selectPage(activitySign, pageNum, pageSize);
        return Result.success(page);
    }

}
