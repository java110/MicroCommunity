(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addAuditUserOtherViewInfo: {
                flowComponent: 'addAuditUserOtherView',
                userId: '',
                userName: '',
                auditLink: '',
                objCode: '',

            }
        },
        watch: {
            addAuditUserOtherViewInfo: {
                deep: true,
                handler: function () {
                    vc.component.saveAddAuditUserOtherInfo();
                }
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {

            vc.on('addAuditUserOtherViewInfo', 'onIndex', function (_index) {
                vc.component.addAuditUserOtherViewInfo.index = _index;
            });

            vc.on('addAuditUserOtherViewInfo', '_clear', function (_staffInfo) {
                vc.component.addAuditUserOtherViewInfo= {
                    flowComponent: 'addAuditUserOtherView',
                    userId: _staffInfo.userId,
                    userName: _staffInfo.name,
                    auditLink: '',
                    objCode: '',
                };
            });
        },
        methods: {
            addAuditUserOtherValidate() {
                return vc.validate.validate({
                    addAuditUserOtherViewInfo: vc.component.addAuditUserOtherViewInfo
                }, {
                    'addAuditUserOtherViewInfo.userId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "用户ID不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "用户ID必须为数字"
                        },
                    ],
                    'addAuditUserOtherViewInfo.userName': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "用户名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,50",
                            errInfo: "用户名称必须在2至50字符之间"
                        },
                    ],
                    'addAuditUserOtherViewInfo.auditLink': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "审核环节不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "审核环节格式错误"
                        },
                    ],
                    'addAuditUserOtherViewInfo.objCode': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "流程对象不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "64",
                            errInfo: "物品库存不能大于64位"
                        },
                    ],

                });
            },
            saveAddAuditUserOtherInfo: function () {
                if (vc.component.addAuditUserOtherValidate()) {
                    //侦听回传
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addAuditUserOtherViewInfo);
                    return;
                }
            }
        }
    });

})(window.vc);
