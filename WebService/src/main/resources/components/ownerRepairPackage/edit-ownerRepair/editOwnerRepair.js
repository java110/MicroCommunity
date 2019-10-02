(function(vc,vm){

    vc.extends({
        data:{
            editOwnerRepairInfo:{
                repairId:'',
repairType:'',
repairName:'',
tel:'',
roomId:'',
appointmentTime:'',
context:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editOwnerRepair','openEditOwnerRepairModal',function(_params){
                vc.component.refreshEditOwnerRepairInfo();
                $('#editOwnerRepairModel').modal('show');
                vc.copyObject(_params, vc.component.editOwnerRepairInfo );
                vc.component.editOwnerRepairInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editOwnerRepairValidate:function(){
                        return vc.validate.validate({
                            editOwnerRepairInfo:vc.component.editOwnerRepairInfo
                        },{
                            'editOwnerRepairInfo.repairType':[
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
'editOwnerRepairInfo.repairName':[
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
'editOwnerRepairInfo.tel':[
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
'editOwnerRepairInfo.roomId':[
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
'editOwnerRepairInfo.appointmentTime':[
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
'editOwnerRepairInfo.context':[
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
'editOwnerRepairInfo.repairId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"报修ID不能为空"
                        }]

                        });
             },
            editOwnerRepair:function(){
                if(!vc.component.editOwnerRepairValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editOwnerRepair',
                    'update',
                    JSON.stringify(vc.component.editOwnerRepairInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editOwnerRepairModel').modal('hide');
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
            refreshEditOwnerRepairInfo:function(){
                vc.component.editOwnerRepairInfo= {
                  repairId:'',
repairType:'',
repairName:'',
tel:'',
roomId:'',
appointmentTime:'',
context:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
