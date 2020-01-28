(function(vc,vm){

    vc.extends({
        data:{
            deleteParkingAreaInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteParkingArea','openDeleteParkingAreaModal',function(_params){

                vc.component.deleteParkingAreaInfo = _params;
                $('#deleteParkingAreaModel').modal('show');

            });
        },
        methods:{
            deleteParkingArea:function(){
                vc.component.deleteParkingAreaInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteParkingArea',
                    'delete',
                    JSON.stringify(vc.component.deleteParkingAreaInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteParkingAreaModel').modal('hide');
                            vc.emit('parkingAreaManage','listParkingArea',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteParkingAreaModel:function(){
                $('#deleteParkingAreaModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
