package com.bdqn.ssm.dao;

import com.bdqn.ssm.entity.Role;
import com.bdqn.ssm.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserDao {
    //登录
    public User doLogin(@Param("userCode") String userCode, @Param("userPassword") String userPassword);
    //查询账单总数
    public int getUserCount(@Param("userName") String userName,@Param("userRole") int userRole);
    //根据名称和是否付款查询账单
    public List<User> getUserList(@Param("userName")String userName, @Param("userRole")int userRole, @Param("pageNo")int currentPageNo,@Param("pageSize") int pageSize);
    public List<Role> getRoleList();
    //添加用户
    public int addUser(User user);
    //删除用户
    public int delUser(@Param("id")int id);
    //根据id查询所有
    public User getUserByid(@Param("id")int id);
    //修改
    public int updateUser(User user);
    public User getLoginUser(@Param("userCode")String userCode);
}
