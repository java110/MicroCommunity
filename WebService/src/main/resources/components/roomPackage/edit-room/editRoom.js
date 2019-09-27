(function(vc,vm){

    vc.extends({
        data:{
            editRoomUnits:[],
            editRoomInfo:{
                roomId:'',
                unitId:'',
                roomNum:'',
                layer:'',
                section:'0',
                apartment:'',
                apartment1:'',
                apartment2:'',
                builtUpArea:'',
                unitPrice:'',
                state:'',
                remark:'',
                communityId:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editRoom','openEditRoomModal',function(_room){
                 vc.copyObject(_room,vc.component.editRoomInfo);
                 vc.component.loadUnitsFromEditRoom(_room.floorId);
                 $('#editRoomModel').modal('show');

                vc.component.editRoomInfo.floorId = _room.floorId;
                vc.component.editRoomInfo.communityId = vc.getCurrentCommunity().communityId;

             });
        },
        methods:{
            /**
                根据楼ID加载房屋
            **/
            loadUnitsFromEditRoom:function(_floorId){
                vc.component.editRoomUnits = [];
                var param = {
                    params:{
                        floorId:_floorId,
                        communityId:vc.getCurrentCommunity().communityId
                    }
                }
                vc.http.get(
                    'editRoom',
                    'loadUnits',
                     param,
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            var tmpUnits = JSON.parse(json);
                            vc.component.editRoomUnits = tmpUnits;

                            if('0.00' == vc.component.editRoomInfo.unitPrice){
                                vc.component.editRoomInfo.unitPrice='';
                            }
                            vc.component.editRoomInfo.apartment1=vc.component.editRoomInfo.apartment.substr(0,2);
                            vc.component.editRoomInfo.apartment2=vc.component.editRoomInfo.apartment.substr(2,5);
                            /*if(tmpUnits == null || tmpUnits.length == 0){
                                return ;
                            }
                            for(var unitIndex = 0; unitIndex < tmpUnits.length;unitIndex++){
                               vc.component.editRoomInfo.units[unitIndex] = tmpUnits[unitIndex];
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
            editRoomValidate:function(){
                        return vc.validate.validate({
                            editRoomInfo:vc.component.editRoomInfo
                        },{
                            'editRoomInfo.unitId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"小区楼房屋不能为空"
                                }
                            ],
                            'editRoomInfo.roomNum':[
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
                            'editRoomInfo.layer':[
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
                            /*'editRoomInfo.section':[
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
                            ],*/
                            'editRoomInfo.state':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"房间状态不能为空"
                                }
                            ],
                            'editRoomInfo.apartment':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"户型不能为空"
                                }
                            ],
                            'editRoomInfo.builtUpArea':[
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
                            /*'editRoomInfo.unitPrice':[
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
                            ],*/
                            'editRoomInfo.remark':[
                                {
                                    limit:"maxLength",
                                    param:"200",
                                    errInfo:"备注长度不能超过200位"
                                },
                            ]

                        });
             },
            editRoom:function(){
                if(!vc.component.editRoomValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }
                vc.component.editRoomInfo.apartment=vc.component.editRoomInfo.apartment1+vc.component.editRoomInfo.apartment2;
                if ('' == vc.component.editRoomInfo.unitPrice || null == vc.component.editRoomInfo.unitPrice){
                    vc.component.editRoomInfo.unitPrice='0';
                }
                vc.http.post(
                    'editRoom',
                    'update',
                    JSON.stringify(vc.component.editRoomInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editRoomModel').modal('hide');
                            vc.emit('room','loadData',{
                                floorId:vc.component.editRoomInfo.floorId
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
            refreshEditRoomInfo:function(){
                vc.component.editRoomInfo= {
                  unitId:'',
                  roomNum:'',
                  layer:'',
                  section:'0',
                  apartment:'',
                  builtUpArea:'',
                  unitPrice:'',
                  state:'',
                  remark:'',
                  communityId:''
                }
            }
        }
    });

})(window.vc,window.vc.component);