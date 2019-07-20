/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewServiceInfo:{
                serviceId:"",
                name:"",
                securityCode:"",
                whileListIp:"",
                blackListIp:"",
                remark:""
            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadServiceInfoData();
        },
        _initEvent:function(){
            vc.on('viewServiceInfo','chooseService',function(_service){
                vc.component.viewServiceInfo = _service;
                vc.emit($props.callBackListener,$props.callBackFunction,_service);
            });

        },
        methods:{

            _openSelectServiceInfoModel(){
                vc.emit('chooseService','openChooseServiceModel',{});
            },
            _openAddServiceInfoModel(){
                vc.emit('addService','openAddServiceModal',{});
            },
            _loadServiceInfoData:function(){

            }
        }
    });

})(window.vc);