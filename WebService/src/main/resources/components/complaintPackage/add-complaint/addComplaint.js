(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addComplaintInfo:{
                complaintId:'',
                storeId:'',
typeCd:'',
roomId:'',
complaintName:'',
tel:'',
state:'',
context:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addComplaint','openAddComplaintModal',function(){
                $('#addComplaintModel').modal('show');
            });
        },
        methods:{
            addComplaintValidate(){
                return vc.validate.validate({
                    addComplaintInfo:vc.component.addComplaintInfo
                },{
                    'addComplaintInfo.storeId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"商户ID不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"商户ID必须为数字"
                        },
                    ],
'addComplaintInfo.typeCd':[
{
                            limit:"required",
                            param:"",
                            errInfo:"投诉类型不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"投诉类型格式错误"
                        },
                    ],
'addComplaintInfo.roomId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"房屋编号不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"房屋编号格式错误"
                        },
                    ],
'addComplaintInfo.complaintName':[
{
                            limit:"required",
                            param:"",
                            errInfo:"投诉人不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"投诉人不能大于200位"
                        },
                    ],
'addComplaintInfo.tel':[
{
                            limit:"required",
                            param:"",
                            errInfo:"投诉电话不能为空"
                        },
 {
                            limit:"phone",
                            param:"",
                            errInfo:"投诉电话格式错误"
                        },
                    ],
'addComplaintInfo.state':[
{
                            limit:"required",
                            param:"",
                            errInfo:"投诉状态不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"投诉状态格式错误"
                        },
                    ],
'addComplaintInfo.context':[
{
                            limit:"required",
                            param:"",
                            errInfo:"投诉内容不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"4000",
                            errInfo:"投诉状态超过4000位"
                        },
                    ],




                });
            },
            saveComplaintInfo:function(){
                if(!vc.component.addComplaintValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.addComplaintInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.addComplaintInfo);
                    $('#addComplaintModel').modal('hide');
                    return ;
                }

                vc.http.post(
                    'addComplaint',
                    'save',
                    JSON.stringify(vc.component.addComplaintInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addComplaintModel').modal('hide');
                            vc.component.clearAddComplaintInfo();
                            vc.emit('complaintManage','listComplaint',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearAddComplaintInfo:function(){
                vc.component.addComplaintInfo = {
                                            storeId:'',
typeCd:'',
roomId:'',
complaintName:'',
tel:'',
state:'',
context:'',

                                        };
            }
        }
    });

})(window.vc);
