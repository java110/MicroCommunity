(function (vc, vm) {

    vc.extends({
        data: {
            editVisitInfo: {

            }
        },
        _initMethod: function () {
        },
        _initEvent: function () {
            vc.on('editVisit', 'openEditVisitModel', function (_params) {
                vc.component.refreshEditAppInfo();
                $('#editAppModel').modal('show');
                console.log(_params);
                vc.component.editVisitInfo = _params;

            });
        },
        methods: {
            editAppValidate: function () {
                return vc.validate.validate({
                    editVisitInfo: vc.component.editVisitInfo
                }, {
                    'editVisitInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客姓名不能为空"
                        }
                    ],
                    'editVisitInfo.visitGender': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客性别不能为空"
                        },
                    ],
                    'editVisitInfo.phoneNumber': [
                        {
                            limit: "phone",
                            param: "",
                            errInfo: "访客联系方式不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "访客联系方式必须是数字"
                        }
                    ],
                    'editVisitInfo.visitTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客到访时间不能为空"
                        },
                    ],
                    'editVisitInfo.departureTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客到访时间不能为空"
                        },
                    ],
                    'editVisitInfo.visitCase': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客到访原因不能为空"
                        },
                    ]


                });
            },
            editVisit: function () {
                if (!vc.component.editAppValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editVisit',
                    'update',
                    JSON.stringify(vc.component.editVisitInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editAppModel').modal('hide');
                            vc.emit('appManage', 'listApp', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditAppInfo: function () {
                vc.component.editAppInfo = {
                    name: '',
                    visitGender: '',
                    phoneNumber: '',
                    visitTime: '',
                    departureTime: '',
                    visitCase: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);
