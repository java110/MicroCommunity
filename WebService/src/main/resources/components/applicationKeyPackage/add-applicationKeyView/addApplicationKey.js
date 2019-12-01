(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addApplicationKeyInfo: {
                applicationKeyId: '',
                name: '',
                tel: '',
                typeCd: '',
                sex: '',
                age: '',
                idCard: '',
                startTime: '',
                endTime: '',
                floorId: '',
                floorNum: '',
                floorName: '',
                unitId: '',
                unitName: '',
                roomId: '',
                locationTypeCd: '',
                locationObjId: '',
                roomName: ''
            }
        },
        _initMethod: function () {
            vc.component._initAddApplicationKeyDateInfo();
        },
        _initEvent: function () {
            vc.on('addApplicationKey', 'openAddApplicationKeyModal', function () {
                $('#addApplicationKeyModel').modal('show');
            });
            vc.on("addApplicationKey", "notify", function (_param) {
                if (_param.hasOwnProperty("floorId")) {
                    vc.component.addApplicationKeyInfo.floorId = _param.floorId;
                }

                if (_param.hasOwnProperty("unitId")) {
                    vc.component.addApplicationKeyInfo.unitId = _param.unitId;
                }

                if(_param.hasOwnProperty("roomId")){
                    vc.component.addApplicationKeyInfo.roomId = _param.roomId;
                }
            });
        },
        methods: {
            _initAddApplicationKeyDateInfo: function () {
                vc.component.addApplicationKeyInfo.startTime = vc.dateFormat(new Date().getTime());
                $('.addApplicationStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd HH:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.addApplicationStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".addApplicationStartTime").val();
                        vc.component.addApplicationKeyInfo.startTime = value;
                    });
                $('.addApplicationEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd HH:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.addApplicationEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".addApplicationEndTime").val();
                        vc.component.addApplicationKeyInfo.endTime = value;
                    });
            },

            addApplicationKeyValidate: function () {
                return vc.validate.validate({
                    addApplicationKeyInfo: vc.component.addApplicationKeyInfo
                }, {
                    'addApplicationKeyInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "姓名不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,64",
                            errInfo: "姓名不能超过64位"
                        },
                    ],
                    'addApplicationKeyInfo.tel': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "手机号不能为空"
                        },
                        {
                            limit: "phone",
                            param: "",
                            errInfo: "手机号格式错误"
                        },
                    ],
                    'addApplicationKeyInfo.typeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "用户类型不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "用户类型格式错误"
                        },
                    ],
                    'addApplicationKeyInfo.sex': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "性别不能为空"
                        }

                    ],
                    'addApplicationKeyInfo.age': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "年龄不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "年龄不是有效数字"
                        },
                    ],
                    'addApplicationKeyInfo.idCard': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "身份证号不能为空"
                        },
                        {
                            limit: "idCard",
                            param: "",
                            errInfo: "不是有效的身份证号"
                        },
                    ],
                    'addApplicationKeyInfo.startTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "开始时间不能为空"
                        },
                        {
                            limit: "dateTime",
                            param: "",
                            errInfo: "不是有效的时间格式"
                        },
                    ],
                    'addApplicationKeyInfo.endTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "结束时间不能为空"
                        },
                        {
                            limit: "dateTime",
                            param: "",
                            errInfo: "不是有效的时间格式"
                        },
                    ],
                    'addApplicationKeyInfo.locationTypeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "申请位置不能为空"
                        }
                    ],
                    'addApplicationKeyInfo.locationObjId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "具体位置不能为空"
                        }
                    ]


                });
            },
            saveApplicationKeyInfo: function () {
                vc.component.addApplicationKeyInfo.communityId = vc.getCurrentCommunity().communityId;
                if (vc.component.addApplicationKeyInfo.locationTypeCd != '2000' && vc.component.addApplicationKeyInfo.locationTypeCd != '3000') { //大门时直接写 小区ID
                    vc.component.addApplicationKeyInfo.locationObjId = vc.component.addApplicationKeyInfo.communityId;
                } else if (vc.component.addApplicationKeyInfo.locationTypeCd == '2000') {
                    vc.component.addApplicationKeyInfo.locationObjId = vc.component.addApplicationKeyInfo.unitId;
                } else if (vc.component.addApplicationKeyInfo.locationTypeCd == '3000') {
                    vc.component.addApplicationKeyInfo.locationObjId = vc.component.addApplicationKeyInfo.roomId;
                } else {
                    vc.toast("设备位置值错误");
                    return;
                }
                if (!vc.component.addApplicationKeyValidate()) {
                    vc.message(vc.validate.errInfo);

                    return;
                }

                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addApplicationKeyInfo);
                    $('#addApplicationKeyModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addApplicationKey',
                    'save',
                    JSON.stringify(vc.component.addApplicationKeyInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            //$('#addApplicationKeyModel').modal('hide');
                            vc.component.clearAddApplicationKeyInfo();
                            vc.emit('applicationKeyManage', 'listApplicationKey', {});

                            return;
                        }
                        vc.message(json);

                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);

                    });
            },
            clearAddApplicationKeyInfo: function () {
                vc.component.addApplicationKeyInfo = {
                    name: '',
                    tel: '',
                    typeCd: '',
                    sex: '',
                    age: '',
                    idCard: '',
                    startTime: '',
                    endTime: '',
                    floorId: '',
                    floorNum: '',
                    floorName: '',
                    unitId: '',
                    unitName: '',
                    roomId: '',
                    locationTypeCd: '',
                    locationObjId: '',
                    roomName: ''

                };
            },
            _closeAddApplicationKeyView: function () {
                vc.emit('applicationKeyManage', 'listApplicationKey', {});
            },
        }
    });

})(window.vc);
