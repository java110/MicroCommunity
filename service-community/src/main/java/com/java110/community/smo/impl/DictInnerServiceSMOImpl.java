package com.java110.community.smo.impl;

import com.java110.community.dao.IDictDao;
import com.java110.intf.community.DictInnerServiceSMO;
import com.java110.dto.Dict.DictDto;
import com.java110.dto.Dict.DictQueryDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <br>
 * Created by hu ping on 10/22/2019
 * <p>
 */
@RestController
public class DictInnerServiceSMOImpl implements DictInnerServiceSMO {

    @Autowired
    private IDictDao iDictDao;

    @Override
    public List<DictDto> queryDict(@RequestBody DictQueryDto queryDto) {
        return this.iDictDao.queryDict(BeanConvertUtil.beanCovertMap(queryDto));
    }
}
