(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            indexContextInfo:{
                ownerCount:'1000',
                noEnterRoomCount:'90',
                roomCount:'2000',
                freeRoomCount:'100',
                parkingSpaceCount:'3000',
                freeParkingSpaceCount:'110',
                shopCount:'70',
                freeShopCount:'10'

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
                        communityId:vc.getCurrentCommunity().communityId
                    }
                }

               //发送get请求
               vc.http.get('indexContext',
                            'getData',
                             param,
                             function(json,res){
                                var listOwnerData =JSON.parse(json);

                                vc.component.listOwnerInfo.total = listOwnerData.total;
                                vc.component.listOwnerInfo.records = listOwnerData.records;
                                vc.component.listOwnerInfo.owners = listOwnerData.owners;


                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            }
        }
    })
})(window.vc);