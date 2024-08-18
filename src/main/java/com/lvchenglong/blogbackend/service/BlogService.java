package com.lvchenglong.blogbackend.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.blogbackend.common.CollaborativeFiltering;
import com.lvchenglong.blogbackend.common.ItemSimilarityMatrix;
import com.lvchenglong.blogbackend.common.enums.LikesModuleEnum;
import com.lvchenglong.blogbackend.common.enums.RoleEnum;
import com.lvchenglong.blogbackend.entity.*;
import com.lvchenglong.blogbackend.mapper.BlogMapper;
import com.lvchenglong.blogbackend.utils.TokenUtils;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 博客信息业务处理
 **/
@Service
public class BlogService {
    @Resource
    private BlogMapper blogMapper;

    @Resource
    UserService userService;

    @Resource
    LikesService likesService;

    @Resource
    CollectService collectService;
    private static final Logger logger = LoggerFactory.getLogger(BlogService.class);

    /**
     * 新增
     */
    public void add(Blog blog) {
        // 发布博文日期为当前日期
        //blog.setDate(DateUtil.today());

        // 发布博文日期为当前日期，格式化为 "yyyy-MM-dd HH:mm:ss"
        String currentDate = DateUtil.now();
        blog.setDate(currentDate);

        // 获取当前的登陆用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            // 只有角色为用户才能显示发布人名称
            blog.setUserId(currentUser.getId());
        }
        blogMapper.insert(blog);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        blogMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            blogMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Blog blog) {
        blogMapper.updateById(blog);
    }

    /**
     * 根据ID查询
     */
    public Blog selectById(Integer id) {
        Blog blog = blogMapper.selectById(id);
        if (blog == null) {
            logger.warn("ID为 {} 的博客不存在", id);
            return null;
        }
        User user = userService.selectById(blog.getUserId());
        if (user == null) {
            return null;
        }
        // 作者（博主）的博客数量
        List<Blog> userBlogList = blogMapper.selectUserBlog(user.getId());
        user.setBlogCount(userBlogList.size());
        // 当前作者（博主）收到的点赞和收藏
        int userLikesCount = 0;
        int userCollectCount = 0;
        for (Blog b : userBlogList) {
            Integer fid = b.getId();
            int LCount = likesService.selectByFidAndModule(fid, LikesModuleEnum.BLOG.getValue());
            userLikesCount += LCount;
            int CCount = collectService.selectByFidAndModule(fid, LikesModuleEnum.BLOG.getValue());
            userCollectCount += CCount;
        }
        user.setLikesCount(userLikesCount);
        user.setCollectCount(userCollectCount);

        blog.setUser(user);  // 设置作者（博主）信息

        int likesCount = likesService.selectByFidAndModule(id, LikesModuleEnum.BLOG.getValue());  // 查询当前博客点赞数据
        blog.setLikesCount(likesCount);
        Likes userLikes = likesService.selectUserLikes(id, LikesModuleEnum.BLOG.getValue());
        blog.setUserLike(userLikes != null);  //返回true，则说明当前用户已点赞
        int collectCount = collectService.selectByFidAndModule(id, LikesModuleEnum.BLOG.getValue());  // 查询当前博客收藏数据
        blog.setCollectCount(collectCount);
        Collect userCollect = collectService.selectUserCollect(id, LikesModuleEnum.BLOG.getValue());
        blog.setUserCollect(userCollect != null);  //返回true，则说明当前用户已收藏

        return blog;
    }


    /**
     * 根据CategoryID查询
     */
    public List<Blog> selectCategoryID(Integer categoryID) {
        List<Blog> blogList = blogMapper.selectCategoryID(categoryID);
        for (Blog b : blogList) {
            int likeCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());  // 查询当前博客点赞数据
            b.setLikesCount(likeCount);
        }
        return blogList;
    }

    /**
     * 查询所有
     */
    public List<Blog> selectAll(Blog blog) {
        List<Blog> blogList = blogMapper.selectAll(blog);
        for (Blog b : blogList) {
            int likeCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());  // 查询当前博客点赞数据
            b.setLikesCount(likeCount);
        }
        return blogList;
    }

    public PageInfo<Blog> selectPageForAll(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Blog> list = blogMapper.selectAll(blog);
        for (Blog b : list) {
            int likeCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());  // 查询当前博客点赞数据
            b.setLikesCount(likeCount);
        }
        return PageInfo.of(list);
    }

    /**
     * 分页查询
     */
    public PageInfo<Blog> selectPage(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list = blogMapper.selectAll(blog);
        return PageInfo.of(list);
    }

    public List<Blog> selectTop() {
        List<Blog> blogList = this.selectAll(null);
        // 倒序
        blogList = blogList.stream().sorted((b1,b2)->b2.getReadCount().compareTo(b1.getReadCount()))
                .limit(10)
                .collect(Collectors.toList());
        return blogList;
    }

