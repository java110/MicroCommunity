(function(vc){

    vc.extends({
        propTypes: {
                   callBackListener:vc.propTypes.string, //父组件名称
                   callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            batchAddFloorInfo:{

                startFloorNum:'',
                endFloorNum:'',
                remark:'',
                errorInfo:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('batchAddFloor','openBatchAddFloorModal',function(){
                $('#batchAddFloor').modal('show');
            });
        },
        methods:{
            batchAddFloorValidate(){
                return vc.validate.validate({
                    batchAddFloorInfo:vc.component.batchAddFloorInfo
                },{
                    'batchAddFloorInfo.startFloorNum':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"楼开始编号不能为空"
                        },
                         {
                            limit:"num",
                            param:"",
                            errInfo:"楼开始编号不是有效的数字"
                        },
                    ],
                    'batchAddFloorInfo.endFloorNum':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"楼结束编号不能为空"
                        },
                        {
                            limit:"num",
                            param:"",
                            errInfo:"楼结束编号不是有效的数字"
                        },
                    ],
                    'batchAddFloorInfo.remark':[

                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注长度不能超过200位"
                        }
                    ]

                });
            },
            batchSaveFloorInfo:function(){
                if(!vc.component.batchAddFloorValidate()){
                    vc.toast(vc.validate.errInfo);

                    return ;
                }

                if(parseInt(vc.component.batchAddFloorInfo.endFloorNum) <= parseInt(vc.component.batchAddFloorInfo.startFloorNum)){
                    vc.message('结束楼栋编号不能小于等于开始楼栋编号');
                    return;
                }


                if(vc.component.batchAddFloorInfo.endFloorNum - vc.component.batchAddFloorInfo.startFloorNum > 50){
                    vc.message('一次批量生成不能超过50栋楼');
                    return;
                }


                vc.component.batchAddFloorInfo.communityId = vc.getCurrentCommunity().communityId;

                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.batchAddFloorInfo);
                    $('#batchAddFloorModel').modal('hide');
                    return ;
                }

                vc.http.post(
                    'batchAddFloor',
                    'saveFloor',
                    JSON.stringify(vc.component.batchAddFloorInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#batchAddFloor').modal('hide');
                            vc.component.clearBatchAddFloorInfo();
                            vc.emit('listFloor','listFloorData',{});
                            var resultInfo = JSON.parse(json);
                            vc.message("楼栋成功生成"+resultInfo.successFloorCount+"，失败"+resultInfo.failFloorCount)
                            return ;
                        }
                        vc.component.batchAddFloorInfo.errorInfo = json;

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.batchAddFloorInfo.errorInfo = errInfo;

                     });
            },
            clearBatchAddFloorInfo:function(){
                vc.component.batchAddFloorInfo = {
                                           startFloorNum:'',
                                           endFloorNum:'',
                                           remark:'',
                                           errorInfo:''
                                        };
            }
        }
    });

})(window.vc);