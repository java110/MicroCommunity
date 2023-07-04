package com.java110.report.cmd.dataReport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.dict.DictDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.dev.IDictV1InnerServiceSMO;
import com.java110.report.statistics.IBaseDataStatistics;
import com.java110.report.statistics.IFeeStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询月实收明细统计
 */
@Java110Cmd(serviceCode = "dataReport.queryMonthOweDetail")
public class QueryMonthOweDetailCmd extends Cmd {

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Autowired
    private IBaseDataStatistics baseDataStatisticsImpl;

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "feeStartDate", "未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "feeEndDate", "未包含结束时间");

        String startDate = reqJson.getString("feeStartDate");
        String endDate = reqJson.getString("feeEndDate");
        if (!StringUtil.isEmpty(startDate) && !startDate.contains(":")) {
            startDate += " 00:00:00";
            reqJson.put("feeStartDate", startDate);
        }
        if (!StringUtil.isEmpty(endDate) && !endDate.contains(":")) {
            endDate += " 23:59:59";
            reqJson.put("feeEndDate", endDate);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("feeStartDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("feeEndDate"));
        queryStatisticsDto.setFloorId(reqJson.getString("floorId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));
        queryStatisticsDto.setPage(reqJson.getInteger("page"));
        queryStatisticsDto.setRow(reqJson.getInteger("row"));
        long count = feeStatisticsImpl.getMonthOweDetailCount(queryStatisticsDto);
        List<Map> infos = null;
        if (count > 0) {
            infos = feeStatisticsImpl.getMonthOweDetailInfo(queryStatisticsDto);
        } else {
            infos = new ArrayList<>();
        }
        // todo 计算欠费金额
        double oweAmount = feeStatisticsImpl.getMonthOweDetailAmount(queryStatisticsDto);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) queryStatisticsDto.getRow()), count, infos);
        resultVo.setSumTotal(oweAmount);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 计算房屋欠费 实收费用
     *
     * @param rooms
     * @return
     */
    private JSONArray computeRoomOweReceivedFee(List<RoomDto> rooms, QueryStatisticsDto queryStatisticsDto) {
        if (rooms == null || rooms.size() < 1) {
            return new JSONArray();
        }

        JSONArray datas = new JSONArray();
        JSONObject data = null;

        List<String> objIds = new ArrayList<>();
        for (RoomDto roomDto : rooms) {
            objIds.add(roomDto.getRoomId());
            data = new JSONObject();
            data.put("roomId", roomDto.getRoomId());
            data.put("roomName", roomDto.getFloorNum() + "-" + roomDto.getUnitNum() + "-" + roomDto.getRoomNum());
            data.put("ownerName", roomDto.getOwnerName());
            data.put("ownerId", roomDto.getOwnerId());
            data.put("link", roomDto.getLink());
            datas.add(data);
        }

        queryStatisticsDto.setObjIds(objIds.toArray(new String[objIds.size()]));
        List<Map> infos = feeStatisticsImpl.getObjReceivedFee(queryStatisticsDto);


        if (infos == null || infos.size() < 1) {
            return datas;
        }

        //todo 清洗数据 将数据转变成 map roomId feeTypeCd->array
        // todo 讲 payerObjId, feeTypeCd,feeName,endTime,deadlineTime,amountOwed 转换为 按payerObjId 纵向转换
        // todo  nInfo.put(info.get("payerObjId").toString(), info.get("payerObjId").toString());
        // todo  nInfo.put(info.get("feeTypeCd").toString(), tmpInfos);
        infos = washInfos(infos);

        //System.out.printf("infos = " + JSONObject.toJSONString(infos));

        BigDecimal receivedFee = null;
        List<Map> itemFees = null;
        String feeTypeCd = "";
        data.put("receivedFee", "0");

        DictDto dictDto = new DictDto();
        dictDto.setTableName("pay_fee_config");
        dictDto.setTableColumns("fee_type_cd_show");
        List<DictDto> dictDtos = dictV1InnerServiceSMOImpl.queryDicts(dictDto);

        // todo 根据房屋ID 和payerObjId 比较 合并数据，讲费用大类 横向 放入 data中，
        // todo 并且计算每个 房屋 费用大类的欠费 和房屋的总欠费
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            receivedFee = new BigDecimal(0.00);
            data = datas.getJSONObject(dataIndex);
            //todo 这里循环费用大类
            for (Map info : infos) {
                if (!data.getString("roomId").equals(info.get("payerObjId"))) {
                    continue;
                }
                for (DictDto tDict : dictDtos) {
                    //feeTypeCd = info.get("feeTypeCd").toString();
                    feeTypeCd = tDict.getStatusCd();
                    if (!info.containsKey(feeTypeCd)) {
                        continue;
                    }
                    receivedFee = receivedFee.add(new BigDecimal(info.get(feeTypeCd + "receivedFee").toString()));
                    data.put("receivedFee" + feeTypeCd, info.get(feeTypeCd));
                }
            }
            data.put("receivedFee", receivedFee.doubleValue());
        }

        return datas;
    }

