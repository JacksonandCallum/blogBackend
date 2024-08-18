package com.lvchenglong.blogbackend.mapper;

import com.lvchenglong.blogbackend.entity.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectMapper {
    void insert(Collect collect);

    Collect selectUserCollect(Collect collect);

    void deleteById(Integer id);

    int selectByFidAndModule(@Param("fid") Integer fid,@Param("module") String module);

    List<Integer> selectFidsByUserIdAndModule(@Param("userId") Integer userId,@Param("module") String module);

    List<Integer> selectUserIdsByFidAndModule(@Param("fid") Integer fid,@Param("module") String module);
}
