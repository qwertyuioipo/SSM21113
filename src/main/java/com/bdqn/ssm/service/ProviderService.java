package com.bdqn.ssm.service;

import com.bdqn.ssm.entity.Provider;

import java.util.List;

public interface ProviderService {
    //查询所有方法
    public List<Provider> getProviderList(String proCode,String proName,int currentPageNo,int pageSize);
    //总页数
    public int getProviderCount(String proCode,String proName);
    //添加
    public int addProvider(Provider provider);
}
