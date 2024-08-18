package com.lvchenglong.blogbackend.controller;

import com.github.pagehelper.PageInfo;
import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.entity.Activity;
import com.lvchenglong.blogbackend.service.ActivityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 活动前端操作接口
 **/
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Resource
    private ActivityService activityService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Activity activity) {
        activityService.add(activity);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        activityService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        activityService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Activity activity) {
        activityService.updateById(activity);
        return Result.success();
    }

    /**
     * 更新浏览量
     */
    @PutMapping("/updateReadCount/{activityId}")
    public Result updateReadCount(@PathVariable Integer activityId) {
        activityService.updateReadCount(activityId);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Activity activity = activityService.selectById(id);
        return Result.success(activity);
    }

    /**
     * 查询用户自己报名的活动
     */
    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Integer userId){
        List<Activity> activityList = activityService.selectByUserId(userId);
        return Result.success(activityList);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Activity activity) {
        List<Activity> list = activityService.selectAll(activity);
        return Result.success(list);
    }

    /**
     * 查询用户自己点赞的活动
     */
    @GetMapping("/selectLikesByUserId/{userId}")
    public Result selectLikesByUserId(@PathVariable Integer userId){
        List<Activity> activityList = activityService.selectLikesByUserId(userId);
        return Result.success(activityList);
    }

    /**
     * 查询用户自己收藏的活动
     */
    @GetMapping("/selectCollectByUserId/{userId}")
    public Result selectCollectByUserId(@PathVariable Integer userId){
        List<Activity> activityList = activityService.selectCollectByUserId(userId);
        return Result.success(activityList);
    }

    /**
     * 查询用户自己评论的活动
     */
    @GetMapping("/selectCommentByUserId/{userId}")
    public Result selectCommentByUserId(@PathVariable Integer userId){
        List<Activity> activityList = activityService.selectCommentByUserId(userId);
        return Result.success(activityList);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Activity activity,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Activity> page = activityService.selectPage(activity, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 热门活动
     */
    @GetMapping("/selectTop")
    public Result selectTop() {
        List<Activity> list = activityService.selectTop();
        return Result.success(list);
    }
}
