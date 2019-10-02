(function(vc){

    vc.extends({
        data:{
            addNoticeViewInfo:{
                title:'',
                noticeTypeCd:'',
                context:'',
                startTime:'',
                endTime:'',

            }
        },
         _initMethod:function(){
            vc.component._initNoticeInfo();
         },
         _initEvent:function(){
            vc.on('addNoticeView','openAddNoticeView',function(){
                //vc.component._initNoticeInfo();

            });
        },
        methods:{
            addNoticeValidate(){
                return vc.validate.validate({
                    addNoticeViewInfo:vc.component.addNoticeViewInfo
                },{
                    'addNoticeViewInfo.title':[
{
                            limit:"required",
                            param:"",
                            errInfo:"标题不能为空"
                        },
 {
                            limit:"maxin",
                            param:"4,100",
                            errInfo:"小区名称必须在2至100字符之间"
                        },
                    ],
'addNoticeViewInfo.noticeTypeCd':[
{
                            limit:"required",
                            param:"",
                            errInfo:"公告类型不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"公告类型错误"
                        },
                    ],
'addNoticeViewInfo.context':[
{
                            limit:"required",
                            param:"",
                            errInfo:"公告内容不能为空"
                        },
                        {
                            limit:"maxLength",
                            param:"10000",
                            errInfo:"公告内容不能超过10000个字"
                        },
                    ],
                    'addNoticeViewInfo.startTime':[
                    {
                        limit:"required",
                        param:"",
                        errInfo:"开始时间不能为空"
                    },
                     {
                            limit:"dateTime",
                            param:"",
                            errInfo:"开始时间不是有效的日期"
                        },
                    ],
                    'addNoticeViewInfo.endTime':[
                    {
                        limit:"required",
                        param:"",
                        errInfo:"开始时间不能为空"
                    },
                     {
                            limit:"dateTime",
                            param:"",
                            errInfo:"开始时间不是有效的日期"
                        },
                    ],




                });
            },
            saveNoticeInfo:function(){
                if(!vc.component.addNoticeValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.addNoticeViewInfo.communityId = vc.getCurrentCommunity().communityId;

                vc.http.post(
                    'addNoticeView',
                    'save',
                    JSON.stringify(vc.component.addNoticeViewInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model

                            vc.component.clearaddNoticeViewInfo();
                            vc.emit('noticeManage','listNotice',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearaddNoticeViewInfo:function(){
                vc.component.addNoticeViewInfo = {
                        title:'',
                                    noticeTypeCd:'',
                                    context:'',
                                    startTime:'',

                                        };
            },
            _initNoticeInfo:function(){
                vc.component.addNoticeViewInfo.startTime = vc.dateFormat(new Date().getTime());
                 $('.noticeStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd HH:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.noticeStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".noticeStartTime").val();
                        vc.component.addNoticeViewInfo.startTime = value;
                    });
                $('.noticeEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd HH:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.noticeEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".noticeEndTime").val();
                        vc.component.addNoticeViewInfo.endTime = value;
                    });
                var $summernote = $('.summernote').summernote({
                    lang:'zh-CN',
                    height: 300,
                    placeholder:'必填，请输入公告内容',
                    callbacks : {
                         onImageUpload: function(files, editor, $editable) {
                             vc.component.sendFile($summernote,files);
                         },
                         onChange:function(contents,$editable){
                            vc.component.addNoticeViewInfo.context = contents;
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
            closeNoticeInfo:function(){
                 vc.emit('noticeManage','listNotice',{});

            },
            sendFile:function($summernote,files){
                console.log('上传图片',files);

                var param = new FormData();
                param.append("uploadFile", files[0]);
                param.append('communityId',vc.getCurrentCommunity().communityId);

                vc.http.upload(
                    'addNoticeView',
                    'uploadImage',
                    param,
                    {
                        emulateJSON:true,
                        //添加请求头
                        headers: {
                            "Content-Type": "multipart/form-data"
                        }
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            var data = JSON.parse(json);
                            //关闭model
                            $summernote.summernote('insertImage', "/callComponent/download/getFile/file?fileId="+data.fileId +"&communityId="+vc.getCurrentCommunity().communityId);
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(errInfo);
                     });

            }

        }
    });

})(window.vc);
