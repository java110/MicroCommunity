(function(vc){
    vc.extends({
        data:{
            deleteRoomInfo:{}
        },
        _initEvent:function(){
             vc.on('deleteRoom','openRoomModel',function(_roomInfo){
                    vc.component.deleteRoomInfo = _roomInfo;
                    $('#deleteRoomModel').modal('show');
                });
        },
        methods:{
            closeDeleteRoomModel:function(){
                $('#deleteRoomModel').modal('hide');
            },
            deleteRoom:function(){

                vc.component.deleteRoomInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteRoom',
                    'delete',
                    JSON.stringify(vc.component.deleteRoomInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteRoomModel').modal('hide');
                            vc.emit('room','loadData',{
                                floorId:vc.component.deleteRoomInfo.floorId
                            });
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