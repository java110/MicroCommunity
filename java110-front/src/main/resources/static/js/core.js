/**
 初始化处理 vue component
 **/
(function (window, undefined) {
    "use strict";
    var vc = window.vc || {};
    var _vmOptions = {};
    var _initMethod = [];
    var _initEvent = [];
    var _component = {};
    var _destroyedMethod = [];
    var _timers = [];//定时器
    var _map = [];// 共享数据存储
    var _namespace = [];


    _vmOptions = {
        el: '#component',
        data: {},
        watch: {},
        methods: {},
        destroyed: function () {
            window.vc.destroyedMethod.forEach(function (eventMethod) {
                eventMethod();
            });
            //清理所有定时器

            window.vc.timers.forEach(function (timer) {
                clearInterval(timer);
            });

            _timers = [];
        }

    };
    vc = {
        version: "v0.0.1",
        name: "vue component",
        author: 'java110',
        vmOptions: _vmOptions,
        namespace: _namespace,
        initMethod: _initMethod,
        initEvent: _initEvent,
        component: _component,
        destroyedMethod: _destroyedMethod,
        debug: false,
        timers: _timers,
        _map: {}
    };
    //通知window对象
    window.vc = vc;
})(window);

/**
 vc 函数初始化
 add by wuxw
 **/
