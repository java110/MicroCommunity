/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            sellRoomInfo:{
                ownerId:'',
                roomId:'',
                state:'',
                remark:''
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('sellRoom','notify',function(_param){
                  //vc.copyObject(_param,vc.component.sellRoomInfo);

                  if(_param.hasOwnProperty("ownerId")){
                    vc.component.sellRoomInfo.ownerId = _param.ownerId;
                  }

                   if(_param.hasOwnProperty("roomId")){
                      vc.component.sellRoomInfo.roomId = _param.roomId;
                    }

                if(_param.hasOwnProperty("otherState")){
                   vc.component.sellRoomInfo.state = _param.otherState;
                 }

                  if(_param.hasOwnProperty("otherRemark")){
                     vc.component.sellRoomInfo.remark = _param.otherRemark;
                   }


            });
        },
        methods:{
            sellRoomValidate:function(){
                        return vc.validate.validate({
                            sellRoomInfo:vc.component.sellRoomInfo
                        },{
                            'sellRoomInfo.ownerId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"未选择业主"
                                }
                            ],
                            'sellRoomInfo.roomId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"未选择房屋"
                                }
                            ],
                            'sellRoomInfo.state':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"未选择出售状态"
                                }
                            ],


                        });
             },

            doSellRoom:function(){
                //
                if(!vc.component.sellRoomValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.component.sellRoomInfo.communityId:vc.getCurrentCommunity().communityId;
            vc.http.post(
                    'sellRoom',
                    'sell',
                    JSON.stringify(vc.component.sellRoomInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model

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