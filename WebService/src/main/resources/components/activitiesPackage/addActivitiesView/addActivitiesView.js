(function (vc) {

    vc.extends({
        data: {
            addActivitiesViewInfo: {
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
            vc.component._initActivitiesInfo();
        },
        _initEvent: function () {
            vc.on('addActivitiesView', 'openAddActivitiesView', function () {
                //vc.component._initActivitiesInfo();

            });

            vc.on("addActivitiesView", "notifyUploadImage", function (_param) {
                if(!vc.isEmpty(_param) && _param.length >0){
                    vc.component.addActivitiesViewInfo.headerImg = _param[0];
                }else{
                    vc.component.addActivitiesViewInfo.headerImg = '';
                }
            });
        },
        methods: {
            addActivitiesValidate() {
                return vc.validate.validate({
                    addActivitiesViewInfo: vc.component.addActivitiesViewInfo
                }, {
                    'addActivitiesViewInfo.title': [
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
                    'addActivitiesViewInfo.typeCd': [
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
                    'addActivitiesViewInfo.headerImg': [
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
                    'addActivitiesViewInfo.context': [
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
                    'addActivitiesViewInfo.startTime': [
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
                    'addActivitiesViewInfo.endTime': [
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

                vc.component.addActivitiesViewInfo.communityId = vc.getCurrentCommunity().communityId;

                vc.http.post(
                    'addActivitiesView',
                    'save',
                    JSON.stringify(vc.component.addActivitiesViewInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model

                            vc.component.clearaddActivitiesViewInfo();
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
            clearaddActivitiesViewInfo: function () {
                vc.component.addActivitiesViewInfo = {
                    activitiesId: '',
                    title: '',
                    typeCd: '',
                    headerImg: '',
                    context: '',
                    startTime: '',
                    endTime: ''

                };
            },
            _initActivitiesInfo: function () {
                vc.component.addActivitiesViewInfo.startTime = vc.dateFormat(new Date().getTime());
                $('.activitiesStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.activitiesStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".activitiesStartTime").val();
                        vc.component.addActivitiesViewInfo.startTime = value;
                    });
                $('.activitiesEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.activitiesEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".activitiesEndTime").val();
                        vc.component.addActivitiesViewInfo.endTime = value;
                    });
                var $summernote = $('.summernote').summernote({
                    lang: 'zh-CN',
                    height: 300,
                    placeholder: '必填，请输入公告内容',
                    callbacks: {
                        onImageUpload: function (files, editor, $editable) {
                            vc.component.sendFile($summernote, files);
                        },
                        onChange: function (contents, $editable) {
                            vc.component.addActivitiesViewInfo.context = contents;
                        }
                    },
                    toolbar: [
                        ['style', ['style']],
                        ['font', ['bold', 'italic', 'underline', 'clear']],
                        ['fontname', ['fontname']],
                        ['color', ['color']],
                        ['para', ['ul', 'ol', 'paragraph']],
                        ['height', ['height']],
                        ['table', ['table']],
                        ['insert', ['link', 'picture']],
                        ['view', ['fullscreen', 'codeview']],
                        ['help', ['help']]
                    ],
                });
            },
            closeActivitiesInfo: function () {
                vc.emit('activitiesManage', 'listActivities', {});

            },
            sendFile: function ($summernote, files) {
                console.log('上传图片', files);

                var param = new FormData();
                param.append("uploadFile", files[0]);
                param.append('communityId', vc.getCurrentCommunity().communityId);

                vc.http.upload(
                    'addActivitiesView',
                    'uploadImage',
                    param,
                    {
                        emulateJSON: true,
                        //添加请求头
                        headers: {
                            "Content-Type": "multipart/form-data"
                        }
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            var data = JSON.parse(json);
                            //关闭model
                            $summernote.summernote('insertImage', "/callComponent/download/getFile/file?fileId=" + data.fileId + "&communityId=" + vc.getCurrentCommunity().communityId);
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.message(errInfo);
                    });

            }

        }
    });

})(window.vc);
