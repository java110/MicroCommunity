(function(vc, vm) {

    vc.extends({
        data: {
            editInspectionRouteInfo: {
                inspectionRouteId: '',
                routeName: '',
                seq: '',
                remark: '',

            }
        },
        _initMethod: function() {

},
        _initEvent: function() {
            vc.on('editInspectionRoute', 'openEditInspectionRouteModal',
            function(_params) {
                vc.component.refreshEditInspectionRouteInfo();
                $('#editInspectionRouteModel').modal('show');
                vc.copyObject(_params, vc.component.editInspectionRouteInfo);
                vc.component.editInspectionRouteInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            editInspectionRouteValidate: function() {
                return vc.validate.validate({
                    editInspectionRouteInfo: vc.component.editInspectionRouteInfo
                },
                {
                    'editInspectionRouteInfo.routeName': [{
                        limit: "required",
                        param: "",
                        errInfo: "路线名称不能为空"
                    },
                    {
                        limit: "maxin",
                        param: "1,100",
                        errInfo: "路线名称字数不能超过100个"
                    },
                    ],
                    'editInspectionRouteInfo.seq': [{
                        limit: "required",
                        param: "",
                        errInfo: "顺序不能为空"
                    },
                    {
                        limit: "num",
                        param: "",
                        errInfo: "顺序必须是数字"
                    },
                    ],
                    'editInspectionRouteInfo.remark': [{
                        limit: "maxin",
                        param: "0,200",
                        errInfo: "备注不能超过200位"
                    },
                    ],
                    'editInspectionRouteInfo.inspectionRouteId': [{
                        limit: "required",
                        param: "",
                        errInfo: "路线ID不能为空"
                    }]

                });
            },
            editInspectionRoute: function() {
                if (!vc.component.editInspectionRouteValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.http.post('editInspectionRoute', 'update', JSON.stringify(vc.component.editInspectionRouteInfo), {
                    emulateJSON: true
                },
                function(json, res) {
                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                    if (res.status == 200) {
                        //关闭model
                        $('#editInspectionRouteModel').modal('hide');
                        vc.emit('inspectionRouteManage', 'listInspectionRoute', {});
                        return;
                    }
                    vc.message(json);
                },
                function(errInfo, error) {
                    console.log('请求失败处理');

                    vc.message(errInfo);
                });
            },
            refreshEditInspectionRouteInfo: function() {
                vc.component.editInspectionRouteInfo = {
                    inspectionRouteId: '',
                    routeName: '',
                    seq: '',
                    remark: '',
                }
            }
        }
    });

})(window.vc, window.vc.component);