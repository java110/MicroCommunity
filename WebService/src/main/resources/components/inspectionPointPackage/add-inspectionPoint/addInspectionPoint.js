(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addInspectionPointInfo: {
                inspectionId: '',
                machineId: '',
                inspectionName: '',
                remark: ''
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('addInspectionPoint', 'openAddInspectionPointModal', function () {
                $('#addInspectionPointModel').modal('show');
            });
            vc.on("addInspectionPointInfo", "notify", function (_param) {
                if (_param.hasOwnProperty("machineId")) {
                    vc.component.addInspectionPointInfo.machineId = _param.machineId;
                }
            });
        },
        methods: {
            addInspectionPointValidate() {
                return vc.validate.validate({
                    addInspectionPointInfo: vc.component.addInspectionPointInfo
                }, {
                    'addInspectionPointInfo.inspectionName': [
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
                    'addInspectionPointInfo.machineId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "设备信息不能为空"
                        },
                    ],
                    'addInspectionPointInfo.remark': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "备注信息不能超过200位"
                        },
                    ],

                });
            },
            saveInspectionPointInfo: function () {
                if (!vc.component.addInspectionPointValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.component.addInspectionPointInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.component.addInspectionPointInfo.machineId = vc.component.addInspectionPointInfo.machineId;
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addInspectionPointInfo);
                    $('#addInspectionPointModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addInspectionPoint',
                    'save',
                    JSON.stringify(vc.component.addInspectionPointInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addInspectionPointModel').modal('hide');
                            vc.component.clearAddInspectionPointInfo();
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
            clearAddInspectionPointInfo: function () {
                vc.component.addInspectionPointInfo = {
                    inspectionName: '',
                    remark: '',

                };
            }
        }
    });

})(window.vc);
