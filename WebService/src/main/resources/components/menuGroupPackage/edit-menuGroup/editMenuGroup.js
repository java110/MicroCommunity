(function(vc,vm){

    vc.extends({
        data:{
            editMenuGroupInfo:{
                gId:'',
name:'',
icon:'',
label:'',
seq:'',
description:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editMenuGroup','openEditMenuGroupModal',function(_params){
                vc.component.refreshEditMenuGroupInfo();
                $('#editMenuGroupModel').modal('show');
                vc.copyObject(_params, vc.component.editMenuGroupInfo );
                vc.component.editMenuGroupInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editMenuGroupValidate:function(){
                        return vc.validate.validate({
                            editMenuGroupInfo:vc.component.editMenuGroupInfo
                        },{
                            'editMenuGroupInfo.name':[
{
                            limit:"required",
                            param:"",
                            errInfo:"组名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"组名称必须在2至10字符之间"
                        },
                    ],
'editMenuGroupInfo.icon':[
{
                            limit:"required",
                            param:"",
                            errInfo:"icon不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,20",
                            errInfo:"icon必须在2至20字符之间"
                        },
                    ],
'editMenuGroupInfo.label':[
{
                            limit:"required",
                            param:"",
                            errInfo:"标签不能为空"
                        },
 {
                            limit:"maxin",
                            param:"1,20",
                            errInfo:"标签错误"
                        },
                    ],
'editMenuGroupInfo.seq':[
{
                            limit:"required",
                            param:"",
                            errInfo:"序列不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"序列必须为整数"
                        },
                    ],
'editMenuGroupInfo.description':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注内容不能超过200"
                        },
                    ],
'editMenuGroupInfo.gId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"组Id不能为空"
                        }]

                        });
             },
            editMenuGroup:function(){
                if(!vc.component.editMenuGroupValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editMenuGroup',
                    'update',
                    JSON.stringify(vc.component.editMenuGroupInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editMenuGroupModel').modal('hide');
                             vc.emit('menuGroupManage','listMenuGroup',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            refreshEditMenuGroupInfo:function(){
                vc.component.editMenuGroupInfo= {
                  gId:'',
name:'',
icon:'',
label:'',
seq:'',
description:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
