(function (vc, vm) {

    vc.extends({
        data: {
            editMachineTranslateInfo: {
                machineTranslateId: '',
                machineCode: '',
                machineId: '',
                typeCd: '',
                objName: '',
                objId: '',
                state: '',

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('editMachineTranslate', 'openEditMachineTranslateModal', function (_params) {
                vc.component.refreshEditMachineTranslateInfo();
                $('#editMachineTranslateModel').modal('show');
                vc.copyObject(_params, vc.component.editMachineTranslateInfo);
                vc.component.editMachineTranslateInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            editMachineTranslateValidate: function () {
                return vc.validate.validate({
                    editMachineTranslateInfo: vc.component.editMachineTranslateInfo
                }, {
                    'editMachineTranslateInfo.machineCode': [
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
                    'editMachineTranslateInfo.machineId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "设备ID不能为空"
                        }],
                    'editMachineTranslateInfo.typeCd':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "对象类型不能为空"
                            },
                            {
                                limit: "num",
                                param: "",
                                errInfo: "对象类型格式错误"
                            },
                        ],
                    'editMachineTranslateInfo.objName':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "对象名称不能为空"
                            }],
                    'editMachineTranslateInfo.objId':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "对象Id不能为空"
                            },
                            {
                                limit: "num",
                                param: "",
                                errInfo: "对象Id必须为数字"
                            },
                        ],
                    'editMachineTranslateInfo.state':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "状态不能为空"
                            },
                            {
                                limit: "num",
                                param: "",
                                errInfo: "状态格式错误"
                            },
                        ],
                    'editMachineTranslateInfo.machineTranslateId':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "同步ID不能为空"
                            }]

                })
                    ;
            },
            editMachineTranslate: function () {
                if (!vc.component.editMachineTranslateValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editMachineTranslate',
                    'update',
                    JSON.stringify(vc.component.editMachineTranslateInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editMachineTranslateModel').modal('hide');
                            vc.emit('machineTranslateManage', 'listMachineTranslate', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditMachineTranslateInfo: function () {
                vc.component.editMachineTranslateInfo = {
                    machineTranslateId: '',
                    machineCode: '',
                    machineId: '',
                    typeCd: '',
                    objName: '',
                    objId: '',
                    state: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);
