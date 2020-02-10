/**
    巡检点 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewInspectionPointInfo:{
                index:0,
                flowComponent:'viewInspectionPointInfo',
                inspection_name:'',
remark:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadInspectionPointInfoData();
        },
        _initEvent:function(){
            vc.on('viewInspectionPointInfo','chooseInspectionPoint',function(_app){
                vc.copyObject(_app, vc.component.viewInspectionPointInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewInspectionPointInfo);
            });

            vc.on('viewInspectionPointInfo', 'onIndex', function(_index){
                vc.component.viewInspectionPointInfo.index = _index;
            });

        },
        methods:{

            _openSelectInspectionPointInfoModel(){
                vc.emit('chooseInspectionPoint','openChooseInspectionPointModel',{});
            },
            _openAddInspectionPointInfoModel(){
                vc.emit('addInspectionPoint','openAddInspectionPointModal',{});
            },
            _loadInspectionPointInfoData:function(){

            }
        }
    });

})(window.vc);