//    public Set<Blog> selectRecommend(Integer blogId) {
//        // 查询当前博客信息
//        Blog blog = this.selectById(blogId);
//        // 获取当前博客所有的tags
//        String tags = blog.getTags();
//        Set<Blog> blogSet = new HashSet<>();
//        if(ObjectUtil.isNotEmpty(tags)){
//            // tags不为空，则查询出所有的博客信息
//            List<Blog> blogList = this.selectAll(null);
//            JSONArray tagsArr = JSONUtil.parseArray(tags);
//            for (Object tag : tagsArr) {
//                // 筛选出包含当前博客标签的其他博客列表
//                Set<Blog> collect = blogList.stream().filter(b -> b.getTags().contains(tag.toString()) && !blogId.equals(b.getId())) // 去除了当前博客
//                        .collect(Collectors.toSet());
//                blogSet.addAll(collect);
//            }
//        }
//        // 返回的推荐博客限制为5条
//        blogSet = blogSet.stream().limit(5).collect(Collectors.toSet());
//        blogSet.forEach(b->{
//            int likeCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BLOG.getValue());  // 查询当前博客点赞数据
//            b.setLikesCount(likeCount);
//        });
//        return blogSet;
//    }


    /**
     * 基于用户的点赞和收藏行为进行协同过滤，为指定博客生成推荐列表
     */
    public List<Blog> selectRecommend(Integer blogId) {
        try {
            // 添加日志输出
            logger.info("开始为博客 {} 生成推荐列表", blogId);

            // 获取当前博客的点赞用户列表和收藏用户列表
            List<Integer> likedUserIds = likesService.selectUserIdsByFidAndModule(blogId, LikesModuleEnum.BLOG.getValue());
            List<Integer> collectedUserIds = collectService.selectUserIdsByFidAndModule(blogId, LikesModuleEnum.BLOG.getValue());

            // 合并点赞用户和收藏用户，去重得到所有有互动的用户
            Set<Integer> interactingUserIds = new HashSet<>();
            interactingUserIds.addAll(likedUserIds);
            interactingUserIds.addAll(collectedUserIds);
            System.out.println("Interacting User IDs: " + interactingUserIds);


            // 构建用户-博客矩阵，记录用户对其他博客的点赞和收藏情况
            Map<Integer, Map<Integer, Integer>> userBlogMatrix = new HashMap<>();
            for (Integer userId : interactingUserIds) {
                Map<Integer, Integer> blogInteractionMap = new HashMap<>();
                List<Integer> likedBlogIds = likesService.selectFidsByUserIdAndModule(userId, LikesModuleEnum.BLOG.getValue());
                System.out.println("likedUserId: " + userId);
                System.out.println("likedBlogIds: " + likedBlogIds);
                List<Integer> collectedBlogIds = collectService.selectFidsByUserIdAndModule(userId, LikesModuleEnum.BLOG.getValue());
                System.out.println("collectedUserId: " + userId);
                System.out.println("collectedBlogIds: " + collectedBlogIds);
                for (Integer likedBlogId : likedBlogIds) {
                    blogInteractionMap.put(likedBlogId, 1); // 表示点赞
                }
                for (Integer collectedBlogId : collectedBlogIds) {
                    blogInteractionMap.put(collectedBlogId, -1); // 表示收藏
                }
                userBlogMatrix.put(userId, blogInteractionMap);
            }

            // 构建协同过滤器实例
            CollaborativeFiltering cf = new CollaborativeFiltering(userBlogMatrix);

            // 为当前博客生成推荐列表
            Map<Integer, Double> recommendations = cf.generateRecommendations(blogId);
            System.out.println("为当前博客生成推荐列表: " + recommendations);

            // 获取推荐博客的ID列表
            List<Integer> recommendedBlogIds = new ArrayList<>(recommendations.keySet());
            System.out.println("获取推荐博客的ID列表: " + recommendedBlogIds);

            // 限制查出前五条推荐
            recommendedBlogIds = recommendedBlogIds.subList(0, Math.min(5, recommendedBlogIds.size()));

            // 查询推荐博客的详细信息
            List<Blog> recommendedBlogs = recommendedBlogIds.stream()
                    .map(this::selectById)
                    .collect(Collectors.toList());

            return recommendedBlogs;
        } catch (Exception e){
            logger.error("生成推荐列表时出现异常：{}", e.getMessage());
            // 可以返回空列表或者其他处理方式
            return Collections.emptyList();
        }

    }

    public List<Blog> selectArticle(Blog blog) {
        List<Blog> list = blogMapper.selectArticle(blog);
        return list;
    }

    public void updateReadCount(Integer blogId) {
        blogMapper.updateReadCount(blogId);
    }


    public List<Blog> selectByUserId(Integer userId) {
        List<Blog> list = blogMapper.selectByUserId(userId);
        for (Blog blog : list) {
            int likeCount = likesService.selectByFidAndModule(blog.getId(), LikesModuleEnum.BLOG.getValue());  // 查询当前博客点赞数据
            blog.setLikesCount(likeCount);
        }
        return list;
    }

    /**
     * 查询用户自己点赞的博客
     */
    public List<Blog> selectLikesByUserId(Integer userId) {
        List<Blog> list = blogMapper.selectLikesByUserId(userId);
        for (Blog blog : list) {
            int likeCount = likesService.selectByFidAndModule(blog.getId(), LikesModuleEnum.BLOG.getValue());  // 查询当前博客点赞数据
            blog.setLikesCount(likeCount);
        }
        return list;
    }

    /**
     * 查询用户自己收藏的博客
     */
    public List<Blog> selectCollectByUserId(Integer userId) {
        List<Blog> list = blogMapper.selectCollectByUserId(userId);
        for (Blog blog : list) {
            int likeCount = likesService.selectByFidAndModule(blog.getId(), LikesModuleEnum.BLOG.getValue());  // 查询当前博客点赞数据
            blog.setLikesCount(likeCount);
        }
        return list;
    }

    /**
     * 查询用户自己评论的博客
     */
    public List<Blog> selectCommentByUserId(Integer userId) {
        List<Blog> list = blogMapper.selectCommentByUserId(userId);
        for (Blog blog : list) {
            int likeCount = likesService.selectByFidAndModule(blog.getId(), LikesModuleEnum.BLOG.getValue());  // 查询当前博客点赞数据
            blog.setLikesCount(likeCount);
        }
        return list;
    }

}
