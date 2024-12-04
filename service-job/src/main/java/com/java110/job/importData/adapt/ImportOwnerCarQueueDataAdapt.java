package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.parking.ParkingAreaPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车辆导入 适配器
 * 前端请求 时 必须传入
 * param.append('importAdapt', "importRoomOwner");
 */
@Service("importOwnerCarQueueData")
public class ImportOwnerCarQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {


    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;


    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Override
    public void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {
        importDatas(assetImportLogDetailDtos);
    }

    private void importDatas(List<AssetImportLogDetailDto> infos) {
        String state = "";
        String msg = "";
        for (AssetImportLogDetailDto assetImportLogDetailDto : infos) {

            try {
                doImportData(assetImportLogDetailDto);
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId());
            } catch (Exception e) {
                e.printStackTrace();
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId(), e);
            }
        }

    }

    /**
     * 导入数据
     *
     * @param assetImportLogDetailDto
     */
    private void doImportData(AssetImportLogDetailDto assetImportLogDetailDto) {
        JSONObject data = JSONObject.parseObject(assetImportLogDetailDto.getContent());
        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(data, OwnerCarDto.class);

        //todo 导入业主信息
        importOwnerData(ownerCarDto);

        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(ownerCarDto, OwnerCarPo.class);
        ownerCarPo.setStartTime(ownerCarDto.getLogStartTime());
        ownerCarPo.setEndTime(ownerCarDto.getLogEndTime());
        //获取房屋名称
        ownerCarPo.setCarTypeCd("1001"); //主车辆
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setNum(ownerCarDto.getAreaNum());
        parkingAreaDto.setTypeCd(ownerCarDto.getTypeCd());
        parkingAreaDto.setCommunityId(ownerCarDto.getCommunityId());

        //查询停车场
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
        //Assert.listOnlyOne(parkingAreaDtos, "查询停车场错误！");
        String paId = "";
        if (ListUtil.isNull(parkingAreaDtos)) {
            paId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_paId);
            ParkingAreaPo parkingAreaPo = new ParkingAreaPo();
            parkingAreaPo.setCommunityId(ownerCarDto.getCommunityId());
            parkingAreaPo.setNum(ownerCarDto.getAreaNum());
            parkingAreaPo.setPaId(paId);
            parkingAreaPo.setTypeCd(ownerCarDto.getTypeCd());
            parkingAreaPo.setRemark("导入数据");
            parkingAreaV1InnerServiceSMOImpl.saveParkingArea(parkingAreaPo);
        } else {
            paId = parkingAreaDtos.get(0).getPaId();
        }
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setNum(ownerCarDto.getNum());
        parkingSpaceDto.setPaId(paId);
        parkingSpaceDto.setCommunityId(ownerCarDto.getCommunityId());
        //查询停车位
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceV1InnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        String state = "";
        String psId = "";
        if (ListUtil.isNull(parkingSpaceDtos)) {
            psId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_psId);
            ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
            parkingSpacePo.setCommunityId(ownerCarDto.getCommunityId());
            parkingSpacePo.setNum(ownerCarDto.getNum());
            parkingSpacePo.setPaId(paId);
            parkingSpacePo.setParkingType(ParkingSpaceDto.TYPE_CD_COMMON);
            parkingSpacePo.setState(ParkingSpaceDto.STATE_FREE);
            parkingSpacePo.setArea("1");
            parkingSpacePo.setPsId(psId);
            parkingSpacePo.setRemark("导入数据");
            parkingSpaceV1InnerServiceSMOImpl.saveParkingSpace(parkingSpacePo);
            state = ParkingSpaceDto.STATE_FREE;
        } else {
            psId = parkingSpaceDtos.get(0).getPsId();
            //获取停车位状态(出售 S，出租 H ，空闲 F)
            state = parkingSpaceDtos.get(0).getState();
        }

        if (!StringUtil.isEmpty(state) && !state.equals("F")) {
            throw new IllegalArgumentException(ownerCarDto.getAreaNum() + "停车场-" + ownerCarDto.getNum() + "停车位不是空闲状态！");
        }
        ownerCarPo.setPsId(psId);
        ownerCarPo.setOwnerId(ownerCarDto.getOwnerId());
        ownerCarPo.setCommunityId(ownerCarDto.getCommunityId());
        ownerCarPo.setCarId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));
        ownerCarPo.setMemberId(ownerCarPo.getCarId());
        ownerCarPo.setState("1001"); //1001 正常状态，2002 车位释放欠费状态，3003 车位释放
        ownerCarPo.setLeaseType(ownerCarDto.getSpaceSate());
        ownerCarV1InnerServiceSMOImpl.saveOwnerCar(ownerCarPo);
        ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
        parkingSpacePo.setPsId(psId); //车位id
        parkingSpacePo.setState(ownerCarDto.getSpaceSate());
        parkingSpaceV1InnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
    }

    private void importOwnerData(OwnerCarDto ownerCarDto) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setName(ownerCarDto.getOwnerName());
        ownerDto.setLink(ownerCarDto.getLink());
        ownerDto.setCommunityId(ownerCarDto.getCommunityId());
        //查询业主
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        //Assert.listOnlyOne(ownerDtos, ownerCarDto.getCarNum() + "查询业主信息错误！");
        if (ListUtil.isNull(ownerDtos)) {
            OwnerPo ownerPo = new OwnerPo();
            ownerPo.setUserId("-1");
            ownerPo.setAge("1");
            ownerPo.setCommunityId(ownerCarDto.getCommunityId());
            ownerPo.setIdCard("");
            ownerPo.setLink(ownerCarDto.getLink());
            ownerPo.setSex("1");
            ownerPo.setMemberId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId));
            ownerPo.setName(ownerCarDto.getOwnerName());
            ownerPo.setOwnerId(ownerPo.getMemberId()); //业主 所以和成员ID需要一样
            ownerPo.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
            ownerPo.setRemark("导入车辆导入");
            ownerPo.setState(OwnerDto.STATE_FINISH);
            ownerPo.setAddress("无");
            ownerPo.setOwnerFlag(OwnerDto.OWNER_FLAG_TRUE);
            ownerPo.setPersonType(OwnerDto.PERSON_TYPE_PERSON);
            ownerPo.setPersonRole(OwnerDto.PERSON_ROLE_OWNER);
            ownerPo.setConcactPerson(ownerCarDto.getOwnerName());
            ownerPo.setConcactLink(ownerCarDto.getLink());
            int flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
            if (flag < 1) {
                throw new IllegalArgumentException("保存业主失败");
            }
            ownerCarDto.setOwnerId(ownerPo.getOwnerId());
        }else{
            ownerCarDto.setOwnerId(ownerDtos.get(0).getOwnerId());
        }

    }


}


