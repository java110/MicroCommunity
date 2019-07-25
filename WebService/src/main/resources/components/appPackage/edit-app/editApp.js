(function(vc,vm){

    vc.extends({
        data:{
            editAppInfo:{
                appId:'',
name:'',
securityCode:'',
whileListIp:'',
blackListIp:'',
remark:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editApp','openEditAppModal',function(_params){
                vc.component.refreshEditAppInfo();
                $('#editAppModel').modal('show');
                vc.component.editAppInfo = _params;
                vc.component.editAppInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editAppValidate:function(){
                        return vc.validate.validate({
                            editAppInfo:vc.component.editAppInfo
                        },{
                            'editAppInfo.name':[
{
                            limit:"required",
                            param:"",
                            errInfo:"应用名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"应用名称必须在2至50字符之间"
                        },
                    ],
'editAppInfo.securityCode':[
 {
                            limit:"maxLength",
                            param:"64",
                            errInfo:"秘钥太长超过64位"
                        },
                    ],
'editAppInfo.whileListIp':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"白名单内容不能超过200"
                        },
                    ],
'editAppInfo.blackListIp':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"黑名单内容不能超过200"
                        },
                    ],
'editAppInfo.remark':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注内容不能超过200"
                        },
                    ],
'editAppInfo.appId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"应用Id不能为空"
                        }]

                        });
             },
            editApp:function(){
                if(!vc.component.editAppValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editApp',
                    'update',
                    JSON.stringify(vc.component.editAppInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editAppModel').modal('hide');
                             vc.emit('appManage','listApp',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            refreshEditAppInfo:function(){
                vc.component.editAppInfo= {
                  appId:'',
name:'',
securityCode:'',
whileListIp:'',
blackListIp:'',
remark:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
