package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.UserDao;
import com.bdqn.ssm.entity.Role;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao ud;


    @Override
    public User doLogin(String userCode, String userPassword) {
        return ud.doLogin(userCode,userPassword);
    }

    @Override
    public int getUserCount(String userName, int userRole) {
        return ud.getUserCount(userName,userRole);
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        return ud.getUserList(userName,userRole,currentPageNo,pageSize);
    }

    @Override
    public List<Role> getRoleList() {
        return ud.getRoleList();
    }

    @Override
    public int addUser(User user) {
        return ud.addUser(user);
    }

    @Override
    public int delUser(int id) {
        return ud.delUser(id);
    }

    @Override
    public User getUserByid(int id) {
        return ud.getUserByid(id);
    }

    @Override
    public int updateUser(User user) {
        return ud.updateUser(user);
    }

    @Override
    public User getLoginUser(String userCode) {
        return ud.getLoginUser(userCode);
    }
}
