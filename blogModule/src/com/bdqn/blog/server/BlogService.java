package com.bdqn.blog.server;

import com.bdqn.blog.pojo.Blog;
import com.bdqn.blog.pojo.BlogComment;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface BlogService{

	//增加博客
	public int addBlog(Blog blog);
	//修改博客
	public int modifyBlog(Blog blog);
	//删除博客
	public int removeBlog(Integer id);

	//根据条件查询博客   其中包括用户的id，title     return    查询后的分页
	List<Blog> selectAllBlog( Integer uid, String title, int pageNo, int pageSize);

	public List<BlogComment> selectAllBlogcomment(Integer bid,int pageNo,int pageSize);

	public int totalCount(Integer uid, String title);


	Blog selectByBid( Integer Bid);

}