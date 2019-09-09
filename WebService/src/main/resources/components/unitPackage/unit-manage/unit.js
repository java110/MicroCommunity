(function(vc){
    //员工权限
    vc.extends({
        data:{
            unitInfo:{
                units:[],
                _currentFloorId:'',
                moreCondition:false,
                conditions:{
                    floorId:'',
                    floorName:'',
                    unitNum:'',
                    layerCount:'',
                    lift:'',
                }
            }
        },
        _initMethod:function(){
            vc.component._loadUnits({'floorId':''});
        },
        _initEvent:function(){
            vc.on('unit','chooseFloor',function(_param){
                vc.component.unitInfo.conditions.floorId = _param.floorId;
                vc.component.unitInfo.conditions.floorName = _param.floorName;
            });

            vc.on('unit','loadUnit',function(_param){
                vc.component.unitInfo.conditions.floorId = _param.floorId;
                vc.component._loadUnits(_param);
            });
            vc.on('unit','loadData',function(_param){
                vc.component._loadUnits(_param);
            });
        },
        methods:{
            _loadUnits:function(_param){
                vc.component.unitInfo._currentFloorId=_param.floorId;
                vc.component.unitInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params:vc.component.unitInfo.conditions
               };
             //发送get请求
                vc.http.get('unit',
                     'loadUnits',
                      param,
                      function(json){
                         var _unitInfo = JSON.parse(json);
                         vc.component.unitInfo.units = _unitInfo;
                      },
                      function(){
                         console.log('请求失败处理');
                      });
            },
            _openDeleteUnitModel:function(_unit){
                _unit.floorId = vc.component.unitInfo._currentFloorId;
                vc.emit('deleteUnit','openUnitModel',_unit);
            },
            _openEditUnitModel:function(_unit){
                _unit.floorId = vc.component.unitInfo._currentFloorId;
                vc.emit('editUnit','openUnitModel',_unit);
            },
            _openChooseFloorMethod:function(){
                vc.emit('searchFloor','openSearchFloorModel',{});
            },
            openAddUnitModel:function(){
                vc.emit('addUnit','addUnitModel',{
                    floorId:vc.component.unitInfo.conditions.floorId
                });
            },
            _queryUnitMethod:function(){
                vc.component._loadUnits({'floorId':vc.component.unitInfo.conditions.floorId});
            }
            _moreCondition:function(){
                if(vc.component.unitInfo.moreCondition){
                    vc.component.unitInfo.moreCondition = false;
                }else{
                    vc.component.unitInfo.moreCondition = true;
                }
            }
        }
    });

})(window.vc);