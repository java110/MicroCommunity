package com.java110.vo.api;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ApiArrearsFeeVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/6/18 19:36
 * @Version 1.0
 * add by wuxw 2019/6/18
 **/
public class ApiArrearsFeeVo extends MorePageVo implements Serializable {

    List<ApiArrearsFeeDataVo> arrears;


    public List<ApiArrearsFeeDataVo> getArrears() {
        return arrears;
    }

    public void setArrears(List<ApiArrearsFeeDataVo> arrears) {
        this.arrears = arrears;
    }
}
