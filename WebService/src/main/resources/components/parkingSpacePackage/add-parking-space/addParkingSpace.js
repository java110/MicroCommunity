(function(vc){

    vc.extends({
        propTypes: {
               notifyLoadDataComponentName:vc.propTypes.string
        },
        data:{
            addParkingSpaceInfo:{
                num:'',
                typeCd:'',
                area:'',
                remark:'',
                psId:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addParkingSpace','openAddParkingSpaceModal',function(_parkingSpaceId){
                if(_parkingSpaceId != null || _parkingSpaceId != -1){
                    vc.component.addParkingSpaceInfo.parkingSpaceId = _parkingSpaceId;
                }
                $('#addParkingSpaceModel').modal('show');
            });
        },
        methods:{
            addParkingSpaceValidate(){
                return vc.validate.validate({
                    addParkingSpaceInfo:vc.component.addParkingSpaceInfo
                },{
                    'addParkingSpaceInfo.num':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"车位编号不能为空"
                        },
                        {
                            limit:"maxLength",
                            param:"12",
                            errInfo:"车位编号长度不能超过12位"
                        },
                    ],
                    'addParkingSpaceInfo.typeCd':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"车位类型不能为空"
                        }
                    ],
                    'addParkingSpaceInfo.area':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"车位面积不能为空"
                        },
                        {
                            limit:"money",
                            param:"",
                            errInfo:"车位面积格式错误，如3.09"
                        }
                    ],
                    'addParkingSpaceInfo.remark':[

                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注长度不能超过200位"
                        }
                    ]

                });
            },
            saveParkingSpaceInfo:function(){
                if(!vc.component.addParkingSpaceValidate()){
                    vc.toast(vc.validate.errInfo);

                    return ;
                }

                vc.component.addParkingSpaceInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'addParkingSpace',
                    'saveParkingSpace',
                    JSON.stringify(vc.component.addParkingSpaceInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addParkingSpaceModel').modal('hide');
                            vc.component.clearAddParkingSpaceInfo();
                            vc.emit($props.notifyLoadDataComponentName,'listParkingSpaceData',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearAddParkingSpaceInfo:function(){
                vc.component.addParkingSpaceInfo = {
                                            num:'',
                                            typeCd:'',
                                            area:'',
                                            remark:''
                                        };
            }
        }
    });

})(window.vc);