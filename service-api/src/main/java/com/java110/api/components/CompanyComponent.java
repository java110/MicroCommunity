package com.java110.api.components;

import com.java110.core.context.IPageData;
import com.java110.api.smo.ICompanyServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 初始化公司组件
 * Created by Administrator on 2019/3/28.
 */
@Component("company")
public class CompanyComponent {


    @Autowired
    ICompanyServiceSMO companyServiceSMOImpl;

    /**
     * 获取商户类型
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> getStoreType(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = companyServiceSMOImpl.getStoreType(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }

    /**
     * 保存公司信息
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> saveCompanyInfo(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = companyServiceSMOImpl.saveCompanyInfo(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }

    /**
     * 功能描述: 查询所有省市
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> getAreas(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = companyServiceSMOImpl.getAreas(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }

    public ICompanyServiceSMO getCompanyServiceSMOImpl() {
        return companyServiceSMOImpl;
    }

    public void setCompanyServiceSMOImpl(ICompanyServiceSMO companyServiceSMOImpl) {
        this.companyServiceSMOImpl = companyServiceSMOImpl;
    }
}
