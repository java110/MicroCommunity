package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 *
 * 初始化 公司接口类
 * Created by Administrator on 2019/3/28.
 */
public interface ICompanyServiceSMO {

    /**
     * 查询商户类型
     * @param pd
     * @return
     */
    public ResponseEntity<String> getStoreType(IPageData pd);

    /**
     * 保存公司信息
     * @param pd
     * @return
     */
    public ResponseEntity<String> saveCompanyInfo(IPageData pd);

    /**
     * 查询所有省市
     * @param pd
     * @return
     */
    public ResponseEntity<String> getAreas(IPageData pd);
}
