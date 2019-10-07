(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addOrgInfo:{
                orgId:'',
                orgName:'',
                orgLevel:'',
                parentOrgId:'',
                description:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addOrg','openAddOrgModal',function(){
                $('#addOrgModel').modal('show');
            });
        },
        methods:{
            addOrgValidate(){
                return vc.validate.validate({
                    addOrgInfo:vc.component.addOrgInfo
                },{
                    'addOrgInfo.orgName':[
{
                            limit:"required",
                            param:"",
                            errInfo:"组织名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"组织名称长度为2至50"
                        },
                    ],
'addOrgInfo.orgLevel':[
{
                            limit:"required",
                            param:"",
                            errInfo:"组织级别不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"报修人名称必须在2至50字符之间"
                        },
                    ],
'addOrgInfo.parentOrgId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"上级ID不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"上级ID不正确"
                        },
                    ],
'addOrgInfo.description':[
{
                            limit:"required",
                            param:"",
                            errInfo:"描述不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"描述不能为空"
                        },
                    ],




                });
            },
            saveOrgInfo:function(){
                if(!vc.component.addOrgValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.addOrgInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.addOrgInfo);
                    $('#addOrgModel').modal('hide');
                    return ;
                }

                vc.http.post(
                    'addOrg',
                    'save',
                    JSON.stringify(vc.component.addOrgInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addOrgModel').modal('hide');
                            vc.component.clearAddOrgInfo();
                            vc.emit('orgManage','listOrg',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearAddOrgInfo:function(){
                vc.component.addOrgInfo = {
                                            orgName:'',
orgLevel:'',
parentOrgId:'',
description:'',

                                        };
            }
        }
    });

})(window.vc);
