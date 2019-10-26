(function(vc,vm){

    vc.extends({
        data:{
            editAuditUserInfo:{
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
             vc.on('editAuditUser','openEditAuditUserModal',function(_params){
                vc.component.refreshEditAuditUserInfo();
                $('#editAuditUserModel').modal('show');
                vc.copyObject(_params, vc.component.editAuditUserInfo );
                vc.component.editAuditUserInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editAuditUserValidate:function(){
                        return vc.validate.validate({
                            editAuditUserInfo:vc.component.editAuditUserInfo
                        },{
                            'editAuditUserInfo.userId':[
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
'editAuditUserInfo.userName':[
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
'editAuditUserInfo.auditLink':[
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
'editAuditUserInfo.objCode':[
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
'editAuditUserInfo.auditUserId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"审核ID不能为空"
                        }]

                        });
             },
            editAuditUser:function(){
                if(!vc.component.editAuditUserValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editAuditUser',
                    'update',
                    JSON.stringify(vc.component.editAuditUserInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editAuditUserModel').modal('hide');
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
            refreshEditAuditUserInfo:function(){
                vc.component.editAuditUserInfo= {
                  auditUserId:'',
userId:'',
userName:'',
auditLink:'',
objCode:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
