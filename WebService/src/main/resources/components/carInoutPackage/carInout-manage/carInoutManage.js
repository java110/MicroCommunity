/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            carInoutManageInfo:{
                carInouts:[],
                total:0,
                records:1,
                moreCondition:false,
                carNum:'',
                conditions:{
                    state:'',
carNum:'',
inoutId:'',
startTime:'',
endTime:'',

                }
            }
        },
        _initMethod:function(){
            vc.component._listCarInouts(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            
            vc.on('carInoutManage','listCarInout',function(_param){
                  vc.component._listCarInouts(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listCarInouts(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listCarInouts:function(_page, _rows){

                vc.component.carInoutManageInfo.conditions.page = _page;
                vc.component.carInoutManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.carInoutManageInfo.conditions
               };

               //发送get请求
               vc.http.get('carInoutManage',
                            'list',
                             param,
                             function(json,res){
                                var _carInoutManageInfo=JSON.parse(json);
                                vc.component.carInoutManageInfo.total = _carInoutManageInfo.total;
                                vc.component.carInoutManageInfo.records = _carInoutManageInfo.records;
                                vc.component.carInoutManageInfo.carInouts = _carInoutManageInfo.carInouts;
                                vc.emit('pagination','init',{
                                     total:vc.component.carInoutManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddCarInoutModal:function(){
                vc.emit('addCarInout','openAddCarInoutModal',{});
            },
            _openEditCarInoutModel:function(_carInout){
                vc.emit('editCarInout','openEditCarInoutModal',_carInout);
            },
            _openDeleteCarInoutModel:function(_carInout){
                vc.emit('deleteCarInout','openDeleteCarInoutModal',_carInout);
            },
            _queryCarInoutMethod:function(){
                vc.component._listCarInouts(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.carInoutManageInfo.moreCondition){
                    vc.component.carInoutManageInfo.moreCondition = false;
                }else{
                    vc.component.carInoutManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);
