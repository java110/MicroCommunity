/**
    菜单 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewMenuInfo:{
                index:0,
                flowComponent:'viewMenuInfo',
                name:'',
url:'',
seq:'',
isShow:'',
description:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadMenuInfoData();
        },
        _initEvent:function(){
            vc.on('viewMenuInfo','chooseMenu',function(_app){
                vc.copyObject(_app, vc.component.viewMenuInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewMenuInfo);
            });

            vc.on('viewMenuInfo', 'onIndex', function(_index){
                vc.component.viewMenuInfo.index = _index;
            });

        },
        methods:{

            _openSelectMenuInfoModel(){
                vc.emit('chooseMenu','openChooseMenuModel',{});
            },
            _openAddMenuInfoModel(){
                vc.emit('addMenu','openAddMenuModal',{});
            },
            _loadMenuInfoData:function(){

            }
        }
    });

})(window.vc);
