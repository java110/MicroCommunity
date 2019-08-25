(function(vc,vm){

    vc.extends({
        data:{
            deleteMenuInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteMenu','openDeleteMenuModal',function(_params){

                vc.component.deleteMenuInfo = _params;
                $('#deleteMenuModel').modal('show');

            });
        },
        methods:{
            deleteMenu:function(){
                vc.component.deleteMenuInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteMenu',
                    'delete',
                    JSON.stringify(vc.component.deleteMenuInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteMenuModel').modal('hide');
                            vc.emit('menuManage','listMenu',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteMenuModel:function(){
                $('#deleteMenuModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
