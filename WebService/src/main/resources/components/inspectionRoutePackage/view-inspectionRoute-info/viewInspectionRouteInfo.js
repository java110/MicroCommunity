/**
    巡检路线 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewInspectionRouteInfo:{
                index:0,
                flowComponent:'viewInspectionRouteInfo',
                routeName:'',
inspectionName:'',
machineQuantity:'',
checkQuantity:'',
remark:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadInspectionRouteInfoData();
        },
        _initEvent:function(){
            vc.on('viewInspectionRouteInfo','chooseInspectionRoute',function(_app){
                vc.copyObject(_app, vc.component.viewInspectionRouteInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewInspectionRouteInfo);
            });

            vc.on('viewInspectionRouteInfo', 'onIndex', function(_index){
                vc.component.viewInspectionRouteInfo.index = _index;
            });

        },
        methods:{

            _openSelectInspectionRouteInfoModel(){
                vc.emit('chooseInspectionRoute','openChooseInspectionRouteModel',{});
            },
            _openAddInspectionRouteInfoModel(){
                vc.emit('addInspectionRoute','openAddInspectionRouteModal',{});
            },
            _loadInspectionRouteInfoData:function(){

            }
        }
    });

})(window.vc);
