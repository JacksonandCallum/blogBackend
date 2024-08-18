package com.lvchenglong.blogbackend.service;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.blogbackend.common.Constants;
import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.common.constant.RedisConstant;
import com.lvchenglong.blogbackend.common.enums.ResultCodeEnum;
import com.lvchenglong.blogbackend.common.enums.RoleEnum;
import com.lvchenglong.blogbackend.common.param.LoginParam;
import com.lvchenglong.blogbackend.entity.Account;
import com.lvchenglong.blogbackend.entity.User;
import com.lvchenglong.blogbackend.exception.CustomException;
import com.lvchenglong.blogbackend.mapper.UserMapper;
import com.lvchenglong.blogbackend.utils.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void add(User user) {
        // 判断用户账号是否重复
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(dbUser != null){
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }

        // 判断密码是否为空
        if(ObjectUtil.isEmpty(user.getPassword())){
            // 若没有，则默认为123
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }

        // 判断用户名是否为空
        if(ObjectUtil.isEmpty(user.getName())){
            // 若没有用户名，则默认账号为用户名
            user.setName(user.getUsername());
        }

        // 默认用户角色
        user.setRole(RoleEnum.USER.name());

        userMapper.insert(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public void updateById(User user) {
        userMapper.updateById(user);
    }


    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    public Account loginAdmin(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if(ObjectUtil.isNull(dbUser)){
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if(! account.getPassword().equals(dbUser.getPassword())){
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData,dbUser.getPassword());
        dbUser.setToken(token);

        return dbUser;
    }

    public void registerAdmin(Account account) {
        User user = new User();
        BeanUtils.copyProperties(account,user);
        this.add(user);
    }

    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (! account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }

    public Result login(LoginParam loginParam) {
        if (loginParam == null) return Result.error(ResultCodeEnum.PARAM_ILLEGAL);

        // 获取参数
        String email = loginParam.getEmail();
        String password = loginParam.getPassword();
        String username = loginParam.getUsername();

        if (StringUtils.isAnyBlank(password) || (StringUtils.isBlank(email) && StringUtils.isBlank(username))) {
            // 如果密码为空，或者用户名和邮箱都为空，则返回参数非法错误
            return Result.error(ResultCodeEnum.PARAM_ILLEGAL);
        }else if (StringUtils.isBlank(email) && StringUtils.isBlank(username)) {
            // 如果用户名和邮箱都为空，则返回参数非法错误
            return Result.error(ResultCodeEnum.PARAM_ILLEGAL);
        }else if (!StringUtil.checkEmail(email) && !StringUtils.isBlank(email)) {
            // 邮箱格式校验
            return Result.error(ResultCodeEnum.EMAIL_ERROR);
        }else if (!StringUtil.checkPassword(password)) {
            // 密码格式
            return Result.error(ResultCodeEnum.PASSWORD_ERROR);
        }

        User user;
        if (!StringUtils.isBlank(email)) {
            user = userMapper.selectUserByEmail(email);
        } else {
            user = userMapper.selectUserByUsername(username);
        }
        if (user == null) {
            // 用户不存在
            return Result.error(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }

        // 获取加密盐
        String salt = user.getSalt();
        String encryptedPassword = DigestUtils.md5Hex(password + salt);
        Integer userId;
        if (!StringUtils.isBlank(email)) {
            userId = userMapper.selectUserIdByEmailAndPassword(email, encryptedPassword);
            // 生成token
            String tokenData = userId + "-" + RoleEnum.USER.name();
            String token = TokenUtils.createToken(tokenData,encryptedPassword);
            user = userMapper.selectUserByEmailForLogin(email);
            user.setToken(token);
            // 获取用户ip
            HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
            String ip = IPUtil.getIpAddr(request);
            String cityInfo = AddressUtil.getCityInfo(ip);
            Map<String,String> response = new HashMap<>();
            response.put("ip",ip);
            response.put("address",cityInfo);
            user.setIp(String.valueOf(response));
            // 更新用户的IP地址信息
            user.setIp(String.valueOf(response));
            userMapper.updateUserIp(userId, String.valueOf(response));

        } else {
            userId = userMapper.selectUserIdByUsernameAndPassword(username, encryptedPassword);
            System.out.println("userId值"+userId);
            // 生成token
            String tokenData = userId + "-" + RoleEnum.USER.name();
            System.out.println("tokenData值"+tokenData);
            String token = TokenUtils.createToken(tokenData,encryptedPassword);
            System.out.println("token值"+token);
            user = userMapper.selectByUsername(username);
            user.setToken(token);
            // 获取用户ip
            HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
            String ip = IPUtil.getIpAddr(request);
            String cityInfo = AddressUtil.getCityInfo(ip);
            Map<String,String> response = new HashMap<>();
            response.put("ip",ip);
            response.put("address",cityInfo);
            user.setIp(String.valueOf(response));
            // 更新用户的IP地址信息
            user.setIp(String.valueOf(response));
            userMapper.updateUserIp(userId, String.valueOf(response));
        }
        return userId == null ? (Result.error(ResultCodeEnum.PASSWORD_ERROR)) : Result.success(user);
    }


    public Result register(LoginParam loginParam) {
        if (loginParam == null) return Result.error(ResultCodeEnum.PARAM_ILLEGAL);

        // 获取参数
        String email = loginParam.getEmail();
        String password = loginParam.getPassword();
        String code = loginParam.getCode();
        String username = loginParam.getUsername();
        String role = loginParam.getRole();

        if (StringUtils.isAnyBlank(email, password,username, code)) {
            // 非空
            return Result.error(ResultCodeEnum.PARAM_ILLEGAL);
        }else if (!StringUtil.checkEmail(email)) {
            // 邮箱格式校验
            return Result.error(ResultCodeEnum.EMAIL_ERROR);
        }else if (!StringUtil.checkPassword(password) || code.length() != 6) {
            // 密码格式和验证码长度校验
            return Result.error(ResultCodeEnum.PARAM_ILLEGAL);
        }

        Integer IdByEmail = userMapper.selectIdByEmail(email);

        User dbUser = userMapper.selectByUsername(username);

        // 查询用户，是否存在
        if (IdByEmail != null) {
            return Result.error(ResultCodeEnum.EMAIL_ALREADY_EXIST);
        } else if (dbUser != null) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }

        // 获取正确的验证码
        String rightCode = redisTemplate.opsForValue().get(RedisConstant.EMAIL + email);
        if (!code.equals(rightCode)) {
            // 验证码比对
            return Result.error(ResultCodeEnum.CODE_ERROR);
        }

        // 删除验证码
        redisTemplate.delete(RedisConstant.EMAIL + email);

        // 注册用户
        User user = new User();
        // 获取加密盐
        String salt = StringUtil.randomEncryptedSalt();
        // 邮箱
        user.setEmail(email);
        // 密码加密（原明文密码 + 随机加密盐） md5加密
        user.setPassword(DigestUtils.md5Hex(password + salt));
        // 加密盐
        user.setSalt(salt);
        System.out.println("salt值："+salt);
        // 用户名（账号）
        user.setUsername(username);
        // 姓名
        user.setName(username);
        // 角色
        user.setRole(role);
        System.out.println("role值："+role);
        // 获取用户ip
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        String cityInfo = AddressUtil.getCityInfo(ip);
        Map<String,String> response = new HashMap<>();
        response.put("ip",ip);
        response.put("address",cityInfo);
        user.setIp(String.valueOf(response));
        // 插入数据
        return userMapper.insertForRegister(user) == 0 ? Result.error(ResultCodeEnum.SYSTEM_ERROR) : Result.success();
    }

    public Result findPassword(LoginParam loginParam) {
        if (loginParam == null) return Result.error(ResultCodeEnum.PARAM_ILLEGAL);

        // 获取参数
        String email = loginParam.getEmail();
        String password = loginParam.getPassword();
        String code = loginParam.getCode();

        if (StringUtils.isAnyBlank(email, password, code)) {
            // 非空
            return Result.error(ResultCodeEnum.PARAM_ILLEGAL);
        }else if (!StringUtil.checkEmail(email)) {
            // 邮箱格式校验
            return Result.error(ResultCodeEnum.EMAIL_ERROR);
        }else if (!StringUtil.checkPassword(password) || code.length() != 6) {
            // 密码格式和验证码长度校验
            return Result.error(ResultCodeEnum.PARAM_ILLEGAL);
        }

        User user = userMapper.selectIdAndSaltByEmail(email);
        if (user == null) {
            return Result.error(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }

        // 获取正确的验证码
        String rightCode = redisTemplate.opsForValue().get(RedisConstant.EMAIL + email);
        if (!code.equals(rightCode)) {
            // 验证码比对
            return Result.error(ResultCodeEnum.CODE_ERROR);
        }

        // 删除验证码
        redisTemplate.delete(RedisConstant.EMAIL + email);

        // 修改密码
        User user1 = new User();
        user1.setId(user.getId());
        user1.setPassword(DigestUtils.md5Hex(password + user.getSalt()));

        // 修改
        return userMapper.updateUser(user1) > 0 ? Result.success() : Result.error(ResultCodeEnum.SYSTEM_ERROR) ;
    }

    public Result updateEmail(LoginParam loginParam) {
        if (loginParam == null) return Result.error(ResultCodeEnum.PARAM_ILLEGAL);

        // 获取参数
        String email = loginParam.getEmail();
        String password = loginParam.getPassword();
        String code = loginParam.getCode();

        if (StringUtils.isAnyBlank(email, password, code)) {
            // 非空
            return Result.error(ResultCodeEnum.PARAM_ILLEGAL);
        }else if (!StringUtil.checkEmail(email)) {
            // 邮箱格式校验
            return Result.error(ResultCodeEnum.EMAIL_ERROR);
        }else if (!StringUtil.checkPassword(password) || code.length() != 6) {
            // 密码格式和验证码长度校验
            return Result.error(ResultCodeEnum.PARAM_ILLEGAL);
        }

        // 检查新的邮箱地址是否已被其他用户使用
        User isExisted = userMapper.selectUserByEmailIsExist(email);
        if (isExisted != null) {
            return Result.error(ResultCodeEnum.EMAIL_ALREADY_EXISTS);
        }

        User user = userMapper.selectIdAndSaltByEmail(email);
        if (user == null) {
            return Result.error(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }

        // 获取正确的验证码
        String rightCode = redisTemplate.opsForValue().get(RedisConstant.EMAIL + email);
        if (!code.equals(rightCode)) {
            // 验证码比对
            return Result.error(ResultCodeEnum.CODE_ERROR);
        }

        // 验证密码是否正确
        String encryptedPassword = DigestUtils.md5Hex(password + user.getSalt());
        if (!encryptedPassword.equals(user.getPassword())) {
            // 密码不匹配，返回错误
            return Result.error(ResultCodeEnum.PASSWORD_ERROR);
        }

        // 删除验证码
        redisTemplate.delete(RedisConstant.EMAIL + email);

        // 修改邮箱
        User user1 = new User();
        user1.setId(user.getId());
        user1.setEmail(email);

        // 修改
        return userMapper.updateEmail(user1) > 0 ? Result.success() : Result.error(ResultCodeEnum.SYSTEM_ERROR) ;
    }

}
