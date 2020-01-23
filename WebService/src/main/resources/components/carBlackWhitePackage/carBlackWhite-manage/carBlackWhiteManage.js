/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            carBlackWhiteManageInfo:{
                carBlackWhites:[],
                total:0,
                records:1,
                moreCondition:false,
                carNum:'',
                conditions:{
                    blackWhite:'',
carNum:'',
bwId:'',

                }
            }
        },
        _initMethod:function(){
            vc.component._listCarBlackWhites(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            
            vc.on('carBlackWhiteManage','listCarBlackWhite',function(_param){
                  vc.component._listCarBlackWhites(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listCarBlackWhites(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listCarBlackWhites:function(_page, _rows){

                vc.component.carBlackWhiteManageInfo.conditions.page = _page;
                vc.component.carBlackWhiteManageInfo.conditions.row = _rows;
                vc.component.carBlackWhiteManageInfo.conditions.communityId = vc.getCurrentCommunity().communityId;

                var param = {
                    params:vc.component.carBlackWhiteManageInfo.conditions
               };

               //发送get请求
               vc.http.get('carBlackWhiteManage',
                            'list',
                             param,
                             function(json,res){
                                var _carBlackWhiteManageInfo=JSON.parse(json);
                                vc.component.carBlackWhiteManageInfo.total = _carBlackWhiteManageInfo.total;
                                vc.component.carBlackWhiteManageInfo.records = _carBlackWhiteManageInfo.records;
                                vc.component.carBlackWhiteManageInfo.carBlackWhites = _carBlackWhiteManageInfo.carBlackWhites;
                                vc.emit('pagination','init',{
                                     total:vc.component.carBlackWhiteManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddCarBlackWhiteModal:function(){
                vc.emit('addCarBlackWhite','openAddCarBlackWhiteModal',{});
            },
            _openEditCarBlackWhiteModel:function(_carBlackWhite){
                vc.emit('editCarBlackWhite','openEditCarBlackWhiteModal',_carBlackWhite);
            },
            _openDeleteCarBlackWhiteModel:function(_carBlackWhite){
                vc.emit('deleteCarBlackWhite','openDeleteCarBlackWhiteModal',_carBlackWhite);
            },
            _queryCarBlackWhiteMethod:function(){
                vc.component._listCarBlackWhites(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.carBlackWhiteManageInfo.moreCondition){
                    vc.component.carBlackWhiteManageInfo.moreCondition = false;
                }else{
                    vc.component.carBlackWhiteManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);
