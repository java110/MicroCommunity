(function(vc,vm){

    vc.extends({
        data:{
            editNoticeInfo:{
                noticeId:'',
title:'',
noticeTypeCd:'',
context:'',
startTime:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editNotice','openEditNoticeModal',function(_params){
                vc.component.refreshEditNoticeInfo();
                $('#editNoticeModel').modal('show');
                vc.component.editNoticeInfo = _params;
                vc.component.editNoticeInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editNoticeValidate:function(){
                        return vc.validate.validate({
                            editNoticeInfo:vc.component.editNoticeInfo
                        },{
                            'editNoticeInfo.title':[
{
                            limit:"required",
                            param:"",
                            errInfo:"标题不能为空"
                        },
 {
                            limit:"maxin",
                            param:"4,100",
                            errInfo:"小区名称必须在4至100字符之间"
                        },
                    ],
'editNoticeInfo.noticeTypeCd':[
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
'editNoticeInfo.context':[
{
                            limit:"required",
                            param:"",
                            errInfo:"公告内容不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"500",
                            errInfo:"公告内容不能超过500个字"
                        },
                    ],
'editNoticeInfo.startTime':[
{
                            limit:"required",
                            param:"",
                            errInfo:"开始时间不能为空"
                        },
 {
                            limit:"dateTime",
                            param:"",
                            errInfo:"开始时间不是有效的日志"
                        },
                    ],
'editNoticeInfo.noticeId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"公告ID不能为空"
                        }]

                        });
             },
            editNotice:function(){
                if(!vc.component.editNoticeValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editNotice',
                    'update',
                    JSON.stringify(vc.component.editNoticeInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editNoticeModel').modal('hide');
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
            refreshEditNoticeInfo:function(){
                vc.component.editNoticeInfo= {
                  noticeId:'',
title:'',
noticeTypeCd:'',
context:'',
startTime:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
