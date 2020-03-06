/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            purchaseApplyManageInfo:{
                purchaseApplys:[],
                total:0,
                records:1,
                moreCondition:false,
                applyOrderId:'',
                states:'',
                conditions:{
                    state:'',
                    userName:''
                }
            }
        },
        _initMethod:function(){
            vc.component._listPurchaseApplys(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('purchaseApplyManage','listPurchaseApply',function(_param){
                  vc.component._listPurchaseApplys(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listPurchaseApplys(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listPurchaseApplys:function(_page, _rows){

                vc.component.purchaseApplyManageInfo.conditions.page = _page;
                vc.component.purchaseApplyManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.purchaseApplyManageInfo.conditions
               };

               //发送get请求
               vc.http.get('purchaseApplyManage',
                            'list',
                             param,
                             function(json,res){
                                var _purchaseApplyManageInfo=JSON.parse(json);
                                vc.component.purchaseApplyManageInfo.total = _purchaseApplyManageInfo.total;
                                vc.component.purchaseApplyManageInfo.records = _purchaseApplyManageInfo.records;
                                vc.component.purchaseApplyManageInfo.purchaseApplys = _purchaseApplyManageInfo.purchaseApplys;
                                vc.emit('pagination','init',{
                                     total:vc.component.purchaseApplyManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddPurchaseApplyModal:function(){
                vc.jumpToPage("/flow/addPurchaseApplyStepFlow");
            },
            _openDetailPurchaseApplyModel:function(_purchaseApply){
                vc.jumpToPage("/flow/purchaseApplyDetailFlow?applyOrderId="+_purchaseApply.applyOrderId);
            },
            _openDeletePurchaseApplyModel:function(_purchaseApply){
                vc.emit('deletePurchaseApply','openDeletePurchaseApplyModal',_purchaseApply);
            },
            _queryPurchaseApplyMethod:function(){
                vc.component._listPurchaseApplys(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.purchaseApplyManageInfo.moreCondition){
                    vc.component.purchaseApplyManageInfo.moreCondition = false;
                }else{
                    vc.component.purchaseApplyManageInfo.moreCondition = true;
                }
            },
            _queryInspectionPlanMethod:function () {
                vc.component._listPurchaseApplys(DEFAULT_PAGE, DEFAULT_ROWS);
            }

             
        }
    });
})(window.vc);
