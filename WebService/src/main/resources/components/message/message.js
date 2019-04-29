(function(vc){

    var vm = new Vue({
        el:'#message',
        data:{
            messageInfo:{
                hide:true,
                errorInfo:'网络超时，请检查网络！'
            }
        },
        mounted:function(){

        },
        methods:{
            //验证码定时
            messageTimer:function(){
                  var num = 10;
                var _timer = vc.createTimer(function(){
                    num --;
                    if(num === 1){
                        vc.clearTimer(_timer);
                        vm.messageInfo.hide=true;
                    }
                },1000);
            }
        }
    });

    vm.$on('message_openMessage',function(_msg){
        console.log("开始加载");
        vm.messageInfo.hide = false;
        vm.messageInfo.errorInfo = _msg.msg;
    });
    /**
        加载遮罩层
    **/
    vc.message = function(_msg){
       vm.$emit('message_openMessage',{msg:_msg});
       vm.messageTimer();
    }
})(window.vc)