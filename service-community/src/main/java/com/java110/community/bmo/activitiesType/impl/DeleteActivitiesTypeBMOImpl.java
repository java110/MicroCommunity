package com.java110.community.bmo.activitiesType.impl;

import com.java110.community.bmo.activitiesType.IDeleteActivitiesTypeBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.community.IActivitiesTypeInnerServiceSMO;
import com.java110.po.activitiesType.ActivitiesTypePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteActivitiesTypeBMOImpl")
public class DeleteActivitiesTypeBMOImpl implements IDeleteActivitiesTypeBMO {

    @Autowired
    private IActivitiesTypeInnerServiceSMO activitiesTypeInnerServiceSMOImpl;

    /**
     * @param activitiesTypePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ActivitiesTypePo activitiesTypePo) {

        int flag = activitiesTypeInnerServiceSMOImpl.deleteActivitiesType(activitiesTypePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
