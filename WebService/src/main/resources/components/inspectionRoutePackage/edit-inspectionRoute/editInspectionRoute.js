(function(vc,vm){

    vc.extends({
        data:{
            editInspectionRouteInfo:{
                configId:'',
routeName:'',
inspectionName:'',
machineQuantity:'',
checkQuantity:'',
remark:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editInspectionRoute','openEditInspectionRouteModal',function(_params){
                vc.component.refreshEditInspectionRouteInfo();
                $('#editInspectionRouteModel').modal('show');
                vc.copyObject(_params, vc.component.editInspectionRouteInfo );
                vc.component.editInspectionRouteInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editInspectionRouteValidate:function(){
                        return vc.validate.validate({
                            editInspectionRouteInfo:vc.component.editInspectionRouteInfo
                        },{
                            'editInspectionRouteInfo.routeName':[
{
                            limit:"required",
                            param:"",
                            errInfo:"路线名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"1,100",
                            errInfo:"路线名称字数不能超过100个"
                        },
                    ],
'editInspectionRouteInfo.inspectionName':[
{
                            limit:"required",
                            param:"",
                            errInfo:"巡检点不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"3000",
                            errInfo:"1个巡检路线的巡检点上限为100个"
                        },
                    ],
'editInspectionRouteInfo.machineQuantity':[
{
                            limit:"required",
                            param:"",
                            errInfo:"设备数量不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"数量（数字）格式错误"
                        },
                    ],
'editInspectionRouteInfo.checkQuantity':[
{
                            limit:"required",
                            param:"",
                            errInfo:"检查项数量不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"数量（数字）格式错误"
                        },
                    ],
'editInspectionRouteInfo.remark':[
 {
                            limit:"maxin",
                            param:"1,200",
                            errInfo:"收费项目不能超过100位"
                        },
                    ],
'editInspectionRouteInfo.configId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"inspectionRouteId不能为空"
                        }]

                        });
             },
            editInspectionRoute:function(){
                if(!vc.component.editInspectionRouteValidate()){
                    vc.toast(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editInspectionRoute',
                    'update',
                    JSON.stringify(vc.component.editInspectionRouteInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editInspectionRouteModel').modal('hide');
                             vc.emit('inspectionRouteManage','listInspectionRoute',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            refreshEditInspectionRouteInfo:function(){
                vc.component.editInspectionRouteInfo= {
                  configId:'',
routeName:'',
inspectionName:'',
machineQuantity:'',
checkQuantity:'',
remark:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
