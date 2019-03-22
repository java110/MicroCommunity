/**
    初始化vue 对象
    @param vc vue component对象
    @param vmOptions Vue参数
**/
(function(vc,vmOptions){

    var vm = new Vue(vmOptions);

    window.vc.component = vm;
})(window.vc,window.vc.vmOptions);