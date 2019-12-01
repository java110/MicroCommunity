/**
    钥匙申请 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewApplicationKeyInfo:{
                index:0,
                flowComponent:'viewApplicationKeyInfo',
                name:'',
tel:'',
typeCd:'',
sex:'',
age:'',
idCard:'',
startTime:'',
endTime:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadApplicationKeyInfoData();
        },
        _initEvent:function(){
            vc.on('viewApplicationKeyInfo','chooseApplicationKey',function(_app){
                vc.copyObject(_app, vc.component.viewApplicationKeyInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewApplicationKeyInfo);
            });

            vc.on('viewApplicationKeyInfo', 'onIndex', function(_index){
                vc.component.viewApplicationKeyInfo.index = _index;
            });

        },
        methods:{

            _openSelectApplicationKeyInfoModel(){
                vc.emit('chooseApplicationKey','openChooseApplicationKeyModel',{});
            },
            _openAddApplicationKeyInfoModel(){
                vc.emit('addApplicationKey','openAddApplicationKeyModal',{});
            },
            _loadApplicationKeyInfoData:function(){

            }
        }
    });

})(window.vc);
