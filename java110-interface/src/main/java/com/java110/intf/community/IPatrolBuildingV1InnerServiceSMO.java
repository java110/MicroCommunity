package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.patrolBuilding.PatrolBuildingDto;
import com.java110.po.patrolBuilding.PatrolBuildingPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/patrolBuildingV1Api")
public interface IPatrolBuildingV1InnerServiceSMO {

    @RequestMapping(value = "/savePatrolBuilding", method = RequestMethod.POST)
    int savePatrolBuilding(@RequestBody PatrolBuildingPo patrolBuildingPo);

    @RequestMapping(value = "/updatePatrolBuilding", method = RequestMethod.POST)
    int updatePatrolBuilding(@RequestBody PatrolBuildingPo patrolBuildingPo);

    @RequestMapping(value = "/deletePatrolBuilding", method = RequestMethod.POST)
    int deletePatrolBuilding(@RequestBody PatrolBuildingPo patrolBuildingPo);

    @RequestMapping(value = "/queryPatrolBuildings", method = RequestMethod.POST)
    List<PatrolBuildingDto> queryPatrolBuildings(@RequestBody PatrolBuildingDto patrolBuildingDto);

    @RequestMapping(value = "/queryPatrolBuildingsCount", method = RequestMethod.POST)
    int queryPatrolBuildingsCount(@RequestBody PatrolBuildingDto patrolBuildingDto);

}
