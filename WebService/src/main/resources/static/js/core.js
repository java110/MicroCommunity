/**
初始化处理
**/
(function(window, undefined){
    "use strict";
   var vc = window.vc || {};
    vc = {
        version:"v0.0.1",
        name:"vue component",
        author:'java110',
        component:new Vue()
    };

    var component = window.component || new Vue({
        methods:{

        }
    });
   //通知window对象
   window.vc = vc;
})(window);

/**
    vc 函数初始化
    add by wuxw
**/
(function(vc){
    vc.http = {
        call:function(componentCode,componentMethod,param,options,successCallback,errorCallback){
        console.log(successCallback)
                Vue.http.post('/callComponent/'+componentCode +"/"+componentMethod, param, options)
                .then(function(res){
                    successCallback(res.bodyText,res);
                }, function(error){
                    errorCallback(error.bodyText,error);
                });
            }

    };

    //绑定跳转函数
    vc.jumpToPage = function(url){
                                    window.location.href = url;
                                };
})(window.vc);