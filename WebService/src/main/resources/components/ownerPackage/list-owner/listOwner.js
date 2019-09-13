(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            listOwnerInfo:{
                owners:[],
                total:0,
                records:1
            }
        },
        _initMethod:function(){
            vc.component._listOwnerData(DEFAULT_PAGE,DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('listOwner','listOwnerData',function(){
                vc.component._listOwnerData(DEFAULT_PAGE,DEFAULT_ROWS);
            });
            vc.on('pagination','page_event',function(_currentPage){
                vc.component._listOwnerData(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listOwnerData:function(_page,_row){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        ownerTypeCd:'1001'
                    }
                }

               //发送get请求
               vc.http.get('listOwner',
                            'list',
                             param,
                             function(json,res){
                                var listOwnerData =JSON.parse(json);

                                vc.component.listOwnerInfo.total = listOwnerData.total;
                                vc.component.listOwnerInfo.records = listOwnerData.records;
                                vc.component.listOwnerInfo.owners = listOwnerData.owners;

                                vc.emit('pagination','init',{
                                    total:vc.component.listOwnerInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            },
            _openAddOwnerModal:function(){ //打开添加框
                //vc.emit('addOwner','openAddOwnerModal',-1);
                vc.jumpToPage("/flow/addOwnerBindingFlow");
            },
            _openDelOwnerModel:function(_owner){ // 打开删除对话框
                vc.emit('deleteOwner','openOwnerModel',_owner);
            },
            _openEditOwnerModel:function(_owner){
                vc.emit('editOwner','openEditOwnerModal',_owner);
            }
        }
    })
})(window.vc);