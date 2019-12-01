/**
 初始化vue 对象
 @param vc vue component对象
 @param vmOptions Vue参数
 **/
(function (vc, vmOptions) {
    console.log("vmOptions:", vmOptions);
    vc.component = new Vue(vmOptions);
})(window.vc, window.vc.vmOptions);


/**
 vc监听事件
 **/
(function (vc) {
    /**
     事件监听
     **/
    vc.on = function () {
        var _namespace = "";
        var _componentName = "";
        var _value = "";
        var _callback = undefined;
        if (arguments.length == 4) {
            _namespace = arguments[0];
            _componentName = arguments[1];
            _value = arguments[2];
            _callback = arguments[3];
        }else if(arguments.length == 3){
            _componentName = arguments[0];
            _value = arguments[1];
            _callback = arguments[2];
        }else{
            console.error("执行on 异常，vc.on 参数只能是3个 或4个");
            return;
        }
        if (vc.notNull(_namespace)) {
            vc.component.$on(_namespace + "_" + _componentName + '_' + _value,
                function (param) {
                    if (vc.debug) {
                        console.log("监听ON事件", _namespace, _componentName, _value, param);
                    }
                    _callback(param);
                }
            );
            return;
        }

        vc.component.$on(_componentName + '_' + _value,
            function (param) {
                if (vc.debug) {
                    console.log("监听ON事件", _componentName, _value, param);
                }
                _callback(param);
            }
        );
    };

    /**
     事件触发
     **/
    vc.emit = function () {
        var _namespace = "";
        var _componentName = "";
        var _value = "";
        var _callback = undefined;
        if (arguments.length == 4) {
            _namespace = arguments[0];
            _componentName = arguments[1];
            _value = arguments[2];
            _callback = arguments[3];
        }else if(arguments.length == 3){
            _componentName = arguments[0];
            _value = arguments[1];
            _callback = arguments[2];
        }else{
            console.error("执行on 异常，vc.on 参数只能是3个 或4个");
            return;
        }
        if (vc.debug) {
            console.log("监听emit事件", _componentName, _value, _param);
        }
        if (vc.notNull(_namespace)) {
            vc.component.$emit(_namespace + "_" + _componentName + '_' + _value, _param);
            return;
        }
        vc.component.$emit(_componentName + '_' + _value, _param);
    };

})(window.vc);

/**
 * vue对象 执行初始化方法
 */
(function (vc) {
    vc.initEvent.forEach(function (eventMethod) {
        eventMethod();
    });
    vc.initMethod.forEach(function (callback) {
        callback();
    });
    vc.namespace.forEach(function (_param) {
        vc[_param.namespace] = vc.component[_param.namespace];
    });
})(window.vc);

