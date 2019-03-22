/**
    导航栏
**/
(function(vc){
    var vm = new Vue({
        el:'#nav',
        data:{
            nav:{}
        },
        mounted:function(){
            this.getNavData();
        },
        methods:{
            getNavData:function(){
                var param = {
                    msg:'123',
                };

                //发送get请求
               vc.http.get('nav',
                            'getNavData',
                             param,
                             function(json){
                                vm.nav = JSON.parse(json);
                             },function(){
                                console.log('请求失败处理');
                             }
                           );

            }
        }

    });
})(window.vc);