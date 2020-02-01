(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            payFeeOrderInfo:{
                feeId:'',
                feeName:'',
                feeTypeCdName: '',
                endTime: '',
                feePrice:0.00,
                cycles:'1',
                totalFeePrice:0.00,
                receivedAmount:'',
                communityId:vc.getCurrentCommunity().communityId
            }
        },
        _initMethod:function(){
            if(vc.notNull(vc.getParam("feeId"))){
                  vc.component.payFeeOrderInfo.feeId = vc.getParam('feeId');
                  vc.component.payFeeOrderInfo.feeName = vc.getParam('feeName');
                  vc.component.payFeeOrderInfo.feeTypeCdName = vc.getParam('feeTypeCdName');
                  vc.component.payFeeOrderInfo.endTime = vc.getParam('endTime').replace(/%3A/g,':');
                  vc.component.payFeeOrderInfo.feePrice = vc.getParam('feePrice');
            };

            vc.component.payFeeOrderInfo.totalFeePrice = vc.component.payFeeOrderInfo.feePrice;
            vc.component.payFeeOrderInfo.receivedAmount = vc.component.payFeeOrderInfo.totalFeePrice ;


        },
        _initEvent:function(){

        },
        methods:{
                payFeeValidate:function(){
                        return vc.validate.validate({
                            payFeeOrderInfo:vc.component.payFeeOrderInfo
                        },{
                            'payFeeOrderInfo.feeId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"费用ID不能为空"
                                }
                            ],
                            'payFeeOrderInfo.cycles':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"缴费周期不能为空"
                                }
                            ],
                            'payFeeOrderInfo.receivedAmount':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"实收金额不能为空"
                                },
                                {
                                    limit:"money",
                                    param:"",
                                    errInfo:"实收金额不是有效的金额"
                                }
                            ]
                        });
             },
            _payFee:function(_page,_row){
                if(!vc.component.payFeeValidate()){
                    vc.toast(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'propertyPay',
                    'payFee',
                    JSON.stringify(vc.component.payFeeOrderInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                             $("#payFeeResult").modal({
                                           backdrop: "static",//点击空白处不关闭对话框
                                           show:true
                                        });
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(errInfo);
                     });
            },
            _changeMonth:function(_cycles){
                vc.component.payFeeOrderInfo.totalFeePrice = parseFloat(_cycles)*parseFloat(vc.component.payFeeOrderInfo.feePrice);
                vc.component.payFeeOrderInfo.receivedAmount = vc.component.payFeeOrderInfo.totalFeePrice ;
            },
            _back:function(){
                $('#payFeeResult').modal("hide");
                vc.getBack();
            },
            _printAndBack:function(){
                $('#payFeeResult').modal("hide");

                vc.getBack();
            }
        }

    });
})(window.vc);
