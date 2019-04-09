(function(vc){
    vc.extends({
        data:{
            deletePrivilegeInfo:{}
        },
        _initEvent:function(){
             vc.on('deletePrivilege','openDeletePrivilegeModel',function(_p){
                    vc.component.deletePrivilegeInfo = _p;
                    $('#deletePrivilegeModel').modal('show');
                });
        },
        methods:{
            closeDeletePrivilegeModel:function(){
                $('#deletePrivilegeModel').modal('hide');
            },
            deletePrivilege:function(){
                console.log("开始删除限组：",vc.component.deletePrivilegeInfo);
                vc.http.post(
                    'deletePrivilege',
                    'delete',
                    JSON.stringify(vc.component.deletePrivilegeInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deletePrivilegeGroupModel').modal('hide');
                            vc.emit('privilege','loadPrivilege',_p.pgId);
                            return ;
                        }
                        vc.component.deletePrivilegeInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.deletePrivilegeInfo.errorInfo = errInfo;
                     });
            }
        }
    });
})(window.vc);