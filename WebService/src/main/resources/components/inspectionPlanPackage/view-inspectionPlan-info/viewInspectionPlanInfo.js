/**
    巡检计划 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewInspectionPlanInfo:{
                index:0,
                flowComponent:'viewInspectionPlanInfo',
                inspectionPlanName:'',
inspectionRouteId:'',
inspectionPlanPeriod:'',
staffId:'',
startTime:'',
endTime:'',
signType:'',
state:'',
remark:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadInspectionPlanInfoData();
        },
        _initEvent:function(){
            vc.on('viewInspectionPlanInfo','chooseInspectionPlan',function(_app){
                vc.copyObject(_app, vc.component.viewInspectionPlanInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewInspectionPlanInfo);
            });

            vc.on('viewInspectionPlanInfo', 'onIndex', function(_index){
                vc.component.viewInspectionPlanInfo.index = _index;
            });

        },
        methods:{

            _openSelectInspectionPlanInfoModel(){
                vc.emit('chooseInspectionPlan','openChooseInspectionPlanModel',{});
            },
            _openAddInspectionPlanInfoModel(){
                vc.emit('addInspectionPlan','openAddInspectionPlanModal',{});
            },
            _loadInspectionPlanInfoData:function(){

            }
        }
    });

})(window.vc);
