(function(vc){
      vc.extends({
        data:{
            registerInfo:{
                username:'',
                passwd:'',
                repasswd:'',
                errorInfo:''
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
             vc.component.$on('errorInfoEvent',function(_errorInfo){
                    vc.component.registerInfo.errorInfo = _errorInfo;
                    console.log('errorInfoEvent 事件被监听',_errorInfo)
                });

             vc.component.$on('validate_tel_change_event',function(params){
                         for(var tmpAttr in params){
                             vc.component.registerInfo[tmpAttr] = params[tmpAttr];
                         }
                         console.log('validate_tel_component_param_change_event 事件被监听',params)
                     });
        },
        methods:{
            doRegister(){
                vc.http.post(
                            'register',
                            'doRegister',
                            JSON.stringify(vc.component.registerInfo),
                            {
                                emulateJSON:true
                             },
                             function(json,res){
                                //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                if(res.status == 200){
                                    vc.jumpToPage("/flow/login");
                                    return ;
                                }
                                vc.component.registerInfo.errorInfo = json;
                             },
                             function(errInfo,error){
                                console.log('请求失败处理');

                                vc.component.registerInfo.errorInfo = errInfo;
                             });

            }
        }
    });


})(window.vc);