(function(vc,vm){

    vc.extends({
        data:{
            deleteCarBlackWhiteInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteCarBlackWhite','openDeleteCarBlackWhiteModal',function(_params){

                vc.component.deleteCarBlackWhiteInfo = _params;
                $('#deleteCarBlackWhiteModel').modal('show');

            });
        },
        methods:{
            deleteCarBlackWhite:function(){
                vc.component.deleteCarBlackWhiteInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteCarBlackWhite',
                    'delete',
                    JSON.stringify(vc.component.deleteCarBlackWhiteInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteCarBlackWhiteModel').modal('hide');
                            vc.emit('carBlackWhiteManage','listCarBlackWhite',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteCarBlackWhiteModel:function(){
                $('#deleteCarBlackWhiteModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
