(function(vc,vm){

    vc.extends({
        data:{
            deleteOrgCommunityInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteOrgCommunity','openDeleteOrgCommunityModal',function(_params){

                vc.component.deleteOrgCommunityInfo = _params;
                $('#deleteOrgCommunityModel').modal('show');

            });
        },
        methods:{
            deleteOrgCommunity:function(){
                vc.component.deleteOrgCommunityInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteOrgCommunity',
                    'delete',
                    JSON.stringify(vc.component.deleteOrgCommunityInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteOrgCommunityModel').modal('hide');
                            vc.emit('orgCommunityManage','listOrgCommunity',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteOrgCommunityModel:function(){
                $('#deleteOrgCommunityModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
