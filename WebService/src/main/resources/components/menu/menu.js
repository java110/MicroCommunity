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
                    params:{
                        msg:this.message
                    }

               }
               //发送get请求
               vc.http.get('menu',
                            'getMenus',
                             param,
                             function(json,res){
                                vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
           },
           refreshMenuActive:function(jsonArray,offset){
                for(var menuIndex =0 ; menuIndex < jsonArray.length;menuIndex ++){
                    if(offset == menuIndex){
                        jsonArray[menuIndex].active=true;
                        continue;
                    }
                    jsonArray[menuIndex].active=false;
                }

                return  jsonArray;
           }
       }

    });

})(window.vc)