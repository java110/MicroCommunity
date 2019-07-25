(function(vc,vm){

    vc.extends({
        data:{
            deleteUnitInfo:{
                _currentFloorId:'',
                _currentUnitId:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteUnit','openUnitModel',function(_params){

                vc.component.deleteUnitInfo._currentFloorId = _params.floorId;
                vc.component.deleteUnitInfo._currentUnitId = _params.unitId;
                $('#deleteUnitModel').modal('show');

            });
        },
        methods:{
            deleteUnit:function(){
                var param = {
                    floorId:vc.component.deleteUnitInfo._currentFloorId,
                    unitId:vc.component.deleteUnitInfo._currentUnitId,
                    communityId:vc.getCurrentCommunity().communityId
                }
                vc.http.post(
                    'deleteUnit',
                    'delete',
                    JSON.stringify(param),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteUnitModel').modal('hide');
                            vc.emit('unit','loadUnit',{
                                floorId:vc.component.deleteUnitInfo._currentFloorId
                            });
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteUnitModel:function(){
                $('#deleteUnitModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);