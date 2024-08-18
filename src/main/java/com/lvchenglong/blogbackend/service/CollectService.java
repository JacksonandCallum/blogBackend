package com.lvchenglong.blogbackend.service;

import com.lvchenglong.blogbackend.entity.Account;
import com.lvchenglong.blogbackend.entity.Collect;
import com.lvchenglong.blogbackend.mapper.CollectMapper;
import com.lvchenglong.blogbackend.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CollectService {

    @Resource
    CollectMapper collectMapper;

    public void set(Collect collect) {
        Account currentUser = TokenUtils.getCurrentUser();
        collect.setUserId(currentUser.getId());
        // 查询用户是否对文章进行收藏（若没收藏可进行收藏,若有收藏可取消）
        Collect dbCollect = collectMapper.selectUserCollect(collect);
        if(dbCollect == null){
            collectMapper.insert(collect);
        }else{
            collectMapper.deleteById(dbCollect.getId());
        }
    }

    /**
     * 查询当前用户是否对文章进行收藏
     * @param fid
     * @param module
     * @return
     */
    public Collect selectUserCollect(Integer fid,String module){
        Account currentUser = TokenUtils.getCurrentUser();
        Collect collect = new Collect();
        collect.setUserId(currentUser.getId());
        collect.setFid(fid);
        collect.setModule(module);
        return collectMapper.selectUserCollect(collect);
    }

    /**
     * 根据文章ID和模块查询收藏数
     *
     * @param fid    文章ID
     * @param module 模块
     * @return 收藏数
     */
    public int selectByFidAndModule(Integer fid, String module) {
        return collectMapper.selectByFidAndModule(fid,module);
    }

    /**
     * 根据用户ID和模块查询用户收藏的文章ID列表
     *
     * @param userId 用户ID
     * @param module 模块
     * @return 用户收藏的文章ID列表
     */
    public List<Integer> selectFidsByUserIdAndModule(Integer userId, String module) {
        return collectMapper.selectFidsByUserIdAndModule(userId, module);
    }

    /**
     * 根据文章ID和模块查询收藏该文章的用户ID列表
     *
     * @param fid    文章ID
     * @param module 模块
     * @return 收藏该文章的用户ID列表
     */
    public List<Integer> selectUserIdsByFidAndModule(Integer fid, String module) {
        return collectMapper.selectUserIdsByFidAndModule(fid, module);
    }

}
