(function(vc,vm){

    vc.extends({
        data:{
            deleteServiceProvideInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteServiceProvide','openDeleteServiceProvideModal',function(_params){

                vc.component.deleteServiceProvideInfo = _params;
                $('#deleteServiceProvideModel').modal('show');

            });
        },
        methods:{
            deleteServiceProvide:function(){
                vc.component.deleteServiceProvideInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteServiceProvide',
                    'delete',
                    JSON.stringify(vc.component.deleteServiceProvideInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteServiceProvideModel').modal('hide');
                            vc.emit('serviceProvideManage','listServiceProvide',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteServiceProvideModel:function(){
                $('#deleteServiceProvideModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
