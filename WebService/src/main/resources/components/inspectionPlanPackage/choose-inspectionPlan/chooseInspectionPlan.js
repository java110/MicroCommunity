(function(vc){
    vc.extends({
        propTypes: {
           emitChooseInspectionPlan:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseInspectionPlanInfo:{
                inspectionPlans:[],
                _currentInspectionPlanName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseInspectionPlan','openChooseInspectionPlanModel',function(_param){
                $('#chooseInspectionPlanModel').modal('show');
                vc.component._refreshChooseInspectionPlanInfo();
                vc.component._loadAllInspectionPlanInfo(1,10,'');
            });
        },
        methods:{
            _loadAllInspectionPlanInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseInspectionPlan',
                            'list',
                             param,
                             function(json){
                                var _inspectionPlanInfo = JSON.parse(json);
                                vc.component.chooseInspectionPlanInfo.inspectionPlans = _inspectionPlanInfo.inspectionPlans;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseInspectionPlan:function(_inspectionPlan){
                if(_inspectionPlan.hasOwnProperty('name')){
                     _inspectionPlan.inspectionPlanName = _inspectionPlan.name;
                }
                vc.emit($props.emitChooseInspectionPlan,'chooseInspectionPlan',_inspectionPlan);
                vc.emit($props.emitLoadData,'listInspectionPlanData',{
                    inspectionPlanId:_inspectionPlan.inspectionPlanId
                });
                $('#chooseInspectionPlanModel').modal('hide');
            },
            queryInspectionPlans:function(){
                vc.component._loadAllInspectionPlanInfo(1,10,vc.component.chooseInspectionPlanInfo._currentInspectionPlanName);
            },
            _refreshChooseInspectionPlanInfo:function(){
                vc.component.chooseInspectionPlanInfo._currentInspectionPlanName = "";
            }
        }

    });
})(window.vc);
