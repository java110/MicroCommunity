package com.java110.generator.smo;

import com.java110.entity.mapping.CodeMapping;

import java.util.List;

/**
 * Created by wuxw on 2017/7/25.
 */
public interface ICommonServiceSmo {

    /**
     * 查询所有有效的映射数据
     *
     * @return
     */
    public List<CodeMapping> getCodeMappingAll()  throws Exception;

    /**
     * 根据域查询对应的映射关系
     *
     * @param codeMapping
     * @return
     */
    public List<CodeMapping> getCodeMappingByDomain(CodeMapping codeMapping)  throws Exception;

    /**
     * 根据HCode查询映射关系
     *
     * @param codeMapping
     * @return
     */
    public List<CodeMapping> getCodeMappingByHCode(CodeMapping codeMapping)  throws Exception;


    /**
     * 根据PCode查询映射关系
     *
     * @param codeMapping
     * @return
     */
    public List<CodeMapping> getCodeMappingByPCode(CodeMapping codeMapping)  throws Exception;

    /**
     * 根据domain 和 hcode 查询映射关系
     *
     * @param codeMapping
     * @return
     */
    public List<CodeMapping> getCodeMappingByDomainAndHCode(CodeMapping codeMapping)  throws Exception;

    /**
     * 根据domain 和 pcode 查询映射关系
     *
     * @param codeMapping
     * @return
     */
    public List<CodeMapping> getCodeMappingByDomainAndPCode(CodeMapping codeMapping)  throws Exception;



}
