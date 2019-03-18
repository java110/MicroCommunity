(function(){
    var vm = new Vue({
        el:'#vueTest',
        data:{
            message:"你好学文",
            version:""
        },
        mounted:function(){
            this.getVersion();
        },
        methods:{
            getVersion:function(){
                var param = {
                    msg:this.message
                }
                //发送get请求
                this.$http.post('/callComponent/vue_test/getTestVersion',
                param,
                {
                    emulateJSON:true
                }).then(function(res){
                    this.version = res.bodyText;
                },function(){
                    console.log('请求失败处理');
                });
            }
        }
    });
})();