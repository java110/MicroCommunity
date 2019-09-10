(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addVisitInfo: {
                vId: '',
                name: '',
                visitGender: '',
                visitGender: '',
                phoneNumber: '',
                visitTime: '',
                departureTime: '',
                visitCase: '',
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('addVisit', 'openAddVisitModal', function () {
                $('#addVisitModel').modal('show');
            });
        },
        methods: {
            addVisitValidate() {
                return vc.validate.validate({
                    addVisitInfo: vc.component.addVisitInfo
                }, {
                    'addVisitInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客姓名不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,10",
                            errInfo: "访客姓名必须在2至10字符之间"
                        },
                    ],
                    'addVisitInfo.visitGender': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客性别不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "性别错误"
                        },
                    ],
                    'addVisitInfo.phoneNumber': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客联系方式不能为空"
                        },
                        {
                            limit: "phone",
                            param: "",
                            errInfo: "不是有效的手机号"
                        },
                    ],
                    'addVisitInfo.visitTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客拜访时间不能为空"
                        },
                        {
                            limit: "date",
                            param: "",
                            errInfo: "访客拜访时间格式错误，如：2019-09-11"
                        },
                    ],
                    'addVisitInfo.departureTime': [
                       {
                           limit: "date",
                           param: "",
                           errInfo: "访客离开时间格式错误，如：2019-09-11"
                       },
                    ],
                    'addVisitInfo.visitCase': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "备注内容不能超过200"
                        },
                    ],


                });
            },
            saveVisitInfo: function () {
                if (!vc.component.addVisitValidate()) {
                    vc.message(vc.validate.errInfo);

                    return;
                }

                vc.component.addVisitInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addVisitInfo);
                    $('#addVisitModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addVisit',
                    'save',
                    JSON.stringify(vc.component.addVisitInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addVisitModel').modal('hide');
                            vc.component.clearAddVisitInfo();
                            vc.emit('visitManage', 'listVisit', {});

                            return;
                        }
                        vc.message(json);

                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);

                    });
            },
            clearAddVisitInfo: function () {
                vc.component.addVisitInfo = {
                    name: '',
                    visitGender: '',
                    visitGender: '',
                    phoneNumber: '',
                    visitTime: '',
                    departureTime: '',
                    visitCase: '',

                };
            }
        }
    });

})(window.vc);
