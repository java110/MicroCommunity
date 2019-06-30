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
                records:1
            }
        },
        _initMethod:function(){
            vc.component.listMyCommunity(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('communityManage','listMyCommunity',function(_param){
                  vc.component._listCommunitys();
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
               vc.http.get('communityManage',
                            'list',
                             param,
                             function(json,res){
                                var _communityManageInfo=JSON.parse(json);
                                vc.component.communityManageInfo.total = _communityManageInfo.total;
                                vc.component.communityManageInfo.records = _communityManageInfo.records;
                                vc.component.communityManageInfo.communitys = _communityManageInfo.communitys;
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openEnterCommunityModal:function(){
                vc.emit('storeEnterCommunity','openStoreEnterCommunity',{});
            },
            _openExitCommunityModel:function(_community){
                vc.emit('storeExitCommunity','openStoreExitCommunityModal',_community);
            },
            _showCommunityStatus(_statusCd){
                if(_statusCd == '1000'){
                    return "入驻审核";
                }else if(_statusCd == '1001'){
                    return "退出审核";
                }else if(_statusCd == '0000'){
                    return "入驻成功";
                }

                return "未知";
            }
        }
    });
})(window.vc);