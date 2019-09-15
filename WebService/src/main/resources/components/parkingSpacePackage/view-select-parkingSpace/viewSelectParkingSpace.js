/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
            callBackComponent:vc.propTypes.string,
            callBackFunction:vc.propTypes.string
        },
        data:{
            parkingSpaceInfo:{
                flowComponent:'viewSelectParkingSpace',
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
                vc.copyObject(_parkingSpace, vc.component.parkingSpaceInfo);
                vc.emit($props.callBackComponent,$props.callBackFunction,vc.component.parkingSpaceInfo);
                vc.emit('hireParkingSpaceFee', 'parkingSpaceInfo',vc.component.parkingSpaceInfo);
                vc.emit('sellParkingSpaceFee', 'parkingSpaceInfo',vc.component.parkingSpaceInfo);
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