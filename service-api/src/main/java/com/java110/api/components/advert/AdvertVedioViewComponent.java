package com.java110.api.components.advert;


import com.java110.core.context.IPageData;
import com.java110.api.smo.advert.IListAdvertPhotoAndVediosSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 发布广告组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("advertVedioView")
public class AdvertVedioViewComponent {

    @Autowired
    private IListAdvertPhotoAndVediosSMO listAdvertPhotoAndVediosSMOImpl;

    /**
     * 查询发布广告列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        /*return listAdvertsSMOImpl.listAdverts(pd);*/
        /*String _tmpParam = "[{\"suffix\":\"VIDEO\",\"url\":\"http://183.134.88.228:8001/video/06d91ddb-69df-4320-bb14-4a58ce31553d.mp4\",\"seq\":\"1\"},{\"suffix\":\"JPEG\",\"url\":\"http://183.134.88.228:8001/callComponent/download/getFile/file?fileId=812019121487560003&communityId=702019120393220007\",\"seq\":\"1\"},{\"suffix\":\"JPEG\",\"url\":\"http://183.134.88.228:8001/callComponent/download/getFile/file?fileId=812019121462430004&communityId=702019120393220007\",\"seq\":\"1\"},{\"suffix\":\"VIDEO\",\"url\":\"http://183.134.88.228:8001/video/659d9395-6471-4e0e-bfdb-0b274ea52395.mp4\",\"seq\":\"1\"}]";
        return new ResponseEntity<String>(_tmpParam, HttpStatus.OK);*/


        return listAdvertPhotoAndVediosSMOImpl.listAdvertPhotoAndVideos(pd);

    }

    public IListAdvertPhotoAndVediosSMO getListAdvertPhotoAndVediosSMOImpl() {
        return listAdvertPhotoAndVediosSMOImpl;
    }

    public void setListAdvertPhotoAndVediosSMOImpl(IListAdvertPhotoAndVediosSMO listAdvertPhotoAndVediosSMOImpl) {
        this.listAdvertPhotoAndVediosSMOImpl = listAdvertPhotoAndVediosSMOImpl;
    }
}
