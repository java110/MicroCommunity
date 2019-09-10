(function(vc,vm){

    vc.extends({
        data:{
            deleteVisitInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteVisit','openDeleteVisitModal',function(_params){

                vc.component.deleteVisitInfo = _params;
                $('#deleteVisitModel').modal('show');

            });
        },
        methods:{
            deleteVisit:function(){
                vc.component.deleteVisitInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteVisit',
                    'delete',
                    JSON.stringify(vc.component.deleteVisitInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteVisitModel').modal('hide');
                            vc.emit('visitManage','listVisit',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteVisitModel:function(){
                $('#deleteVisitModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
