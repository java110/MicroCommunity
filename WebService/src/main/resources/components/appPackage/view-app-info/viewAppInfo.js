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
            viewAppInfo:{
                index:0,
                flowComponent:'App',
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
            vc.on('viewAppInfo','chooseApp',function(_app){
                vc.copyObject(_app, vc.component.viewAppInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewAppInfo);
            });

            vc.on('viewAppInfo', 'onIndex', function(_index){
                vc.component.viewAppInfo.index = _index;
            });

        },
        methods:{

            _openSelectAppInfoModel(){
                vc.emit('chooseApp','openChooseAppModel',{});
            },
            _openAddAppInfoModel(){
                vc.emit('addApp','openAddAppModal',{});
            },
            _loadAppInfoData:function(){

            }
        }
    });

})(window.vc);