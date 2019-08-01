(function(vc){
    vc.extends({
        propTypes: {
           emitChooseUnit:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseUnitInfo:{
                units:[],
                _currentUnitName:'',
                floorId:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseUnit','openChooseUnitModel',function(_param){
                $('#chooseUnitModel').modal('show');
                vc.component._refreshChooseUnitInfo();
                vc.component._loadAllUnitInfo(1,10,'');
            });
            vc.on('chooseUnit','onFloorInfo',function(_param){
                vc.component.chooseUnitInfo.floorId = _param.floorId;
            });
        },
        methods:{
            _loadAllUnitInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        floorId:vc.component.chooseUnitInfo.floorId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseUnit',
                            'list',
                             param,
                             function(json){
                                var _unitInfo = JSON.parse(json);
                                vc.component.chooseUnitInfo.units = _unitInfo;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseUnit:function(_unit){
                vc.emit($props.emitChooseUnit,'chooseUnit',_unit);
                vc.emit($props.emitLoadData,'listUnitData',{
                    unitId:_unit.unitId
                });
                $('#chooseUnitModel').modal('hide');
            },
            queryUnits:function(){
                vc.component._loadAllUnitInfo(1,10,vc.component.chooseUnitInfo._currentUnitName);
            },
            _refreshChooseUnitInfo:function(){
                vc.component.chooseUnitInfo._currentUnitName = "";
            }
        }

    });
})(window.vc);
