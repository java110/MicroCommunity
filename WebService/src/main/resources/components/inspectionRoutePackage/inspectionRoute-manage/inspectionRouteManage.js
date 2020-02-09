/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            inspectionRouteManageInfo:{
                inspectionRoutes:[],
                total:0,
                records:1,
                moreCondition:false,
                routeName:'',
                conditions:{
                    routeName:'',
inspectionName:'',
remark:'',

                }
            }
        },
        _initMethod:function(){
            vc.component._listInspectionRoutes(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            
            vc.on('inspectionRouteManage','listInspectionRoute',function(_param){
                  vc.component._listInspectionRoutes(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listInspectionRoutes(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listInspectionRoutes:function(_page, _rows){

                vc.component.inspectionRouteManageInfo.conditions.page = _page;
                vc.component.inspectionRouteManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.inspectionRouteManageInfo.conditions
               };

               //发送get请求
               vc.http.get('inspectionRouteManage',
                            'list',
                             param,
                             function(json,res){
                                var _inspectionRouteManageInfo=JSON.parse(json);
                                vc.component.inspectionRouteManageInfo.total = _inspectionRouteManageInfo.total;
                                vc.component.inspectionRouteManageInfo.records = _inspectionRouteManageInfo.records;
                                vc.component.inspectionRouteManageInfo.inspectionRoutes = _inspectionRouteManageInfo.inspectionRoutes;
                                vc.emit('pagination','init',{
                                     total:vc.component.inspectionRouteManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddInspectionRouteModal:function(){
                vc.emit('addInspectionRoute','openAddInspectionRouteModal',{});
            },
            _openEditInspectionRouteModel:function(_inspectionRoute){
                vc.emit('editInspectionRoute','openEditInspectionRouteModal',_inspectionRoute);
            },
            _openDeleteInspectionRouteModel:function(_inspectionRoute){
                vc.emit('deleteInspectionRoute','openDeleteInspectionRouteModal',_inspectionRoute);
            },
            _queryInspectionRouteMethod:function(){
                vc.component._listInspectionRoutes(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.inspectionRouteManageInfo.moreCondition){
                    vc.component.inspectionRouteManageInfo.moreCondition = false;
                }else{
                    vc.component.inspectionRouteManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);
