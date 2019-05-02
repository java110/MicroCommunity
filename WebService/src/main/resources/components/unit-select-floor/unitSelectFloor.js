/**
    权限组
**/
(function(vc){

    vc.extends({
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
            vc.on('unitSelectFloor','chooseFloor',function(_floor){
                vc.component.floorInfo = _floor;
            });

        },
        methods:{

            openSearchFloorModel(){
                vc.emit('searchFloor','openSearchFloorModel',{});
            },
            openAddUnitModel(){
                vc.emit('addUnit','addUnitModel',{
                    floorId:vc.component.floorInfo.floorId
                });
            }
        }
    });

})(window.vc);