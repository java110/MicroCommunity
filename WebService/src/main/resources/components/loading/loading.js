(function(vc){

    var vm = new Vue({
        el:'#loadingComponent',
        data:{
            loadingInfo:{
                hide:true,
            }
        },
        mounted:function(){

        },
        methods:{

        }
    });

    vm.$on('loading_openLoading',function(){
                    console.log("开始加载");
                    vm.loadingInfo.hide = false;
    });
    vm.$on('loading_closeLoading',function(){
        console.log("结束加载");
        vm.loadingInfo.hide = true;
    });
    /**
        加载遮罩层
    **/
    vc.loading = function(_flag){
        console.log('操作加载层');
        if(_flag == 'open'){
            vm.$emit('loading_openLoading',{});
            return ;
        }

        vm.$emit('loading_closeLoading',{});
    }
})(window.vc)