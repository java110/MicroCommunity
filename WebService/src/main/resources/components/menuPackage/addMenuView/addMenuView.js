(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addMenuViewInfo: {
                flowComponent: 'addMenuView',
                name: '',
                url: '',
                seq: '',
                isShow: '',
                description: '',
            }
        },
        watch: {
            addMenuViewInfo: {
                deep: true,
                handler: function () {
                    vc.component.saveAddMenuInfo();
                }
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {

            vc.on('addMenuViewInfo', 'onIndex', function (_index) {
                vc.component.addMenuViewInfo.index = _index;
            });
        },
        methods: {
            addMenuValidate() {
                return vc.validate.validate({
                    addMenuViewInfo: vc.component.addMenuViewInfo
                }, {
                    'addMenuViewInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "菜单名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,10",
                            errInfo: "菜单名称必须在2至10字符之间"
                        },
                    ],
                    'addMenuViewInfo.url': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "菜单地址不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "菜单地址不能超过200"
                        },
                    ],
                    'addMenuViewInfo.seq': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "序列不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "序列必须为整数"
                        },
                    ],
                    'addMenuViewInfo.isShow': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "菜单显示不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,12",
                            errInfo: "菜单显示错误"
                        },
                    ],
                    'addMenuViewInfo.description': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "备注内容不能超过200"
                        },
                    ],

                });
            },
            saveAddMenuInfo: function () {
                if (vc.component.addMenuValidate()) {

                    vc.emit('addPrivilegeViewInfo', 'syncData', {
                        name: vc.component.addMenuViewInfo.name,
                        description: vc.component.addMenuViewInfo.description
                    });
                    //侦听回传
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addMenuViewInfo);
                    return;
                }
            }
        }
    });

})(window.vc);
