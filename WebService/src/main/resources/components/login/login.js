(function(vc){
      vc.extends({
        data:{
            loginInfo:{
                username:'',
                passwd:'',
                errorInfo:''
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
             vc.component.$on('errorInfoEvent',function(_errorInfo){
                    vc.component.loginInfo.errorInfo = _errorInfo;
                    console.log('errorInfoEvent 事件被监听',_errorInfo)
                });

             vc.component.$on('login_param_change_event',function(params){
                         for(var tmpAttr in params){
                             vc.component.loginInfo[tmpAttr] = params[tmpAttr];
                         }
                         console.log('errorInfoEvent 事件被监听',params)
                     });
        },
        methods:{
            doLogin(){
                vc.http.call(
                            'login',
                            'doLogin',
                            JSON.stringify(vc.component.loginInfo),
                            {
                                emulateJSON:true
                             },
                             function(json,res){
                                //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                if(res.status == 200){
                                    vc.jumpToPage("/");
                                    return ;
                                }
                                vc.component.loginInfo.errorInfo = json;
                             },
                             function(errInfo,error){
                                console.log('请求失败处理');

                                vc.component.loginInfo.errorInfo = errInfo;
                             });

            }
        }
    });


})(window.vc);