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
            vc.on('memberSelectRoom','chooseRoom',function(_room){
                vc.component.roomInfo = _room;
            });

        },
        methods:{

            openSearchRoomModel(){
                vc.emit('searchRoom','openSearchRoomModel',{});
            }
        }
    });

})(window.vc);