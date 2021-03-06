package com.bdqn.blog.server;

import com.bdqn.blog.pojo.BlogComment;
import com.bdqn.blog.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService{

	int modifyUser(User user);
	int removeUser(Integer id);
	//登录
	User getLoginUser( String name,  String pwd)throws Exception;
	//用户注册
	int doRegister(User user);
	//用户名查重
	User checkUserByName(String userName);

	/**
	 * 根据用户id获得用户下面所有评论 查询后的分页
	 * @param uid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<BlogComment> selectAllBlogcomment(int uid,int pageNo,int pageSize);

}