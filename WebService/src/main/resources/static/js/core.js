/**
初始化处理
**/
(function(window, undefined){
    "use strict";
   var vc = window.vc || {};
   var _vmOptions = window.vc.vmOptions || {};

   _vmOptions = {
        el:'#component',
        data:{

        },
        methods:{

        },

   };

    vc = {
        version:"v0.0.1",
        name:"vue component",
        author:'java110',
        vmOptions:_vmOptions
    };


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

    var vmOptions = vc.vmOptions;
    //继承方法,合并 _vmOptions 的数据到 vmOptions中
    vc.extends = function(_vmOptions){
        if(typeof _vmOptions !== Object){
            throw "_vmOptions is not Object"
        }
        //处理 data 对象
        if(_vmOptions.hasOwnProperty('data')){
            for(var dataAttr in _vmOptions.data){
                vmOptions.data[dataAttr] = _vmOptions.data[dataAttr];
            }
        }
        //处理methods 对象
        if(_vmOptions.hasOwnProperty('methods')){
            for(var dataAttr in _vmOptions.data){
                vmOptions.data[dataAttr] = _vmOptions.data[dataAttr];
            }
        }
    };

    //绑定跳转函数
    vc.jumpToPage = function(url){
                                    window.location.href = url;
                                };
})(window.vc);