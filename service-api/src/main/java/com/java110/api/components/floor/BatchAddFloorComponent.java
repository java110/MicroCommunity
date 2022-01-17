package com.java110.api.components.floor;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.api.smo.IFloorServiceSMO;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加小区楼组件
 */
@Component("batchAddFloor")
public class BatchAddFloorComponent {

    private static Logger logger = LoggerFactory.getLogger(BatchAddFloorComponent.class);


    @Autowired
    private IFloorServiceSMO floorServiceSMOImpl;

    /**
     * 查询小区楼信息
     *
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> saveFloor(IPageData pd) {

        JSONObject floors = JSONObject.parseObject(pd.getReqData());

        Assert.hasKeyAndValue(floors, "startFloorNum", "请求报文中未包含startFloorNum 节点");
        Assert.hasKeyAndValue(floors, "endFloorNum", "请求报文中未包含startFloorNum 节点");
        Assert.hasKeyAndValue(floors, "communityId", "请求报文中未包含communityId 节点");

        Assert.isInteger(floors.getString("startFloorNum"), "开始楼栋编号不是数字");
        Assert.isInteger(floors.getString("endFloorNum"), "结束楼栋编号不是数字");

        int startFloorNum = floors.getInteger("startFloorNum");
        int endFloorNum = floors.getInteger("endFloorNum");

        if (endFloorNum <= startFloorNum) {
            throw new IllegalArgumentException("结束楼栋编号不能小于等于开始楼栋编号");
        }

        if (endFloorNum - startFloorNum > 50) {
            throw new IllegalArgumentException("一次批量生成不能超过50栋楼");
        }

        int successFloorCount = 0;
        int failFloorCount = 0;

        IPageData newPd = null;

        /**
         * "communityId", "未包含
         * "name", "未包含小区楼名称")
         * "floorNum", "未包含小区楼
         * "remark", "未包含小区楼备注
         */
        JSONObject needReqParam = null;
        ResponseEntity<String> floorEntity = null;
        for (int floorIndex = startFloorNum; floorIndex <= endFloorNum; floorIndex++) {
            try {
                needReqParam = new JSONObject();
                needReqParam.put("communityId", floors.getString("communityId"));
                needReqParam.put("floorNum", floorIndex);
                needReqParam.put("name", floorIndex + "栋");
                needReqParam.put("remark", floors.containsKey("remark") ? floors.getString("remark") : "");
                newPd = PageData.newInstance().builder(pd.getUserId(),
                        pd.getUserName(),
                        pd.getToken(),
                        needReqParam.toJSONString(),
                        pd.getComponentCode(),
                        pd.getComponentMethod(), "", pd.getSessionId(),pd.getAppId(),pd.getHeaders());
                floorEntity = floorServiceSMOImpl.saveFloor(newPd);

                if (floorEntity.getStatusCode() == HttpStatus.OK) {
                    successFloorCount++;
                } else {
                    failFloorCount++;
                }
            } catch (Exception e) {
                logger.error("批量生成楼栋失败", e);
                failFloorCount++;
            }
        }

        JSONObject outParam = new JSONObject();
        outParam.put("successFloorCount", successFloorCount);
        outParam.put("failFloorCount", failFloorCount);


        return new ResponseEntity<String>(outParam.toJSONString(), HttpStatus.OK);
    }


    public IFloorServiceSMO getFloorServiceSMOImpl() {
        return floorServiceSMOImpl;
    }

    public void setFloorServiceSMOImpl(IFloorServiceSMO floorServiceSMOImpl) {
        this.floorServiceSMOImpl = floorServiceSMOImpl;
    }
}
