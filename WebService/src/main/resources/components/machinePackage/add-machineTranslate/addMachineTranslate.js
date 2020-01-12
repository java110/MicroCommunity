(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addMachineTranslateInfo: {
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
            vc.on('addMachineTranslate', 'openAddMachineTranslateModal', function () {
                $('#addMachineTranslateModel').modal('show');
            });
        },
        methods: {
            addMachineTranslateValidate() {
                return vc.validate.validate({
                    addMachineTranslateInfo: vc.component.addMachineTranslateInfo
                }, {
                    'addMachineTranslateInfo.machineCode': [
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
                    'addMachineTranslateInfo.machineId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "设备ID不能为空"
                        }],
                    'addMachineTranslateInfo.typeCd':
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
                    'addMachineTranslateInfo.objName':
                        [
                            {
                                limit: "required",
                                param: "",
                                errInfo: "对象名称不能为空"
                            }],
                    'addMachineTranslateInfo.objId':
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
                    'addMachineTranslateInfo.state':
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


                })
                    ;
            },
            saveMachineTranslateInfo: function () {
                if (!vc.component.addMachineTranslateValidate()) {
                    vc.toast(vc.validate.errInfo);

                    return;
                }

                vc.component.addMachineTranslateInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addMachineTranslateInfo);
                    $('#addMachineTranslateModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addMachineTranslate',
                    'save',
                    JSON.stringify(vc.component.addMachineTranslateInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addMachineTranslateModel').modal('hide');
                            vc.component.clearAddMachineTranslateInfo();
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
            clearAddMachineTranslateInfo: function () {
                vc.component.addMachineTranslateInfo = {
                    machineCode: '',
                    machineId: '',
                    typeCd: '',
                    objName: '',
                    objId: '',
                    state: '',

                };
            }
        }
    });

})(window.vc);
