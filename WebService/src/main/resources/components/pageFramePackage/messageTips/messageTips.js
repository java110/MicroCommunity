(function(vc){

    var vm = new Vue({
        el:'#messageTips',
        data:{
            messageTips:{
                hide:true,
                errorInfo:'网络超时，请检查网络！'
            }
        },
        mounted:function(){

        },
        methods:{
            //验证码定时
            messageTimer_Tips:function(){
                  var num = 3;
                var _timer = vc.createTimer(function(){
                    num --;
                    if(num === 1){
                        vc.clearTimer(_timer);
                        vm.messageTips.hide=true;
                    }
                },1000);
            }
        }
    });

    vm.$on('messageTips_openMessage',function(_msg){
        //console.log("开始加载");
        vm.messageTips.hide = false;
        vm.messageTips.errorInfo = _msg.msg;
    });
    /**
        加载遮罩层2
        @param _msg 提示内容
        @param _notAutoHide 是否需要自动隐藏
    **/
    vc.messageTips = function(_msg, _notAutoHide){
       vm.$emit('messageTips_openMessage',{msg:_msg});
       if(!_notAutoHide){
            vm.messageTimer_Tips();
       }

    }
})(window.vc)