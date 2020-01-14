(function(vc,vm){

    vc.extends({
        data:{
            editActivitiesViewInfo:{
                activitiesId: '',
                title: '',
                typeCd: '',
                headerImg: '',
                context: '',
                startTime: '',
                endTime: ''
            }
        },
         _initMethod:function(){
             vc.component._initEditActivitiesInfo();
         },
         _initEvent:function(){
             vc.on('editActivitiesViewInfo','openEditActivitiesModal',function(_params){
                vc.component.refreshEditActivitiesInfo();
                _params.context = filterXSS(_params.context);
                vc.component.editActivitiesViewInfo = _params;
            });
            vc.on('editActivitiesViewInfo','activitiesEditActivitiesInfo',function(_params){
                vc.component.refreshEditActivitiesInfo();
                _params.context = filterXSS(_params.context);
                vc.copyObject(_params,vc.component.editActivitiesViewInfo);
                $(".eidtSummernote").summernote('code', vc.component.editActivitiesViewInfo.context);
                var photos = [];
                photos.push(vc.component.editActivitiesViewInfo.headerImg);
                vc.emit('editActivitiesView', 'uploadImage', 'notifyPhotos', photos);
            });

             vc.on("editActivitiesViewInfo", "notifyUploadImage", function (_param) {
                 if(vc.notNull(_param) && _param.length >0){
                     vc.component.editActivitiesViewInfo.headerImg = _param[0];
                 }else{
                     vc.component.editActivitiesViewInfo.headerImg = '';
                 }
             });

        },
        methods:{
            editActivitiesValidate:function(){
                return vc.validate.validate({
                    editActivitiesViewInfo: vc.component.editActivitiesViewInfo
                }, {
                    'editActivitiesViewInfo.title': [
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
                    'editActivitiesViewInfo.typeCd': [
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
                    'editActivitiesViewInfo.headerImg': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "头部照片不能为空"
                        }
                    ],
                    'editActivitiesViewInfo.context': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "活动内容不能为空"
                        }
                    ],
                    'editActivitiesViewInfo.startTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "开始时间不能为空"
                        },
                        {
                            limit: "dateTime",
                            param: "",
                            errInfo: "开始时间格式错误"
                        },
                    ],
                    'editActivitiesViewInfo.endTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "结束时间不能为空"
                        },
                        {
                            limit: "dateTime",
                            param: "",
                            errInfo: "结束时间格式错误"
                        },
                    ],
                    'editActivitiesViewInfo.activitiesId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "活动ID不能为空"
                        }]

                });
             },
            editActivities:function(){
                if(!vc.component.editActivitiesValidate()){
                    vc.toast(vc.validate.errInfo);
                    return ;
                }
                vc.component.editActivitiesViewInfo.communityId = vc.getCurrentCommunity().communityId;

                vc.http.post(
                    'editActivities',
                    'update',
                    JSON.stringify(vc.component.editActivitiesViewInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                             vc.emit('activitiesManage','listActivities',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            refreshEditActivitiesInfo:function(){
                vc.component.editActivitiesViewInfo= {
                    activitiesId: '',
                    title: '',
                    typeCd: '',
                    headerImg: '',
                    context: '',
                    startTime: '',
                    endTime: '',
                }
            },
            _initEditActivitiesInfo:function(){
                //vc.component.editActivitiesViewInfo.startTime = vc.dateFormat(new Date().getTime());
                 $('.editActivitiesStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.editActivitiesStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".editActivitiesStartTime").val();
                        vc.component.editActivitiesViewInfo.startTime = value;
                    });
                $('.editActivitiesEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.editActivitiesEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".editActivitiesEndTime").val();
                        vc.component.editActivitiesViewInfo.endTime = value;
                    });
                $('.eidtSummernote').summernote({
                    lang:'zh-CN',
                    height: 300,
                    placeholder:'必填，请输入公告内容',
                    callbacks : {
                         onImageUpload: function(files, editor, $editable) {
                             vc.component.sendEditFile(files);
                         },
                         onChange:function(contents,$editable){
                            vc.component.editActivitiesViewInfo.context = contents;
                         }
                    }
                });

            },
            sendEditFile:function(files){
                console.log('上传图片');
            },
            closeEditActivitiesInfo:function(){
                 vc.emit('activitiesManage','listActivities',{});

            },
        }
    });

})(window.vc,window.vc.component);
