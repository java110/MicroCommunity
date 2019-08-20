/**
    权限 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewBasePrivilegeInfo:{
                index:0,
                flowComponent:'viewBasePrivilegeInfo',
                name:'',
domain:'',
description:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadBasePrivilegeInfoData();
        },
        _initEvent:function(){
            vc.on('viewBasePrivilegeInfo','chooseBasePrivilege',function(_app){
                vc.copyObject(_app, vc.component.viewBasePrivilegeInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewBasePrivilegeInfo);
            });

            vc.on('viewBasePrivilegeInfo', 'onIndex', function(_index){
                vc.component.viewBasePrivilegeInfo.index = _index;
            });

        },
        methods:{

            _openSelectBasePrivilegeInfoModel(){
                vc.emit('chooseBasePrivilege','openChooseBasePrivilegeModel',{});
            },
            _openAddBasePrivilegeInfoModel(){
                vc.emit('addBasePrivilege','openAddBasePrivilegeModal',{});
            },
            _loadBasePrivilegeInfoData:function(){

            }
        }
    });

})(window.vc);
