(function(vc,vm){

    vc.extends({
        data:{
            deleteServiceRegisterInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteServiceRegister','openDeleteServiceRegisterModal',function(_params){

                vc.component.deleteServiceRegisterInfo = _params;
                $('#deleteServiceRegisterModel').modal('show');

            });
        },
        methods:{
            deleteServiceRegister:function(){
                //vc.component.deleteServiceRegisterInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteServiceRegister',
                    'delete',
                    JSON.stringify(vc.component.deleteServiceRegisterInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteServiceRegisterModel').modal('hide');
                            vc.emit('serviceRegisterManage','listServiceRegister',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteServiceRegisterModel:function(){
                $('#deleteServiceRegisterModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
