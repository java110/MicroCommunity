/**
    业主报修 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewOwnerRepairInfo:{
                index:0,
                flowComponent:'viewOwnerRepairInfo',
                repairType:'',
repairName:'',
tel:'',
roomId:'',
appointmentTime:'',
context:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadOwnerRepairInfoData();
        },
        _initEvent:function(){
            vc.on('viewOwnerRepairInfo','chooseOwnerRepair',function(_app){
                vc.copyObject(_app, vc.component.viewOwnerRepairInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewOwnerRepairInfo);
            });

            vc.on('viewOwnerRepairInfo', 'onIndex', function(_index){
                vc.component.viewOwnerRepairInfo.index = _index;
            });

        },
        methods:{

            _openSelectOwnerRepairInfoModel(){
                vc.emit('chooseOwnerRepair','openChooseOwnerRepairModel',{});
            },
            _openAddOwnerRepairInfoModel(){
                vc.emit('addOwnerRepair','openAddOwnerRepairModal',{});
            },
            _loadOwnerRepairInfoData:function(){

            }
        }
    });

})(window.vc);
