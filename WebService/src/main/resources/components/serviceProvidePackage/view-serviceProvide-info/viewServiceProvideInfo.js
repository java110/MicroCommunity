/**
    服务提供 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewServiceProvideInfo:{
                index:0,
                flowComponent:'viewServiceProvideInfo',
                name:'',
serviceCode:'',
params:'',
queryModel:'',
sql:'',
template:'',
proc:'',
javaScript:'',
remark:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadServiceProvideInfoData();
        },
        _initEvent:function(){
            vc.on('viewServiceProvideInfo','chooseServiceProvide',function(_app){
                vc.copyObject(_app, vc.component.viewServiceProvideInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewServiceProvideInfo);
            });

            vc.on('viewServiceProvideInfo', 'onIndex', function(_index){
                vc.component.viewServiceProvideInfo.index = _index;
            });

        },
        methods:{

            _openSelectServiceProvideInfoModel(){
                vc.emit('chooseServiceProvide','openChooseServiceProvideModel',{});
            },
            _openAddServiceProvideInfoModel(){
                vc.emit('addServiceProvide','openAddServiceProvideModal',{});
            },
            _loadServiceProvideInfoData:function(){

            }
        }
    });

})(window.vc);
