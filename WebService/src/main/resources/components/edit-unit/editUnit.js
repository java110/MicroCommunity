(function(vc,vm){

    vc.extends({
        data:{
            editUnitInfo:{
                floorId:'',
                unitNum:'',
                layerCount:'',
                lift:'',
                remark:'',
                communityId:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editUnit','openUnitModel',function(_params){
                vc.component.refreshEditUnitInfo();
                $('#editUnitModel').modal('show');
                vc.component.editUnitInfo = _params;
                vc.component.editUnitInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editUnitValidate:function(){
                        return vc.validate.validate({
                            editUnitInfo:vc.component.editUnitInfo
                        },{
                            'editUnitInfo.floorId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"小区楼不能为空"
                                }
                            ],
                            'editUnitInfo.unitNum':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"单元编号不能为空"
                                },
                                {
                                    limit:"maxLength",
                                    param:"12",
                                    errInfo:"单元编号长度不能超过12位"
                                },
                            ],
                            'editUnitInfo.layerCount':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"单元楼层高度不能为空"
                                },
                                {
                                    limit:"num",
                                    param:"",
                                    errInfo:"单元楼层高度必须为数字"
                                }
                            ],
                            'editUnitInfo.lift':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"必须选择单元是否电梯"
                                }
                            ],
                            'editUnitInfo.remark':[
                                {
                                    limit:"maxLength",
                                    param:"200",
                                    errInfo:"备注长度不能超过200位"
                                },
                            ]

                        });
             },
            editUnit:function(){
                if(!vc.component.editUnitValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editUnit',
                    'update',
                    JSON.stringify(vc.component.editUnitInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editUnitModel').modal('hide');
                            vc.emit('unit','loadUnit',{
                                floorId:vc.component.editUnitInfo.floorId
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
            refreshEditUnitInfo:function(){
                vc.component.editUnitInfo= {
                  floorId:'',
                  unitNum:'',
                  layerCount:'',
                  lift:'',
                  remark:'',
                  communityId:''
                }
            }
        }
    });

})(window.vc,window.vc.component);