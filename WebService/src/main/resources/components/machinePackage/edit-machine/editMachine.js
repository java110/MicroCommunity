(function (vc, vm) {

    vc.extends({
        data: {
            editMachineInfo: {
                machineId: '',
                machineCode: '',
                machineVersion: '',
                machineName: '',
                machineTypeCd: '',
                authCode: '',
                machineIp: '',
                machineMac: '',

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('editMachine', 'openEditMachineModal', function (_params) {
                vc.component.refreshEditMachineInfo();
                $('#editMachineModel').modal('show');
                vc.copyObject(_params, vc.component.editMachineInfo);
                vc.component.editMachineInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            editMachineValidate: function () {
                return vc.validate.validate({
                    editMachineInfo: vc.component.editMachineInfo
                }, {
                    'editMachineInfo.machineCode': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "设备编码不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,30",
                            errInfo: "设备编码不能超过30位"
                        },
                    ],
                    'editMachineInfo.machineVersion': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "版本号不能为空"
                        }],
                    'editMachineInfo.machineName':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "设备名称不能为空"
                            }],
                    'editMachineInfo.machineTypeCd':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "设备类型不能为空"
                            },
                            {
                                limit: "num",
                                param: "",
                                errInfo: "设备类型格式错误"
                            },
                        ],
                    'editMachineInfo.authCode':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "鉴权编码不能为空"
                            },
                            {
                                limit: "maxLength",
                                param: "64",
                                errInfo: "鉴权编码不能大于64位"
                            },
                        ],
                    'editMachineInfo.machineIp':
                        [
                            {
                                limit: "maxLength",
                                param: "64",
                                errInfo: "设备IP格式错误"
                            },
                        ],
                    'editMachineInfo.machineMac':
                        [
                            {
                                limit: "maxLength",
                                param: "64",
                                errInfo: "设备MAC 格式错误"
                            },
                        ],
                    'editMachineInfo.machineId':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "设备ID不能为空"
                            }]

                })
                    ;
            },
            editMachine: function () {
                if (!vc.component.editMachineValidate()) {
                    vc.message(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editMachine',
                    'update',
                    JSON.stringify(vc.component.editMachineInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editMachineModel').modal('hide');
                            vc.emit('machineManage', 'listMachine', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditMachineInfo: function () {
                vc.component.editMachineInfo = {
                    machineId: '',
                    machineCode: '',
                    machineVersion: '',
                    machineName: '',
                    machineTypeCd: '',
                    authCode: '',
                    machineIp: '',
                    machineMac: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);
