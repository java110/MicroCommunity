/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            activitiesManageInfo:{
                activitiess:[],
                total:0,
                records:1,
                moreCondition:false,
                title:'',
                conditions:{
                    title:'',
typeCd:'',
userName:'',
activitiesId:'',

                }
            }
        },
        _initMethod:function(){
            vc.component._listActivitiess(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            
            vc.on('activitiesManage','listActivities',function(_param){
                  vc.component._listActivitiess(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listActivitiess(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listActivitiess:function(_page, _rows){

                vc.component.activitiesManageInfo.conditions.page = _page;
                vc.component.activitiesManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.activitiesManageInfo.conditions
               };

               //发送get请求
               vc.http.get('activitiesManage',
                            'list',
                             param,
                             function(json,res){
                                var _activitiesManageInfo=JSON.parse(json);
                                vc.component.activitiesManageInfo.total = _activitiesManageInfo.total;
                                vc.component.activitiesManageInfo.records = _activitiesManageInfo.records;
                                vc.component.activitiesManageInfo.activitiess = _activitiesManageInfo.activitiess;
                                vc.emit('pagination','init',{
                                     total:vc.component.activitiesManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddActivitiesModal:function(){
                vc.emit('addActivities','openAddActivitiesModal',{});
            },
            _openEditActivitiesModel:function(_activities){
                vc.emit('editActivities','openEditActivitiesModal',_activities);
            },
            _openDeleteActivitiesModel:function(_activities){
                vc.emit('deleteActivities','openDeleteActivitiesModal',_activities);
            },
            _queryActivitiesMethod:function(){
                vc.component._listActivitiess(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.activitiesManageInfo.moreCondition){
                    vc.component.activitiesManageInfo.moreCondition = false;
                }else{
                    vc.component.activitiesManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);
