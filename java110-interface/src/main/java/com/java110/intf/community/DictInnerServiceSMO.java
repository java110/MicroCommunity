package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.Dict.DictDto;
import com.java110.dto.Dict.DictQueryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <br>
 * Created by hu ping on 10/22/2019
 * <p>
 */
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/dictApi")
public interface DictInnerServiceSMO {

    @RequestMapping(value = "/queryDict",method = RequestMethod.POST)
    List<DictDto> queryDict(@RequestBody DictQueryDto queryDto);

}
