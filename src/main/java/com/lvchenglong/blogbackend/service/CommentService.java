package com.lvchenglong.blogbackend.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.blogbackend.common.enums.RoleEnum;
import com.lvchenglong.blogbackend.entity.Account;
import com.lvchenglong.blogbackend.entity.Comment;
import com.lvchenglong.blogbackend.mapper.CommentMapper;
import com.lvchenglong.blogbackend.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务处理
 **/
@Service
public class CommentService {
    @Resource
    private CommentMapper commentMapper;

    /**
     * 新增
     */
    public void add(Comment comment) {
        // 获取当前登录用户
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            // 如果当前用户是普通用户，则设置评论的用户ID为当前用户的ID
            comment.setUserId(currentUser.getId());
        }
        // 设置评论时间
        comment.setTime(DateUtil.now());
        // 插入评论数据
        commentMapper.insert(comment);  // 先插入数据，拿到主键，再设置数据
        if(comment.getRootId() == null){
            comment.setRootId(comment.getId());
            commentMapper.updateById(comment);  // 更新root_id
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            commentMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    /**
     * 根据ID查询
     */
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }

    /**
     * 查询评论信息
     */
    public List<Comment> selectForUser(Comment comment) {
        List<Comment> commentList = commentMapper.selectForUser(comment);  // 查询一级评论
        for (Comment c : commentList) {  // 查询二级评论（回复列表）
            Comment param = new Comment();
            param.setRootId(c.getId());
            List<Comment> children = this.selectAll(param);
            // 排除当前查询结果中的（父级--一级）最外层节点
            children = children.stream().filter(child->!child.getId().equals(c.getId())).collect(Collectors.toList());
            c.setChildren(children);
        }
        return commentList;
    }

    /**
     * 查询评论个数
     */
    public Integer selectCount(Integer fid, String module) {
        return commentMapper.selectCount(fid,module);
    }

    /**
     * 分页查询
     */
    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }


}
