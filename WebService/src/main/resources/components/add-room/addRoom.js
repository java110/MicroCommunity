(function(vc,vm){

    vc.extends({
        data:{
            addRoomUnits:[],
            addRoomInfo:{
                unitId:'',
                roomNum:'',
                layer:'',
                section:'',
                apartment:'',
                builtUpArea:'',
                unitPrice:'',
                remark:'',
                communityId:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('addRoom','addRoomModel',function(_params){
                vc.component.refreshAddRoomInfo();
                vc.component.loadUnitsFromAddRoom(_params.floorId);
                $('#addRoomModel').modal('show');
                vc.component.addRoomInfo.floorId = _params.floorId;
                vc.component.addRoomInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            /**
                根据楼ID加载房屋
            **/
            loadUnitsFromAddRoom:function(_floorId){
                vc.component.addRoomUnits = [];
                var param = {
                    params:{
                        floorId:_floorId,
                        communityId:vc.getCurrentCommunity().communityId
                    }
                }
                vc.http.get(
                    'addRoom',
                    'loadUnits',
                     param,
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            var tmpUnits = JSON.parse(json);
                            vc.component.addRoomUnits = tmpUnits;
                            /*if(tmpUnits == null || tmpUnits.length == 0){
                                return ;
                            }
                            for(var unitIndex = 0; unitIndex < tmpUnits.length;unitIndex++){
                               vc.component.addRoomInfo.units[unitIndex] = tmpUnits[unitIndex];
                            }*/
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            addRoomValidate:function(){
                        return vc.validate.validate({
                            addRoomInfo:vc.component.addRoomInfo
                        },{
                            'addRoomInfo.unitId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"小区楼房屋不能为空"
                                }
                            ],
                            'addRoomInfo.roomNum':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"房屋编号不能为空"
                                },
                                {
                                    limit:"maxLength",
                                    param:"12",
                                    errInfo:"房屋编号长度不能超过12位"
                                },
                            ],
                            'addRoomInfo.layer':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"房屋楼层高度不能为空"
                                },
                                {
                                    limit:"num",
                                    param:"",
                                    errInfo:"房屋楼层高度必须为数字"
                                }
                            ],
                            'addRoomInfo.section':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"房间数不能为空"
                                },
                                {
                                    limit:"num",
                                    param:"",
                                    errInfo:"房间数必须为数字"
                                }
                            ],
                            'addRoomInfo.apartment':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"户型不能为空"
                                }
                            ],
                            'addRoomInfo.builtUpArea':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"建筑面积不能为空"
                                },
                                {
                                    limit:"money",
                                    param:"",
                                    errInfo:"建筑面积错误，如 300.00"
                                },
                                {
                                  limit:"maxLength",
                                  param:"12",
                                  errInfo:"建筑面积数字长度不能超过6位"
                                }
                            ],
                            'addRoomInfo.unitPrice':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"房屋单价不能为空"
                                },
                                {
                                    limit:"money",
                                    param:"",
                                    errInfo:"房屋单价错误 如 300.00"
                                },
                                 {
                                   limit:"maxLength",
                                   param:"12",
                                   errInfo:"房屋单价数字长度不能超过12位"
                                 }
                            ],
                            'addRoomInfo.remark':[
                                {
                                    limit:"maxLength",
                                    param:"200",
                                    errInfo:"备注长度不能超过200位"
                                },
                            ]

                        });
             },
            addRoom:function(){
                if(!vc.component.addRoomValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'addRoom',
                    'save',
                    JSON.stringify(vc.component.addRoomInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addRoomModel').modal('hide');
                            vc.emit('room','loadData',{
                                floorId:vc.component.addRoomInfo.floorId
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
            refreshAddRoomInfo:function(){
                vc.component.addRoomInfo= {
                  unitId:'',
                  roomNum:'',
                  layer:'',
                  section:'',
                  apartment:'',
                  builtUpArea:'',
                  unitPrice:'',
                  remark:'',
                  communityId:''
                }
            }
        }
    });

})(window.vc,window.vc.component);