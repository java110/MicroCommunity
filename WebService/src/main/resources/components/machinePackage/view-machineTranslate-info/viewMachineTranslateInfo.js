/**
    设备同步 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewMachineTranslateInfo:{
                index:0,
                flowComponent:'viewMachineTranslateInfo',
                machineCode:'',
machineId:'',
typeCd:'',
objName:'',
objId:'',
state:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadMachineTranslateInfoData();
        },
        _initEvent:function(){
            vc.on('viewMachineTranslateInfo','chooseMachineTranslate',function(_app){
                vc.copyObject(_app, vc.component.viewMachineTranslateInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewMachineTranslateInfo);
            });

            vc.on('viewMachineTranslateInfo', 'onIndex', function(_index){
                vc.component.viewMachineTranslateInfo.index = _index;
            });

        },
        methods:{

            _openSelectMachineTranslateInfoModel(){
                vc.emit('chooseMachineTranslate','openChooseMachineTranslateModel',{});
            },
            _openAddMachineTranslateInfoModel(){
                vc.emit('addMachineTranslate','openAddMachineTranslateModal',{});
            },
            _loadMachineTranslateInfoData:function(){

            }
        }
    });

})(window.vc);
