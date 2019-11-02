(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addComplainViewInfo: {
                flowComponent: 'addComplainView',
                typeCd: '',
                complaintName: '',
                tel: '',
                context: '',

            }
        },
        watch: {
            addComplainViewInfo: {
                deep: true,
                handler: function () {
                    vc.component.saveAddComplainInfo();
                }
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {

            vc.on('addComplainViewInfo', 'onIndex', function (_index) {
                vc.component.addComplainViewInfo.index = _index;
            });
        },
        methods: {
            addComplainValidate: function () {
                return vc.validate.validate({
                    addComplainViewInfo: vc.component.addComplainViewInfo
                }, {
                    'addComplainViewInfo.typeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投诉类型不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "投诉类型格式错误"
                        },
                    ],
                    'addComplainViewInfo.complaintName': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投诉人不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "投诉人不能大于200位"
                        },
                    ],
                    'addComplainViewInfo.tel': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投诉电话不能为空"
                        },
                        {
                            limit: "phone",
                            param: "",
                            errInfo: "投诉电话格式错误"
                        },
                    ],
                    'addComplainViewInfo.context': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投诉内容不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "4000",
                            errInfo: "投诉状态超过4000位"
                        },
                    ],

                });
            },
            saveAddComplainInfo: function () {
                if (vc.component.addComplainValidate()) {
                    //侦听回传
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addComplainViewInfo);
                    return;
                }
            }
        }
    });

})(window.vc);
