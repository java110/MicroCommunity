(function(vc){
    vc.extends({
        data:{
            exitRoomInfo:{}
        },
        _initEvent:function(){
             vc.on('ownerExitRoom','openExitRoomModel',function(_roomInfo){
                    vc.component.exitRoomInfo = _roomInfo;
                    $('#exitRoomModel').modal('show');
                });
        },
        methods:{
            closeExitRoomModel:function(){
                $('#exitRoomModel').modal('hide');
            },
            doOwnerExitRoom:function(){

                vc.component.exitRoomInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'ownerExitRoom',
                    'exit',
                    JSON.stringify(vc.component.exitRoomInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#exitRoomModel').modal('hide');
                            vc.emit('showOwnerRoom','notify',vc.component.exitRoomInfo);
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