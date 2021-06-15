package com.java110.intf.order;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.idSeq.IdSeqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户信息管理，服务
 * Created by wuxw on 2017/4/5.
 */
@FeignClient(name = "order-service", configuration = {FeignConfiguration.class})
@RequestMapping("/idSeq")
public interface IIdSeqInnerServiceSMO {

    /**
     * 根据sequence 表中name 查询ID
     *
     * @param primaryKeyInfo name信息封装
     * @return
     */
    @RequestMapping(value = "/queryPrimaryKey", method = RequestMethod.POST)
    public JSONObject queryPrimaryKey(@RequestBody JSONObject primaryKeyInfo) throws Exception;

    /**
     * 生成编码
     * @param idSeqDto
     * @throws Exception
     */
    @RequestMapping(value = "/generateCode", method = RequestMethod.POST)
    public IdSeqDto generateCode(@RequestBody IdSeqDto idSeqDto) ;
}
