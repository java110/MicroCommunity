(function(vc){

    vc.extends({
        data:{
            addPrivilegeInfo:{
                _currentPgId:'',
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
                vc.component.addPrivilegeInfo._currentPgId = _params.pgId;
                //查询没有添加的权限
                vc.component.listNoAddPrivilege();
            });
        },
        methods:{
            listNoAddPrivilege:function(){
                vc.component.addPrivilegeInfo._noAddPrivilege=[];
                var param = {
                    params:{
                        pgId:vc.component.addPrivilegeInfo._currentPgId
                    }
                }
                vc.http.get(
                            'addPrivilege',
                            'listNoAddPrivilege',
                             param,
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
            addPrivilegeToPrivilegeGroup:function(_privilegeInfo){

                vc.component.addPrivilegeInfo.errorInfo = "";
                _privilegeInfo.pgId = vc.component.addPrivilegeInfo._currentPgId;
                vc.http.post(
                    'addPrivilege',
                    'addPrivilegeToPrivilegeGroup',
                    JSON.stringify(_privilegeInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            vc.component.listNoAddPrivilege();
                            vc.component.$emit('privilege_loadPrivilege',vc.component.addPrivilegeInfo._currentPgId);
                            return ;
                        }
                        vc.component.addPrivilegeInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.addPrivilegeInfo.errorInfo = errInfo;
                     });
            }
        }
    });

})(window.vc);