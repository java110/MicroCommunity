/**
    权限组
**/
(function(vc){

    vc.extends({

        data:{
            viewAppInfo:{
                appId:"",
                name:"",
                securityCode:"",
                whileListIp:"",
                blackListIp:"",
                remark:""
            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadAppInfoData();
        },
        _initEvent:function(){
            vc.on('viewAppInfo','chooseAppInfo',function(_app){
                vc.component.viewAppInfo = _app;
                //vc.emit($props.callBackComponent,'notify',_owner);
            });

        },
        methods:{

            _openSelectAppInfoModel(){
                vc.emit('selectAppInfo','openSelectAppModel',{});
            },
            _openAddAppInfoModel(){
                vc.emit('addApp','openAddAppModal',{});
            },
            _loadAppInfoData:function(){

            }
        }
    });

})(window.vc);