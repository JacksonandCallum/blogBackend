package com.lvchenglong.blogbackend.controller;

import com.github.pagehelper.PageInfo;
import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.common.enums.RoleEnum;
import com.lvchenglong.blogbackend.common.param.LoginParam;
import com.lvchenglong.blogbackend.entity.User;
import com.lvchenglong.blogbackend.ip2region.ip;
import com.lvchenglong.blogbackend.service.UserService;
import com.lvchenglong.blogbackend.service.mailServiceImpl.CommonService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private CommonService commonService;


    /**
     * 权限码请求接口
     * @param emailJson
     * @return
     */
    @PostMapping("code/request")
    public Result getRequestPermissionCode(@RequestBody String emailJson) {
        return commonService.getRequestPermissionCode(emailJson);
    }

    /**
     * 邮箱验证码接口
     * @param loginParam
     * @return
     */
    @PostMapping("code/email")
    public Result sendEmailCode(@RequestBody LoginParam loginParam) {
        return commonService.sendEmailCode(loginParam);
    }

    /**
     * 登录
     * @param loginParam (邮箱和密码)
     * @return
     */
    @ip
    @PostMapping("login")
    public Result login(@RequestBody LoginParam loginParam) {
        if(RoleEnum.USER.name().equals(loginParam.getRole())){
            return userService.login(loginParam);
        }
        return Result.success();
    }

    /**
     * 注册
     * @param loginParam (邮箱、密码、验证码)
     * @return
     */
    @ip
    @PostMapping("register")
    public Result register(@RequestBody LoginParam loginParam) {
        return userService.register(loginParam);
    }

    /**
     * 找回密码
     * @param loginParam (邮箱、密码、验证码)
     * @return
     */
    @PostMapping("findPassword")
    public Result findPassword(@RequestBody LoginParam loginParam) {
        return userService.findPassword(loginParam);
    }

    /**
     * 修改邮箱
     * @param loginParam (邮箱、密码、验证码)
     * @return
     */
    @PostMapping("/updateEmail")
    public Result updateEmail(@RequestBody LoginParam loginParam){
        return userService.updateEmail(loginParam);

    }


    /**
     * 新增
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user){
        userService.add(user);
        return Result.success();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        userService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){  // json数组[1,2,3]
        userService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     * @param user
     * @return
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody User user){
        userService.updateById(user);
        return Result.success();
    }


    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        User user = userService.selectById(id);
        return Result.success(user);
    }

    /**
     * 查询所有
     * @param user
     * @return
     */
    @GetMapping("/selectAll")
    public Result selectAll(User user){
        List<User> list = userService.selectAll(user);
        return Result.success(list);
    }

    /**
     * 分页查询
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/selectPage")
    public Result selectPage(User user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<User> page = userService.selectPage(user,pageNum,pageSize);
        return Result.success(page);
    }
}
