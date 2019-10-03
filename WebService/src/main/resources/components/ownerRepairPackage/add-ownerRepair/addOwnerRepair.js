(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addOwnerRepairInfo:{
                repairId:'',
                repairType:'',
                repairName:'',
                tel:'',
                roomId:'',
                roomName:'',
                appointmentTime:'',
                context:'',

            }
        },
         _initMethod:function(){
            vc.component._initAddOwnerRepairInfo();
         },
         _initEvent:function(){
            vc.on('addOwnerRepair','openAddOwnerRepairModal',function(_ownerInfo){
                vc.component._freshOwnerInfo(_ownerInfo);
                $('#addOwnerRepairModel').modal('show');
            });
        },
        methods:{
            addOwnerRepairValidate(){
                return vc.validate.validate({
                    addOwnerRepairInfo:vc.component.addOwnerRepairInfo
                },{
                    'addOwnerRepairInfo.repairType':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"报修类型不能为空"
                        },
                        {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"报修类型错误"
                        },
                    ],
                        'addOwnerRepairInfo.repairName':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"报修人不能为空"
                        },
                        {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"报修人名称必须在2至50字符之间"
                        },
                    ],
                        'addOwnerRepairInfo.tel':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"联系方式不能为空"
                        },
                        {
                            limit:"phone",
                            param:"",
                            errInfo:"联系方式格式不正确"
                        },
                    ],
                    'addOwnerRepairInfo.roomId':[
                    {
                            limit:"required",
                            param:"",
                            errInfo:"房屋ID不能为空"
                        },
                    {
                            limit:"num",
                            param:"",
                            errInfo:"房屋ID错误"
                        },
                    ],
                    'addOwnerRepairInfo.appointmentTime':[
                    {
                            limit:"required",
                            param:"",
                            errInfo:"预约时间不能为空"
                        },
                        {
                            limit:"dateTime",
                            param:"",
                            errInfo:"预约时间格式错误"
                        },
                    ],
                    'addOwnerRepairInfo.context':[
                    {
                            limit:"required",
                            param:"",
                            errInfo:"报修内容不能为空"
                        },
                        {
                            limit:"maxLength",
                            param:"2000",
                            errInfo:"报修内容不能超过2000"
                        },
                    ],
                });
            },
            saveOwnerRepairInfo:function(){
                if(!vc.component.addOwnerRepairValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.addOwnerRepairInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.addOwnerRepairInfo);
                    $('#addOwnerRepairModel').modal('hide');
                    return ;
                }

                vc.http.post(
                    'addOwnerRepair',
                    'save',
                    JSON.stringify(vc.component.addOwnerRepairInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addOwnerRepairModel').modal('hide');
                            vc.component.clearAddOwnerRepairInfo();
                            vc.emit('ownerRepairManage','listOwnerRepair',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearAddOwnerRepairInfo:function(){
                vc.component.addOwnerRepairInfo = {
                        repairType:'',
                        repairName:'',
                        tel:'',
                        roomId:'',
                        appointmentTime:'',
                        context:'',
                    };
            },
            _freshOwnerInfo:function(_ownerInfo){
                vc.component.addOwnerRepairInfo.roomId = _ownerInfo.roomId;
                vc.component.addOwnerRepairInfo.roomName = _ownerInfo.roomName;

                var param={
                    params:{
                        ownerId:_ownerInfo.ownerId,
                        communityId:vc.getCurrentCommunity().communityId,
                        ownerTypeCd:'1001',
                        page:1,
                        row:1
                    }
                }
                //查询房屋信息 业主信息
               vc.http.get('addOwnerRepair',
                            'getOwner',
                             param,
                             function(json,res){
                                var _ownerInfos=JSON.parse(json);
                                var _ownerInfo = _ownerInfos.owners[0];
                               vc.component.addOwnerRepairInfo.repairName= _ownerInfo.name;
                                vc.component.addOwnerRepairInfo.tel= _ownerInfo.link;
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _initAddOwnerRepairInfo:function(){
                    vc.component.addOwnerRepairInfo.appointmentTime = vc.dateFormat(new Date().getTime());
                     $('.addAppointmentTime').datetimepicker({
                        language: 'zh-CN',
                        format: 'yyyy-mm-dd HH:ii:ss',
                        initTime: true,
                        initialDate: new Date(),
                        autoClose: 1,
                        todayBtn: true

                    });
                    $('.addAppointmentTime').datetimepicker()
                        .on('changeDate', function (ev) {
                            var value = $(".addAppointmentTime").val();
                            vc.component.addOwnerRepairInfo.appointmentTime = value;
                    });

            }
        }
    });

})(window.vc);
