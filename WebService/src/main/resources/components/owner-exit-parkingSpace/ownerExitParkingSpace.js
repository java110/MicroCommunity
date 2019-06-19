(function(vc){
    vc.extends({
        data:{
            exitParkingSpaceInfo:{}
        },
        _initEvent:function(){
             vc.on('ownerExitParkingSpace','openExitParkingSpaceModel',function(_parkingSpaceInfo){
                    vc.component.exitParkingSpaceInfo = _parkingSpaceInfo;
                    $('#exitParkingSpaceModel').modal('show');
                });
        },
        methods:{
            closeExitParkingSpaceModel:function(){
                $('#exitParkingSpaceModel').modal('hide');
            },
            doOwnerExitParkingSpace:function(){

                vc.component.exitParkingSpaceInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'ownerExitParkingSpace',
                    'exit',
                    JSON.stringify(vc.component.exitParkingSpaceInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#exitParkingSpaceModel').modal('hide');
                            vc.emit('showOwnerParkingSpace','notify',vc.component.exitParkingSpaceInfo);
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