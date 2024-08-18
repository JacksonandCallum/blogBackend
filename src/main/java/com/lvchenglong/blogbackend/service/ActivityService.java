package com.lvchenglong.blogbackend.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.blogbackend.common.enums.LikesModuleEnum;
import com.lvchenglong.blogbackend.entity.*;
import com.lvchenglong.blogbackend.mapper.ActivityMapper;
import com.lvchenglong.blogbackend.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动业务处理
 **/
@Service
public class ActivityService {
    @Resource
    private ActivityMapper activityMapper;

    @Resource
    ActivitySignService activitySignService;

    @Resource
    LikesService likesService;

    @Resource
    CollectService collectService;

    /**
     * 新增
     */
    public void add(Activity activity) {
        activityMapper.insert(activity);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        activityMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            activityMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Activity activity) {
        activityMapper.updateById(activity);
    }

    /**
     * 根据ID查询
     */
    public Activity selectById(Integer id) {
        Activity activity = activityMapper.selectById(id);
        this.setAct(activity,TokenUtils.getCurrentUser());

        // 更新阅读量
        // activity.setReadCount(activity.getReadCount() + 1);
        // this.updateById(activity);

        // 点赞
        int likesCount = likesService.selectByFidAndModule(id, LikesModuleEnum.ACTIVITY.getValue());
        activity.setLikesCount(likesCount);

        // 收藏
        int collectCount = collectService.selectByFidAndModule(id, LikesModuleEnum.ACTIVITY.getValue());
        activity.setCollectCount(collectCount);

        // 用户是否点赞
        Likes likes = likesService.selectUserLikes(id, LikesModuleEnum.ACTIVITY.getValue());
        activity.setIsLike(likes != null);

        // 用户是否收藏
        Collect collect = collectService.selectUserCollect(id, LikesModuleEnum.ACTIVITY.getValue());
        activity.setIsCollect(collect != null);

        return activity;
    }

    /**
     * 查询所有
     */
    public List<Activity> selectAll(Activity activity) {
        List<Activity> activityList = activityMapper.selectAll(activity);
        Account currentUser = TokenUtils.getCurrentUser();
        for (Activity act : activityList) {
            this.setAct(act,currentUser);
        }
        return activityList;
    }

    /**
     * 设置活动额外信息
     * @param act
     * @param currentUser
     */
    public void setAct(Activity act, Account currentUser){
        act.setIsEnd(DateUtil.parseDate(act.getEnd()).isBefore(new Date()));  //活动结束时间在当前时间之前就结束了
        ActivitySign activitySign = activitySignService.selectByActivityIdAndUserId(act.getId(), currentUser.getId());
        act.setIsSign(activitySign != null);
    }

    /**
     * 分页查询
     */
    public PageInfo<Activity> selectPage(Activity activity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectAll(activity);
        return PageInfo.of(list);
    }

    public List<Activity> selectTop() {
        List<Activity> activityList = this.selectAll(null);
        // 倒序
        activityList = activityList.stream().sorted((b1,b2)->b2.getReadCount().compareTo(b1.getReadCount()))
                .limit(2)
                .collect(Collectors.toList());
        return activityList;
    }

    public void updateReadCount(Integer activityId) {
        activityMapper.updateReadCount(activityId);
    }

    public List<Activity> selectByUserId(Integer userId) {
        List<Activity> activityList = activityMapper.selectByUserId(userId);
        Account currentUser = TokenUtils.getCurrentUser();
        for (Activity activity : activityList) {
            this.setAct(activity,currentUser);
        }
        return activityList;
    }

    /**
     * 查询用户自己点赞的活动
     */
    public List<Activity> selectLikesByUserId(Integer userId) {
        List<Activity> list = activityMapper.selectLikesByUserId(userId);
        Account currentUser = TokenUtils.getCurrentUser();
        for (Activity activity : list) {
            this.setAct(activity,currentUser);
        }
        return list;
    }

    /**
     * 查询用户自己收藏的活动
     */
    public List<Activity> selectCollectByUserId(Integer userId) {
        List<Activity> list = activityMapper.selectCollectByUserId(userId);
        Account currentUser = TokenUtils.getCurrentUser();
        for (Activity activity : list) {
            this.setAct(activity,currentUser);
        }
        return list;
    }

    /**
     * 查询用户自己评论的活动
     */
    public List<Activity> selectCommentByUserId(Integer userId) {
        List<Activity> list = activityMapper.selectCommentByUserId(userId);
        Account currentUser = TokenUtils.getCurrentUser();
        for (Activity activity : list) {
            this.setAct(activity,currentUser);
        }
        return list;
    }
}
