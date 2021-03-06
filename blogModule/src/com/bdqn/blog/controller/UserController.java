package com.bdqn.blog.controller;


import com.bdqn.blog.pojo.User;
import com.bdqn.blog.server.UserService;
import com.bdqn.blog.utils.MD5Tool;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *Created by IntelliJ IDEA.
 User: fujiawei
 Date: 2018/1/6
 Time: 17:19
 */
@Controller
@RequestMapping(value="/user")
public class UserController {
    @Resource
    private UserService userServer;

    @RequestMapping(value="/loginCheck",method= RequestMethod.POST)
    public String loginCheck(@RequestParam String name, @RequestParam String pwd, HttpServletRequest request){
        User user  = null;
        try {
            //密码加密
            String newPwd = MD5Tool.MD5(pwd);
            user = userServer.getLoginUser(name,newPwd);
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(user ==null){
            request.setAttribute("error","登录失败,密码或用户名错误");
            return "login";
        }

        return "redirect:/blog/selectBlog";


    }

    /**
     * @author kanxueke
     *
     */
    @RequestMapping(value = "/goRegister", method=RequestMethod.GET)
    public String goRegister(){
        return "register";
    }

    @RequestMapping(value = "/goLogin", method=RequestMethod.GET)
    public String goLogin(){
        return "login";
    }
    /**
     * @author kanxueke
     */
    @RequestMapping(value = "/doRegister", method=RequestMethod.POST)
    public String doRegister(User user,Model model){
        String userPassword = user.getUserPassword();
        String newUserPassword = MD5Tool.MD5(userPassword);
        user.setUserPassword(newUserPassword);
        //通过用户名查重
       /* if(userServer.checkUserByName(user.getUserName())!=null){
            model.addAttribute("RegError","用户名重复");
            return "register";
        }*/
        int count = userServer.doRegister(user);
        user.setUserPassword(userPassword);
        if(count>0){
            model.addAttribute("user",user);
            return"login";
        }
        return "fail";
    }
    @RequestMapping(value = "checkUserName", method=RequestMethod.POST)
    @ResponseBody
    public String checkUserName(@RequestParam(value = "userName") String userName){
        if(userServer.checkUserByName(userName)!=null){

            return "exist";
        }
        return "noExist";
    }

    /**
     * 退出功能
     * @author linbingyang
     * @param request
     * @return
     */
    @RequestMapping(value = "/exit")
    public String exit(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return "redirect:/blog/selectBlog";
    }

}
