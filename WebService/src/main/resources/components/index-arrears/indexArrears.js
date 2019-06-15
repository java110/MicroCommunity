(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            indexArrearsInfo:{
                arrears:[],
                total:0,
                records:1
            }
        },
        _initMethod:function(){
            //vc.component._listOwnerData(DEFAULT_PAGE,DEFAULT_ROWS);
        },
        _initEvent:function(){

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

            }
        }
    })
})(window.vc);