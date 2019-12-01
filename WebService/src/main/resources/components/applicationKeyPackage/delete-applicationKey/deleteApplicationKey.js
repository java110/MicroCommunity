(function(vc,vm){

    vc.extends({
        data:{
            deleteApplicationKeyInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteApplicationKey','openDeleteApplicationKeyModal',function(_params){

                vc.component.deleteApplicationKeyInfo = _params;
                $('#deleteApplicationKeyModel').modal('show');

            });
        },
        methods:{
            deleteApplicationKey:function(){
                vc.component.deleteApplicationKeyInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteApplicationKey',
                    'delete',
                    JSON.stringify(vc.component.deleteApplicationKeyInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteApplicationKeyModel').modal('hide');
                            vc.emit('applicationKeyManage','listApplicationKey',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteApplicationKeyModel:function(){
                $('#deleteApplicationKeyModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
