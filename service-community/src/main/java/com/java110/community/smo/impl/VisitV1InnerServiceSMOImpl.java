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

import com.java110.community.dao.IVisitV1ServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IPhotoSMO;
import com.java110.dto.PageDto;
import com.java110.dto.RoomDto;
import com.java110.dto.accessControlWhite.AccessControlWhiteDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.visit.VisitDto;
import com.java110.dto.visitSetting.VisitSettingDto;
import com.java110.intf.common.IAccessControlWhiteAuthV1InnerServiceSMO;
import com.java110.intf.common.IAccessControlWhiteV1InnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.intf.user.ICarBlackWhiteV1InnerServiceSMO;
import com.java110.po.accessControlWhite.AccessControlWhitePo;
import com.java110.po.accessControlWhiteAuth.AccessControlWhiteAuthPo;
import com.java110.po.car.CarBlackWhitePo;
import com.java110.po.owner.VisitPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-08-04 17:07:07 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class VisitV1InnerServiceSMOImpl extends BaseServiceSMO implements IVisitV1InnerServiceSMO {

    @Autowired
    private IVisitV1ServiceDao visitV1ServiceDaoImpl;

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private ICarBlackWhiteV1InnerServiceSMO carBlackWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IAccessControlWhiteV1InnerServiceSMO accessControlWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IAccessControlWhiteAuthV1InnerServiceSMO accessControlWhiteAuthV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    @Override
    public int saveVisit(@RequestBody VisitPo visitPo) {
        int saveFlag = visitV1ServiceDaoImpl.saveVisitInfo(BeanConvertUtil.beanCovertMap(visitPo));
        return saveFlag;
    }

    @Override
    public int updateVisit(@RequestBody VisitPo visitPo) {
        int saveFlag = visitV1ServiceDaoImpl.updateVisitInfo(BeanConvertUtil.beanCovertMap(visitPo));
        if (saveFlag < 1 || !VisitDto.STATE_C.equals(visitPo.getState())) {
            //物业需要审核返回
            return saveFlag;
        }
        //物业无需审核走下方
        VisitDto visitDto = new VisitDto();
        visitDto.setvId(visitPo.getvId());
        visitDto.setPage(1);
        visitDto.setRow(1);
        List<VisitDto> visitDtos = queryVisits(visitDto);
        if (visitDtos == null || visitDtos.size() < 1) {
            return saveFlag;
        }
        visitDto = visitDtos.get(0);
        String faceWay = "Y";
        String carNumWay = "N";
        // 查询访客设置
        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(visitDto.getCommunityId());
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);
        if (visitSettingDtos != null && visitSettingDtos.size() > 0) {
            faceWay = visitSettingDtos.get(0).getFaceWay(); //人脸同步
            carNumWay = visitSettingDtos.get(0).getCarNumWay(); //车辆同步
            if (!StringUtil.isEmpty(visitDto.getCarNum()) && visitDto.getCarState().equals(VisitDto.CAR_STATE_C)) { //carState表示车辆审核状态 0表示未审核；1表示审核通过；2表示审核拒绝；3表示审核中
                // 同步车牌 这里需要停车场，所以没有配置访客设置，不同步
                synchronizedVisitCarNum(visitDto, carNumWay, visitSettingDtos.get(0));
            }
        }
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(visitDtos.get(0).getvId());
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() < 1) {
            return saveFlag;
        }
        // 同步访客人脸
        synchronousVisitFace(visitDto, faceWay, fileRelDtos.get(0).getFileSaveName());
        return saveFlag;
    }

    private void synchronousVisitFace(VisitDto visitDto, String faceWay, String photo) {
        if (VisitSettingDto.FACE_WAY_NO.equals(faceWay) || StringUtil.isEmpty(photo)) {
            return;
        }
        // 查询 访问业主可以访问的门禁设备
        RoomDto roomDto = new RoomDto();
        roomDto.setOwnerId(visitDto.getOwnerId());
        //这种情况说明 业主已经删掉了 需要查询状态为 1 的数据
        List<RoomDto> rooms = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);
        //拿到小区ID
        String communityId = visitDto.getCommunityId();
        //根据小区ID查询现有设备
        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityId);
        //String[] locationObjIds = new String[]{communityId};
        List<String> locationObjIds = new ArrayList<>();
        locationObjIds.add(communityId);
        for (RoomDto tRoomDto : rooms) {
            locationObjIds.add(tRoomDto.getUnitId());
            locationObjIds.add(tRoomDto.getRoomId());
            locationObjIds.add(tRoomDto.getFloorId());
        }
        machineDto.setLocationObjIds(locationObjIds.toArray(new String[locationObjIds.size()]));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        if (machineDtos == null || machineDtos.size() < 1) {
            return;
        }
        // 同步到 门禁白名单中
        for (MachineDto tmpMachineDto : machineDtos) {
            if (!"9999".equals(tmpMachineDto.getMachineTypeCd())) {
                continue;
            }
            AccessControlWhiteDto accessControlWhiteDto = new AccessControlWhiteDto();
            accessControlWhiteDto.setCommunityId(communityId);
            accessControlWhiteDto.setTel(visitDto.getPhoneNumber());
            accessControlWhiteDto.setMachineId(tmpMachineDto.getMachineId());
            List<AccessControlWhiteDto> accessControlWhiteDtos = accessControlWhiteV1InnerServiceSMOImpl.queryAccessControlWhites(accessControlWhiteDto);
            AccessControlWhitePo accessControlWhitePo = new AccessControlWhitePo();
            if (accessControlWhiteDtos == null || accessControlWhiteDtos.size() < 1) {
                accessControlWhitePo.setAcwId(GenerateCodeFactory.getGeneratorId("10"));
                accessControlWhitePo.setCommunityId(visitDto.getCommunityId());
                accessControlWhitePo.setEndTime(visitDto.getDepartureTime());
                accessControlWhitePo.setIdCard("");
                accessControlWhitePo.setMachineId("-1");
                accessControlWhitePo.setPersonName(visitDto.getvName());
                accessControlWhitePo.setPersonType(AccessControlWhiteDto.PERSON_TYPE_VISIT);
                accessControlWhitePo.setStartTime(visitDto.getVisitTime());
                accessControlWhitePo.setTel(visitDto.getPhoneNumber());
                accessControlWhitePo.setThirdId(visitDto.getvId());
                int flag = accessControlWhiteV1InnerServiceSMOImpl.saveAccessControlWhite(accessControlWhitePo);
                if (flag < 1) {
                    throw new CmdException("同步门禁白名单失败");
                }
                AccessControlWhiteAuthPo accessControlWhiteAuthPo = new AccessControlWhiteAuthPo();
                accessControlWhiteAuthPo.setAcwaId(GenerateCodeFactory.getGeneratorId("10"));
                accessControlWhiteAuthPo.setAcwId(accessControlWhitePo.getAcwId());
                accessControlWhiteAuthPo.setCommunityId(accessControlWhitePo.getCommunityId());
                accessControlWhiteAuthPo.setMachineId(tmpMachineDto.getMachineId());
                flag = accessControlWhiteAuthV1InnerServiceSMOImpl.saveAccessControlWhiteAuth(accessControlWhiteAuthPo);
                if (flag < 1) {
                    throw new CmdException("同步门禁白名单失败");
                }
            } else {
                accessControlWhitePo.setAcwId(accessControlWhiteDtos.get(0).getAcwId());
                accessControlWhitePo.setStartTime(visitDto.getVisitTime());
                accessControlWhitePo.setEndTime(visitDto.getDepartureTime());
                int flag = accessControlWhiteV1InnerServiceSMOImpl.updateAccessControlWhite(accessControlWhitePo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
            }
            photoSMOImpl.savePhoto(photo, accessControlWhitePo.getAcwId(), accessControlWhitePo.getCommunityId());
        }
    }

    /**
     * 预约车辆 加入 白名单 是最合适的
     * 不应该加入到业主车辆中
     *
     * @param visitDto
     * @param carNumWay
     * @param visitSettingDto
     */
    private void synchronizedVisitCarNum(VisitDto visitDto, String carNumWay, VisitSettingDto visitSettingDto) {
        if (VisitSettingDto.CAR_NUM_WAY_NO.equals(carNumWay)) {
            return;
        }
        CarBlackWhiteDto carBlackWhiteDto = new CarBlackWhiteDto();
        carBlackWhiteDto.setCarNum(visitDto.getCarNum());
        //根据车牌号查询白名单车辆信息
        List<CarBlackWhiteDto> carBlackWhiteDtos = carBlackWhiteV1InnerServiceSMOImpl.queryCarBlackWhites(carBlackWhiteDto);
        if (carBlackWhiteDtos != null && carBlackWhiteDtos.size() > 0) { //如果车牌号已在白名单中，就更新白名单车辆信息(并释放车位信息)
            VisitDto visitDto1 = new VisitDto();
            visitDto1.setCarNum(visitDto.getCarNum());
            List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto1);
            if(visitDtos != null && visitDtos.size() > 0) {
                for (VisitDto visit : visitDtos) {
                    if (!StringUtil.isEmpty(visit.getPsId())) {
                        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                        parkingSpaceDto.setPsId(visit.getPsId());
                        parkingSpaceDto.setState("H"); //车位状态 出售 S，出租 H ，空闲 F
                        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
                        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
                            continue;
                        }
                        ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
                        parkingSpacePo.setPsId(visit.getPsId());
                        parkingSpacePo.setState("F"); //车位状态 出售 S，出租 H ，空闲 F
                        parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
                    } else {
                        continue;
                    }
                }
            }
            CarBlackWhitePo carBlackWhitePo = new CarBlackWhitePo();
            carBlackWhitePo.setStartTime(visitDto.getVisitTime());
            carBlackWhitePo.setEndTime(visitDto.getFreeTime());
            carBlackWhitePo.setPaId(visitSettingDto.getPaId());
            carBlackWhitePo.setCommunityId(visitDto.getCommunityId());
            carBlackWhitePo.setBwId(carBlackWhiteDtos.get(0).getBwId());
            int flag = carBlackWhiteV1InnerServiceSMOImpl.updateCarBlackWhite(carBlackWhitePo);
            if (flag < 1) {
                throw new CmdException("更新预约车辆白名单失败");
            }
        } else { //白名单中不存在该车牌号，就新增白名单信息
            CarBlackWhitePo carBlackWhitePo = new CarBlackWhitePo();
            carBlackWhitePo.setCarNum(visitDto.getCarNum());
            carBlackWhitePo.setBlackWhite(CarBlackWhiteDto.BLACK_WHITE_WHITE);
            carBlackWhitePo.setCommunityId(visitDto.getCommunityId());
            carBlackWhitePo.setPaId(visitSettingDto.getPaId());
            carBlackWhitePo.setBwId(GenerateCodeFactory.getGeneratorId("11"));
            carBlackWhitePo.setStartTime(visitDto.getVisitTime());
            carBlackWhitePo.setEndTime(visitDto.getFreeTime());
            int flag = carBlackWhiteV1InnerServiceSMOImpl.saveCarBlackWhite(carBlackWhitePo);
            if (flag < 1) {
                throw new CmdException("预约车辆添加白名单失败");
            }
        }
    }

    @Override
    public int deleteVisit(@RequestBody VisitPo visitPo) {
        visitPo.setStatusCd("1");
        int saveFlag = visitV1ServiceDaoImpl.updateVisitInfo(BeanConvertUtil.beanCovertMap(visitPo));
        return saveFlag;
    }

    @Override
    public List<VisitDto> queryVisits(@RequestBody VisitDto visitDto) {
        //校验是否传了 分页信息
        int page = visitDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            visitDto.setPage((page - 1) * visitDto.getRow());
        }
        List<VisitDto> visits = BeanConvertUtil.covertBeanList(visitV1ServiceDaoImpl.getVisitInfo(BeanConvertUtil.beanCovertMap(visitDto)), VisitDto.class);
        return visits;
    }

    @Override
    public int queryVisitsCount(@RequestBody VisitDto visitDto) {
        return visitV1ServiceDaoImpl.queryVisitsCount(BeanConvertUtil.beanCovertMap(visitDto));
    }
}
