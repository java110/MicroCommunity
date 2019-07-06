(function(vc){

    vc.extends({
        data:{
            addAppInfo:{
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
            vc.on('addApp','openAddAppModal',function(){
                $('#addAppModel').modal('show');
            });
        },
        methods:{
            addAppValidate(){
                return vc.validate.validate({
                    addAppInfo:vc.component.addAppInfo
                },{
                    'addAppInfo.name':[
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
'addAppInfo.securityCode':[
 {
                            limit:"maxLength",
                            param:"64",
                            errInfo:"秘钥太长超过64位"
                        },
                    ],
'addAppInfo.whileListIp':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"白名单内容不能超过200"
                        },
                    ],
'addAppInfo.blackListIp':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"黑名单内容不能超过200"
                        },
                    ],
'addAppInfo.remark':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注内容不能超过200"
                        },
                    ],




                });
            },
            saveAppInfo:function(){
                if(!vc.component.addAppValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.addAppInfo.communityId = vc.getCurrentCommunity().communityId;

                vc.http.post(
                    'addApp',
                    'save',
                    JSON.stringify(vc.component.addAppInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addAppModel').modal('hide');
                            vc.component.clearAddAppInfo();
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
            clearAddAppInfo:function(){
                vc.component.addAppInfo = {
                                            name:'',
securityCode:'',
whileListIp:'',
blackListIp:'',
remark:'',

                                        };
            }
        }
    });

})(window.vc);
