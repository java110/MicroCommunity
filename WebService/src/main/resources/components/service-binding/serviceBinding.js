/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            serviceBindingInfo:{
                $step:{},
                index:0,
                infos:[]
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("serviceBinding", "notify", function(_info){
                vc.component.serviceBindingInfo.infos[vc.component.serviceBindingInfo.index] = _info;
            });

        },
        methods:{
            _initStep:function(){
                vc.component.serviceBindingInfo.$step = $("#step");
                vc.component.serviceBindingInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择应用", "选择服务", "确认绑定"]
                });
                vc.component.serviceBindingInfo.index = vc.component.serviceBindingInfo.$step.getIndex();
            },
            _prevStep:function(){
                vc.component.serviceBindingInfo.$step.prevStep();
                vc.component.serviceBindingInfo.index = vc.component.serviceBindingInfo.$step.getIndex();
            },
            _nextStep:function(){
                var _currentData = vc.component.serviceBindingInfo.infos[vc.component.serviceBindingInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择相关信息");
                    return ;
                }
                vc.component.serviceBindingInfo.$step.nextStep();
                vc.component.serviceBindingInfo.index = vc.component.serviceBindingInfo.$step.getIndex();

                vc.emit('viewAppInfo', 'onIndex', vc.component.serviceBindingInfo.index);
                vc.emit('viewServiceInfo', 'onIndex', vc.component.serviceBindingInfo.index);
            },
            _finishStep:function(){

            }
        }
    });
})(window.vc);
