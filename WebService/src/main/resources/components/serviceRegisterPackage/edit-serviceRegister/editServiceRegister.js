(function (vc, vm) {

    vc.extends({
        data: {
            editServiceRegisterInfo: {
                id: '',
                appId: '',
                serviceId: '',
                orderTypeCd: '',
                invokeLimitTimes: '1000',
                invokeModel: '',

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('editServiceRegister', 'openEditServiceRegisterModal', function (_params) {
                vc.component.refreshEditServiceRegisterInfo();
                $('#editServiceRegisterModel').modal('show');
                vc.copyObject(_params, vc.component.editServiceRegisterInfo);
                vc.component.editServiceRegisterInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            editServiceRegisterValidate: function () {
                return vc.validate.validate({
                    editServiceRegisterInfo: vc.component.editServiceRegisterInfo
                }, {
                    'editServiceRegisterInfo.appId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "应用ID不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "50",
                            errInfo: "应用ID不能超过50位"
                        },
                    ],
                    'editServiceRegisterInfo.serviceId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "服务ID不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "50",
                            errInfo: "服务ID不能超过50位"
                        },
                    ],
                    'editServiceRegisterInfo.orderTypeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "订单类型不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,4",
                            errInfo: "订单类型错误"
                        },
                    ],
                    'editServiceRegisterInfo.invokeLimitTimes': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "调用次数不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "调用次数错误"
                        },
                    ],
                    'editServiceRegisterInfo.invokeModel': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "调用方式不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "50",
                            errInfo: "消息队列不能超过50"
                        },
                    ],
                    'editServiceRegisterInfo.id': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "绑定ID不能为空"
                        }]

                });
            },
            editServiceRegister: function () {
                if (!vc.component.editServiceRegisterValidate()) {
                    vc.message(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editServiceRegister',
                    'update',
                    JSON.stringify(vc.component.editServiceRegisterInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editServiceRegisterModel').modal('hide');
                            vc.emit('serviceRegisterManage', 'listServiceRegister', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditServiceRegisterInfo: function () {
                vc.component.editServiceRegisterInfo = {
                    id: '',
                    appId: '',
                    serviceId: '',
                    orderTypeCd: '',
                    invokeLimitTimes: '1000',
                    invokeModel: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);
