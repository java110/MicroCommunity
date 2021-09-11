package com.java110.api.components.applicationKey;

import com.java110.core.context.IPageData;
import com.java110.api.smo.applicationKey.IDeleteApplicationKeySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加钥匙申请组件
 */
@Component("deleteApplicationKey")
public class DeleteApplicationKeyComponent {

@Autowired
private IDeleteApplicationKeySMO deleteApplicationKeySMOImpl;

/**
 * 添加钥匙申请数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteApplicationKeySMOImpl.deleteApplicationKey(pd);
    }

public IDeleteApplicationKeySMO getDeleteApplicationKeySMOImpl() {
        return deleteApplicationKeySMOImpl;
    }

public void setDeleteApplicationKeySMOImpl(IDeleteApplicationKeySMO deleteApplicationKeySMOImpl) {
        this.deleteApplicationKeySMOImpl = deleteApplicationKeySMOImpl;
    }
            }
