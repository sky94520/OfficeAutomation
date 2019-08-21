package com.threeluoxuan.Service;

import com.threeluoxuan.DAO.User;
import org.activiti.engine.IdentityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    IdentityService identityService;
    /**
     * 登录
     *
     * @param user 用户名和密码
     * @return Result
     */
    public Boolean checkUserInfo(User user) {
        boolean f = identityService.checkPassword("100", "abc");

        System.out.println("0-------------------" + user);

//        try {
//            Long userId = userMapper.login(user);
//            if (userId == null) {
//                result.setMsg("用户名或密码错误");
//            } else {
//                result.setMsg("登录成功");
//                result.setSuccess(true);
//                user.setId(userId);
//                result.setDetail(user);
//            }
//        } catch (Exception e) {
//            result.setMsg(e.getMessage());
//            e.printStackTrace();
//        }
        return f;
    }
}
