/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            visitManageInfo:{
                visits:[],
                total:0,
                records:1,
                moreCondition:false,
                name:'',
                conditions:{
                    name:'',

                }
            }
        },
        _initMethod:function(){
            vc.component._listVisits(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            
            vc.on('visitManage','listVisit',function(_param){
                  vc.component._listVisits(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listVisits(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listVisits:function(_page, _rows){

                vc.component.visitManageInfo.conditions.page = _page;
                vc.component.visitManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.visitManageInfo.conditions
               };

               //发送get请求
               vc.http.get('visitManage',
                            'list',
                             param,
                             function(json,res){
                                var _visitManageInfo=JSON.parse(json);
                                vc.component.visitManageInfo.total = _visitManageInfo.total;
                                vc.component.visitManageInfo.records = _visitManageInfo.records;
                                vc.component.visitManageInfo.visits = _visitManageInfo.visits;
                                vc.emit('pagination','init',{
                                     total:vc.component.visitManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddVisitModal:function(){
                vc.emit('addVisit','openAddVisitModal',{});
            },
            _openEditVisitModel:function(_visit){
                vc.emit('editVisit','openEditVisitModal',_visit);
            },
            _openDeleteVisitModel:function(_visit){
                vc.emit('deleteVisit','openDeleteVisitModal',_visit);
            },
            _queryVisitMethod:function(){
                vc.component._listVisits(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.visitManageInfo.moreCondition){
                    vc.component.visitManageInfo.moreCondition = false;
                }else{
                    vc.component.visitManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);
