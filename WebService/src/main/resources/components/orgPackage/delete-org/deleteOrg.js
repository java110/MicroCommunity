(function(vc,vm){

    vc.extends({
        data:{
            deleteOrgInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteOrg','openDeleteOrgModal',function(_params){

                vc.component.deleteOrgInfo = _params;
                $('#deleteOrgModel').modal('show');

            });
        },
        methods:{
            deleteOrg:function(){
                vc.component.deleteOrgInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteOrg',
                    'delete',
                    JSON.stringify(vc.component.deleteOrgInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteOrgModel').modal('hide');
                            vc.emit('orgManage','listOrg',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteOrgModel:function(){
                $('#deleteOrgModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
