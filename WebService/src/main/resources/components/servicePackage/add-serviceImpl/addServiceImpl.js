(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addServiceImplInfo: {
                serviceBusinessId: '',
                businessTypeCd: '',
                name: '',
                invokeType: '',
                url: '',
                messageTopic: '',
                timeout: '60',
                retryCount: '3',
                description: '',

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('addServiceImpl', 'openAddServiceImplModal', function () {
                $('#addServiceImplModel').modal('show');
            });
        },
        methods: {
            addServiceImplValidate() {
                return vc.validate.validate({
                    addServiceImplInfo: vc.component.addServiceImplInfo
                }, {

                    'addServiceImplInfo.businessTypeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "业务类型不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,50",
                            errInfo: "业务类型必须在2至50字符之间"
                        },
                    ],
                    'addServiceImplInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "业务名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,50",
                            errInfo: "业务类型必须在2至50字符之间"
                        },
                    ],
                    'addServiceImplInfo.invokeType': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "调用类型不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,4",
                            errInfo: "调用类型错误"
                        },
                    ],
                    'addServiceImplInfo.url': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "调用地址不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "调用地址超过200位"
                        },
                    ],
                    'addServiceImplInfo.messageTopic': [
                        {
                            limit: "maxLength",
                            param: "50",
                            errInfo: "kafka主题不能超过50"
                        },
                    ],
                    'addServiceImplInfo.timeout': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "超时时间不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "超时时间必须为数字"
                        },
                    ],
                    'addServiceImplInfo.retryCount': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "重试次数不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "重试次数必须为数字"
                        },
                    ],
                    'addServiceImplInfo.description': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "备注内容不能超过200"
                        },
                    ],


                });
            },
            saveServiceImplInfo: function () {
                if (!vc.component.addServiceImplValidate()) {
                    vc.toast(vc.validate.errInfo);

                    return;
                }
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addServiceImplInfo);
                    $('#addServiceImplModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addServiceImpl',
                    'save',
                    JSON.stringify(vc.component.addServiceImplInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addServiceImplModel').modal('hide');
                            vc.component.clearAddServiceImplInfo();
                            vc.emit('serviceImplManage', 'listServiceImpl', {});

                            return;
                        }
                        vc.message(json);

                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);

                    });
            },
            clearAddServiceImplInfo: function () {
                vc.component.addServiceImplInfo = {
                    serviceBusinessId: '',
                    businessTypeCd: '',
                    name: '',
                    invokeType: '',
                    url: '1000',
                    messageTopic: '',
                    timeout: '60',
                    retryCount: '3',
                    description: '',

                };
            }
        }
    });

})(window.vc);