    /**
     * //todo 清洗数据 将数据转变成 map roomId feeTypeCd->array
     * // todo 讲 payerObjId, feeTypeCd,feeName,endTime,deadlineTime,amountOwed 转换为 按payerObjId 纵向转换
     * // todo  nInfo.put(info.get("feeTypeCd").toString(), tmpInfos);
     *
     * @param infos
     * @return
     */
    private List<Map> washInfos(List<Map> infos) {
        List<Map> newInfos = new ArrayList<>();
        for (Map info : infos) {
            generatorNewInfo(newInfos, info);
        }


        List<Map> tmpInfos = null;
        Map dInfo = null;
        for (Map nInfo : newInfos) {
            for (Map info : infos) {
                if (!nInfo.get("payerObjId").equals(info.get("payerObjId"))) {
                    continue;
                }
                //todo 根据feeTypeCd 去寻找
                tmpInfos = getTmpInfos(nInfo, info);
                //todo 深拷贝
                dInfo = new HashMap();
                dInfo.putAll(info);
                tmpInfos.add(dInfo);
                //计算单项 欠费金额
                computeReceivedAmount(tmpInfos, info.get("feeTypeCd").toString(), nInfo);
                nInfo.put(info.get("feeTypeCd").toString(), tmpInfos);
            }
        }

        return newInfos;

    }

    /**
     * 计算每个费用大类的 欠费
     *
     * @param tmpInfos
     * @param feeTypeCd
     * @param nInfo
     */
    private void computeReceivedAmount(List<Map> tmpInfos, String feeTypeCd, Map nInfo) {
        if (tmpInfos == null || tmpInfos.size() < 1) {
            nInfo.put(feeTypeCd + "receivedFee", 0.0);
            return;
        }
        BigDecimal receivedAmount = new BigDecimal(0.0);
        for (Map tInfo : tmpInfos) {
            receivedAmount = receivedAmount.add(new BigDecimal(tInfo.get("receivedAmount").toString()));
        }
        receivedAmount = receivedAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        nInfo.put(feeTypeCd + "receivedFee", receivedAmount.doubleValue());
    }

    private List<Map> getTmpInfos(Map nInfo, Map info) {
        String feeTypeCd = info.get("feeTypeCd").toString();
        if (nInfo.containsKey(feeTypeCd)) {
            return (List<Map>) nInfo.get(feeTypeCd);
        }

        return new ArrayList<>();
    }

    /**
     * 查询 新数据对方
     *
     * @param newInfos
     * @param info
     * @return
     */
    private void generatorNewInfo(List<Map> newInfos, Map info) {
        if (newInfos.size() < 1) {
            newInfos.add(info);
            return;
        }

        for (Map newInfo : newInfos) {
            if (newInfo.get("payerObjId").equals(info.get("payerObjId"))) {
                return;
            }
        }

        newInfos.add(info);
    }

}
