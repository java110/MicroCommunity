(function (vc) {

    vc.extends({
        data: {
            addNoticeInfo: {
                title: '',
                noticeTypeCd: '',
                context: '',
                startTime: '',

            }
        },
        _initMethod: function () {
            vc.component._initNoticeContextText();

        },
        _initEvent: function () {
            vc.on('addNotice', 'openAddNoticeModal', function () {
                $('#addNoticeModel').modal('show');
            });
        },
        methods: {
            addNoticeValidate() {
                return vc.validate.validate({
                    addNoticeInfo: vc.component.addNoticeInfo
                }, {
                    'addNoticeInfo.title': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "标题不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "4,100",
                            errInfo: "标题必须在4至100字符之间"
                        },
                    ],
                    'addNoticeInfo.noticeTypeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "公告类型不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "公告类型错误"
                        },
                    ],
                    'addNoticeInfo.context': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "公告内容不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "500",
                            errInfo: "公告内容不能超过500个字"
                        },
                    ],
                    'addNoticeInfo.startTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "开始时间不能为空"
                        },
                        {
                            limit: "date",
                            param: "",
                            errInfo: "开始时间不是有效的日志"
                        },
                    ],


                });
            },
            saveNoticeInfo: function () {
                if (!vc.component.addNoticeValidate()) {
                    vc.toast(vc.validate.errInfo);

                    return;
                }

                vc.component.addNoticeInfo.communityId = vc.getCurrentCommunity().communityId;

                vc.http.post(
                    'addNotice',
                    'save',
                    JSON.stringify(vc.component.addNoticeInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addNoticeModel').modal('hide');
                            vc.component.clearAddNoticeInfo();
                            vc.emit('noticeManage', 'listNotice', {});

                            return;
                        }
                        vc.message(json);

                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);

                    });
            },
            clearAddNoticeInfo: function () {
                vc.component.addNoticeInfo = {
                    title: '',
                    noticeTypeCd: '',
                    context: '',
                    startTime: '',

                };
            },
            _initNoticeContextText: function () {
                $('.summernote').summernote();
            }
        }
    });

})(window.vc);
