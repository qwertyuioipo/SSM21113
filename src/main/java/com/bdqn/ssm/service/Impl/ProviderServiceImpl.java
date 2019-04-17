package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.ProviderDao;
import com.bdqn.ssm.entity.Provider;
import com.bdqn.ssm.service.ProviderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {
    @Resource
    private ProviderDao pd;
    @Override
    public List<Provider> getProviderList(String proCode, String proName, int currentPageNo, int pageSize) {
        return pd.getProviderList(proCode,proName,currentPageNo,pageSize);
    }

    @Override
    public int getProviderCount(String proCode,String proName) {
        return pd.getProviderCount(proCode,proName);
    }

    @Override
    public int addProvider(Provider provider) {
        return pd.addProvider(provider);
    }
}
