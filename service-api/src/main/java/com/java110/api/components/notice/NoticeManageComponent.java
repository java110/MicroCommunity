package com.java110.api.components.notice;


import com.java110.core.context.IPageData;
import com.java110.api.smo.notice.IListNoticesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 公告组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("noticeManage")
public class NoticeManageComponent {

    @Autowired
    private IListNoticesSMO listNoticesSMOImpl;

    /**
     * 查询公告列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listNoticesSMOImpl.listNotices(pd);
    }

    public IListNoticesSMO getListNoticesSMOImpl() {
        return listNoticesSMOImpl;
    }

    public void setListNoticesSMOImpl(IListNoticesSMO listNoticesSMOImpl) {
        this.listNoticesSMOImpl = listNoticesSMOImpl;
    }
}
