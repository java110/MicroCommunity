(function(vc){

    var vm = new Vue({
        el:'#loadingComponent',
        data:{
            loadingInfo:{
                hide:true,
                count:0,
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

        if(_flag == 'open'){
            vm.loadingInfo.count ++;
            if(vm.loadingInfo.count == 1){
                vm.$emit('loading_openLoading',{});
            }
            return ;
        }

        vm.loadingInfo.count --;

        if(vm.loadingInfo.count == 0){
            vm.$emit('loading_closeLoading',{});
        }

    }
})(window.vc)