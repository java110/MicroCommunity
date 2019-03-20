(function(vc){
    var vm = new Vue({
        el:'#login',
        data:{
            loginInfo:{
                username:'',
                passwd:'',
                errorInfo:''
            }
        },
        methods:{
            doLogin(){

                vc.http.call('login','doLogin',JSON.stringify(this.loginInfo),
                            {
                                emulateJSON:true
                             },function(json,res){
                                //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                if(res.status == 200){
                                    vc.jumpToPage("/");
                                    return ;
                                }
                                vm.loginInfo.errorInfo = json;
                             },function(errInfo,error){
                                console.log('请求失败处理');

                                vm.loginInfo.errorInfo = errInfo;
                             })

            }
        }
    });

    vc.component.$on('errorInfoEvent',function(_errorInfo){
        vm.loginInfo.errorInfo = _errorInfo;
        console.log('errorInfoEvent 事件被监听',_errorInfo)
    })

})(window.vc);