/**
    导航栏
**/
(function(vc){
    var vm = new Vue({
        el:'#nav',
        data:{
            nav:{},
            userName:"",
        },
        mounted:function(){
            this.getNavData();
            this.getUserInfo();
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

            },
            logout:function(){
                var param = {
                    msg:123
                };
                  //发送get请求
               vc.http.post('nav',
                            'logout',
                            JSON.stringify(param),
                           {
                               emulateJSON:true
                            },
                             function(json,res){
                               if(res.status == 200){
                                   vc.jumpToPage("/flow/login");
                                   return ;
                               }
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            getUserInfo:function(){
                //获取用户名
                var param = {
                                    msg:'123',
                };

                //发送get请求
               vc.http.get('nav',
                            'getUserInfo',
                             param,
                             function(json,res){
                                if(res.status == 200){
                                    var tmpUserInfo = JSON.parse(json);
                                   vm.userName = tmpUserInfo.name;
                               }
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            }
        }


    });
})(window.vc);