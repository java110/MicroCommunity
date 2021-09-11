package com.java110.api.components.complaint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.complaint.IDeleteComplaintSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加投诉建议组件
 */
@Component("deleteComplaint")
public class DeleteComplaintComponent {

@Autowired
private IDeleteComplaintSMO deleteComplaintSMOImpl;

/**
 * 添加投诉建议数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteComplaintSMOImpl.deleteComplaint(pd);
    }

public IDeleteComplaintSMO getDeleteComplaintSMOImpl() {
        return deleteComplaintSMOImpl;
    }

public void setDeleteComplaintSMOImpl(IDeleteComplaintSMO deleteComplaintSMOImpl) {
        this.deleteComplaintSMOImpl = deleteComplaintSMOImpl;
    }
            }
