(function(vc,vm){

    vc.extends({
        data:{
            deleteInspectionRouteInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteInspectionRoute','openDeleteInspectionRouteModal',function(_params){

                vc.component.deleteInspectionRouteInfo = _params;
                $('#deleteInspectionRouteModel').modal('show');

            });
        },
        methods:{
            deleteInspectionRoute:function(){
                vc.component.deleteInspectionRouteInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteInspectionRoute',
                    'delete',
                    JSON.stringify(vc.component.deleteInspectionRouteInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteInspectionRouteModel').modal('hide');
                            vc.emit('inspectionRouteManage','listInspectionRoute',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteInspectionRouteModel:function(){
                $('#deleteInspectionRouteModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
