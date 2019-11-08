/**
    设备 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewMachineInfo:{
                index:0,
                flowComponent:'viewMachineInfo',
                machineCode:'',
machineVersion:'',
machineName:'',
machineTypeCd:'',
authCode:'',
machineIp:'',
machineMac:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadMachineInfoData();
        },
        _initEvent:function(){
            vc.on('viewMachineInfo','chooseMachine',function(_app){
                vc.copyObject(_app, vc.component.viewMachineInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewMachineInfo);
            });

            vc.on('viewMachineInfo', 'onIndex', function(_index){
                vc.component.viewMachineInfo.index = _index;
            });

        },
        methods:{

            _openSelectMachineInfoModel(){
                vc.emit('chooseMachine','openChooseMachineModel',{});
            },
            _openAddMachineInfoModel(){
                vc.emit('addMachine','openAddMachineModal',{});
            },
            _loadMachineInfoData:function(){

            }
        }
    });

})(window.vc);
