/**
    开门记录 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewMachineRecordInfo:{
                index:0,
                flowComponent:'viewMachineRecordInfo',
                machineCode:'',
machineId:'',
name:'',
openTypeCd:'',
tel:'',
idCard:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadMachineRecordInfoData();
        },
        _initEvent:function(){
            vc.on('viewMachineRecordInfo','chooseMachineRecord',function(_app){
                vc.copyObject(_app, vc.component.viewMachineRecordInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewMachineRecordInfo);
            });

            vc.on('viewMachineRecordInfo', 'onIndex', function(_index){
                vc.component.viewMachineRecordInfo.index = _index;
            });

        },
        methods:{

            _openSelectMachineRecordInfoModel(){
                vc.emit('chooseMachineRecord','openChooseMachineRecordModel',{});
            },
            _openAddMachineRecordInfoModel(){
                vc.emit('addMachineRecord','openAddMachineRecordModal',{});
            },
            _loadMachineRecordInfoData:function(){

            }
        }
    });

})(window.vc);
