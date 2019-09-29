/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            noticeManageInfo:{
                notices:[],
                total:0,
                records:1,
                componentShow:'noticeList',
                conditions:{
                    title:'',
                    noticeTypeCd:'',
                    noticeId:''
                }
            }
        },
        _initMethod:function(){
            vc.component._listNotices(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('noticeManage','listNotice',function(_param){
                  vc.component.noticeManageInfo.componentShow = 'noticeList';

                  vc.component._listNotices(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listNotices(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listNotices:function(_page, _rows){
                vc.component.noticeManageInfo.conditions.page = _page;
                vc.component.noticeManageInfo.conditions.row = _rows;
                vc.component.noticeManageInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params:vc.component.noticeManageInfo.conditions
               };

               //发送get请求
               vc.http.get('noticeManage',
                            'list',
                             param,
                             function(json,res){
                                var _noticeManageInfo=JSON.parse(json);
                                vc.component.noticeManageInfo.total = _noticeManageInfo.total;
                                vc.component.noticeManageInfo.records = _noticeManageInfo.records;
                                vc.component.noticeManageInfo.notices = _noticeManageInfo.notices;
                                vc.emit('pagination','init',{
                                     total:vc.component.noticeManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddNoticeModal:function(){
                vc.component.noticeManageInfo.componentShow = 'addNoticeView';
                vc.emit('addNoticeView','openAddNoticeView',{});

            },
            _openEditNoticeModel:function(_notice){

                vc.emit('editNoticeViewInfo','noticeEditNoticeInfo',_notice);
                vc.component.noticeManageInfo.componentShow = 'editNoticeView';
            },
            _openDeleteNoticeModel:function(_notice){
                vc.emit('deleteNotice','openDeleteNoticeModal',_notice);
            },
            _openNoticeDetail:function(_notice){
                vc.jumpToPage("/flow/noticeDetailFlow?noticeId="+_notice.noticeId);

            },
            _queryNoticeMethod:function(){
                vc.component._listNotices(DEFAULT_PAGE, DEFAULT_ROWS);

            }
        }
    });
})(window.vc);
