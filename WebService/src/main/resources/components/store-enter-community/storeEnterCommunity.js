(function(vc){

    vc.extends({
        data:{
            storeEnterCommunityInfo:{
                communityInfo:[],
                errorInfo:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('storeEnterCommunity','openStoreEnterCommunity',function(_params){
                $('#storeEnterCommunityModel').modal('show');
                vc.component.listNoEnterCommunity();
            });
        },
        methods:{

            _saveEnterCommunity:function(){
                var param = {};
                vc.component.storeEnterCommunityInfo.errorInfo = "";
                vc.http.post(
                    'storeEnterCommunity',
                    'enterCommunity',
                    JSON.stringify(param),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#storeEnterCommunityModel').modal('hide');
                            vc.emit('enterCommunity','listMyCommunity',{});
                            return ;
                        }
                        vc.component.storeEnterCommunityInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.storeEnterCommunityInfo.errorInfo = errInfo;
                     });
            },
            listNoEnterCommunity:function(){
                            var param = {
                                params:{
                                    msg:'123'
                                }

                           }
                           //发送get请求
                           vc.http.get('storeEnterCommunity',
                                        'listNoEnterCommunity',
                                         param,
                                         function(json,res){
                                            vc.component.storeEnterCommunityInfo.communityInfo=JSON.parse(json);
                                         },function(errInfo,error){
                                            console.log('请求失败处理');
                                         }
                                       );
                        },

        }
    });

})(window.vc);