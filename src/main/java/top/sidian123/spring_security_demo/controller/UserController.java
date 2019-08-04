package top.sidian123.spring_security_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sidian
 * @date 19-7-23 下午6:38
 */
@RestController
public class UserController {

    @Autowired


    @GetMapping("/user")
    public String getUser(){
        return "没有用户,啦啦啦啦";
    }

    @GetMapping("/admin")
    public String getAdmin(){
        return "我是管理员";
    }

    @GetMapping("/")
    @Secured("ROLE_USER")
    public String home(){
        return "首页";
    }

    @GetMapping("/other")
    public String other(){
        return "其他人";
    }
}
