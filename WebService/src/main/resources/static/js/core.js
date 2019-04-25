/**
初始化处理 vue component
**/
(function(window, undefined){
    "use strict";
   var vc = window.vc || {};
   var _vmOptions = {};
   var _initMethod = [];
   var _initEvent = [];
   var _component = {};
   var _destroyedMethod = [];
   var _timers = [];//定时器

   _vmOptions = {
        el:'#component',
        data:{

        },
        watch: {

        },
        methods:{

        },
        destroyed:function(){
            window.vc.destroyedMethod.forEach(function(eventMethod){
                 eventMethod();
            });
            //清理所有定时器

            window.vc.timers.forEach(function(timer){
                clearInterval(timer);
            });

            _timers = [];
        }

   };
    vc = {
        version:"v0.0.1",
        name:"vue component",
        author:'java110',
        vmOptions:_vmOptions,
        initMethod:_initMethod,
        initEvent:_initEvent,
        component:_component,
        destroyedMethod:_destroyedMethod,
        timers:_timers
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
        post:function(componentCode,componentMethod,param,options,successCallback,errorCallback){
                Vue.http.post('/callComponent/'+componentCode +"/"+componentMethod, param, options)
                .then(function(res){
                    successCallback(res.bodyText,res);
                }, function(error){
                    errorCallback(error.bodyText,error);
                });
        },
        get:function(componentCode,componentMethod,param,successCallback,errorCallback){
                Vue.http.get('/callComponent/'+componentCode +"/"+componentMethod, param)
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
        if(typeof _vmOptions !== "object"){
            throw "_vmOptions is not Object";
        }
        //处理 data 对象
        if(_vmOptions.hasOwnProperty('data')){
            for(var dataAttr in _vmOptions.data){
                vmOptions.data[dataAttr] = _vmOptions.data[dataAttr];
            }
        }
        //处理methods 对象
        if(_vmOptions.hasOwnProperty('methods')){
            for(var methodAttr in _vmOptions.methods){
                vmOptions.methods[methodAttr] = _vmOptions.methods[methodAttr];
            }
        }
        //处理methods 对象
        if(_vmOptions.hasOwnProperty('watch')){
            for(var watchAttr in _vmOptions.watch){
                vmOptions.watch[watchAttr] = _vmOptions.watch[watchAttr];
            }
        }
        //处理_initMethod 初始化执行函数
        if(_vmOptions.hasOwnProperty('_initMethod')){
            vc.initMethod.push(_vmOptions._initMethod);
        }
        //处理_initEvent
        if(_vmOptions.hasOwnProperty('_initEvent')){
            vc.initEvent.push(_vmOptions._initEvent);
        }

         //处理_initEvent_destroyedMethod
        if(_vmOptions.hasOwnProperty('_destroyedMethod')){
            vc.destroyedMethod.push(_vmOptions._destroyedMethod);
        }


    };


    //绑定跳转函数
    vc.jumpToPage = function(url){
                                    window.location.href = url;
                                };
    //保存菜单
    vc.setCurrentMenu = function(_menuId){
        window.localStorage.setItem('hc_menuId',_menuId);
    };
    //获取菜单
    vc.getCurrentMenu = function(){
        return window.localStorage.getItem('hc_menuId');
    };

    //保存用户菜单
    vc.setMenus = function(_menus){
        window.localStorage.setItem('hc_menus',JSON.stringify(_menus));
    };
    //获取用户菜单
    vc.getMenus = function(){
        return JSON.parse(window.localStorage.getItem('hc_menus'));
    };

    //保存当前小区信息 _communityInfo : {"communityId":"123213","name":"测试小区"}
    vc.setCurrentCommunity = function(_currentCommunityInfo){
        window.localStorage.setItem('hc_currentCommunityInfo',JSON.stringify(_currentCommunityInfo));
    };

    //获取当前小区信息
    // @return   {"communityId":"123213","name":"测试小区"}
    vc.getCurrentCommunity = function(){
        return JSON.parse(window.localStorage.getItem('hc_currentCommunityInfo'));
    };

    //保存当前小区信息 _communityInfos : [{"communityId":"123213","name":"测试小区"}]
    vc.setCommunitys = function(_communityInfos){
        window.localStorage.setItem('hc_communityInfos',JSON.stringify(_communityInfos));
    };

    //获取当前小区信息
    // @return   {"communityId":"123213","name":"测试小区"}
    vc.getCommunitys = function(){
        return JSON.parse(window.localStorage.getItem('hc_communityInfos'));
    };

    //删除缓存数据
    vc.clearCacheData = function(){
        window.localStorage.clear();
    };



})(window.vc);

/**
    vc 定时器处理
**/
(function(w,vc){

    /**
        创建定时器
    **/
    vc.createTimer = function(func,sec){
        var _timer = w.setInterval(func,sec);
        vc.timers.push(_timer); //这里将所有的定时器保存起来，页面退出时清理

        return _timer;
    };
    //清理定时器
    vc.clearTimer = function(timer){
        clearInterval(timer);
    }


})(window,window.vc);

/**
    时间处理工具类
**/
(function(vc){
    function add0(m){return m<10?'0'+m:m }
    vc.dateFormat = function(shijianchuo){
      //shijianchuo是整数，否则要parseInt转换
      var time = new Date(parseInt(shijianchuo));
      var y = time.getFullYear();
      var m = time.getMonth()+1;
      var d = time.getDate();
      var h = time.getHours();
      var mm = time.getMinutes();
      var s = time.getSeconds();
      return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
    }
})(window.vc);