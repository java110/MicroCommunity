/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            auditEnterCommunityManageInfo:{
                communitys:[],
                total:0,
                records:1,
                currentCommunityMemberId:''
            }
        },
        _initMethod:function(){
            vc.component._listCommunitys(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('auditEnterCommunityManage','listCommunity',function(_param){
                  vc.component._listCommunitys(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('auditEnterCommunityManage','notifyAuditInfo',function(_auditInfo){
                  vc.component._auditEnterCommunityState(_auditInfo);
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
               vc.http.get('auditEnterCommunityManage',
                            'list',
                             param,
                             function(json,res){
                                var _auditEnterCommunityManageInfo=JSON.parse(json);
                                vc.component.auditEnterCommunityManageInfo.total = _auditEnterCommunityManageInfo.total;
                                vc.component.auditEnterCommunityManageInfo.records = _auditEnterCommunityManageInfo.records;
                                vc.component.auditEnterCommunityManageInfo.communitys = _auditEnterCommunityManageInfo.communitys;
                                vc.emit('pagination','init',{
                                    total:vc.component.auditEnterCommunityManageInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openEnterAuditCommunityModal:function(_community){
                vc.component.auditEnterCommunityManageInfo.currentCommunityMemberId = _community.communityMemberId;
                vc.emit('audit','openAuditModal',{});
            },
            _auditEnterCommunityState:function(_auditInfo){
                _auditInfo.communityMemberId = vc.component.auditEnterCommunityManageInfo.currentCommunityMemberId;
                vc.http.post(
                    'auditEnterCommunityManage',
                    'audit',
                    JSON.stringify(_auditInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                             vc.component._listCommunitys(DEFAULT_PAGE, DEFAULT_ROWS);
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(errInfo);
                });
            }

        }
    });
})(window.vc);