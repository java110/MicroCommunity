(function(vc,vm){

    vc.extends({
        data:{
            edit@@TemplateCode@@Info:{
                @@templateCodeColumns@@
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('edit@@TemplateCode@@','openEdit@@TemplateCode@@Modal',function(_params){
                vc.component.refreshEdit@@TemplateCode@@Info();
                $('#edit@@TemplateCode@@Model').modal('show');
                vc.copyObject(_params, vc.component.edit@@TemplateCode@@Info );
                vc.component.edit@@TemplateCode@@Info.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            edit@@TemplateCode@@Validate:function(){
                        return vc.validate.validate({
                            edit@@TemplateCode@@Info:vc.component.edit@@TemplateCode@@Info
                        },{
                            @@editTemplateCodeValidate@@
                        });
             },
            edit@@TemplateCode@@:function(){
                if(!vc.component.edit@@TemplateCode@@Validate()){
                    vc.toast(vc.validate.errInfo);
                    return ;
                }

                vc.http.apiPost(
                    '@@templateCode@@.update@@TemplateCode@@',
                    JSON.stringify(vc.component.edit@@TemplateCode@@Info),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#edit@@TemplateCode@@Model').modal('hide');
                             vc.emit('@@templateCode@@Manage','list@@TemplateCode@@',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            refreshEdit@@TemplateCode@@Info:function(){
                vc.component.edit@@TemplateCode@@Info= {
                  @@templateCodeColumns@@
                }
            }
        }
    });

})(window.vc,window.vc.component);