(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            storesCommunityInfo:{
                communityInfo:[],
                errorInfo:'',
                searchCommunityName:'',
                storeId:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('storesCommunity','openStoresCommunity',function(_params){
                $('#storesCommunityModel').modal('show');
                vc.component.storesCommunityInfo.searchCommunityName = '';
                vc.component.storesCommunityInfo.storeId = _params.storeId;
                vc.component.listMyCommunity(DEFAULT_PAGE,DEFAULT_ROWS);
            });
             vc.on('storesCommunity','paginationPlus', 'page_event', function (_currentPage) {
                vc.component.listMyCommunity(_currentPage, DEFAULT_ROWS);
            });
        },
        methods:{
            listMyCommunity:function(_page,_row){
                    var param = {
                        params:{
                            communityName:vc.component.storesCommunityInfo.searchCommunityName,
                            storeId:vc.component.storesCommunityInfo.storeId,
                            page:_page,
                            row:_row
                        }

                   }
                   //发送get请求
                   vc.http.get('storesCommunity',
                                'listMyCommunity',
                                 param,
                                 function(json,res){
                                    var _communityInfo =JSON.parse(json);
                                    vc.component.storesCommunityInfo.communityInfo = _communityInfo.communitys
                                    vc.emit('storesCommunity','paginationPlus', 'init', {
                                        total: _communityInfo.records,
                                        currentPage: _page
                                    });
                                 },function(errInfo,error){
                                    console.log('请求失败处理');
                                 }
                   );
             }
        }
    });

})(window.vc);