(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addAuditUserInfo:{
                auditUserId:'',
                userId:'',
userName:'',
auditLink:'',
objCode:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addAuditUser','openAddAuditUserModal',function(){
                $('#addAuditUserModel').modal('show');
            });
        },
        methods:{
            addAuditUserValidate(){
                return vc.validate.validate({
                    addAuditUserInfo:vc.component.addAuditUserInfo
                },{
                    'addAuditUserInfo.userId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"用户ID不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"用户ID必须为数字"
                        },
                    ],
'addAuditUserInfo.userName':[
{
                            limit:"required",
                            param:"",
                            errInfo:"用户名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"用户名称必须在2至50字符之间"
                        },
                    ],
'addAuditUserInfo.auditLink':[
{
                            limit:"required",
                            param:"",
                            errInfo:"审核环节不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"审核环节格式错误"
                        },
                    ],
'addAuditUserInfo.objCode':[
{
                            limit:"required",
                            param:"",
                            errInfo:"流程对象不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"64",
                            errInfo:"物品库存不能大于64位"
                        },
                    ],




                });
            },
            saveAuditUserInfo:function(){
                if(!vc.component.addAuditUserValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.addAuditUserInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.addAuditUserInfo);
                    $('#addAuditUserModel').modal('hide');
                    return ;
                }

                vc.http.post(
                    'addAuditUser',
                    'save',
                    JSON.stringify(vc.component.addAuditUserInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addAuditUserModel').modal('hide');
                            vc.component.clearAddAuditUserInfo();
                            vc.emit('auditUserManage','listAuditUser',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearAddAuditUserInfo:function(){
                vc.component.addAuditUserInfo = {
                                            userId:'',
userName:'',
auditLink:'',
objCode:'',

                                        };
            }
        }
    });

})(window.vc);
