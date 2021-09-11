package com.java110.api.components.mapping;

import com.java110.core.context.IPageData;
import com.java110.api.smo.mapping.IDeleteMappingSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加编码映射组件
 */
@Component("deleteMapping")
public class DeleteMappingComponent {

@Autowired
private IDeleteMappingSMO deleteMappingSMOImpl;

/**
 * 添加编码映射数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteMappingSMOImpl.deleteMapping(pd);
    }

public IDeleteMappingSMO getDeleteMappingSMOImpl() {
        return deleteMappingSMOImpl;
    }

public void setDeleteMappingSMOImpl(IDeleteMappingSMO deleteMappingSMOImpl) {
        this.deleteMappingSMOImpl = deleteMappingSMOImpl;
    }
            }
