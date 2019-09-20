/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string, //父组件监听方法
           needShowAddAppButton:vc.propTypes.string = 'true' // 是否显示添加应用button
        },
        data:{
            viewAppInfo:{
                index:0,
                flowComponent:'visit',
                appId:"",
                name:"",
                securityCode:"",
                whileListIp:"",
                blackListIp:"",
                remark:"",
                needShowAddAppButton:$props.needShowAddAppButton
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
                alert("打开查询访客模态框");
                // vc.emit('chooseApp','openChooseAppModel',{});
            },
            _openAddAppInfoModel(){
                // vc.emit('addApp','openAddAppModal',{});
            },
            _loadAppInfoData:function(){

            }
        }
    });

})(window.vc);