(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addServiceProvideInfo:{
                id:'',
                name:'',
serviceCode:'',
params:'',
queryModel:'',
sql:'',
template:'',
proc:'',
javaScript:'',
remark:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addServiceProvide','openAddServiceProvideModal',function(){
                $('#addServiceProvideModel').modal('show');
            });
        },
        methods:{
            addServiceProvideValidate(){
                return vc.validate.validate({
                    addServiceProvideInfo:vc.component.addServiceProvideInfo
                },{
                    'addServiceProvideInfo.name':[
{
                            limit:"required",
                            param:"",
                            errInfo:"服务名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"服务名称必须在2至50字符之间"
                        },
                    ],
'addServiceProvideInfo.serviceCode':[
{
                            limit:"required",
                            param:"",
                            errInfo:"服务编码不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"服务编码必须在2至50字符之间"
                        },
                    ],
'addServiceProvideInfo.params':[
{
                            limit:"required",
                            param:"",
                            errInfo:"参数不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"500",
                            errInfo:"参数内容不能超过200"
                        },
                    ],
'addServiceProvideInfo.queryModel':[
{
                            limit:"required",
                            param:"",
                            errInfo:"实现方式不能为空"
                        },
 {
                            limit:"maxin",
                            param:"1,12",
                            errInfo:"实现方式错误"
                        },
                    ],
'addServiceProvideInfo.sql':[
 {
                            limit:"maxLength",
                            param:"2000",
                            errInfo:"sql不能超过2000"
                        },
                    ],
'addServiceProvideInfo.template':[
 {
                            limit:"maxLength",
                            param:"2000",
                            errInfo:"输出模板不能超过2000"
                        },
                    ],
'addServiceProvideInfo.proc':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"存储过程不能超过200"
                        },
                    ],
'addServiceProvideInfo.javaScript':[
 {
                            limit:"maxLength",
                            param:"2000",
                            errInfo:"java不能超过2000"
                        },
                    ],
'addServiceProvideInfo.remark':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注内容不能超过200"
                        },
                    ],




                });
            },
            saveServiceProvideInfo:function(){
                if(!vc.component.addServiceProvideValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.addServiceProvideInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.addServiceProvideInfo);
                    $('#addServiceProvideModel').modal('hide');
                    return ;
                }

                vc.http.post(
                    'addServiceProvide',
                    'save',
                    JSON.stringify(vc.component.addServiceProvideInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addServiceProvideModel').modal('hide');
                            vc.component.clearAddServiceProvideInfo();
                            vc.emit('serviceProvideManage','listServiceProvide',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearAddServiceProvideInfo:function(){
                vc.component.addServiceProvideInfo = {
                                            name:'',
serviceCode:'',
params:'',
queryModel:'',
sql:'',
template:'',
proc:'',
javaScript:'',
remark:'',

                                        };
            }
        }
    });

})(window.vc);
