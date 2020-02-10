(function (vc, vm) {

    vc.extends({
        data: {
            editInspectionPointInfo: {
                inspectionId: '',
                machineId: '',
                machineName: '',
                inspectionName: '',
                communityId:'',
                remark: '',
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('editInspectionPoint', 'openEditInspectionPointModal', function (_params) {
                vc.component.refreshEditInspectionPointInfo();
                $('#editInspectionPointModel').modal('show');
                vc.copyObject(_params, vc.component.editInspectionPointInfo);
                //传输数据到machineSelect2组件
                vc.emit('editInspectionPoint', 'machineSelect2', 'setMachine', {
                    machineId: vc.component.editInspectionPointInfo.machineId,
                    machineName: vc.component.editInspectionPointInfo.machineName,
                });
                vc.component.editInspectionPointInfo.communityId = vc.getCurrentCommunity().communityId;
            });

            vc.on("editInspectionPointInfo", "notify", function (_param) {
                if (_param.hasOwnProperty("machineId")) {
                    vc.component.editInspectionPointInfo.machineId = _param.machineId;
                }
            });
        },
        methods: {
            editInspectionPointValidate: function () {
                return vc.validate.validate({
                    editInspectionPointInfo: vc.component.editInspectionPointInfo
                }, {
                    'editInspectionPointInfo.inspectionName': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "巡检点名称不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "100",
                            errInfo: "巡检点名称不能超过100位"
                        },
                    ],
                    'editInspectionPointInfo.machineId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "设备信息不能为空"
                        },
                    ],
                    'editInspectionPointInfo.remark': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "备注信息不能超过200位"
                        },
                    ],
                    'editInspectionPointInfo.inspectionId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "巡检点ID不能为空"
                        }]

                });
            },
            editInspectionPoint: function () {
                if (!vc.component.editInspectionPointValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editInspectionPoint',
                    'update',
                    JSON.stringify(vc.component.editInspectionPointInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editInspectionPointModel').modal('hide');
                            vc.emit('inspectionPointManage', 'listInspectionPoint', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditInspectionPointInfo: function () {
                vc.component.editInspectionPointInfo = {
                    inspectionId: '',
                    machineId: '',
                    machineName: '',
                    inspectionName: '',
                    communityId:'',
                    remark: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);
