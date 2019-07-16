/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            serviceBindingInfo:{
                $step:'',
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){

        },
        methods:{
            _initStep:function(){
                vc.component.serviceBindingInfo.$step = $("#step");
                vc.component.serviceBindingInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择应用", "选择服务", "确认绑定"]
                });
                //vc.component.serviceBindingInfo.step = $step.getIndex();
            },
            _prevStep:function(){
                vc.component.serviceBindingInfo.$step.prevStep();
                //vc.component.serviceBindingInfo.step = $step.getIndex();
            },
            _nextStep:function(){
                vc.component.serviceBindingInfo.$step.nextStep();
                //vc.component.serviceBindingInfo.step = $step.getIndex();
            }
        }
    });
})(window.vc);
