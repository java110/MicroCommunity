(function (vc, vm) {

    vc.extends({
        data: {
            editApplicationKeyInfo: {
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
            vc.component._initEditApplicationKeyDateInfo();

        },
        _initEvent: function () {
            vc.on('editApplicationKey', 'openEditApplicationKeyModal', function (_params) {
                vc.component.refreshEditApplicationKeyInfo();
               // $('#editApplicationKeyModel').modal('show');
                vc.copyObject(_params, vc.component.editApplicationKeyInfo);
                if (vc.component.editApplicationKeyInfo.locationTypeCd == '2000') {
                    vc.emit('editApplicationKey','floorSelect2','setFloor',{
                        floorId:vc.component.editApplicationKeyInfo.floorId,
                        floorNum:vc.component.editApplicationKeyInfo.floorNum
                    });
                    vc.emit('editApplicationKey','unitSelect2','setUnit',{
                        floorId:vc.component.editApplicationKeyInfo.floorId,
                        floorNum:vc.component.editApplicationKeyInfo.floorNum,
                        unitId:vc.component.editApplicationKeyInfo.unitId,
                        unitNum:vc.component.editApplicationKeyInfo.unitNum,
                    });
                } else if (vc.component.editApplicationKeyInfo.locationTypeCd == '3000') {
                    vc.emit('editApplicationKey','floorSelect2','setFloor',{
                        floorId:vc.component.editApplicationKeyInfo.floorId,
                        floorNum:vc.component.editApplicationKeyInfo.floorNum
                    });
                    vc.emit('editApplicationKey','unitSelect2','setUnit',{
                        floorId:vc.component.editApplicationKeyInfo.floorId,
                        floorNum:vc.component.editApplicationKeyInfo.floorNum,
                        unitId:vc.component.editApplicationKeyInfo.unitId,
                        unitNum:vc.component.editApplicationKeyInfo.unitNum,
                    });
                    vc.emit('editApplicationKey','roomSelect2','setRoom',{
                        floorId:vc.component.editApplicationKeyInfo.floorId,
                        floorNum:vc.component.editApplicationKeyInfo.floorNum,
                        unitId:vc.component.editApplicationKeyInfo.unitId,
                        unitNum:vc.component.editApplicationKeyInfo.unitNum,
                        roomId:vc.component.editApplicationKeyInfo.roomId,
                        roomNum:vc.component.editApplicationKeyInfo.roomNum,
                    });
                }
                vc.component.editApplicationKeyInfo.communityId = vc.getCurrentCommunity().communityId;
            });

            vc.on("editApplicationKey", "notify", function (_param) {
                if (_param.hasOwnProperty("floorId")) {
                    vc.component.editApplicationKeyInfo.floorId = _param.floorId;
                }

                if (_param.hasOwnProperty("unitId")) {
                    vc.component.editApplicationKeyInfo.unitId = _param.unitId;
                }

                if(_param.hasOwnProperty("roomId")){
                    vc.component.editApplicationKeyInfo.roomId = _param.roomId;
                }
            });
        },
        methods: {
            _initEditApplicationKeyDateInfo: function () {
                vc.component.editApplicationKeyInfo.startTime = vc.dateFormat(new Date().getTime());
                $('.editApplicationStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd HH:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.editApplicationStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".editApplicationStartTime").val();
                        vc.component.editApplicationKeyInfo.startTime = value;
                    });
                $('.editApplicationEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd HH:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.editApplicationEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".editApplicationEndTime").val();
                        vc.component.editApplicationKeyInfo.endTime = value;
                    });
            },
            editApplicationKeyValidate: function () {
                return vc.validate.validate({
                    editApplicationKeyInfo: vc.component.editApplicationKeyInfo
                }, {
                    'editApplicationKeyInfo.name': [
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
                    'editApplicationKeyInfo.tel': [
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
                    'editApplicationKeyInfo.typeCd': [
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
                    'editApplicationKeyInfo.sex': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "性别不能为空"
                        }
                    ],
                    'editApplicationKeyInfo.age': [
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
                    'editApplicationKeyInfo.idCard': [
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
                    'editApplicationKeyInfo.startTime': [
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
                    'editApplicationKeyInfo.endTime': [
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
                    'editApplicationKeyInfo.applicationKeyId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "钥匙申请ID不能为空"
                        }],
                    'editApplicationKeyInfo.locationTypeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "申请位置不能为空"
                        }
                    ],
                    'editApplicationKeyInfo.locationObjId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "具体位置不能为空"
                        }
                    ]

                });
            },
            editApplicationKey: function () {
                vc.component.editApplicationKeyInfo.communityId = vc.getCurrentCommunity().communityId;
                if (vc.component.editApplicationKeyInfo.locationTypeCd != '2000' && vc.component.editApplicationKeyInfo.locationTypeCd != '3000') { //大门时直接写 小区ID
                    vc.component.editApplicationKeyInfo.locationObjId = vc.component.editApplicationKeyInfo.communityId;
                } else if (vc.component.editApplicationKeyInfo.locationTypeCd == '2000') {
                    vc.component.editApplicationKeyInfo.locationObjId = vc.component.editApplicationKeyInfo.unitId;
                } else if (vc.component.editApplicationKeyInfo.locationTypeCd == '3000') {
                    vc.component.editApplicationKeyInfo.locationObjId = vc.component.editApplicationKeyInfo.roomId;
                } else {
                    vc.toast("设备位置值错误");
                    return;
                }
                if (!vc.component.editApplicationKeyValidate()) {
                    vc.message(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editApplicationKey',
                    'update',
                    JSON.stringify(vc.component.editApplicationKeyInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editApplicationKeyModel').modal('hide');
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
            refreshEditApplicationKeyInfo: function () {
                vc.component.editApplicationKeyInfo = {
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
            _closeEditApplicationKeyView: function () {
                vc.emit('applicationKeyManage', 'listApplicationKey', {});
            },
        }
    });

})(window.vc, window.vc.component);
