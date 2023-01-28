/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.community.smo.impl;


import com.java110.community.dao.IParkingSpaceV1ServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-04-11 13:21:22 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ParkingSpaceV1InnerServiceSMOImpl extends BaseServiceSMO implements IParkingSpaceV1InnerServiceSMO {

    @Autowired
    private IParkingSpaceV1ServiceDao parkingSpaceV1ServiceDaoImpl;


    @Override
    public int saveParkingSpace(@RequestBody ParkingSpacePo parkingSpacePo) {
        int saveFlag = parkingSpaceV1ServiceDaoImpl.saveParkingSpaceInfo(BeanConvertUtil.beanCovertMap(parkingSpacePo));
        return saveFlag;
    }

    @Override
    public int updateParkingSpace(@RequestBody ParkingSpacePo parkingSpacePo) {
        int saveFlag = parkingSpaceV1ServiceDaoImpl.updateParkingSpaceInfo(BeanConvertUtil.beanCovertMap(parkingSpacePo));
        return saveFlag;
    }

    @Override
    public int deleteParkingSpace(@RequestBody ParkingSpacePo parkingSpacePo) {
        parkingSpacePo.setStatusCd("1");
        int saveFlag = parkingSpaceV1ServiceDaoImpl.updateParkingSpaceInfo(BeanConvertUtil.beanCovertMap(parkingSpacePo));
        return saveFlag;
    }

    @Override
    public List<ParkingSpaceDto> queryParkingSpaces(@RequestBody ParkingSpaceDto parkingSpaceDto) {

        //校验是否传了 分页信息

        int page = parkingSpaceDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            parkingSpaceDto.setPage((page - 1) * parkingSpaceDto.getRow());
        }

        List<ParkingSpaceDto> parkingSpaces = BeanConvertUtil.covertBeanList(parkingSpaceV1ServiceDaoImpl.getParkingSpaceInfo(BeanConvertUtil.beanCovertMap(parkingSpaceDto)), ParkingSpaceDto.class);

        return parkingSpaces;
    }


    @Override
    public int queryParkingSpacesCount(@RequestBody ParkingSpaceDto parkingSpaceDto) {
        return parkingSpaceV1ServiceDaoImpl.queryParkingSpacesCount(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
    }

    @Override
    public int saveParkingSpaces(@RequestBody List<ParkingSpacePo> parkingSpacePos) {
        Map info = new HashMap();
        info.put("parkingSpacePos",parkingSpacePos);
        int saveFlag = parkingSpaceV1ServiceDaoImpl.saveParkingSpaceInfos(info);
        return saveFlag;
    }
}
