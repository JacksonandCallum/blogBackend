package com.lvchenglong.blogbackend.service;

import com.lvchenglong.blogbackend.entity.Account;
import com.lvchenglong.blogbackend.entity.Likes;
import com.lvchenglong.blogbackend.mapper.LikesMapper;
import com.lvchenglong.blogbackend.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LikesService {

    @Resource
    LikesMapper likesMapper;

    public void set(Likes likes) {
        Account currentUser = TokenUtils.getCurrentUser();
        likes.setUserId(currentUser.getId());
        // 查询用户是否对文章进行点赞（若没点赞可进行点赞,若有点赞可取消）
        Likes dbLikes = likesMapper.selectUserLikes(likes);
        if(dbLikes == null){
            likesMapper.insert(likes);
        }else{
            likesMapper.deleteById(dbLikes.getId());
        }
    }

    /**
     * 查询当前用户是否对文章进行点赞
     * @param fid
     * @param module
     * @return
     */
    public Likes selectUserLikes(Integer fid,String module){
        Account currentUser = TokenUtils.getCurrentUser();
        Likes likes = new Likes();
        likes.setUserId(currentUser.getId());
        likes.setFid(fid);
        likes.setModule(module);
        return likesMapper.selectUserLikes(likes);
    }

    /**
     * 根据文章ID和模块查询点赞数
     *
     * @param fid    文章ID
     * @param module 模块
     * @return 点赞数
     */
    public int selectByFidAndModule(Integer fid, String module) {
        return likesMapper.selectByFidAndModule(fid,module);
    }

    /**
     * 根据用户ID和模块查询用户点赞的文章ID列表
     *
     * @param userId 用户ID
     * @param module 模块
     * @return 用户点赞的文章ID列表
     */
    public List<Integer> selectFidsByUserIdAndModule(Integer userId, String module) {
        return likesMapper.selectFidsByUserIdAndModule(userId, module);
    }

    /**
     * 根据文章ID和模块查询点赞该文章的用户ID列表
     *
     * @param fid    文章ID
     * @param module 模块
     * @return 点赞该文章的用户ID列表
     */
    public List<Integer> selectUserIdsByFidAndModule(Integer fid, String module) {
        return likesMapper.selectUserIdsByFidAndModule(fid, module);
    }
}
