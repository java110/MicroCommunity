(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            indexArrearsInfo:{
                arrears:[],
                feeTypeCd:'888800010001',
                total:0,
                records:1
            }
        },
        _initMethod:function(){
            vc.component._listArrearsData(DEFAULT_PAGE,DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on("indexArrears","_listArrearsData",function(){
                vc.component._listArrearsData(DEFAULT_PAGE,DEFAULT_ROWS);
            });
        },
        methods:{
            _listArrearsData:function(_page,_row){
                if(vc.getCurrentCommunity() == null || vc.getCurrentCommunity == undefined){
                    return ;
                }
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        feeTypeCd:vc.component.indexArrearsInfo.feeTypeCd
                    }
                }

               //发送get请求
               vc.http.get('listArrears',
                            'list',
                             param,
                             function(json,res){
                                var listArrearsData =JSON.parse(json);

                                vc.component.indexArrearsInfo.total = listArrearsData.total;
                                vc.component.indexArrearsInfo.records = listArrearsData.records;
                                vc.component.indexArrearsInfo.arrears = listArrearsData.arrears;

                                vc.emit('pagination','init',{
                                    total:vc.component.indexArrearsInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            },
            _switchFeeType:function(_feeTypeCd){
                console.log('_feeTypeCd')
                vc.component.indexArrearsInfo.feeTypeCd = _feeTypeCd;
                vc.component._listArrearsData(DEFAULT_PAGE,DEFAULT_ROWS);
            }
        }
    })
})(window.vc);