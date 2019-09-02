(function(vc,vm){

    vc.extends({
        data:{
            deleteServiceInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteService','openDeleteServiceModal',function(_params){

                vc.component.deleteServiceInfo = _params;
                $('#deleteServiceModel').modal('show');

            });
        },
        methods:{
            deleteService:function(){
                vc.component.deleteServiceInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteService',
                    'delete',
                    JSON.stringify(vc.component.deleteServiceInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteServiceModel').modal('hide');
                            vc.emit('serviceManage','listService',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteServiceModel:function(){
                $('#deleteServiceModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
