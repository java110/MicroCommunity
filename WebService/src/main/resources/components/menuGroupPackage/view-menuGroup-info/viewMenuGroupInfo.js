/**
    菜单组 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewMenuGroupInfo:{
                index:0,
                flowComponent:'viewMenuGroupInfo',
                name:'',
                icon:'',
                label:'',
                seq:'',
                description:'',
                gId:''
            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadMenuGroupInfoData();
        },
        _initEvent:function(){
            vc.on('viewMenuGroupInfo','chooseMenuGroup',function(_app){
                vc.copyObject(_app, vc.component.viewMenuGroupInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewMenuGroupInfo);
            });

            vc.on('viewMenuGroupInfo', 'onIndex', function(_index){
                vc.component.viewMenuGroupInfo.index = _index;
            });

        },
        methods:{

            _openSelectMenuGroupInfoModel(){
                vc.emit('chooseMenuGroup','openChooseMenuGroupModel',{});
            },
            _openAddMenuGroupInfoModel(){
                vc.emit('addMenuGroup','openAddMenuGroupModal',{});
            },
            _loadMenuGroupInfoData:function(){

            }
        }
    });

})(window.vc);
