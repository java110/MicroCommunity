/**
    服务实现 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewServiceImplInfo:{
                index:0,
                flowComponent:'viewServiceImplInfo',
                serviceBusinessId:'',
businessTypeCd:'',
name:'',
invokeType:'',
url:'',
messageTopic:'',
timeout:'',
retryCount:'',
description:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadServiceImplInfoData();
        },
        _initEvent:function(){
            vc.on('viewServiceImplInfo','chooseServiceImpl',function(_app){
                vc.copyObject(_app, vc.component.viewServiceImplInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewServiceImplInfo);
            });

            vc.on('viewServiceImplInfo', 'onIndex', function(_index){
                vc.component.viewServiceImplInfo.index = _index;
            });

        },
        methods:{

            _openSelectServiceImplInfoModel(){
                vc.emit('chooseServiceImpl','openChooseServiceImplModel',{});
            },
            _openAddServiceImplInfoModel(){
                vc.emit('addServiceImpl','openAddServiceImplModal',{});
            },
            _loadServiceImplInfoData:function(){

            }
        }
    });

})(window.vc);
