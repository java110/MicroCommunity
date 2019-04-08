(function(vc){

    vc.extends({
        data:{
            addPrivilegeInfo:{
                name:'',
                description:'',
                errorInfo:'',
                _noAddPrivilege:[]
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.component.$on('addPrivilege_openPrivilegeModel',function(_params){
                $('#addPrivilegeModel').modal('show');
                //查询没有添加的权限
                vc.component.listNoAddPrivilege();
            });
        },
        methods:{
            listNoAddPrivilege:function(){
                vc.component.addPrivilegeInfo._noAddPrivilege=[];
                var param = {
                    _id:'123'
                }
                vc.http.get(
                            'addPrivilege',
                            'listNoAddPrivilege',
                            JSON.stringify(param),
                             function(json,res){
                                //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                if(res.status == 200){
                                    vc.component.addPrivilegeInfo._noAddPrivilege = JSON.parse(json);
                                    return ;
                                }
                                vc.component.addPrivilegeInfo.errorInfo = json;
                             },
                             function(errInfo,error){
                                console.log('请求失败处理');

                                vc.component.addPrivilegeInfo.errorInfo = errInfo;
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