/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            auditCommunityManageInfo:{
                communitys:[],
                total:0,
                records:1
            }
        },
        _initMethod:function(){
            vc.component._listCommunitys(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('communityManage','listCommunity',function(_param){
                  vc.component._listCommunitys(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listCommunitys(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listCommunitys:function(_page, _rows){
                var param = {
                    params:{
                        page:_page,
                        row:_rows
                    }

               }
               //发送get请求
               vc.http.get('auditCommunityManage',
                            'list',
                             param,
                             function(json,res){
                                var _auditCommunityManageInfo=JSON.parse(json);
                                vc.component.auditCommunityManageInfo.total = _auditCommunityManageInfo.total;
                                vc.component.auditCommunityManageInfo.records = _auditCommunityManageInfo.records;
                                vc.component.auditCommunityManageInfo.communitys = _auditCommunityManageInfo.communitys;
                                vc.emit('pagination','init',{
                                    total:vc.component.auditCommunityManageInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAuditCommunityModal:function(){
                vc.emit('auditCommunity','openAuditCommunityModal',{});
            }

        }
    });
})(window.vc);