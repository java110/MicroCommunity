(function(vc){

    vc.extends({
        data:{
            add@@TemplateCode@@Info:{
                @@templateCodeColumns@@
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('add@@TemplateCode@@','openAdd@@TemplateCode@@Modal',function(){
                $('#add@@TemplateCode@@Model').modal('show');
            });
        },
        methods:{
            add@@TemplateCode@@Validate(){
                return vc.validate.validate({
                    add@@TemplateCode@@Info:vc.component.add@@TemplateCode@@Info
                },{
                    'add@@TemplateCode@@Info.name':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"楼名称不能为空"
                        },
                        {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"楼名称长度必须在2位至10位"
                        },
                    ]

                });
            },
            save@@TemplateCode@@Info:function(){
                if(!vc.component.add@@TemplateCode@@Validate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.add@@TemplateCode@@Info.communityId = vc.getCurrentCommunity().communityId;

                vc.http.post(
                    'add@@TemplateCode@@',
                    'save',
                    JSON.stringify(vc.component.add@@TemplateCode@@Info),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#add@@TemplateCode@@Model').modal('hide');
                            vc.component.clearAdd@@TemplateCode@@Info();
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
            clearAdd@@TemplateCode@@Info:function(){
                vc.component.add@@TemplateCode@@Info = {
                                            @@templateCodeColumns@@
                                        };
            }
        }
    });

})(window.vc);