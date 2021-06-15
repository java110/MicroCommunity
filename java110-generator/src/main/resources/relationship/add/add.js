(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            @@columnTemplateCode@@ViewInfo:{
                flowComponent:'@@columnTemplateCode@@View',
                @@templateCodeColumns@@
            }
        },
        watch:{
            @@columnTemplateCode@@ViewInfo:{
                deep: true,
                handler:function(){
                    vc.component.save@@ColumnTemplateCode@@Info();
                }
             }
        },
         _initMethod:function(){

         },
         _initEvent:function(){

            vc.on('@@columnTemplateCode@@ViewInfo', 'onIndex', function(_index){
                vc.component.@@columnTemplateCode@@ViewInfo.index = _index;
            });
        },
        methods:{
            @@columnTemplateCode@@Validate(){
                return vc.validate.validate({
                    @@columnTemplateCode@@ViewInfo:vc.component.@@columnTemplateCode@@ViewInfo
                },{
                    @@addTemplateCodeValidate@@
                });
            },
            save@@ColumnTemplateCode@@Info:function(){
                if(vc.component.@@columnTemplateCode@@Validate()){
                    //侦听回传
                    vc.emit($props.callBackListener,$props.callBackFunction, vc.component.@@columnTemplateCode@@ViewInfo);
                    return ;
                }
            }
        }
    });

})(window.vc);