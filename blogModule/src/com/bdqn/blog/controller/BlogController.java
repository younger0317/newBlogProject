package com.bdqn.blog.controller;

import com.bdqn.blog.pojo.Blog;
import com.bdqn.blog.pojo.BlogGenre;
import com.bdqn.blog.pojo.User;
import com.bdqn.blog.server.BlogGenreServer;
import com.bdqn.blog.server.BlogService;
import com.bdqn.blog.utils.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


/**
 *Created by IntelliJ IDEA.
 User: caiwenfeng
 Date: 2018/1/7

 */
@Controller
@RequestMapping(value="/blog")
public class BlogController {

    @Resource
    @Autowired
    private BlogService blogService;

    @Resource
    @Autowired
    private BlogGenreServer blogGenreServer;


    private PageSupport pageSupport = new PageSupport();

    //增加博客
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBolg(@RequestParam Integer genreId, HttpServletRequest request, @RequestParam String title, @RequestParam String contentPath) {
        HttpSession Session = request.getSession();
        User user = (User) Session.getAttribute("user");
        Integer uid=null;
        if(user!=null){
             uid = user.getUid();
        }

        if (title != null && uid != null && contentPath != null && genreId != null) {
            Blog blog = new Blog();
            blog.setTitle(title);
            blog.setContentPath(contentPath);
            blog.setCreateTime(new Date());
            blog.setUid(uid);
            blog.setGenreId(genreId);
            blog.setReadAmout(0);
            blogService.addBlog(blog);
            System.out.println("ggggg");
        }


        return "redirect:selectUserBlog";
    }

    /**
     * 跳到增加页面
     *
     * @return
     */
    @RequestMapping(value = "/addPage", method = RequestMethod.GET)
    public String addBlogPage(Model model) {
        List<BlogGenre> BlogGenres = blogGenreServer.getBlogGenreAll();
        model.addAttribute("BlogGenres", BlogGenres);
        return "blog/blogBizAdd";
    }

    //修改博客
    @RequestMapping(value = "/modifyBlog",method = RequestMethod.POST)
    public String modifyBlog(@ModelAttribute(value = "blog") Blog blog) {
        System.out.println("blog是"+blog.getBid());
        if (blog != null&&blog.getBid()!=null) {
            blogService.modifyBlog(blog);
            return "redirect:selectUserBlog";
        }
        return "test";
    }
    /**
     * 跳去修改页面
     */
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String updatePage(@RequestParam(value = "bid", required = false) Integer bid,Model model){
        System.out.println("bid是"+bid);
        if(bid !=null){
         Blog blog=  blogService.selectByBid(bid);
            List<BlogGenre> BlogGenres = blogGenreServer.getBlogGenreAll();
            model.addAttribute("BlogGenres", BlogGenres);
         model.addAttribute("blog",blog);
       }

        return "blog/blogBizModify";
    }
    //删除博客

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping("/removeBlog")
    @ResponseBody
    public String removeBlog(@RequestParam("bid") Integer id) {
        if (id != null) {
            Blog blog = blogService.selectByBid(id);
            if (blog != null) {
                blogService.removeBlog(id);
                return "true";
            }
        }
        return "false";
    }

    /**
     * 首页博客
     * <p>
     *   like  title 标题   ，pageNo
     */
    @RequestMapping("/selectBlog")
    public String selectBlog(Model Model, @RequestParam(value = "pageNo", required = false) Integer pageNo,
                             @RequestParam(value = "title", required = false) String title) {
        List<BlogGenre> BlogGenres = blogGenreServer.getBlogGenreAll();

        if (pageNo != null) {
            pageSupport.setCurrentPageNo(pageNo);
        }
        System.out.println("uid" +"aaa" + ",title" + title + "pageNo" + (pageSupport.getCurrentPageNo() - 1) + "pageSize" + pageSupport.getPageSize());
        List<Blog> blogs = blogService.selectAllBlog(null, title, (pageSupport.getCurrentPageNo() - 1)*pageSupport.getPageSize(), pageSupport.getPageSize());
        System.out.println(blogs+"ggggaa");
        pageSupport.setTotalCount(blogService.totalCount(null, title));

        pageSupport.setTotalPageCountByRs();

        Model.addAttribute("pages", pageSupport);
        Model.addAttribute("blogs", blogs);
        Model.addAttribute("BlogGenres", BlogGenres);
        return "blogIndex";
    }


    /**
     * 管理博客
     */
    @RequestMapping("/selectUserBlog")
    public String selectUserBlog(Model Model, @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                 HttpServletRequest request,
                                 @RequestParam(value = "title", required = false) String title) {

        HttpSession Session = request.getSession();
        User user = (User) Session.getAttribute("user");
        if(user==null){
            return "test";
        }
        Integer uid = user.getUid();
        System.out.println("uid是"+uid);

        if (pageNo != null) {
            pageSupport.setCurrentPageNo(pageNo);
        }
        System.out.println("uid" + uid + ",title" + title + "pageNo" + (pageSupport.getCurrentPageNo() - 1) + "pageSize" + pageSupport.getPageSize());
        List<Blog> blogs = blogService.selectAllBlog(uid, title, (pageSupport.getCurrentPageNo() - 1)*pageSupport.getPageSize(), pageSupport.getPageSize());

        System.out.println("blogs是"+blogs);
        pageSupport.setTotalCount(blogService.totalCount(uid, title));

        pageSupport.setTotalPageCountByRs();

        Model.addAttribute("pageSupport", pageSupport);
        Model.addAttribute("blogs", blogs);

        return "blog/blogBizList";
    }



    @RequestMapping("/selectBlogGenres")
    public String selectBlogGenres(Model Model){
    List<BlogGenre> BlogGenres = blogGenreServer.getBlogGenreAll();
    Model.addAttribute("BlogGenres", BlogGenres);
    return "blog/blogBizCategoryList";
    }

    @RequestMapping("/view")
    public String veiwBlog(Integer bid,Model model){
        Blog blogByBid = blogService.getBlogByBid(bid);
        model.addAttribute("blog",blogByBid);
        return "blog/blogText";
    }

}