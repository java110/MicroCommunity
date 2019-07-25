(function(vc){

    vc.extends({
        data:{
            indexContextInfo:{
                ownerCount:'0',
                noEnterRoomCount:'0',
                roomCount:'0',
                freeRoomCount:'0',
                parkingSpaceCount:'0',
                freeParkingSpaceCount:'0',
                shopCount:'0',
                freeShopCount:'0'
            }
        },
        _initMethod:function(){
            vc.component._queryIndexContextData();
        },
        _initEvent:function(){
            vc.on("indexContext","_queryIndexContextData",function(){
                vc.component._queryIndexContextData();
            });
        },
        methods:{
            _queryIndexContextData:function(){

                if(vc.getCurrentCommunity() == null || vc.getCurrentCommunity() == undefined){
                    return ;
                }

                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId
                    }
                }

               //发送get请求
               vc.http.get('indexContext',
                            'getData',
                             param,
                             function(json,res){
                                var indexData =JSON.parse(json);

                                vc.copyObject(indexData, vc.component.indexContextInfo);

                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            }
        }
    })
})(window.vc);