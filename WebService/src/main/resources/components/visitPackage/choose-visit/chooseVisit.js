(function(vc){
    vc.extends({
        propTypes: {
           emitChooseVisit:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseVisitInfo:{
                visits:[],
                _currentVisitName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseVisit','openChooseVisitModel',function(_param){
                $('#chooseVisitModel').modal('show');
                vc.component._refreshChooseVisitInfo();
                vc.component._loadAllVisitInfo(1,10,'');
            });
        },
        methods:{
            _loadAllVisitInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseVisit',
                            'list',
                             param,
                             function(json){
                                var _visitInfo = JSON.parse(json);
                                vc.component.chooseVisitInfo.visits = _visitInfo.visits;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseVisit:function(_visit){
                if(_visit.hasOwnProperty('name')){
                     _visit.visitName = _visit.name;
                }
                vc.emit($props.emitChooseVisit,'chooseVisit',_visit);
                vc.emit($props.emitLoadData,'listVisitData',{
                    visitId:_visit.visitId
                });
                $('#chooseVisitModel').modal('hide');
            },
            queryVisits:function(){
                vc.component._loadAllVisitInfo(1,10,vc.component.chooseVisitInfo._currentVisitName);
            },
            _refreshChooseVisitInfo:function(){
                vc.component.chooseVisitInfo._currentVisitName = "";
            }
        }

    });
})(window.vc);
