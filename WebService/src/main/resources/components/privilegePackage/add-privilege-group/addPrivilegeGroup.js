(function(vc){

    vc.extends({
        data:{
            addPrivilegeGroupInfo:{
                name:'',
                description:'',
                errorInfo:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.component.$on('addPrivilegeGroup_openPrivilegeGroupModel',function(_params){
                $('#addPrivilegeGroupModel').modal('show');
            });
        },
        methods:{
            addPrivilegeGroupValidate(){
                return vc.validate.validate({
                    addPrivilegeGroupInfo:vc.component.addPrivilegeGroupInfo
                },{
                    'addPrivilegeGroupInfo.name':[
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
                    'addPrivilegeGroupInfo.description':[
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
            saveAddPrivilegeGroup:function(){
                if(!vc.component.addPrivilegeGroupValidate()){
                    vc.component.addPrivilegeGroupInfo.errorInfo = vc.validate.errInfo;
                    return ;
                }
                vc.component.addPrivilegeGroupInfo.errorInfo = "";
                vc.http.post(
                    'addPrivilegeGroup',
                    'savePrivilegeGroupInfo',
                    JSON.stringify(vc.component.addPrivilegeGroupInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addPrivilegeGroupModel').modal('hide');
                            vc.component.clearAddPrivilegeGroupInfo();
                            vc.component.$emit('privilegeGroup_loadPrivilegeGroup',{});
                            return ;
                        }
                        vc.component.addPrivilegeGroupInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.addPrivilegeGroupInfo.errorInfo = errInfo;
                     });
            },
            clearAddPrivilegeGroupInfo:function(){
                vc.component.addPrivilegeGroupInfo = {
                                            name:'',
                                            description:'',
                                            errorInfo:''
                                        };
            }
        }
    });

})(window.vc);