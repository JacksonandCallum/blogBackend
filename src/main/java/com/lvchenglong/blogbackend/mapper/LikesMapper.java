package com.lvchenglong.blogbackend.mapper;

import com.lvchenglong.blogbackend.entity.Likes;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LikesMapper {
    void insert(Likes likes);

    Likes selectUserLikes(Likes likes);

    void deleteById(Integer id);

    int selectByFidAndModule(@Param("fid") Integer fid,@Param("module") String module);

    List<Integer> selectFidsByUserIdAndModule(@Param("userId")Integer userId,@Param("module") String module);

    List<Integer> selectUserIdsByFidAndModule(@Param("fid") Integer fid,@Param("module") String module);
}
