(function(vc,vm){

    vc.extends({
        data:{
            deleteAppInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            //  vc.on('deleteApp','openDeleteAppModal',function(_params){
            //      alert("监听成功");
            //      console.log(_params);
            //     vc.component.deleteAppInfo = _params;
            //     $('#deleteAppModel').modal('show');
            //
            // });
        },
        methods:{
            deleteApp:function(){
                vc.component.deleteAppInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteApp',
                    'delete',
                    JSON.stringify(vc.component.deleteAppInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteAppModel').modal('hide');
                            vc.emit('appManage','listApp',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteAppModel:function(){
                $('#deleteAppModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
