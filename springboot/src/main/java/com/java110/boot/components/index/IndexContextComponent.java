package com.java110.boot.components.index;


import com.java110.boot.smo.IIndexServiceSMO;
import com.java110.core.context.IPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("indexContext")
public class IndexContextComponent {


    @Autowired
    private IIndexServiceSMO indexServiceSMOImpl;

    /**
     * 查询数据
     *
     * @param pd 页面数据封装
     * @return 查询数据 ResponseEntity对象
     */
    public ResponseEntity<String> getData(IPageData pd) {

        return indexServiceSMOImpl.getStatisticInformation(pd);
    }


    public IIndexServiceSMO getIndexServiceSMOImpl() {
        return indexServiceSMOImpl;
    }

    public void setIndexServiceSMOImpl(IIndexServiceSMO indexServiceSMOImpl) {
        this.indexServiceSMOImpl = indexServiceSMOImpl;
    }
}
