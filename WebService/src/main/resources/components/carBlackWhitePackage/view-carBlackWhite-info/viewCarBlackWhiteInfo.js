/**
    黑白名单 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewCarBlackWhiteInfo:{
                index:0,
                flowComponent:'viewCarBlackWhiteInfo',
                blackWhite:'',
carNum:'',
startTime:'',
endTime:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadCarBlackWhiteInfoData();
        },
        _initEvent:function(){
            vc.on('viewCarBlackWhiteInfo','chooseCarBlackWhite',function(_app){
                vc.copyObject(_app, vc.component.viewCarBlackWhiteInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewCarBlackWhiteInfo);
            });

            vc.on('viewCarBlackWhiteInfo', 'onIndex', function(_index){
                vc.component.viewCarBlackWhiteInfo.index = _index;
            });

        },
        methods:{

            _openSelectCarBlackWhiteInfoModel(){
                vc.emit('chooseCarBlackWhite','openChooseCarBlackWhiteModel',{});
            },
            _openAddCarBlackWhiteInfoModel(){
                vc.emit('addCarBlackWhite','openAddCarBlackWhiteModal',{});
            },
            _loadCarBlackWhiteInfoData:function(){

            }
        }
    });

})(window.vc);
