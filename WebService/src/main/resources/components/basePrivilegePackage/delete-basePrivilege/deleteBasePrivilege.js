(function(vc,vm){

    vc.extends({
        data:{
            deleteBasePrivilegeInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteBasePrivilege','openDeleteBasePrivilegeModal',function(_params){

                vc.component.deleteBasePrivilegeInfo = _params;
                $('#deleteBasePrivilegeModel').modal('show');

            });
        },
        methods:{
            deleteBasePrivilege:function(){
                vc.component.deleteBasePrivilegeInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteBasePrivilege',
                    'delete',
                    JSON.stringify(vc.component.deleteBasePrivilegeInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteBasePrivilegeModel').modal('hide');
                            vc.emit('basePrivilegeManage','listBasePrivilege',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteBasePrivilegeModel:function(){
                $('#deleteBasePrivilegeModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
