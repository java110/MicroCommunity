(function(vc,vm){

    vc.extends({
        data:{
            editVisitInfo:{
                vId:'',
                name:'',
                visitGender:'',
                phoneNumber:'',
                visitTime:'',
                departureTime:'',
                visitCase:'',
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editVisit','openEditVisitModal',function(_params){
                vc.component.refreshEditVisitInfo();
                $('#editVisitModel').modal('show');
                vc.copyObject(_params, vc.component.editVisitInfo );
                vc.component.editVisitInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editVisitValidate:function(){
                        return vc.validate.validate({
                            editVisitInfo:vc.component.editVisitInfo
                        },{
                            'editVisitInfo.name':[
{
                            limit:"required",
                            param:"",
                            errInfo:"访客姓名不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"访客姓名必须在2至10字符之间"
                        },
                    ],
'editVisitInfo.visitGender':[
{
                            limit:"required",
                            param:"",
                            errInfo:"访客性别不能为空"
                        },
 {
                             limit: "num",
                             param: "",
                             errInfo: "性别错误"
                         },
                    ],
'editVisitInfo.phoneNumber':[
{
                            limit:"required",
                            param:"",
                            errInfo:"访客联系方式不能为空"
                        },
 {
                             limit: "phone",
                             param: "",
                             errInfo: "不是有效的手机号"
                         },
                    ],
'editVisitInfo.visitTime':[
{
                            limit:"required",
                            param:"",
                            errInfo:"访客拜访时间不能为空"
                        },
   {
                             limit: "date",
                             param: "",
                             errInfo: "访客拜访时间格式错误，如：2019-09-11"
                         },
                    ],
'editVisitInfo.departureTime':[
 {
                            limit: "date",
                            param: "",
                            errInfo: "访客离开时间格式错误，如：2019-09-11"
                        },
                    ],
'editVisitInfo.visitCase':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注内容不能超过200"
                        },
                    ],
'editVisitInfo.vId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"访客记录ID不能为空"
                        }]

                        });
             },
            editVisit:function(){
                if(!vc.component.editVisitValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editVisit',
                    'update',
                    JSON.stringify(vc.component.editVisitInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editVisitModel').modal('hide');
                             vc.emit('visitManage','listVisit',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            refreshEditVisitInfo:function(){
                vc.component.editVisitInfo= {
                  vId:'',
name:'',
visitGender:'',
visitGender:'',
phoneNumber:'',
visitTime:'',
departureTime:'',
visitCase:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
