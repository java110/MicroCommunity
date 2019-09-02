/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            hireParkingSpace:{
                ownerId:'',
                carNum:'',
                carBrand:'',
                carType:'',
                carColor:'',
                carRemark:"",
                cycles:"",
                psId:'',
                typeCd:'',
                additionalAmount:'0.00',
                receivableAmount: "0.00",
                receivedAmount:"0.00",
                sellOrHire:"H",
                ownerInfo:{
                    ownerId:"",
                    name:"",
                    age:"",
                    sex:"",
                    userName:"",
                    remark:"",
                    link:"",
                },
            }
        },
        _initMethod:function(){

        },
        watch:{
                    "hireParkingSpace.cycles":{//深度监听，可监听到对象、数组的变化
                        handler(val, oldVal){
                            vc.component.computeReceivableAmount(val);
                        },
                        deep:true
                    }
        },
        _initEvent:function(){
            vc.on('hireParkingSpace','notify',function(_param){
                  vc.copyObject(_param,vc.component.hireParkingSpace);
                  vc.copyObject(_param,vc.component.hireParkingSpace.ownerInfo);


                  if(_param.hasOwnProperty("typeCd")){
                        vc.component._loadFireParkingSpaceFee();
                  }

            });
        },
        methods:{
            sellCarValidate:function(){
                        return vc.validate.validate({
                            hireParkingSpace:vc.component.hireParkingSpace
                        },{
                            'hireParkingSpace.ownerId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"未选择业主"
                                }
                            ],
                            'hireParkingSpace.psId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"未选择停车位"
                                }
                            ],
                            'hireParkingSpace.carNum':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"车牌号不能为空"
                                },
                                {
                                    limit:"maxin",
                                    param:"2,12",
                                    errInfo:"车牌号不正确"
                                }
                            ],
                            'hireParkingSpace.carBrand':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"车品牌不能为空"
                                },
                                {
                                    limit:"maxLength",
                                    param:"50",
                                    errInfo:"车品牌超出限制"
                                }
                            ],
                            'hireParkingSpace.carType':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"车类型不能为空"
                                }
                            ],
                            'hireParkingSpace.carColor':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"车颜色不能为空"
                                },
                                {
                                    limit:"maxLength",
                                    param:"12",
                                    errInfo:"车颜色超出限制"
                                }
                            ],
                            'hireParkingSpace.cycles':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"缴费周期不能为空"
                                }
                            ],
                            'hireParkingSpace.receivedAmount':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"实收金额不能为空"
                                },
                                {
                                    limit:"money",
                                    param:"",
                                    errInfo:"实收金额格式错误，如3.00"
                                }
                            ]
                        });
             },

            doHireParkingSpace:function(){
                //
                if(!vc.component.sellCarValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }
                //改remark
                vc.component.hireParkingSpace.remark = vc.component.hireParkingSpace.carRemark;

                vc.component.hireParkingSpace.communityId=vc.getCurrentCommunity().communityId;
            vc.http.post(
                    'hireParkingSpace',
                    'sell',
                    JSON.stringify(vc.component.hireParkingSpace),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            vc.jumpToPage("/flow/ownerParkingSpaceFlow?" + vc.objToGetParam(vc.component.hireParkingSpace.ownerInfo));
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });

            },
            _loadFireParkingSpaceFee:function(){
                //
                var param = {
                        params:{
                            communityId:vc.getCurrentCommunity().communityId,
                            typeCd:vc.component.hireParkingSpace.typeCd
                        }
                    };
                    vc.http.get(
                        'hireParkingSpace',
                        'loadSellParkingSpaceConfigData',
                         param,
                         function(json,res){
                            //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                            if(res.status == 200){
                                //关闭model
                                var configFee = JSON.parse(json);
                                vc.component.hireParkingSpace.additionalAmount = configFee.additionalAmount;
                                //重新算费
                                vc.component.computeReceivableAmount(vc.component.hireParkingSpace.cycles);
                                return ;
                            }
                            vc.message(json);
                         },
                         function(errInfo,error){
                            console.log('请求失败处理');

                            vc.message(errInfo);
                         });

            },
            computeReceivableAmount:function(_cycles){
                        if(_cycles == null || _cycles == "" || _cycles == undefined){
                            _cycles = "0.00";
                        }
                        vc.component.hireParkingSpace.receivableAmount = (parseFloat(vc.component.hireParkingSpace.additionalAmount) * parseFloat(_cycles)).toFixed(2);
                        vc.component.hireParkingSpace.receivedAmount = vc.component.hireParkingSpace.receivableAmount;
            }
        }
    });
})(window.vc);