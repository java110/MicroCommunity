(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addMachineInfo: {
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
                unitName: '',
                roomId: '',
                locationTypeCd: '',
                locationObjId: '',
                roomName: '',
                direction:''
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('addMachine', 'openAddMachineModal', function () {
                $('#addMachineModel').modal('show');
            });

            vc.on("addMachine", "notify", function (_param) {
                if (_param.hasOwnProperty("floorId")) {
                    vc.component.addMachineInfo.floorId = _param.floorId;
                }

                if (_param.hasOwnProperty("unitId")) {
                    vc.component.addMachineInfo.unitId = _param.unitId;
                }

                if(_param.hasOwnProperty("roomId")){
                    vc.component.addMachineInfo.roomId = _param.roomId;
                }
            });
        },
        methods: {
            addMachineValidate: function () {
                return vc.validate.validate({
                    addMachineInfo: vc.component.addMachineInfo
                }, {
                    'addMachineInfo.machineCode': [
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
                    'addMachineInfo.machineVersion': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "版本号不能为空"
                        }],
                    'addMachineInfo.machineName':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "设备名称不能为空"
                            }],
                    'addMachineInfo.machineTypeCd':
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
                        'addMachineInfo.direction':
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
                    'addMachineInfo.authCode':
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
                    'addMachineInfo.machineIp':
                        [
                            {
                                limit: "maxLength",
                                param: "64",
                                errInfo: "设备IP格式错误"
                            },
                        ],
                    'addMachineInfo.machineMac':
                        [
                            {
                                limit: "maxLength",
                                param: "64",
                                errInfo: "设备MAC 格式错误"
                            }
                        ],
                    'addMachineInfo.locationTypeCd':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "请选择设备位置"
                            }
                        ],
                    'addMachineInfo.locationObjId':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "请选择位置"
                            }
                        ]
                });
            },
            saveMachineInfo: function () {
                vc.component.addMachineInfo.communityId = vc.getCurrentCommunity().communityId;
                if (vc.component.addMachineInfo.locationTypeCd != '2000' && vc.component.addMachineInfo.locationTypeCd != '3000') { //大门时直接写 小区ID
                    vc.component.addMachineInfo.locationObjId = vc.component.addMachineInfo.communityId;
                } else if (vc.component.addMachineInfo.locationTypeCd == '2000') {
                    vc.component.addMachineInfo.locationObjId = vc.component.addMachineInfo.unitId;
                } else if (vc.component.addMachineInfo.locationTypeCd == '3000') {
                    vc.component.addMachineInfo.locationObjId = vc.component.addMachineInfo.roomId;
                } else {
                    vc.toast("设备位置值错误");
                    return;
                }

                if (!vc.component.addMachineValidate()) {
                    vc.toast(vc.validate.errInfo);

                    return;
                }


                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addMachineInfo);
                    $('#addMachineModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addMachine',
                    'save',
                    JSON.stringify(vc.component.addMachineInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addMachineModel').modal('hide');
                            vc.component.clearAddMachineInfo();
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
            clearAddMachineInfo: function () {
                vc.component.addMachineInfo = {
                    machineCode: '',
                    machineVersion: '',
                    machineName: '',
                    machineTypeCd: '',
                    authCode: '',
                    machineIp: '',
                    machineMac: '',
                    direction:''
                };
            },
            _initAddMachineData: function () {
                $('.floorSelector').select2({
                    placeholder: '必填，请选择楼栋',
                    ajax: {
                        url: "sdata.json",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            return {
                                floorNum: vc.component.addMachineInfo.floorNum,
                                /* page:*/
                            };
                        },
                        processResults: function (data) {
                            return {
                                results: data
                            };
                        },
                        cache: true
                    },
                    minimumInputLength: 2
                });
            }
        }
    });

})(window.vc);
