(function(vc,vm){

    vc.extends({
        propTypes: {
                   callBackListener:vc.propTypes.string, //父组件名称
                   callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addUnitInfo:{
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
             vc.on('addUnit','openAddUnitModal',function(_params){
                 vc.component.refreshAddUnitInfo();
                $('#addUnitModel').modal('show');
                vc.component.addUnitInfo.floorId = _params.floorId;
                vc.component.addUnitInfo.communityId = vc.getCurrentCommunity().communityId;
             });
             vc.on('addUnit','addUnitModel',function(_params){
                vc.component.refreshAddUnitInfo();
                $('#addUnitModel').modal('show');
                vc.component.addUnitInfo.floorId = _params.floorId;
                vc.component.addUnitInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            addUnitValidate:function(){
                        return vc.validate.validate({
                            addUnitInfo:vc.component.addUnitInfo
                        },{
                            'addUnitInfo.floorId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"小区楼不能为空"
                                }
                            ],
                            'addUnitInfo.unitNum':[
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
                            'addUnitInfo.layerCount':[
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
                            'addUnitInfo.lift':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"必须选择单元是否电梯"
                                }
                            ],
                            'addUnitInfo.remark':[
                                {
                                    limit:"maxLength",
                                    param:"200",
                                    errInfo:"备注长度不能超过200位"
                                },
                            ]

                        });
             },
            addUnit:function(){
                if(!vc.component.addUnitValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                 vc.component.addUnitInfo.communityId = vc.getCurrentCommunity().communityId;

                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.addUnitInfo);
                    $('#addUnitModel').modal('hide');
                    return ;
                }

                vc.http.post(
                    'addUnit',
                    'save',
                    JSON.stringify(vc.component.addUnitInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addUnitModel').modal('hide');
                            vc.emit('unit','loadUnit',{
                                floorId:vc.component.addUnitInfo.floorId
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
            refreshAddUnitInfo:function(){
                vc.component.addUnitInfo= {
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