/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
            @OpenAddRoomModelName:'string'
        },
        data:{
            floorInfo:{
                floorId:"",
                floorName:"",
                floorNum:""
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('roomSelectFloor','chooseFloor',function(_floor){
                vc.component.floorInfo = _floor;
            });

        },
        methods:{

            openSearchFloorModel:function(){
                vc.emit('searchFloor','openSearchFloorModel',{});
            },
            openAddRoomModel:function(){
                vc.emit(@OpenAddRoomModelName,'addRoomModel',{
                    floorId:vc.component.floorInfo.floorId
                });
            },
            roomSelectFloorCallBack:function(obj){
                console.log("回调函数",obj);
            }
        }
    });

})(window.vc);