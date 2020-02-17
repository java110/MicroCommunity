(function(vc){

    vc.extends({
        data:{
            editPrivilegeGroupInfo:{
                pgId:'',
                name:'',
                description:'',
                errorInfo:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editPrivilegeGroup','openPrivilegeGroupModel',function(_params){
                vc.copyObject(_params,vc.component.editPrivilegeGroupInfo)
                $('#editPrivilegeGroupModel').modal('show');
            });
        },
        methods:{
            editPrivilegeGroupValidate(){
                return vc.validate.validate({
                    editPrivilegeGroupInfo:vc.component.editPrivilegeGroupInfo
                },{
                    'editPrivilegeGroupInfo.pgId':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"权限组ID不能为空"
                        }

                    ],
                    'editPrivilegeGroupInfo.name':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"权限组名不能为空"
                        },
                        {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"权限组名长度必须在2位至10位"
                        },
                    ],
                    'editPrivilegeGroupInfo.description':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"权限组描述不能为空"
                        },
                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"权限组描述长度不能超过200位"
                        },
                    ]

                });
            },
            saveEditPrivilegeGroup:function(){
                if(!vc.component.editPrivilegeGroupValidate()){
                    vc.component.editPrivilegeGroupInfo.errorInfo = vc.validate.errInfo;
                    return ;
                }
                vc.component.editPrivilegeGroupInfo.errorInfo = "";
                vc.http.post(
                    'editPrivilegeGroup',
                    'editPrivilegeGroupInfo',
                    JSON.stringify(vc.component.editPrivilegeGroupInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editPrivilegeGroupModel').modal('hide');
                            vc.component.clearEditPrivilegeGroupInfo();
                            vc.component.$emit('privilegeGroup_loadPrivilegeGroup',{});
                            return ;
                        }
                        vc.component.editPrivilegeGroupInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.editPrivilegeGroupInfo.errorInfo = errInfo;
                     });
            },
            clearEditPrivilegeGroupInfo:function(){
                vc.component.editPrivilegeGroupInfo = {
                    pgId:'',
                    name:'',
                    description:'',
                    errorInfo:''
                };
            }
        }
    });

})(window.vc);