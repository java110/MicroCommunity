/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
            callBackComponent:vc.propTypes.string
        },
        data:{
            parkingSpaceInfo:{
                num:'',
                typeCd:'',
                area:'',
                state:'',
                remark:'',
                psId:''
            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component.loadParkingSpaceData();
        },
        _initEvent:function(){
            vc.on('viewSelectParkingSpace','chooseParkingSpace',function(_parkingSpace){
                vc.component.parkingSpaceInfo = _parkingSpace;
                vc.emit($props.callBackComponent,'notify',_parkingSpace);
            });

        },
        methods:{

            openSearchParkingSpaceModel(){
                vc.emit('searchParkingSpace','openSearchParkingSpaceModel',{});
            },
            loadParkingSpaceData:function(){
               vc.component.parkingSpaceInfo.psId = vc.getParam("psId");
               vc.component.parkingSpaceInfo.name = vc.getParam("name");
               vc.component.parkingSpaceInfo.age = vc.getParam("age");
               vc.component.parkingSpaceInfo.sex = vc.getParam("sex");
               vc.component.parkingSpaceInfo.userName = vc.getParam("userName");
               vc.component.parkingSpaceInfo.link = vc.getParam("link");

               if(vc.component.parkingSpaceInfo.psId != ''){
                  vc.emit($props.callBackComponent,'notify',vc.component.parkingSpaceInfo);
               }
            }
        }
    });

})(window.vc);