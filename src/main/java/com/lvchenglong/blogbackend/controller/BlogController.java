package com.lvchenglong.blogbackend.controller;

import com.github.pagehelper.PageInfo;
import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.entity.Blog;
import com.lvchenglong.blogbackend.service.BlogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 博客信息前端操作接口
 **/
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Resource
    private BlogService blogService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Blog blog) {
        blogService.add(blog);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        blogService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        blogService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Blog blog) {
        blogService.updateById(blog);
        return Result.success();
    }

    /**
     * 更新浏览量
     */
    @PutMapping("/updateReadCount/{blogId}")
    public Result updateReadCount(@PathVariable Integer blogId) {
        blogService.updateReadCount(blogId);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Blog blog = blogService.selectById(id);
        return Result.success(blog);
    }

    /**
     * 根据CategoryID查询
     */
    @GetMapping("/selectCategoryID/{CategoryID}")
    public Result selectCategoryID(@PathVariable Integer CategoryID) {
        List<Blog> blogList = blogService.selectCategoryID(CategoryID);
        return Result.success(blogList);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Blog blog) {
        List<Blog> list = blogService.selectAll(blog);
        return Result.success(list);
    }

    @GetMapping("/selectPageForAll")
    public Result selectPageForAll(Blog blog,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Blog> page = blogService.selectPageForAll(blog, pageNum, pageSize);
        return Result.success(page);
    }


    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Blog> page = blogService.selectPage(blog, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 查询用户自己的博客
     */
    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Integer userId){
        List<Blog> blogList = blogService.selectByUserId(userId);
        return Result.success(blogList);
    }

    /**
     * 查询用户自己点赞的博客
     */
    @GetMapping("/selectLikesByUserId/{userId}")
    public Result selectLikesByUserId(@PathVariable Integer userId){
        List<Blog> blogList = blogService.selectLikesByUserId(userId);
        return Result.success(blogList);
    }

    /**
     * 查询用户自己收藏的博客
     */
    @GetMapping("/selectCollectByUserId/{userId}")
    public Result selectCollectByUserId(@PathVariable Integer userId){
        List<Blog> blogList = blogService.selectCollectByUserId(userId);
        return Result.success(blogList);
    }

    /**
     * 查询用户自己评论的博客
     */
    @GetMapping("/selectCommentByUserId/{userId}")
    public Result selectCommentByUserId(@PathVariable Integer userId){
        List<Blog> blogList = blogService.selectCommentByUserId(userId);
        return Result.success(blogList);
    }

    /**
     * 用户在首页搜索框搜索博客
     * @param blog
     * @return
     */
    @GetMapping("/selectArticle")
    public Result selectArticle(Blog blog){
        List<Blog> list = blogService.selectArticle(blog);
        return Result.success(list);
    }

    /**
     * 博客榜单
     */
    @GetMapping("/selectTop")
    public Result selectTop() {
        List<Blog> list = blogService.selectTop();
        return Result.success(list);
    }

    /**
     * 博客推荐
     */
    //    @GetMapping("/selectRecommend/{blogId}")
    //    public Result selectRecommend(@PathVariable Integer blogId) {
    //        Set<Blog> blogSet = blogService.selectRecommend(blogId);
    //        return Result.success(blogSet);
    //    }
    @GetMapping("/selectRecommend/{blogId}")
    public Result selectRecommend(@PathVariable Integer blogId) {
        List<Blog> blogList = blogService.selectRecommend(blogId);
        return Result.success(blogList);
    }
}
