(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addApplicationKeyInfo: {
                applicationKeyId: '',
                name: '',
                tel: '',
                typeCd: '',
                sex: '',
                age: '',
                idCard: '',
                startTime: '',
                endTime: '',

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('addApplicationKey', 'openAddApplicationKeyModal', function () {
                $('#addApplicationKeyModel').modal('show');
            });
        },
        methods: {
            addApplicationKeyValidate() {
                return vc.validate.validate({
                    addApplicationKeyInfo: vc.component.addApplicationKeyInfo
                }, {
                    'addApplicationKeyInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "姓名不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,64",
                            errInfo: "姓名不能超过64位"
                        },
                    ],
                    'addApplicationKeyInfo.tel': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "手机号不能为空"
                        },
                        {
                            limit: "phone",
                            param: "",
                            errInfo: "手机号格式错误"
                        },
                    ],
                    'addApplicationKeyInfo.typeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "用户类型不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "用户类型格式错误"
                        },
                    ],
                    'addApplicationKeyInfo.sex': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "性别不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "性别格式错误"
                        },
                    ],
                    'addApplicationKeyInfo.age': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "年龄不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "年龄不是有效数字"
                        },
                    ],
                    'addApplicationKeyInfo.idCard': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "身份证号不能为空"
                        },
                        {
                            limit: "idCard",
                            param: "",
                            errInfo: "不是有效的身份证号"
                        },
                    ],
                    'addApplicationKeyInfo.startTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "开始时间不能为空"
                        },
                        {
                            limit: "dateTime",
                            param: "",
                            errInfo: "不是有效的时间格式"
                        },
                    ],
                    'addApplicationKeyInfo.endTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "结束时间不能为空"
                        },
                        {
                            limit: "dateTime",
                            param: "",
                            errInfo: "不是有效的时间格式"
                        },
                    ],


                });
            },
            saveApplicationKeyInfo: function () {
                if (!vc.component.addApplicationKeyValidate()) {
                    vc.message(vc.validate.errInfo);

                    return;
                }

                vc.component.addApplicationKeyInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addApplicationKeyInfo);
                    $('#addApplicationKeyModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addApplicationKey',
                    'save',
                    JSON.stringify(vc.component.addApplicationKeyInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addApplicationKeyModel').modal('hide');
                            vc.component.clearAddApplicationKeyInfo();
                            vc.emit('applicationKeyManage', 'listApplicationKey', {});

                            return;
                        }
                        vc.message(json);

                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);

                    });
            },
            clearAddApplicationKeyInfo: function () {
                vc.component.addApplicationKeyInfo = {
                    name: '',
                    tel: '',
                    typeCd: '',
                    sex: '',
                    age: '',
                    idCard: '',
                    startTime: '',
                    endTime: '',

                };
            }
        }
    });

})(window.vc);
