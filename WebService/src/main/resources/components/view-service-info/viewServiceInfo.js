/**
    权限组
**/
(function(vc){

    vc.extends({

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
                //vc.emit($props.callBackComponent,'notify',_owner);
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