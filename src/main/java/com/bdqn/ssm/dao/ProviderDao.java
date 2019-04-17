package com.bdqn.ssm.dao;

import com.bdqn.ssm.entity.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderDao {
    //查询所有方法
    public List<Provider> getProviderList(@Param("proCode")String proCode, @Param("proName")String proName, @Param("pageNo")int currentPageNo, @Param("pageSize") int pageSize);
    //总页数
    public int getProviderCount(@Param("proCode")String proCode, @Param("proName")String proName);
    //添加
    public int addProvider(Provider provider);
}
