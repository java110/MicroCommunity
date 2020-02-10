(function(vc){
    vc.extends({
        propTypes: {
           emitChooseInspectionRoute:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseInspectionRouteInfo:{
                inspectionRoutes:[],
                _currentInspectionRouteName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseInspectionRoute','openChooseInspectionRouteModel',function(_param){
                $('#chooseInspectionRouteModel').modal('show');
                vc.component._refreshChooseInspectionRouteInfo();
                vc.component._loadAllInspectionRouteInfo(1,10,'');
            });
        },
        methods:{
            _loadAllInspectionRouteInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseInspectionRoute',
                            'list',
                             param,
                             function(json){
                                var _inspectionRouteInfo = JSON.parse(json);
                                vc.component.chooseInspectionRouteInfo.inspectionRoutes = _inspectionRouteInfo.inspectionRoutes;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseInspectionRoute:function(_inspectionRoute){
                if(_inspectionRoute.hasOwnProperty('name')){
                     _inspectionRoute.inspectionRouteName = _inspectionRoute.name;
                }
                vc.emit($props.emitChooseInspectionRoute,'chooseInspectionRoute',_inspectionRoute);
                vc.emit($props.emitLoadData,'listInspectionRouteData',{
                    inspectionRouteId:_inspectionRoute.inspectionRouteId
                });
                $('#chooseInspectionRouteModel').modal('hide');
            },
            queryInspectionRoutes:function(){
                vc.component._loadAllInspectionRouteInfo(1,10,vc.component.chooseInspectionRouteInfo._currentInspectionRouteName);
            },
            _refreshChooseInspectionRouteInfo:function(){
                vc.component.chooseInspectionRouteInfo._currentInspectionRouteName = "";
            }
        }

    });
})(window.vc);
