package com.bdqn.ssm.service;

import com.bdqn.ssm.entity.Role;
import com.bdqn.ssm.entity.User;

import java.util.List;

public interface UserService {
    //登录
    public User doLogin(String userCode, String userPassword);
    //查询账单总数
    public int getUserCount(String userName,int userRole);
    //根据名称和是否付款查询账单
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);
    public List<Role> getRoleList();
    //添加用户
    public int addUser(User user);
    //删除用户
    public int delUser(int id);
    //根据id查询所有
    public User getUserByid(int id);
    //修改
    public int updateUser(User user);
    public User getLoginUser(String userCode);
}
