package com.lvchenglong.blogbackend.controller;

import com.github.pagehelper.PageInfo;
import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.entity.Comment;
import com.lvchenglong.blogbackend.ip2region.ip;
import com.lvchenglong.blogbackend.service.CommentService;
import com.lvchenglong.blogbackend.utils.AddressUtil;
import com.lvchenglong.blogbackend.utils.HttpContextUtil;
import com.lvchenglong.blogbackend.utils.IPUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端操作接口
 **/
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    /**
     * 新增
     */
    @ip
    @PostMapping("/add")
    public Result add(@RequestBody Comment comment) {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        String cityInfo = AddressUtil.getCityInfo(ip);
        Map<String, String> response = new HashMap<>();
        response.put("ip", ip);
        response.put("address", cityInfo);
        comment.setIp(String.valueOf(response));  // 将IP地址设置到评论对象中
        commentService.add(comment);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        commentService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        commentService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Comment comment) {
        commentService.updateById(comment);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Comment comment = commentService.selectById(id);
        return Result.success(comment);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Comment comment) {
        List<Comment> list = commentService.selectAll(comment);
        return Result.success(list);
    }

    /**
     * 查询评论信息
     */
    @GetMapping("/selectForUser")
    public Result selectForUser(Comment comment) {
        List<Comment> list = commentService.selectForUser(comment);
        return Result.success(list);
    }

    /**
     * 查询评论个数
     */
    @GetMapping("/selectCount")
    public Result selectCount(@RequestParam Integer fid,@RequestParam String module) {
        Integer count = commentService.selectCount(fid,module);
        return Result.success(count);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Comment comment,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Comment> page = commentService.selectPage(comment, pageNum, pageSize);
        return Result.success(page);
    }
}
