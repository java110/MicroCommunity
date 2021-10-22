package com.java110.api.components.advert;

import com.java110.core.context.IPageData;
import com.java110.api.smo.advert.IDeleteAdvertSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加发布广告组件
 */
@Component("deleteAdvert")
public class DeleteAdvertComponent {

@Autowired
private IDeleteAdvertSMO deleteAdvertSMOImpl;

/**
 * 添加发布广告数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteAdvertSMOImpl.deleteAdvert(pd);
    }

public IDeleteAdvertSMO getDeleteAdvertSMOImpl() {
        return deleteAdvertSMOImpl;
    }

public void setDeleteAdvertSMOImpl(IDeleteAdvertSMO deleteAdvertSMOImpl) {
        this.deleteAdvertSMOImpl = deleteAdvertSMOImpl;
    }
            }
