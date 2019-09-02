(function(vc,vm){

    vc.extends({
        data:{
            deleteServiceImplInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteServiceImpl','openDeleteServiceImplModal',function(_params){

                vc.component.deleteServiceImplInfo = _params;
                $('#deleteServiceImplModel').modal('show');

            });
        },
        methods:{
            deleteServiceImpl:function(){
                vc.component.deleteServiceImplInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteServiceImpl',
                    'delete',
                    JSON.stringify(vc.component.deleteServiceImplInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteServiceImplModel').modal('hide');
                            vc.emit('serviceImplManage','listServiceImpl',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteServiceImplModel:function(){
                $('#deleteServiceImplModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
