/**
    服务绑定 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewServiceRegisterInfo:{
                index:0,
                flowComponent:'viewServiceRegisterInfo',
                appId:'',
serviceId:'',
orderTypeCd:'',
invokeLimitTimes:'',
invokeModel:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadServiceRegisterInfoData();
        },
        _initEvent:function(){
            vc.on('viewServiceRegisterInfo','chooseServiceRegister',function(_app){
                vc.copyObject(_app, vc.component.viewServiceRegisterInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewServiceRegisterInfo);
            });

            vc.on('viewServiceRegisterInfo', 'onIndex', function(_index){
                vc.component.viewServiceRegisterInfo.index = _index;
            });

        },
        methods:{

            _openSelectServiceRegisterInfoModel(){
                vc.emit('chooseServiceRegister','openChooseServiceRegisterModel',{});
            },
            _openAddServiceRegisterInfoModel(){
                vc.emit('addServiceRegister','openAddServiceRegisterModal',{});
            },
            _loadServiceRegisterInfoData:function(){

            }
        }
    });

})(window.vc);
