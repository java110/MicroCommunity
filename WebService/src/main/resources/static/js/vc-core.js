/**
    初始化vue 对象
    @param vc vue component对象
    @param vmOptions Vue参数
**/
(function(vc,vmOptions){
    console.log("vmOptions:",vmOptions);
    vc.component = new Vue(vmOptions);
})(window.vc,window.vc.vmOptions);

/**
 * vue对象 执行初始化方法
 */
(function(vc){
    vc.initEvent.forEach(function(eventMethod){
        eventMethod();
   });
   vc.initMethod.forEach(function(callback){
        callback();
   });
})(window.vc);