(function(vc, vm) {

    vc.extends({
        data: {
            editCarBlackWhiteInfo: {
                bwId: '',
                blackWhite: '',
                carNum: '',
                startTime: '',
                endTime: '',

            }
        },
        _initMethod: function() {
            vc.component._initEditCarBlackWhiteDateInfo();
        },
        _initEvent: function() {
            vc.on('editCarBlackWhite', 'openEditCarBlackWhiteModal',
            function(_params) {
                vc.component.refreshEditCarBlackWhiteInfo();
                $('#editCarBlackWhiteModel').modal('show');
                vc.copyObject(_params, vc.component.editCarBlackWhiteInfo);
                vc.component.editCarBlackWhiteInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            _initEditCarBlackWhiteDateInfo: function () {
                vc.component.editCarBlackWhiteInfo.startTime = vc.dateFormat(new Date().getTime());
                $('.editCarBlackWhiteStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.editCarBlackWhiteStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".editCarBlackWhiteStartTime").val();
                        vc.component.editCarBlackWhiteInfo.startTime = value;
                    });
                $('.editCarBlackWhiteEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.editCarBlackWhiteEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".editCarBlackWhiteEndTime").val();
                        vc.component.editCarBlackWhiteInfo.endTime = value;
                    });
            },
            editCarBlackWhiteValidate: function() {
                return vc.validate.validate({
                    editCarBlackWhiteInfo: vc.component.editCarBlackWhiteInfo
                },
                {
                    'editCarBlackWhiteInfo.blackWhite': [{
                        limit: "required",
                        param: "",
                        errInfo: "名单类型不能为空"
                    },
                    {
                        limit: "num",
                        param: "",
                        errInfo: "名单类型格式错误"
                    },
                    ],
                    'editCarBlackWhiteInfo.carNum': [{
                        limit: "required",
                        param: "",
                        errInfo: "车牌号不能为空"
                    },
                    {
                        limit: "maxin",
                        param: "1,12",
                        errInfo: "车牌号大于12位"
                    },
                    ],
                    'editCarBlackWhiteInfo.startTime': [{
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
                    'editCarBlackWhiteInfo.endTime': [{
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
                    'editCarBlackWhiteInfo.bwId': [{
                        limit: "required",
                        param: "",
                        errInfo: "黑白名单ID不能为空"
                    }]

                });
            },
            editCarBlackWhite: function() {
                vc.component.editCarBlackWhiteInfo.communityId = vc.getCurrentCommunity().communityId;

                if (!vc.component.editCarBlackWhiteValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.http.post('editCarBlackWhite', 'update', JSON.stringify(vc.component.editCarBlackWhiteInfo), {
                    emulateJSON: true
                },
                function(json, res) {
                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                    if (res.status == 200) {
                        //关闭model
                        $('#editCarBlackWhiteModel').modal('hide');
                        vc.emit('carBlackWhiteManage', 'listCarBlackWhite', {});
                        return;
                    }
                    vc.message(json);
                },
                function(errInfo, error) {
                    console.log('请求失败处理');

                    vc.message(errInfo);
                });
            },
            refreshEditCarBlackWhiteInfo: function() {
                vc.component.editCarBlackWhiteInfo = {
                    bwId: '',
                    blackWhite: '',
                    carNum: '',
                    startTime: '',
                    endTime: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);