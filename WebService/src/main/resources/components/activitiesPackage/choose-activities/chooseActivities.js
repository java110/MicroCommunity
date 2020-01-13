(function(vc){
    vc.extends({
        propTypes: {
           emitChooseActivities:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseActivitiesInfo:{
                activitiess:[],
                _currentActivitiesName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseActivities','openChooseActivitiesModel',function(_param){
                $('#chooseActivitiesModel').modal('show');
                vc.component._refreshChooseActivitiesInfo();
                vc.component._loadAllActivitiesInfo(1,10,'');
            });
        },
        methods:{
            _loadAllActivitiesInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseActivities',
                            'list',
                             param,
                             function(json){
                                var _activitiesInfo = JSON.parse(json);
                                vc.component.chooseActivitiesInfo.activitiess = _activitiesInfo.activitiess;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseActivities:function(_activities){
                if(_activities.hasOwnProperty('name')){
                     _activities.activitiesName = _activities.name;
                }
                vc.emit($props.emitChooseActivities,'chooseActivities',_activities);
                vc.emit($props.emitLoadData,'listActivitiesData',{
                    activitiesId:_activities.activitiesId
                });
                $('#chooseActivitiesModel').modal('hide');
            },
            queryActivitiess:function(){
                vc.component._loadAllActivitiesInfo(1,10,vc.component.chooseActivitiesInfo._currentActivitiesName);
            },
            _refreshChooseActivitiesInfo:function(){
                vc.component.chooseActivitiesInfo._currentActivitiesName = "";
            }
        }

    });
})(window.vc);
