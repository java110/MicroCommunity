package com.java110.community.dao.impl;

import com.java110.community.dao.IDictDao;
import com.java110.dto.Dict.DictDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * Created by hu ping on 10/22/2019
 * <p>
 */
@Repository
public class DictDaoImpl implements IDictDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<DictDto> queryDict(Map param) {
        List<DictDto> dicts = this.sqlSessionTemplate.selectList("dictDaoImpl.queryDict", param);
        return dicts;
    }
}
