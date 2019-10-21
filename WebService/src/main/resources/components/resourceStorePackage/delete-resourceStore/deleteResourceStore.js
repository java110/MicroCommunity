(function(vc,vm){

    vc.extends({
        data:{
            deleteResourceStoreInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteResourceStore','openDeleteResourceStoreModal',function(_params){

                vc.component.deleteResourceStoreInfo = _params;
                $('#deleteResourceStoreModel').modal('show');

            });
        },
        methods:{
            deleteResourceStore:function(){
                vc.component.deleteResourceStoreInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteResourceStore',
                    'delete',
                    JSON.stringify(vc.component.deleteResourceStoreInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteResourceStoreModel').modal('hide');
                            vc.emit('resourceStoreManage','listResourceStore',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteResourceStoreModel:function(){
                $('#deleteResourceStoreModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
