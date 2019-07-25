(function(vc,vm){

    vc.extends({
        data:{
            deleteMappingInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteMapping','openDeleteMappingModal',function(_params){

                vc.component.deleteMappingInfo = _params;
                $('#deleteMappingModel').modal('show');

            });
        },
        methods:{
            deleteMapping:function(){
                vc.component.deleteMappingInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteMapping',
                    'delete',
                    JSON.stringify(vc.component.deleteMappingInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteMappingModel').modal('hide');
                            vc.emit('mappingManage','listMapping',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteMappingModel:function(){
                $('#deleteMappingModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
