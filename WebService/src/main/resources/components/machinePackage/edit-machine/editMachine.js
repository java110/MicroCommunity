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
                floorId: '',
                floorNum: '',
                floorName: '',
                unitId: '',
                unitNum: '',
                roomId: '',
                locationTypeCd: '',
                locationObjId: '',
                roomNum: '',
                machineUrl: '',
                direction:''

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('editMachine', 'openEditMachineModal', function (_params) {
                vc.component.refreshEditMachineInfo();
                $('#editMachineModel').modal('show');
                vc.copyObject(_params, vc.component.editMachineInfo);
                vc.component._initMachineUrl();
                //根据位置类型 传输数据
                if (vc.component.editMachineInfo.locationTypeCd == '2000') {
                    vc.emit('editMachine', 'floorSelect2', 'setFloor', {
                        floorId: vc.component.editMachineInfo.floorId,
                        floorNum: vc.component.editMachineInfo.floorNum
                    });
                    vc.emit('editMachine', 'unitSelect2', 'setUnit', {
                        floorId: vc.component.editMachineInfo.floorId,
                        floorNum: vc.component.editMachineInfo.floorNum,
                        unitId: vc.component.editMachineInfo.unitId,
                        unitNum: vc.component.editMachineInfo.unitNum,
                    });
                } else if (vc.component.editMachineInfo.locationTypeCd == '3000') {
                    vc.emit('editMachine', 'floorSelect2', 'setFloor', {
                        floorId: vc.component.editMachineInfo.floorId,
                        floorNum: vc.component.editMachineInfo.floorNum
                    });
                    vc.emit('editMachine', 'unitSelect2', 'setUnit', {
                        floorId: vc.component.editMachineInfo.floorId,
                        floorNum: vc.component.editMachineInfo.floorNum,
                        unitId: vc.component.editMachineInfo.unitId,
                        unitNum: vc.component.editMachineInfo.unitNum,
                    });
                    vc.emit('editMachine', 'roomSelect2', 'setRoom', {
                        floorId: vc.component.editMachineInfo.floorId,
                        floorNum: vc.component.editMachineInfo.floorNum,
                        unitId: vc.component.editMachineInfo.unitId,
                        unitNum: vc.component.editMachineInfo.unitNum,
                        roomId: vc.component.editMachineInfo.roomId,
                        roomNum: vc.component.editMachineInfo.roomNum,
                    });
                }
                vc.component.editMachineInfo.communityId = vc.getCurrentCommunity().communityId;
            });

            vc.on("editMachine", "notify", function (_param) {
                if (_param.hasOwnProperty("floorId")) {
                    vc.component.editMachineInfo.floorId = _param.floorId;
                }

                if (_param.hasOwnProperty("unitId")) {
                    vc.component.editMachineInfo.unitId = _param.unitId;
                }

                if (_param.hasOwnProperty("roomId")) {
                    vc.component.editMachineInfo.roomId = _param.roomId;
                }
            });
        },
        methods: {
            _initMachineUrl: function () {
                var sysInfo = vc.getData("_sysInfo");
                if (sysInfo == null) {
                    return;
                }

                var apiUrl = sysInfo.apiUrl + "/api/machineTranslate.machineHeartbeart?communityId="
                    + vc.getCurrentCommunity().communityId + "&transaction_id=-1&req_time=20181113225612&user_id=-1"
                    + "&app_id=" + vc.component.editMachineInfo.machineTypeCd;
                vc.component.editMachineInfo.machineUrl = apiUrl;


            },
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
                    'editMachineInfo.direction':
                    [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "设备方向不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "设备方向格式错误"
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
                            }],
                    'editMachineInfo.locationTypeCd':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "请选择设备位置"
                            }
                        ],
                    'editMachineInfo.locationObjId':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "请选择位置"
                            }
                        ]

                })
                    ;
            },
            editMachine: function () {
                vc.component.editMachineInfo.communityId = vc.getCurrentCommunity().communityId;
                if (vc.component.editMachineInfo.locationTypeCd != '2000' && vc.component.editMachineInfo.locationTypeCd != '3000') { //大门时直接写 小区ID
                    vc.component.editMachineInfo.locationObjId = vc.component.editMachineInfo.communityId;
                } else if (vc.component.editMachineInfo.locationTypeCd == '2000') {
                    vc.component.editMachineInfo.locationObjId = vc.component.editMachineInfo.unitId;
                } else if (vc.component.editMachineInfo.locationTypeCd == '3000') {
                    vc.component.editMachineInfo.locationObjId = vc.component.editMachineInfo.roomId;
                } else {
                    vc.toast("设备位置值错误");
                    return;
                }
                if (!vc.component.editMachineValidate()) {
                    vc.toast(vc.validate.errInfo);
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
                    floorId: '',
                    floorNum: '',
                    floorName: '',
                    unitId: '',
                    unitNum: '',
                    roomId: '',
                    locationTypeCd: '',
                    locationObjId: '',
                    roomNum: '',
                    machineUrl: '',
                     direction:''

                }
            }
        }
    });

})(window.vc, window.vc.component);
