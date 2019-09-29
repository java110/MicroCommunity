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
                    'addNotice',
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
                $('.summernote').summernote({
                    lang:'zh-CN',
                    height: 300,
                    placeholder:'必填，请输入公告内容',
                    callbacks : {
                         onImageUpload: function(files, editor, $editable) {
                             sendFile(files);
                         },
                         onChange:function(contents,$editable){
                            vc.component.addNoticeViewInfo.context = contents;
                         }
                    }
                });
            },
            closeNoticeInfo:function(){
                 vc.emit('noticeManage','listNotice',{});

            },
            sendFile:function(files){
                console.log('上传图片');
            }

        }
    });

})(window.vc);
