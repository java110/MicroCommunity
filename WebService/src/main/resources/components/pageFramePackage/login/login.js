(function (vc) {
    vc.extends({
        data: {
            loginInfo: {
                logo: '',
                username: 'wuxw',
                passwd: 'admin',
                validateCode: '',
                errorInfo: ''
            }
        },
        _initMethod: function () {
            vc.component.clearCacheData();
            vc.component._loadSysInfo();
        },
        _initEvent: function () {
            vc.component.$on('errorInfoEvent', function (_errorInfo) {
                vc.component.loginInfo.errorInfo = _errorInfo;
                console.log('errorInfoEvent 事件被监听', _errorInfo)
            });

            vc.component.$on('validate_code_component_param_change_event', function (params) {
                for (var tmpAttr in params) {
                    vc.component.loginInfo[tmpAttr] = params[tmpAttr];
                }
                console.log('errorInfoEvent 事件被监听', params)
            });
            vc.on('login', 'doLogin', function () {
                vc.component.doLogin();
            })
        },
        methods: {
            clearCacheData: function () {
                vc.clearCacheData();
            },
            _loadSysInfo: function () {
                var param = {
                    params: {
                        sys: 'HC'
                    }
                }
                vc.http.get(
                    'login',
                    'getSysInfo',
                    param,
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status != 200) {
                            console.log("加载系统信息失败");
                            vc.saveData("_sysInfo", {logo: 'HC'});
                            vc.copyObject(json, vc.component.loginInfo);
                            return;
                        }
                        vc.copyObject(json, vc.component.loginInfo);
                        //保存到浏览器
                        vc.saveData("_sysInfo", json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.saveData("_sysInfo", {logo: 'HC'});
                        vc.copyObject(json, vc.component.loginInfo);
                        vc.component.loginInfo.errorInfo = errInfo;
                    });
            },
            doLogin: function () {
                if (!vc.notNull(vc.component.loginInfo.username)) {
                    vc.toast('用户名不能为空');
                    return;
                }
                if (!vc.notNull(vc.component.loginInfo.passwd)) {
                    vc.toast('密码不能为空');
                    return;
                }
                if (!vc.notNull(vc.component.loginInfo.validateCode)) {
                    vc.toast('验证码不能为空');
                    return;
                }
                vc.http.post(
                    'login',
                    'doLogin',
                    JSON.stringify(vc.component.loginInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            vc.jumpToPage("/");
                            return;
                        }
                        vc.component.loginInfo.errorInfo = json;
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.component.loginInfo.errorInfo = errInfo;
                    });

            }
        },
        _destroyedMethod: function () {
            console.log("登录页面销毁调用");
        }
    });


})(window.vc);