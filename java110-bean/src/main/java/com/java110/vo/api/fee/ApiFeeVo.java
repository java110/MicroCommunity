package com.java110.vo.api.fee;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ApiFeeVo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/2/1 15:55
 * @Version 1.0
 * add by wuxw 2020/2/1
 **/
public class ApiFeeVo extends MorePageVo implements Serializable {
    List<ApiFeeDataVo> fees;


    public List<ApiFeeDataVo> getFees() {
        return fees;
    }

    public void setFees(List<ApiFeeDataVo> fees) {
        this.fees = fees;
    }
}
