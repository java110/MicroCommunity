(function(vc,vm){

    vc.extends({
        data:{
            editOrgInfo:{
                orgId:'',
orgName:'',
orgLevel:'',
parentOrgId:'',
description:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editOrg','openEditOrgModal',function(_params){
                vc.component.refreshEditOrgInfo();
                $('#editOrgModel').modal('show');
                vc.copyObject(_params, vc.component.editOrgInfo );
                vc.component.editOrgInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editOrgValidate:function(){
                        return vc.validate.validate({
                            editOrgInfo:vc.component.editOrgInfo
                        },{
                            'editOrgInfo.orgName':[
{
                            limit:"required",
                            param:"",
                            errInfo:"组织名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"组织名称长度为2至50"
                        },
                    ],
'editOrgInfo.orgLevel':[
{
                            limit:"required",
                            param:"",
                            errInfo:"组织级别不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"报修人名称必须在2至50字符之间"
                        },
                    ],
'editOrgInfo.parentOrgId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"上级ID不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"上级ID不正确"
                        },
                    ],
'editOrgInfo.description':[
{
                            limit:"required",
                            param:"",
                            errInfo:"描述不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"描述不能为空"
                        },
                    ],
'editOrgInfo.orgId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"组织ID不能为空"
                        }]

                        });
             },
            editOrg:function(){
                if(!vc.component.editOrgValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editOrg',
                    'update',
                    JSON.stringify(vc.component.editOrgInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editOrgModel').modal('hide');
                             vc.emit('orgManage','listOrg',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            refreshEditOrgInfo:function(){
                vc.component.editOrgInfo= {
                  orgId:'',
orgName:'',
orgLevel:'',
parentOrgId:'',
description:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
