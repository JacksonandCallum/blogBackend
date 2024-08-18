package com.lvchenglong.blogbackend.mapper;

import com.lvchenglong.blogbackend.entity.Blog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作blog相关数据接口
 */
public interface BlogMapper {
    /**
     * 新增
     */
    int insert(Blog blog);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Blog blog);

    /**
     * 根据ID查询
     */
    Blog selectById(Integer id);

    /**
     * 根据CategoryID查询
     */
    List<Blog> selectCategoryID(Integer categoryID);

    /**
     * 查询所有
     */
    List<Blog> selectAll(Blog blog);

    @Select("select * from blog where user_id = #{userId}")
    List<Blog> selectUserBlog(Integer userId);


    List<Blog> selectArticle(@Param("searchBlogs") Blog blog);
    @Update("update blog set read_count = read_count + 1 where id = #{blogId}")
    void updateReadCount(Integer blogId);

    List<Blog> selectByUserId(Integer userId);

    /**
     * 查询用户自己点赞的博客
     */
    List<Blog> selectLikesByUserId(Integer userId);

    /**
     * 查询用户自己收藏的博客
     */
    List<Blog> selectCollectByUserId(Integer userId);

    /**
     * 查询用户自己评论的博客
     */
    List<Blog> selectCommentByUserId(Integer userId);

}
