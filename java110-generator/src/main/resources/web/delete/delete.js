(function(vc,vm){

    vc.extends({
        data:{
            delete@@TemplateCode@@Info:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('delete@@TemplateCode@@','openDelete@@TemplateCode@@Modal',function(_params){

                vc.component.delete@@TemplateCode@@Info = _params;
                $('#delete@@TemplateCode@@Model').modal('show');

            });
        },
        methods:{
            delete@@TemplateCode@@:function(){
                vc.component.delete@@TemplateCode@@Info.communityId=vc.getCurrentCommunity().communityId;
                vc.http.apiPost(
                    '@@templateCode@@.delete@@TemplateCode@@',
                    JSON.stringify(vc.component.delete@@TemplateCode@@Info),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#delete@@TemplateCode@@Model').modal('hide');
                            vc.emit('@@templateCode@@Manage','list@@TemplateCode@@',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDelete@@TemplateCode@@Model:function(){
                $('#delete@@TemplateCode@@Model').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);