(function(vc){
    vc.extends({
        propTypes: {
           emitChooseInspectionPoint:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseInspectionPointInfo:{
                inspectionPoints:[],
                _currentInspectionPointName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseInspectionPoint','openChooseInspectionPointModel',function(_param){
                $('#chooseInspectionPointModel').modal('show');
                vc.component._refreshChooseInspectionPointInfo();
                vc.component._loadAllInspectionPointInfo(1,10,'');
            });
        },
        methods:{
            _loadAllInspectionPointInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseInspectionPoint',
                            'list',
                             param,
                             function(json){
                                var _inspectionPointInfo = JSON.parse(json);
                                vc.component.chooseInspectionPointInfo.inspectionPoints = _inspectionPointInfo.inspectionPoints;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseInspectionPoint:function(_inspectionPoint){
                if(_inspectionPoint.hasOwnProperty('name')){
                     _inspectionPoint.inspectionPointName = _inspectionPoint.name;
                }
                vc.emit($props.emitChooseInspectionPoint,'chooseInspectionPoint',_inspectionPoint);
                vc.emit($props.emitLoadData,'listInspectionPointData',{
                    inspectionPointId:_inspectionPoint.inspectionPointId
                });
                $('#chooseInspectionPointModel').modal('hide');
            },
            queryInspectionPoints:function(){
                vc.component._loadAllInspectionPointInfo(1,10,vc.component.chooseInspectionPointInfo._currentInspectionPointName);
            },
            _refreshChooseInspectionPointInfo:function(){
                vc.component.chooseInspectionPointInfo._currentInspectionPointName = "";
            }
        }

    });
})(window.vc);
