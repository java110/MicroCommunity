/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            roomInfo:{
                roomId:"",
                roomNum:"",
                unitNum:"",
                layer:"",
                section:"",
                apartment:"",
                builtUpArea:"",
                unitPrice:"",
                userName:"",
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('sellRoomSelectRoom','chooseRoom',function(_room){
                vc.component.roomInfo = _room;
                vc.emit('sellRoom','notify',_room);
            });

        },
        methods:{

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
            },

            openSearchRoomModel(){
                vc.emit('searchRoom','openSearchRoomModel',{});
            }
        }
    });

})(window.vc);