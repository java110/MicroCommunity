/**
    菜单 处理
**/
(function(vc){
    var vm = new Vue({
       el:'#menu-nav',
       data:{
           menus:[]
       },
       mounted:function(){
           this.getMenus();
       },
       methods:{
           getMenus:function(){
               var param = {
                   msg:this.message
               }
               //发送get请求
               vc.http.call('menu',
                            'getMenus',
                             param,
                             {
                                emulateJSON:true
                             },function(json){
                                vm.menus = JSON.parse(json);
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
           }
       }

    });

})(window.vc)