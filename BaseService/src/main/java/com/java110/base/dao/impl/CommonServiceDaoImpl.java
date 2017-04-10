package com.java110.base.dao.impl;

import com.java110.base.dao.ICommonServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.mapping.CodeMapping;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 公用处理，与数据库交互 实现类
 * 如：映射关系加载
 * Created by wuxw on 2017/3/2.
 * version:1.0
 */
@Service("commonServiceDaoImpl")
@Transactional
public class CommonServiceDaoImpl extends BaseServiceDao implements ICommonServiceDao {

    /**
     * 查询所有有效的映射数据
     *
     * @return
     */
    @Override
    public List<CodeMapping> getCodeMappingAll() {
        return sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingAll");
    }

    /**
     * 根据域查询对应的映射关系
     *
     * @param codeMapping
     * @return
     */

    @Override
    public List<CodeMapping> getCodeMappingByDomain(CodeMapping codeMapping) {
        return sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingByDomain",codeMapping);
    }

    /**
     * 根据HCode查询映射关系
     * @param codeMapping
     * @return
     */
    @Override
    public List<CodeMapping> getCodeMappingByHCode(CodeMapping codeMapping) {
        return sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingByHCode",codeMapping);
    }

    /**
     * 根据PCode查询映射关系
     * @param codeMapping
     * @return
     */
    @Override
    public List<CodeMapping> getCodeMappingByPCode(CodeMapping codeMapping) {
        return sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingByPCode",codeMapping);
    }

    /**
     * 根据domain 和 hcode 查询映射关系
     * @param codeMapping
     * @return
     */
    @Override
    public List<CodeMapping> getCodeMappingByDomainAndHCode(CodeMapping codeMapping) {
        return sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingByDomainAndHCode",codeMapping);
    }

    /**
     * 根据domain 和 pcode 查询映射关系
     * @param codeMapping
     * @return
     */
    @Override
    public List<CodeMapping> getCodeMappingByDomainAndPCode(CodeMapping codeMapping) {
        return sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingByDomainAndPCode",codeMapping);
    }
}
