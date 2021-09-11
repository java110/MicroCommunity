package com.java110.api.components.undo;


import com.java110.core.context.IPageData;
import com.java110.api.smo.undo.IUndoSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 待办查询
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("undo")
public class UndoComponent {

    @Autowired
    private IUndoSMO undoSMOImpl;

    /**
     * 查询钥匙申请列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return undoSMOImpl.listUndos(pd);
    }


}
