package com.java110.api.components.parkingArea;

import com.java110.core.context.IPageData;
import com.java110.api.smo.parkingArea.IDeleteParkingAreaSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加停车场组件
 */
@Component("deleteParkingArea")
public class DeleteParkingAreaComponent {

@Autowired
private IDeleteParkingAreaSMO deleteParkingAreaSMOImpl;

/**
 * 添加停车场数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteParkingAreaSMOImpl.deleteParkingArea(pd);
    }

public IDeleteParkingAreaSMO getDeleteParkingAreaSMOImpl() {
        return deleteParkingAreaSMOImpl;
    }

public void setDeleteParkingAreaSMOImpl(IDeleteParkingAreaSMO deleteParkingAreaSMOImpl) {
        this.deleteParkingAreaSMOImpl = deleteParkingAreaSMOImpl;
    }
            }
