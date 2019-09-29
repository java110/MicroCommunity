/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 1;
    vc.extends({
        data:{
            noticeDetailInfo:{
                noticeId:'',
                title:'',
                context:'',
                startTime:'',
                endTime:'',
                createTime:''
            }
        },
        _initMethod:function(){
            var tmpNoticeId = vc.getParam('noticeId');
            if(!vc.notNull(tmpNoticeId)){
                return ;
            }
            vc.component.noticeDetailInfo.noticeId = tmpNoticeId;
            vc.component._listNotices(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){

        },
        methods:{
            _listNotices:function(_page, _rows){

                var param = {
                    params:{
                        page:_page,
                        row:_rows,
                        communityId:vc.getCurrentCommunity().communityId,
                        noticeId:vc.component.noticeDetailInfo.noticeId
                    }
               };

               //发送get请求
               vc.http.get('noticeDetail',
                            'get',
                             param,
                             function(json,res){
                                var _noticeDetailInfo=JSON.parse(json);

                                var _notices = _noticeDetailInfo.notices;
                                if(_notices.length >0){
                                    //filterXSS
                                    _notices[0].context = filterXSS(_notices[0].context);
                                    vc.copyObject(_notices[0], vc.component.noticeDetailInfo);
                                }

                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            }

        }
    });
})(window.vc);
