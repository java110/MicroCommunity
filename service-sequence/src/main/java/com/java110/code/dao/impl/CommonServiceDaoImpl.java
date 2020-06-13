package com.java110.code.dao.impl;

import com.java110.code.dao.ICommonServiceDao;
import com.java110.utils.util.SerializeUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.mapping.CodeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

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

    protected final static Logger logger = LoggerFactory.getLogger(CommonServiceDaoImpl.class);


    /**
     * 查询所有有效的映射数据
     *
     * @return
     */
    @Override
    @Cacheable(key= "CodeMappingAll")
    public List<CodeMapping> getCodeMappingAll() throws Exception{
       Jedis jedis = this.getJedis();

       List<CodeMapping> codeMappings = null;
       if(jedis.exists("CodeMappingAll".getBytes())){
           codeMappings = SerializeUtil.unserializeList(jedis.get("CodeMappingAll".getBytes()),CodeMapping.class);
       }else{
           codeMappings = sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingAll");

           jedis.set("CodeMappingAll".getBytes(),SerializeUtil.serializeList(codeMappings));
       }
        return codeMappings;
    }

    /**
     * 根据域查询对应的映射关系
     *
     * @param codeMapping
     * @return
     */

    @Override
    @Cacheable(key= "CodeMappingByDomain")
    public List<CodeMapping> getCodeMappingByDomain(CodeMapping codeMapping)  throws Exception{

        Jedis jedis = this.getJedis();
        List<CodeMapping> codeMappings = null;
        if(jedis.exists("CodeMappingByDomain".getBytes())){
            codeMappings = SerializeUtil.unserializeList(jedis.get("CodeMappingByDomain".getBytes()),CodeMapping.class);
        }else{
            codeMappings = sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingByDomain", codeMapping);

            jedis.set("CodeMappingByDomain".getBytes(),SerializeUtil.serializeList(codeMappings));
        }
        return codeMappings;
    }

    /**
     * 根据HCode查询映射关系
     *
     * @param codeMapping
     * @return
     */
    @Override
    @Cacheable(key= "CodeMappingByHCode")
    public List<CodeMapping> getCodeMappingByHCode(CodeMapping codeMapping) throws Exception{

        Jedis jedis = this.getJedis();
        List<CodeMapping> codeMappings = null;
        if(jedis.exists("CodeMappingByHCode".getBytes())){
            codeMappings = SerializeUtil.unserializeList(jedis.get("CodeMappingByHCode".getBytes()),CodeMapping.class);
        }else{
            codeMappings = sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingByHCode", codeMapping);

            jedis.set("CodeMappingByHCode".getBytes(),SerializeUtil.serializeList(codeMappings));
        }
        return codeMappings;
    }

    /**
     * 根据PCode查询映射关系
     *
     * @param codeMapping
     * @return
     */
    @Override
    @Cacheable(key= "CodeMappingByPCode")
    public List<CodeMapping> getCodeMappingByPCode(CodeMapping codeMapping)  throws Exception{

        Jedis jedis = this.getJedis();
        List<CodeMapping> codeMappings = null;
        if(jedis.exists("CodeMappingByPCode".getBytes())){
            codeMappings = SerializeUtil.unserializeList(jedis.get("CodeMappingByPCode".getBytes()),CodeMapping.class);
        }else{
            codeMappings = sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingByPCode", codeMapping);

            jedis.set("CodeMappingByPCode".getBytes(),SerializeUtil.serializeList(codeMappings));
        }
        return codeMappings;
    }

    /**
     * 根据domain 和 hcode 查询映射关系
     *
     * @param codeMapping
     * @return
     */
    @Override
    @Cacheable(key= "CodeMappingByDomainAndHCode")
    public List<CodeMapping> getCodeMappingByDomainAndHCode(CodeMapping codeMapping)  throws Exception{

        Jedis jedis = this.getJedis();
        List<CodeMapping> codeMappings = null;
        if(jedis.exists("CodeMappingByDomainAndHCode".getBytes())){
            codeMappings = SerializeUtil.unserializeList(jedis.get("CodeMappingByDomainAndHCode".getBytes()),CodeMapping.class);
        }else{
            codeMappings = sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingByDomainAndHCode", codeMapping);

            jedis.set("CodeMappingByDomainAndHCode".getBytes(),SerializeUtil.serializeList(codeMappings));
        }
        return codeMappings;
    }

    /**
     * 根据domain 和 pcode 查询映射关系
     *
     * @param codeMapping
     * @return
     */
    @Override
    @Cacheable(key= "CodeMappingByDomainAndPCode")
    public List<CodeMapping> getCodeMappingByDomainAndPCode(CodeMapping codeMapping)  throws Exception{

        Jedis jedis = this.getJedis();
        List<CodeMapping> codeMappings = null;
        if(jedis.exists("CodeMappingByDomainAndPCode".getBytes())){
            codeMappings = SerializeUtil.unserializeList(jedis.get("CodeMappingByDomainAndPCode".getBytes()),CodeMapping.class);
        }else{
            codeMappings = sqlSessionTemplate.selectList("commonServiceDaoImpl.getCodeMappingByDomainAndPCode", codeMapping);

            jedis.set("CodeMappingByDomainAndPCode".getBytes(),SerializeUtil.serializeList(codeMappings));
        }
        return codeMappings;
    }
}
