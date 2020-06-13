package com.java110.generator.smo.impl;

import com.java110.generator.dao.ICommonServiceDao;
import com.java110.generator.smo.ICommonServiceSmo;
import com.java110.entity.mapping.CodeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wuxw on 2017/7/25.
 */
@Service("commonServiceSmoImpl")
public class CommonServiceSmoImpl implements ICommonServiceSmo {

    protected final static Logger logger = LoggerFactory.getLogger(CommonServiceSmoImpl.class);

    @Autowired
    ICommonServiceDao commonServiceDaoImpl;

    @Override
    public List<CodeMapping> getCodeMappingAll()  throws Exception{
        return commonServiceDaoImpl.getCodeMappingAll();
    }

    @Override
    public List<CodeMapping> getCodeMappingByDomain(CodeMapping codeMapping)  throws Exception{
        return commonServiceDaoImpl.getCodeMappingByDomain(codeMapping);
    }

    @Override
    public List<CodeMapping> getCodeMappingByHCode(CodeMapping codeMapping)  throws Exception{
        return commonServiceDaoImpl.getCodeMappingByHCode(codeMapping);
    }

    @Override
    public List<CodeMapping> getCodeMappingByPCode(CodeMapping codeMapping)  throws Exception{
        return commonServiceDaoImpl.getCodeMappingByPCode(codeMapping);
    }

    @Override
    public List<CodeMapping> getCodeMappingByDomainAndHCode(CodeMapping codeMapping)  throws Exception{
        return commonServiceDaoImpl.getCodeMappingByDomainAndHCode(codeMapping);
    }

    @Override
    public List<CodeMapping> getCodeMappingByDomainAndPCode(CodeMapping codeMapping)  throws Exception{
        return commonServiceDaoImpl.getCodeMappingByDomainAndPCode(codeMapping);
    }


    public ICommonServiceDao getCommonServiceDaoImpl() {
        return commonServiceDaoImpl;
    }

    public void setCommonServiceDaoImpl(ICommonServiceDao commonServiceDaoImpl) {
        this.commonServiceDaoImpl = commonServiceDaoImpl;
    }
}
