/**
    审核人员 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewAuditUserInfo:{
                index:0,
                flowComponent:'viewAuditUserInfo',
                userId:'',
userName:'',
auditLink:'',
objCode:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadAuditUserInfoData();
        },
        _initEvent:function(){
            vc.on('viewAuditUserInfo','chooseAuditUser',function(_app){
                vc.copyObject(_app, vc.component.viewAuditUserInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewAuditUserInfo);
            });

            vc.on('viewAuditUserInfo', 'onIndex', function(_index){
                vc.component.viewAuditUserInfo.index = _index;
            });

        },
        methods:{

            _openSelectAuditUserInfoModel(){
                vc.emit('chooseAuditUser','openChooseAuditUserModel',{});
            },
            _openAddAuditUserInfoModel(){
                vc.emit('addAuditUser','openAddAuditUserModal',{});
            },
            _loadAuditUserInfoData:function(){

            }
        }
    });

})(window.vc);
