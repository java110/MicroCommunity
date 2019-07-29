(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            @@templateCode@@ViewInfo:{
                @@templateCodeColumns@@
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){

            vc.on('@@templateCode@@ViewInfo', 'onIndex', function(_index){
                vc.component.@@templateCode@@ViewInfo.index = _index;
            });
        },
        methods:{
            add@@TemplateCode@@Validate(){
                return vc.validate.validate({
                    @@templateCode@@ViewInfo:vc.component.@@templateCode@@ViewInfo
                },{
                    @@addTemplateCodeValidate@@
                });
            },
            save@@TemplateCode@@Info:function(){
                if(!vc.component.@@templateCode@@Validate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.@@templateCode@@ViewInfo.communityId = vc.getCurrentCommunity().communityId;

                //侦听回传
            }
        }
    });

})(window.vc);