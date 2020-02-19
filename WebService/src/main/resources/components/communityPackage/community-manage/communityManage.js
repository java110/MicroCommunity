/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            communityManageInfo:{
                communitys:[],
                total:0,
                records:1,
                storeTypeCd:vc.getData('/nav/getUserInfo').storeTypeCd,
                conditions: {
                    name: '',
                    areaCode:''
                }

            }
        },
        _initMethod:function(){
            vc.component._listCommunitys(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('communityManage','listCommunity',function(_param){
                  vc.component._listCommunitys(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on("communityManage","communityManage","notifyArea",function(_param){
                vc.component.communityManageInfo.conditions.areaCode = _param.selectArea;
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listCommunitys(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listCommunitys:function(_page, _rows){

               vc.component.communityManageInfo.conditions.page = _page;
               vc.component.communityManageInfo.conditions.row = _rows;
               //发送get请求
               vc.http.get('communityManage',
                            'list',
                             vc.component.communityManageInfo.conditions,
                             function(json,res){
                                var _communityManageInfo=JSON.parse(json);
                                vc.component.communityManageInfo.total = _communityManageInfo.total;
                                vc.component.communityManageInfo.records = _communityManageInfo.records;
                                vc.component.communityManageInfo.communitys = _communityManageInfo.communitys;
                                vc.emit('pagination','init',{
                                    total:vc.component.communityManageInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddCommunityModal:function(){
                vc.emit('addCommunity','openAddCommunityModal',{});
            },
            _openEditCommunityModel:function(_community){
                vc.emit('editCommunity','openEditCommunityModal',_community);
            },
            _openDeleteCommunityModel:function(_community){
                vc.emit('deleteCommunity','openDeleteCommunityModal',_community);
            },
            _openRecallCommunityModel:function(_community){
                vc.emit('recallAuditFinishCommunity','openRecallAuditFinishCommunityModal',_community);
            }
        }
    });
})(window.vc);