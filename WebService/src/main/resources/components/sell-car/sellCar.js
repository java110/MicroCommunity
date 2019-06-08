/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            sellCarInfo:{
                ownerId:'',
                carNum:'',
                carBrand:'',
                carType:'',
                carColor:'',
                carRemark:"",
                psId:'',
                typeCd:'',
                receivableAmount: "0.00",
                receivedAmount:"0.00",
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
        _initEvent:function(){
            vc.on('sellCar','notify',function(_param){
                  vc.copyObject(_param,vc.component.sellCarInfo);
                  vc.copyObject(_param,vc.component.sellCarInfo.ownerInfo);


                  if(_param.hasOwnProperty("typeCd")){
                        vc.component.computeReceivableAmount();
                  }

            });
        },
        methods:{
            sellCarValidate:function(){
                        return vc.validate.validate({
                            sellCarInfo:vc.component.sellCarInfo
                        },{
                            'sellCarInfo.ownerId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"未选择业主"
                                }
                            ],
                            'sellCarInfo.psId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"未选择停车位"
                                }
                            ],
                            'sellCarInfo.carNum':[
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
                            'sellCarInfo.carBrand':[
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
                            'sellCarInfo.carType':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"车类型不能为空"
                                }
                            ],
                            'sellCarInfo.carColor':[
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
                            'sellCarInfo.receivedAmount':[
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

            doSellCar:function(){
                //
                if(!vc.component.sellCarValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }
                //改remark
                vc.component.sellCarInfo.remark = vc.component.sellCarInfo.carRemark;

                vc.component.sellCarInfo.communityId=vc.getCurrentCommunity().communityId;
            vc.http.post(
                    'sellCar',
                    'sell',
                    JSON.stringify(vc.component.sellCarInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            vc.jumpToPage("/flow/ownerParkingSpaceFlow?" + vc.objToGetParam(vc.component.sellRoomInfo.ownerInfo));
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });

            },
            computeReceivableAmount:function(){
                //
                var param = {
                        params:{
                            communityId:vc.getCurrentCommunity().communityId,
                            typeCd:vc.component.sellCarInfo.typeCd
                        }
                    };
                    vc.http.get(
                        'sellCar',
                        'loadSellParkingSpaceConfigData',
                         param,
                         function(json,res){
                            //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                            if(res.status == 200){
                                //关闭model
                                var configFee = JSON.parse(json);

                                vc.component.sellCarInfo.receivableAmount = configFee.additionalAmount;
                                vc.component.sellCarInfo.receivedAmount = configFee.additionalAmount;
                                return ;
                            }
                            vc.message(json);
                         },
                         function(errInfo,error){
                            console.log('请求失败处理');

                            vc.message(errInfo);
                         });

            }
        }
    });
})(window.vc);