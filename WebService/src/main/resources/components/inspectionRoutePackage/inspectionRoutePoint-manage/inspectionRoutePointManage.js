/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            inspectionRoutePointManageInfo:{
                inspectionRoutes:[],
                inspectionRouteId:'',
                total:0,
                records:1,
                routeName:'',
            }
        },
        _initMethod:function(){
            //vc.component._listInspectionRoutePoints(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('inspectionRoutePointManage','notify',function(_param){
                  vc.component._listInspectionRoutePoints(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            
            vc.on('inspectionRoutePointManage','listInspectionPoint',function(_param){
                  if(!_param.hasOwnProperty('inspectionRouteId')){
                        return ;
                  }
                  vc.component.inspectionRoutePointManageInfo.inspectionRouteId = _param.inspectionRouteId;
                  vc.component._listInspectionRoutePoints(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listInspectionRoutes(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listInspectionRoutePoints:function(_page, _rows){

                var param = {
                    params:{
                        page:_page,
                        row:_rows,
                        communityId:vc.getCurrentCommunity().communityId,
                        inspectionRouteId:vc.component.inspectionRoutePointManageInfo.inspectionRouteId
                    }
               };

               //发送get请求
               vc.http.get('inspectionRoutePointManage',
                            'list',
                             param,
                             function(json,res){
                                var _inspectionRouteManageInfo=JSON.parse(json);
                                vc.component.inspectionRoutePointManageInfo.total = _inspectionRouteManageInfo.total;
                                vc.component.inspectionRoutePointManageInfo.records = _inspectionRouteManageInfo.records;
                                vc.component.inspectionRoutePointManageInfo.inspectionPonits = _inspectionRouteManageInfo.inspectionPonits;
                                vc.emit('pagination','init',{
                                     total:vc.component.inspectionRoutePointManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddInspectionRoutePointModal:function(){
                vc.emit('addInspectionRoutePoint','openAddInspectionRoutePointModal',{
                    inspectionRouteId:vc.component.inspectionRoutePointManageInfo.inspectionRouteId
                });
            },
            _openDeleteInspectionPointModel:function(_inspectionPoint){
                vc.emit('deleteInspectionRoutePoint','openDeleteInspectionRoutePointModal',_inspectionPoint);
            },
            _goBack:function(){
                vc.emit('inspectionRouteManage','goBack',{});
            }
        }
    });
})(window.vc);