(function (vc) {
    var DEFAULT_NAMESPACE = "default";
    vc.http = {
        post: function (componentCode, componentMethod, param, options, successCallback, errorCallback) {
            vc.loading('open');
            Vue.http.post('/callComponent/' + componentCode + "/" + componentMethod, param, options)
                .then(function (res) {
                    successCallback(res.bodyText, res);
                    vc.loading('close');
                }, function (error) {
                    errorCallback(error.bodyText, error);
                    vc.loading('close');
                });
        },
        get: function (componentCode, componentMethod, param, successCallback, errorCallback) {
            //加入缓存机制
            var _getPath = '/' + componentCode + '/' + componentMethod;
            if (vc.constant.GET_CACHE_URL.includes(_getPath)) {
                var _cacheData = vc.getData(_getPath);
                //浏览器缓存中能获取到
                if (_cacheData != null && _cacheData != undefined) {
                    successCallback(JSON.stringify(_cacheData), {status: 200});
                    return;
                }
            }
            vc.loading('open');
            Vue.http.get('/callComponent/' + componentCode + "/" + componentMethod, param)
                .then(function (res) {
                    successCallback(res.bodyText, res);
                    if (vc.constant.GET_CACHE_URL.includes(_getPath) && res.status == 200) {
                        vc.saveData(_getPath, JSON.parse(res.bodyText));
                    }
                    vc.loading('close');
                }, function (error) {
                    errorCallback(error.bodyText, error);
                    vc.loading('close');
                });
        },
        upload: function (componentCode, componentMethod, param, options, successCallback, errorCallback) {
            vc.loading('open');
            Vue.http.post('/callComponent/upload/' + componentCode + "/" + componentMethod, param, options)
                .then(function (res) {
                    successCallback(res.bodyText, res);
                    vc.loading('close');
                }, function (error) {
                    errorCallback(error.bodyText, error);
                    vc.loading('close');
                });
        },

    };

    var vmOptions = vc.vmOptions;
    //继承方法,合并 _vmOptions 的数据到 vmOptions中
    vc.extends = function (_vmOptions) {
        if (typeof _vmOptions !== "object") {
            throw "_vmOptions is not Object";
        }
        var nameSpace = DEFAULT_NAMESPACE;
        if (_vmOptions.hasOwnProperty("namespace")) {
            nameSpace = _vmOptions.namespace;
            vc.namespace.push({
                namespace:_vmOptions.namespace,
            })
        }

        //处理 data 对象

        if (_vmOptions.hasOwnProperty('data')) {
            for (var dataAttr in _vmOptions.data) {
                if (nameSpace == DEFAULT_NAMESPACE) {
                    vmOptions.data[dataAttr] = _vmOptions.data[dataAttr];
                } else {
                    if(!vmOptions.data.hasOwnProperty(nameSpace)){
                        vmOptions.data[nameSpace] = {};
                    }
                    var dataNameSpace = vmOptions.data[nameSpace];
                    dataNameSpace[dataAttr] = _vmOptions.data[dataAttr];
                }
            }
        }
        //处理methods 对象
        if (_vmOptions.hasOwnProperty('methods')) {
            for (var methodAttr in _vmOptions.methods) {
                if (nameSpace == DEFAULT_NAMESPACE) {
                    vmOptions.methods[methodAttr] = _vmOptions.methods[methodAttr];
                } else {
                    if(!vmOptions.methods.hasOwnProperty(nameSpace)){
                        vmOptions.methods[nameSpace] = {};
                    }
                    var methodNameSpace = vmOptions.methods[nameSpace];
                    methodNameSpace[methodAttr] = _vmOptions.data[methodAttr];
                    /*vmOptions.methods[nameSpace][methodAttr] = _vmOptions.methods[methodAttr];*/
                }
            }
        }
        //处理methods 对象
        if (_vmOptions.hasOwnProperty('watch')) {
            for (var watchAttr in _vmOptions.watch) {
                if (nameSpace == DEFAULT_NAMESPACE) {
                    vmOptions.watch[watchAttr] = _vmOptions.watch[watchAttr];
                } else {
                    if(!vmOptions.watch.hasOwnProperty(nameSpace)){
                        vmOptions.watch[nameSpace] = {};
                    }
                    var methodNameSpace = vmOptions.watch[nameSpace];
                    methodNameSpace[watchAttr] = _vmOptions.watch[watchAttr];
                    /*vmOptions.watch[nameSpace][watchAttr] = _vmOptions.watch[watchAttr];*/
                }
            }
        }
        //处理_initMethod 初始化执行函数
        if (_vmOptions.hasOwnProperty('_initMethod')) {
            vc.initMethod.push(_vmOptions._initMethod);
        }
        //处理_initEvent
        if (_vmOptions.hasOwnProperty('_initEvent')) {
            vc.initEvent.push(_vmOptions._initEvent);
        }

        //处理_initEvent_destroyedMethod
        if (_vmOptions.hasOwnProperty('_destroyedMethod')) {
            vc.destroyedMethod.push(_vmOptions._destroyedMethod);
        }


    };


    //绑定跳转函数
    vc.jumpToPage = function (url) {
        window.location.href = url;
    };
    //保存菜单
    vc.setCurrentMenu = function (_menuId) {
        window.localStorage.setItem('hc_menuId', _menuId);
    };
    //获取菜单
    vc.getCurrentMenu = function () {
        return window.localStorage.getItem('hc_menuId');
    };

    //保存用户菜单
    vc.setMenus = function (_menus) {
        window.localStorage.setItem('hc_menus', JSON.stringify(_menus));
    };
    //获取用户菜单
    vc.getMenus = function () {
        return JSON.parse(window.localStorage.getItem('hc_menus'));
    };

    //保存菜单状态
    vc.setMenuState = function (_menuState) {
        window.localStorage.setItem('hc_menu_state', _menuState);
    };
    //获取菜单状态
    vc.getMenuState = function () {
        return window.localStorage.getItem('hc_menu_state');
    };

    //保存用户菜单
    vc.saveData = function (_key, _value) {
        window.localStorage.setItem(_key, JSON.stringify(_value));
    };
    //获取用户菜单
    vc.getData = function (_key) {
        return JSON.parse(window.localStorage.getItem(_key));
    };

    //保存当前小区信息 _communityInfo : {"communityId":"123213","name":"测试小区"}
    vc.setCurrentCommunity = function (_currentCommunityInfo) {
        window.localStorage.setItem('hc_currentCommunityInfo', JSON.stringify(_currentCommunityInfo));
    };

    //获取当前小区信息
    // @return   {"communityId":"123213","name":"测试小区"}
    vc.getCurrentCommunity = function () {
        return JSON.parse(window.localStorage.getItem('hc_currentCommunityInfo'));
    };

    //保存当前小区信息 _communityInfos : [{"communityId":"123213","name":"测试小区"}]
    vc.setCommunitys = function (_communityInfos) {
        window.localStorage.setItem('hc_communityInfos', JSON.stringify(_communityInfos));
    };

    //获取当前小区信息
    // @return   {"communityId":"123213","name":"测试小区"}
    vc.getCommunitys = function () {
        return JSON.parse(window.localStorage.getItem('hc_communityInfos'));
    };

    //删除缓存数据
    vc.clearCacheData = function () {
        window.localStorage.clear();
    };

    //将org 对象的属性值赋值给dst 属性名为一直的属性
    vc.copyObject = function (org, dst) {
        //for(key in Object.getOwnPropertyNames(dst)){
        for (var key in dst) {
            if (org.hasOwnProperty(key)) {
                dst[key] = org[key]
            }
        }
    };
    //扩展 现有的对象 没有的属性扩充上去
    vc.extendObject = function (org, dst) {
        for (var key in dst) {
            if (!org.hasOwnProperty(key)) {
                dst[key] = org[key]
            }
        }
    };
    //获取url参数
    vc.getParam = function (_key) {
        //返回当前 URL 的查询部分（问号 ? 之后的部分）。
        var urlParameters = location.search;
        //如果该求青中有请求的参数，则获取请求的参数，否则打印提示此请求没有请求的参数
        if (urlParameters.indexOf('?') != -1) {
            //获取请求参数的字符串
            var parameters = decodeURI(urlParameters.substr(1));
            //将请求的参数以&分割中字符串数组
            parameterArray = parameters.split('&');
            //循环遍历，将请求的参数封装到请求参数的对象之中
            for (var i = 0; i < parameterArray.length; i++) {
                if (_key == parameterArray[i].split('=')[0]) {
                    return parameterArray[i].split('=')[1];
                }
            }
        }

        return "";
    };
    //查询url
    vc.getUrl = function () {
        //返回当前 URL 的查询部分（问号 ? 之后的部分）。
        var urlParameters = location.pathname;
        return urlParameters;
    };
    //对象转get参数
    vc.objToGetParam = function (obj) {
        var str = [];
        for (var p in obj)
            if (obj.hasOwnProperty(p)) {
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
        return str.join("&");
    };
    //空判断 true 为非空 false 为空
    vc.notNull = function (_paramObj) {
        if (_paramObj == null || _paramObj == undefined || _paramObj.trim() == '') {
            return false;
        }
        return true;
    };
    //设置debug 模式
    vc.setDebug = function (_param) {
        vc.debug = _param;
    };
    //数据共享存放 主要为了组件间传递数据
    vc.put = function (_key, _value) {
        vc.map[_key] = _value;
    };
    //数据共享 获取 主要为了组件间传递数据
    vc.get = function (_key) {
        return vc.map[_key];
    };


})(window.vc);

/**
 vc 定时器处理
 **/
(function (w, vc) {

    /**
     创建定时器
     **/
    vc.createTimer = function (func, sec) {
        var _timer = w.setInterval(func, sec);
        vc.timers.push(_timer); //这里将所有的定时器保存起来，页面退出时清理

        return _timer;
    };
    //清理定时器
    vc.clearTimer = function (timer) {
        clearInterval(timer);
    }


})(window, window.vc);

/**
 时间处理工具类
 **/
(function (vc) {
    function add0(m) {
        return m < 10 ? '0' + m : m
    }

    vc.dateFormat = function (shijianchuo) {
        //shijianchuo是整数，否则要parseInt转换
        var time = new Date(parseInt(shijianchuo));
        var y = time.getFullYear();
        var m = time.getMonth() + 1;
        var d = time.getDate();
        var h = time.getHours();
        var mm = time.getMinutes();
        var s = time.getSeconds();
        return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
    }

})(window.vc);


(function (vc) {

    vc.propTypes = {
        string: "string",//字符串类型
        array: "array",
        object: "object",
        number: "number"
    }

})(window.vc);

/**
 toast
 **/
(function (vc) {
    vc.toast = function Toast(msg, duration) {
        duration = isNaN(duration) ? 3000 : duration;
        var m = document.createElement('div');
        m.innerHTML = msg;
        m.style.cssText = "max-width:60%;min-width: 150px;padding:0 14px;height: 40px;color: rgb(255, 255, 255);line-height: 40px;text-align: center;border-radius: 4px;position: fixed;top: 30%;left: 50%;transform: translate(-50%, -50%);z-index: 999999;background: rgba(0, 0, 0,.7);font-size: 16px;";
        document.body.appendChild(m);
        setTimeout(function () {
            var d = 0.5;
            m.style.webkitTransition = '-webkit-transform ' + d + 's ease-in, opacity ' + d + 's ease-in';
            m.style.opacity = '0';
            setTimeout(function () {
                document.body.removeChild(m)
            }, d * 1000);
        }, duration);
    }
})(window.vc);