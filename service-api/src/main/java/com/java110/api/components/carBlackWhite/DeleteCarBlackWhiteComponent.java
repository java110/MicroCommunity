package com.java110.api.components.carBlackWhite;

import com.java110.core.context.IPageData;
import com.java110.api.smo.carBlackWhite.IDeleteCarBlackWhiteSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加黑白名单组件
 */
@Component("deleteCarBlackWhite")
public class DeleteCarBlackWhiteComponent {

@Autowired
private IDeleteCarBlackWhiteSMO deleteCarBlackWhiteSMOImpl;

/**
 * 添加黑白名单数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteCarBlackWhiteSMOImpl.deleteCarBlackWhite(pd);
    }

public IDeleteCarBlackWhiteSMO getDeleteCarBlackWhiteSMOImpl() {
        return deleteCarBlackWhiteSMOImpl;
    }

public void setDeleteCarBlackWhiteSMOImpl(IDeleteCarBlackWhiteSMO deleteCarBlackWhiteSMOImpl) {
        this.deleteCarBlackWhiteSMOImpl = deleteCarBlackWhiteSMOImpl;
    }
            }
