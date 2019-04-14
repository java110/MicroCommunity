(function(vc,vm){

    vc.extends({
        data:{
            deleteStaffPrivilegeInfo:{
                _currentStaffId:'',
                _currentPId:'',
                _currentPFlag:'',
                deleteMessage:'',
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteStaffPrivilege','openStaffPrivilegeModel',function(_params){

                vc.component.deleteStaffPrivilegeInfo._currentStaffId = _params.staffId;
                vc.component.deleteStaffPrivilegeInfo._currentPId = _params._pId;
                if(_params.pgId == '-1'){

                   vc.component.deleteStaffPrivilegeInfo._currentPFlag = '0';
                   vc.component.deleteStaffPrivilegeInfo.deleteMessage = '确定是否删除当前权限';
                }else{
                   vc.component.deleteStaffPrivilegeInfo._currentPId = _params.pgId;
                   vc.component.deleteStaffPrivilegeInfo._currentPFlag = '1';
                    vc.component.deleteStaffPrivilegeInfo.deleteMessage = '您删除是权限组下权限，会删除整个权限组权限，确认是否删除';

                }
                $('#deleteStaffPrivilegeModel').modal('show');

            });
        },
        methods:{
            deleteStaffPrivilege:function(){
                var param = {
                    userId:vc.component.deleteStaffPrivilegeInfo._currentStaffId,
                    pId:vc.component.deleteStaffPrivilegeInfo._currentPId,
                    pFlag:vc.component.deleteStaffPrivilegeInfo._currentPFlag,
                }
                vc.http.post(
                    'deleteStaffPrivilege',
                    'delete',
                    JSON.stringify(vc.component.deleteStaffInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteStaffModel').modal('hide');
                            vc.component.$emit('deleteStaff_reload_event',{});
                            return ;
                        }
                        vc.component.deleteStaffInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.deleteStaffInfo.errorInfo = errInfo;
                     });
            },
            closeDeleteStaffPrivilegeModel:function(){
                $('#deleteStaffPrivilegeModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);