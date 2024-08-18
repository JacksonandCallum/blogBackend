package com.lvchenglong.blogbackend.mapper;

import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    void insert(User user);
    int insertForRegister(User user);
    void insertForLogin(User user);
    User selectByUsername(String username);

    void deleteById(Integer id);

    void updateById(User user);

    User selectById(Integer id);

    List<User> selectAll(User user);

    @Select("select salt from user where username = #{username}  LIMIT 1")
    User selectUserByUsername(String username);

    @Select("select salt from user where email = #{email}  LIMIT 1")
    User selectUserByEmail(String email);

    @Select("select id from user where username = #{username} and password = #{password} LIMIT 1")
    Integer selectUserIdByUsernameAndPassword(@Param("username")String username,@Param("password") String encryptedPassword);
    @Select("select id from user where email = #{email} and password = #{password} LIMIT 1")
    Integer selectUserIdByEmailAndPassword(@Param("email") String email,@Param("password") String encryptedPassword);

    @Select("select id from user where email = #{email} LIMIT 1")
    Integer selectIdByEmail(String email);
    @Select("select id, salt from user where email = #{email} LIMIT 1")
    User selectIdAndSaltByEmail(String email);
    @Select("select * from user where email = #{email}  LIMIT 1")
    User selectUserByEmailForLogin(String email);

    int updateUser(User user1);

    int updateEmail(User user1);
    @Select("select * from user where email = #{email}  LIMIT 1")
    User selectUserByEmailIsExist(String email);

    void updateUserIp(@Param("userId") Integer userId, @Param("ip") String ip);
}
