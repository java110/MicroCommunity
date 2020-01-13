(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addActivitiesInfo: {
                activitiesId: '',
                title: '',
                typeCd: '',
                headerImg: '',
                context: '',
                startTime: '',
                endTime: '',

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('addActivities', 'openAddActivitiesModal', function () {
                $('#addActivitiesModel').modal('show');
            });
        },
        methods: {
            addActivitiesValidate() {
                return vc.validate.validate({
                    addActivitiesInfo: vc.component.addActivitiesInfo
                }, {
                    'addActivitiesInfo.title': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "活动标题不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,200",
                            errInfo: "活动标题不能超过200位"
                        },
                    ],
                    'addActivitiesInfo.typeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "活动类型不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "活动类型格式错误"
                        },
                    ],
                    'addActivitiesInfo.headerImg': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "头部照片不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,200",
                            errInfo: "头部照片格式错误"
                        },
                    ],
                    'addActivitiesInfo.context': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "活动内容不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,4000",
                            errInfo: "活动内容太长"
                        },
                    ],
                    'addActivitiesInfo.startTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "开始时间不能为空"
                        },
                        {
                            limit: "date",
                            param: "",
                            errInfo: "开始时间格式错误"
                        },
                    ],
                    'addActivitiesInfo.endTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "结束时间不能为空"
                        },
                        {
                            limit: "date",
                            param: "",
                            errInfo: "结束时间格式错误"
                        },
                    ],


                });
            },
            saveActivitiesInfo: function () {
                if (!vc.component.addActivitiesValidate()) {
                    vc.toast(vc.validate.errInfo);

                    return;
                }

                vc.component.addActivitiesInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addActivitiesInfo);
                    $('#addActivitiesModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addActivities',
                    'save',
                    JSON.stringify(vc.component.addActivitiesInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addActivitiesModel').modal('hide');
                            vc.component.clearAddActivitiesInfo();
                            vc.emit('activitiesManage', 'listActivities', {});

                            return;
                        }
                        vc.message(json);

                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);

                    });
            },
            clearAddActivitiesInfo: function () {
                vc.component.addActivitiesInfo = {
                    title: '',
                    typeCd: '',
                    headerImg: '',
                    context: '',
                    startTime: '',
                    endTime: '',

                };
            }
        }
    });

})(window.vc);
