(function(vc){
    var vm = new Vue({
        el:'#validate-code',
            data:{
                    code:'',
                    codeImage:'',
                    errorInfo:''
            },
            mounted:function(){
                       this.generateCode();
                   },
            methods:{
                generateCode(){
                    var param = {
                        _uId:'123'
                    }
                    vc.http.call('validate-code','generateValidateCode',param,
                                {
                                    emulateJSON:true
                                 },function(json,res){
                                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                    if(res.status == 200){
                                        vm.codeImage = json;
                                        return ;
                                    }
                                    vm.$emit('errorInfoEvent',json);
                                 },function(errInfo,error){
                                    console.log('请求失败处理');

                                    vm.loginInfo.errorInfo = errInfo;
                                 })

                },
                testEvent(){
                //vc.dispatchEvent('errorInfoEvent',"测试");
                    vc.component.$emit('errorInfoEvent',"测试");
                    console.log("testEvent")
                }
            }

    });

})(window.vc);