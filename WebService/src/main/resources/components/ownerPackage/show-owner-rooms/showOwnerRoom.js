/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            showOwnerRoomInfo:{
                ownerId:'',
                rooms:[]
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('showOwnerRoom','notify',function(_owner){
                vc.component.showOwnerRoomInfo.ownerId = _owner.ownerId;

                //查询 根据业主查询房屋信息
                vc.component.loadRooms();
            });

        },
        methods:{

            loadRooms:function(){
                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        ownerId:vc.component.showOwnerRoomInfo.ownerId
                    }
                };

                //发送get请求
                vc.http.get('showOwnerRoom',
                            'list',
                             param,
                             function(json){
                                var _roomInfo = JSON.parse(json);
                                vc.component.showOwnerRoomInfo.rooms = _roomInfo.rooms;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },

            ownerExitRoomModel:function(_roomId){
                vc.emit('ownerExitRoom','openExitRoomModel',{
                    ownerId:vc.component.showOwnerRoomInfo.ownerId,
                    roomId:_roomId
                });
            },

            showState:function(_state){
                if(_state == '2001'){
                    return "房屋已售";
                }else if(_state == '2002'){
                    return "房屋未售";
                }else if(_state == '2003'){
                    return "已交定金";
                }
                else if(_state == '2004'){
                    return "已出租";
                }else{
                    return "未知";
                }
            }
        }
    });

})(window.vc);