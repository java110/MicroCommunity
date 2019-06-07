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
                remark:"",
                psId:''
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('sellCar','notify',function(_param){
                  vc.copyObject(_param,vc.component.sellCarInfo.ownerInfo);
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
                            'sellCarInfo.roomId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"未选择房屋"
                                }
                            ],
                            'sellCarInfo.state':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"未选择出售状态"
                                }
                            ],


                        });
             },

            doSellCar:function(){
                //
                if(!vc.component.sellCarValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

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
                            vc.jumpToPage("/flow/ownerCarFlow?" + vc.objToGetParam(vc.component.sellCarInfo.ownerInfo));
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