/**
初始化处理
**/
(function(window, undefined){
    "use strict";
   var vc = window.vc || {};
    vc = {
        version:"v0.0.1",
        name:"vue component"
    };
   //通知window对象
   window.vc = vc;
})(window);

/**
    异步请求后台
    add by wuxw
**/
(function(vc){
    vc.http = {
        call:function(componentCode,componentMethod,param,options,successCallback,errorCallback){
        console.log(successCallback)
                Vue.http.post('/callComponent/'+componentCode +"/"+componentMethod, param, options)
                .then(function(res){
                    successCallback(res.bodyText);
                }, function(error){
                    errorCallback(error);
                });
            }

    };
})(window.vc);