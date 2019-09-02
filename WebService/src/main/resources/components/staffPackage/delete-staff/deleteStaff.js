(function(vc){
    vc.extends({
        data:{
            deleteStaffInfo:{}
        },
        _initEvent:function(){
             vc.component.$on('delete_staff_event',function(_staffInfo){
                    vc.component.deleteStaffInfo = _staffInfo;
                    $('#deleteStaffModel').modal('show');
                });
        },
        methods:{
            closeDeleteStaffModel:function(){
                $('#deleteStaffModel').modal('hide');
            },
            deleteStaff:function(){
                console.log("开始删除工号：",vc.component.deleteStaffInfo);
                vc.http.post(
                    'deleteStaff',
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
            }
        }
    });
})(window.vc);