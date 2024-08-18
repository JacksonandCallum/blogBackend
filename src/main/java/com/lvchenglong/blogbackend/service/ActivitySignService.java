package com.lvchenglong.blogbackend.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.blogbackend.common.enums.ResultCodeEnum;
import com.lvchenglong.blogbackend.entity.Account;
import com.lvchenglong.blogbackend.entity.ActivitySign;
import com.lvchenglong.blogbackend.exception.CustomException;
import com.lvchenglong.blogbackend.mapper.ActivitySignMapper;
import com.lvchenglong.blogbackend.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivitySignService {
    @Resource
    ActivitySignMapper activitySignMapper;

    public void add(ActivitySign activitySign) {
        Account currentUser = TokenUtils.getCurrentUser();
        ActivitySign as = this.selectByActivityIdAndUserId(activitySign.getActivityId(), currentUser.getId());  // 查看用户是否已经报名
        if (as != null) {
            throw new CustomException(ResultCodeEnum.ACTIVITY_SIGN_ERROR);
        }
        activitySign.setUserId(currentUser.getId());
        activitySign.setTime(DateUtil.now());
        activitySignMapper.insert(activitySign);
    }

    public ActivitySign selectByActivityIdAndUserId(Integer actId, Integer userId) {
        return activitySignMapper.selectByActivityIdAndUserId(actId, userId);
    }

    public PageInfo<ActivitySign> selectPage(ActivitySign activitySign, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ActivitySign> list = activitySignMapper.selectAll(activitySign);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        activitySignMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public void userCancel(Integer activityId, Integer userId) {
        activitySignMapper.userCancel(activityId,userId);
    }
}
