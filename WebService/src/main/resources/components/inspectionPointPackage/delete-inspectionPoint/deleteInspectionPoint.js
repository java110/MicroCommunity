(function(vc,vm){

    vc.extends({
        data:{
            deleteInspectionPointInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteInspectionPoint','openDeleteInspectionPointModal',function(_params){

                vc.component.deleteInspectionPointInfo = _params;
                $('#deleteInspectionPointModel').modal('show');

            });
        },
        methods:{
            deleteInspectionPoint:function(){
                vc.component.deleteInspectionPointInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteInspectionPoint',
                    'delete',
                    JSON.stringify(vc.component.deleteInspectionPointInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteInspectionPointModel').modal('hide');
                            vc.emit('inspectionPointManage','listInspectionPoint',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteInspectionPointModel:function(){
                $('#deleteInspectionPointModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
