(function(vc){
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
                       vc.http.call('vue_test',
                                       'getTestVersion',
                                       param,
                                       {
                                           emulateJSON:true
                                       },function(json){
                                           vm.version = json;
                                       },function(){
                                           console.log('请求失败处理');
                                       }
                                   );
                   }
               }
           });
})(window.vc);