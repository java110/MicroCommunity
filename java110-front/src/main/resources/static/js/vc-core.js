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
    vc.on = function (_componentName, _value, _callback) {

        vc.component.$on(_componentName + '_' + _value,
            function (param) {
                if (vc.consoleFlag) {
                    console.log("监听ON事件", _componentName, _value, param);
                }
                _callback(param);
            }
        );
    };

    /**
     事件触发
     **/
    vc.emit = function (_componentName, _value, _param) {
        if (vc.consoleFlag) {
            console.log("监听emit事件", _componentName, _value, _param);
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
})(window.vc);

