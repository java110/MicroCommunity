(function (vc, vm) {

    vc.extends({
        data: {
            editAdvertInfo: {
                advertId: '',
                adName: '',
                adTypeCd: '',
                classify: '',
                locationTypeCd: '',
                locationObjId: '',
                state: '',
                seq: '',
                startTime: '',
                endTime: '',
                floorId: '',
                floorNum: '',
                floorName: '',
                unitId: '',
                unitNum: '',
                roomId: '',
                roomNum: '',
                photos: [],
                viewType: '',
                vedioName: '',

            }
        },
        _initMethod: function () {
            vc.component._initEditAdvertDateInfo();

        },
        _initEvent: function () {
            vc.on('editAdvert', 'openEditAdvertModal', function (_params) {
                vc.component.refreshEditAdvertInfo();
                $('#editAdvertModel').modal('show');
                vc.copyObject(_params, vc.component.editAdvertInfo);
                //根据位置类型 传输数据
                if (vc.component.editAdvertInfo.locationTypeCd == '4000') {
                    vc.emit('editAdvert', 'floorSelect2', 'setFloor', {
                        floorId: vc.component.editAdvertInfo.floorId,
                        floorNum: vc.component.editAdvertInfo.floorNum
                    });

                } else if (vc.component.editAdvertInfo.locationTypeCd == '2000') {
                    vc.emit('editAdvert', 'floorSelect2', 'setFloor', {
                        floorId: vc.component.editAdvertInfo.floorId,
                        floorNum: vc.component.editAdvertInfo.floorNum
                    });
                    vc.emit('editAdvert', 'unitSelect2', 'setUnit', {
                        floorId: vc.component.editAdvertInfo.floorId,
                        floorNum: vc.component.editAdvertInfo.floorNum,
                        unitId: vc.component.editAdvertInfo.unitId,
                        unitNum: vc.component.editAdvertInfo.unitNum,
                    });
                } else if (vc.component.editAdvertInfo.locationTypeCd == '3000') {
                    vc.emit('editAdvert', 'floorSelect2', 'setFloor', {
                        floorId: vc.component.editAdvertInfo.floorId,
                        floorNum: vc.component.editAdvertInfo.floorNum
                    });
                    vc.emit('editAdvert', 'unitSelect2', 'setUnit', {
                        floorId: vc.component.editAdvertInfo.floorId,
                        floorNum: vc.component.editAdvertInfo.floorNum,
                        unitId: vc.component.editAdvertInfo.unitId,
                        unitNum: vc.component.editAdvertInfo.unitNum,
                    });
                    vc.emit('editAdvert', 'roomSelect2', 'setRoom', {
                        floorId: vc.component.editAdvertInfo.floorId,
                        floorNum: vc.component.editAdvertInfo.floorNum,
                        unitId: vc.component.editAdvertInfo.unitId,
                        unitNum: vc.component.editAdvertInfo.unitNum,
                        roomId: vc.component.editAdvertInfo.roomId,
                        roomNum: vc.component.editAdvertInfo.roomNum,
                    });
                }
                vc.component._loadAdvertItem();
                vc.component.editAdvertInfo.communityId = vc.getCurrentCommunity().communityId;
                //查询 广告属性
            });

            vc.on("editAdvert", "notify", function (_param) {
                if (_param.hasOwnProperty("floorId")) {
                    vc.component.editAdvertInfo.floorId = _param.floorId;
                }

                if (_param.hasOwnProperty("unitId")) {
                    vc.component.editAdvertInfo.unitId = _param.unitId;
                }

                if (_param.hasOwnProperty("roomId")) {
                    vc.component.editAdvertInfo.roomId = _param.roomId;
                }
            });

            vc.on("editAdvert", "notifyUploadImage", function (_param) {
                vc.component.editAdvertInfo.photos = _param;
            });
            vc.on("editAdvert", "notifyUploadVedio", function (_param) {
                vc.component.editAdvertInfo.vedioName = _param.realFileName;
            });
        },
        methods: {
            _initEditAdvertDateInfo: function () {
                vc.component.editAdvertInfo.startTime = vc.dateFormat(new Date().getTime());
                $('.editAdvertStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd HH:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.editAdvertStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".editAdvertStartTime").val();
                        vc.component.editAdvertInfo.startTime = value;
                    });
                $('.editAdvertEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd HH:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.editAdvertEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".editAdvertEndTime").val();
                        vc.component.editAdvertInfo.endTime = value;
                    });
            },
            editAdvertValidate: function () {
                return vc.validate.validate({
                    editAdvertInfo: vc.component.editAdvertInfo
                }, {
                    'editAdvertInfo.adName': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "广告名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,200",
                            errInfo: "广告名称不能超过200位"
                        },
                    ],
                    'editAdvertInfo.adTypeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "广告类型不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "广告类型不能为空"
                        },
                    ],
                    'editAdvertInfo.classify': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "广告分类不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "广告分类格式错误"
                        },
                    ],
                    'editAdvertInfo.locationTypeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投放位置不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "投放位置格式错误"
                        },
                    ],
                    'editAdvertInfo.locationObjId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "具体位置不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "具体位置不是有效数字"
                        },
                    ],
                    'editAdvertInfo.seq': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "播放顺序不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "播放顺序不是有效的数字"
                        },
                    ],
                    'editAdvertInfo.startTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投放时间不能为空"
                        },
                        {
                            limit: "dateTime",
                            param: "",
                            errInfo: "不是有效的时间格式"
                        },
                    ],
                    'editAdvertInfo.endTime': [
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
                    'editAdvertInfo.advertId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "广告ID不能为空"
                        }]

                });
            },
            editAdvert: function () {
                vc.component.editAdvertInfo.communityId = vc.getCurrentCommunity().communityId;
                if (vc.component.editAdvertInfo.locationTypeCd == '1000') { //大门时直接写 小区ID
                    vc.component.editAdvertInfo.locationObjId = vc.component.editAdvertInfo.communityId;
                } else if (vc.component.editAdvertInfo.locationTypeCd == '2000') {
                    vc.component.editAdvertInfo.locationObjId = vc.component.editAdvertInfo.unitId;
                } else if (vc.component.editAdvertInfo.locationTypeCd == '3000') {
                    vc.component.editAdvertInfo.locationObjId = vc.component.editAdvertInfo.roomId;
                } else if (vc.component.editAdvertInfo.locationTypeCd == '4000') {
                    vc.component.editAdvertInfo.locationObjId = vc.component.editAdvertInfo.floorId;
                } else {
                    vc.toast("设备位置值错误");
                    return;
                }
                if (!vc.component.editAdvertValidate()) {
                    vc.message(vc.validate.errInfo);
                    return;
                }

                if (vc.component.editAdvertInfo.viewType == '99') {
                    vc.component.editAdvertInfo.vedioName = '';
                } else {
                    vc.component.editAdvertInfo.photos = [];

                }

                vc.http.post(
                    'editAdvert',
                    'update',
                    JSON.stringify(vc.component.editAdvertInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editAdvertModel').modal('hide');
                            vc.emit('advertManage', 'listAdvert', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            _loadAdvertItem: function () {
                var param = {
                    params: {
                        advertId: vc.component.editAdvertInfo.advertId,
                        communityId: vc.getCurrentCommunity().communityId
                    }
                };
                //发送get请求
                vc.http.get('editAdvert',
                    'listAdvertItem',
                    param,
                    function (json, res) {
                        var _advertItemInfo = JSON.parse(json);
                        vc.component._freshPhotoOrVedio(_advertItemInfo.advertItems);

                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _freshPhotoOrVedio: function (_advertItems) {
                //判断属性中是否有照片
                _advertItems.forEach(function (_item) {
                        vc.component.editAdvertInfo.viewType = _item.itemTypeCd;

                        if (_item.itemTypeCd == '8888') {
                            vc.component.editAdvertInfo.photos.push(_item.url);
                            vc.emit('editAdvert', 'uploadImage', 'notifyPhotos', vc.component.editAdvertInfo.photos);
                        } else {
                            vc.component.editAdvertInfo.vedioName = _item.url;
                            vc.emit('editAdvert', 'uploadVedio', 'notifyVedio', _item.url);
                        }

                    }
                );


            },
            refreshEditAdvertInfo: function () {
                vc.component.editAdvertInfo = {
                    advertId: '',
                    adName: '',
                    adTypeCd: '',
                    classify: '',
                    locationTypeCd: '',
                    locationObjId: '',
                    state: '',
                    seq: '',
                    startTime: '',
                    endTime: '',
                    floorId: '',
                    floorNum: '',
                    floorName: '',
                    unitId: '',
                    unitNum: '',
                    roomId: '',
                    roomNum: '',
                    photos: [],
                    viewType: '',
                    vedioName: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);
