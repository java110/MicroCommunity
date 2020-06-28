(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            add@@TemplateCode@@Info:{
                @@templateKey@@:'',
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
                    @@addTemplateCodeValidate@@



                });
            },
            save@@TemplateCode@@Info:function(){
                if(!vc.component.add@@TemplateCode@@Validate()){
                    vc.toast(vc.validate.errInfo);

                    return ;
                }

                vc.component.add@@TemplateCode@@Info.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.add@@TemplateCode@@Info);
                    $('#add@@TemplateCode@@Model').modal('hide');
                    return ;
                }

                vc.http.apiPost(
                    '@@templateCode@@.save@@TemplateCode@@',
                    JSON.stringify(vc.component.add@@TemplateCode@@Info),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        let _json = JSON.parse(json);
                        if (_json.code == 0) {
                            //关闭model
                            $('#add@@TemplateCode@@Model').modal('hide');
                            vc.component.clearAdd@@TemplateCode@@Info();
                            vc.emit('@@templateCode@@Manage','list@@TemplateCode@@',{});

                            return ;
                        }
                        vc.message(_json.msg);

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