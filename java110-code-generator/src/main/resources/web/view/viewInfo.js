/**
    @@templateName@@ 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            view@@TemplateCode@@Info:{
                index:0,
                flowComponent:'@@TemplateCode@@',
                @@templateCodeColumns@@
            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._load@@TemplateCode@@InfoData();
        },
        _initEvent:function(){
            vc.on('view@@TemplateCode@@Info','choose@@TemplateCode@@',function(_app){
                vc.copyObject(_app, vc.component.view@@TemplateCode@@Info);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.view@@TemplateCode@@Info);
            });

            vc.on('view@@TemplateCode@@Info', 'onIndex', function(_index){
                vc.component.view@@TemplateCode@@Info.index = _index;
            });

        },
        methods:{

            _openSelect@@TemplateCode@@InfoModel(){
                vc.emit('choose@@TemplateCode@@','openChoose@@TemplateCode@@Model',{});
            },
            _openAdd@@TemplateCode@@InfoModel(){
                vc.emit('add@@TemplateCode@@','openAdd@@TemplateCode@@Modal',{});
            },
            _load@@TemplateCode@@InfoData:function(){

            }
        }
    });

})(window.vc);