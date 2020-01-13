(function(vc,vm){

    vc.extends({
        data:{
            editActivitiesInfo:{
                activitiesId:'',
title:'',
typeCd:'',
headerImg:'',
context:'',
startTime:'',
endTime:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editActivities','openEditActivitiesModal',function(_params){
                vc.component.refreshEditActivitiesInfo();
                $('#editActivitiesModel').modal('show');
                vc.copyObject(_params, vc.component.editActivitiesInfo );
                vc.component.editActivitiesInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editActivitiesValidate:function(){
                        return vc.validate.validate({
                            editActivitiesInfo:vc.component.editActivitiesInfo
                        },{
                            'editActivitiesInfo.title':[
{
                            limit:"required",
                            param:"",
                            errInfo:"活动标题不能为空"
                        },
 {
                            limit:"maxin",
                            param:"1,200",
                            errInfo:"活动标题不能超过200位"
                        },
                    ],
'editActivitiesInfo.typeCd':[
{
                            limit:"required",
                            param:"",
                            errInfo:"活动类型不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"活动类型格式错误"
                        },
                    ],
'editActivitiesInfo.headerImg':[
{
                            limit:"required",
                            param:"",
                            errInfo:"头部照片不能为空"
                        },
 {
                            limit:"maxin",
                            param:"1,200",
                            errInfo:"头部照片格式错误"
                        },
                    ],
'editActivitiesInfo.context':[
{
                            limit:"required",
                            param:"",
                            errInfo:"活动内容不能为空"
                        },
 {
                            limit:"maxin",
                            param:"1,4000",
                            errInfo:"活动内容太长"
                        },
                    ],
'editActivitiesInfo.startTime':[
{
                            limit:"required",
                            param:"",
                            errInfo:"开始时间不能为空"
                        },
 {
                            limit:"date",
                            param:"",
                            errInfo:"开始时间格式错误"
                        },
                    ],
'editActivitiesInfo.endTime':[
{
                            limit:"required",
                            param:"",
                            errInfo:"结束时间不能为空"
                        },
 {
                            limit:"date",
                            param:"",
                            errInfo:"结束时间格式错误"
                        },
                    ],
'editActivitiesInfo.activitiesId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"活动ID不能为空"
                        }]

                        });
             },
            editActivities:function(){
                if(!vc.component.editActivitiesValidate()){
                    vc.toast(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editActivities',
                    'update',
                    JSON.stringify(vc.component.editActivitiesInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editActivitiesModel').modal('hide');
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
                vc.component.editActivitiesInfo= {
                  activitiesId:'',
title:'',
typeCd:'',
headerImg:'',
context:'',
startTime:'',
endTime:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
