(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addAdvertInfo: {
                advertId: '',
                adName: '',
                adTypeCd: '',
                classify: '',
                locationTypeCd: '',
                locationObjId: '',
                seq: '',
                startTime: '',
                endTime: '',
                floorId: '',
                floorNum: '',
                floorName: '',
                unitId: '',
                unitName: '',
                roomId: '',
                photos: [],
                viewType: '',
                vedioName: ''

            }
        },
        _initMethod: function () {
            vc.component._initAddAdvertDateInfo();
        },
        _initEvent: function () {
            vc.on('addAdvert', 'openAddAdvertModal', function () {
                $('#addAdvertModel').modal('show');
            });

            vc.on("addAdvert", "notify", function (_param) {
                if (_param.hasOwnProperty("floorId")) {
                    vc.component.addAdvertInfo.floorId = _param.floorId;
                }

                if (_param.hasOwnProperty("unitId")) {
                    vc.component.addAdvertInfo.unitId = _param.unitId;
                }

                if (_param.hasOwnProperty("roomId")) {
                    vc.component.addAdvertInfo.roomId = _param.roomId;
                }
            });

            vc.on("addAdvert", "notifyUploadImage", function (_param) {
                vc.component.addAdvertInfo.photos = _param;
            });
            vc.on("addAdvert", "notifyUploadVedio", function (_param) {
                vc.component.addAdvertInfo.vedioName = _param.realFileName;
            });
        },
        methods: {
            _initAddAdvertDateInfo: function () {
                vc.component.addAdvertInfo.startTime = vc.dateFormat(new Date().getTime());
                $('.addAdvertStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd HH:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.addAdvertStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".addAdvertStartTime").val();
                        vc.component.addAdvertInfo.startTime = value;
                    });
                $('.addAdvertEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd HH:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.addAdvertEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".addAdvertEndTime").val();
                        vc.component.addAdvertInfo.endTime = value;
                    });
            },
            addAdvertValidate: function () {
                return vc.validate.validate({
                    addAdvertInfo: vc.component.addAdvertInfo
                }, {
                    'addAdvertInfo.adName': [
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
                    'addAdvertInfo.adTypeCd': [
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
                    'addAdvertInfo.classify': [
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
                    'addAdvertInfo.locationTypeCd': [
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
                    'addAdvertInfo.locationObjId': [
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
                    'addAdvertInfo.seq': [
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
                    'addAdvertInfo.startTime': [
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
                    'addAdvertInfo.endTime': [
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


                });
            },
            saveAdvertInfo: function () {
                vc.component.addAdvertInfo.communityId = vc.getCurrentCommunity().communityId;
                if (vc.component.addAdvertInfo.locationTypeCd == '1000') { //大门时直接写 小区ID
                    vc.component.addAdvertInfo.locationObjId = vc.component.addAdvertInfo.communityId;
                } else if (vc.component.addAdvertInfo.locationTypeCd == '2000') {
                    vc.component.addAdvertInfo.locationObjId = vc.component.addAdvertInfo.unitId;
                } else if (vc.component.addAdvertInfo.locationTypeCd == '3000') {
                    vc.component.addAdvertInfo.locationObjId = vc.component.addAdvertInfo.roomId;
                } else if (vc.component.addAdvertInfo.locationTypeCd == '4000') {
                    vc.component.addAdvertInfo.locationObjId = vc.component.addAdvertInfo.floorId;
                } else {
                    vc.toast("设备位置值错误");
                    return;
                }
                if (!vc.component.addAdvertValidate()) {
                    vc.message(vc.validate.errInfo);

                    return;
                }
                if (vc.component.addAdvertInfo.viewType == '8888') {
                    vc.component.addAdvertInfo.vedioName = '';
                } else {
                    vc.component.addAdvertInfo.photos = [];

                }

                vc.component.addAdvertInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addAdvertInfo);
                    $('#addAdvertModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addAdvert',
                    'save',
                    JSON.stringify(vc.component.addAdvertInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addAdvertModel').modal('hide');
                            vc.component.clearAddAdvertInfo();
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
            clearAddAdvertInfo: function () {
                vc.emit('addAdvert', 'floorSelect2', 'clearFloor', {});
                vc.emit('addAdvert', 'unitSelect2', 'clearUnit', {});
                vc.emit('addAdvert', 'roomSelect2', 'clearRoom', {});
                vc.emit('addAdvert', 'uploadImage', 'clearImage', {});
                vc.emit('addAdvert', 'uploadVedio', 'clearVedio', {});
                vc.component._initAddAdvertDateInfo();

                vc.component.addAdvertInfo = {
                    advertId: '',
                    adName: '',
                    adTypeCd: '',
                    classify: '',
                    locationTypeCd: '',
                    locationObjId: '',
                    seq: '',
                    startTime: '',
                    endTime: '',
                    floorId: '',
                    floorNum: '',
                    floorName: '',
                    unitId: '',
                    unitName: '',
                    roomId: '',
                    photos: [],
                    viewType: '',
                    vedioName: ''
                };
            }
        }
    });

})(window.vc);
