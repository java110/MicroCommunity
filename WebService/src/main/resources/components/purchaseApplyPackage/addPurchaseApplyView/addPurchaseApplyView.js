(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addPurchaseApplyViewInfo: {
                flowComponent: 'addPurchaseApplyView',
                description:''

            }
        },
        watch: {
            addPurchaseApplyViewInfo: {
                deep: true,
                handler: function () {
                    vc.component.saveAddComplainInfo();
                }
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {


            vc.on('addPurchaseApplyViewInfo', 'setPurchaseApplyInfo', function () {
                vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addPurchaseApplyViewInfo);
            });

            vc.on('addPurchaseApplyViewInfo', 'onIndex', function (_index) {
                vc.component.addPurchaseApplyViewInfo.index = _index;
            });
        },
        methods: {
            addComplainValidate: function () {
                return vc.validate.validate({
                    addPurchaseApplyViewInfo: vc.component.addPurchaseApplyViewInfo
                }, {
                    'addPurchaseApplyViewInfo.description': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "申请说明不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "申请说明不能超过200位"
                        },
                    ],

                });
            },
            saveAddComplainInfo: function () {
                if (vc.component.addComplainValidate()) {
                    //侦听回传
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addPurchaseApplyViewInfo);
                    return;
                }
            }
        }
    });

})(window.vc);
