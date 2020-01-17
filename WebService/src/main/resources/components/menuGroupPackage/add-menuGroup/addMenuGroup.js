(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addMenuGroupInfo:{
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
            vc.on('addMenuGroup','openAddMenuGroupModal',function(){
                $('#addMenuGroupModel').modal('show');
            });
        },
        methods:{
            addMenuGroupValidate(){
                return vc.validate.validate({
                    addMenuGroupInfo:vc.component.addMenuGroupInfo
                },{
                    'addMenuGroupInfo.name':[
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
'addMenuGroupInfo.icon':[
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
'addMenuGroupInfo.label':[
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
'addMenuGroupInfo.seq':[
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
'addMenuGroupInfo.description':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注内容不能超过200"
                        },
                    ],




                });
            },
            saveMenuGroupInfo:function(){
                if(!vc.component.addMenuGroupValidate()){
                    vc.toast(vc.validate.errInfo);

                    return ;
                }

                //vc.component.addMenuGroupInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.addMenuGroupInfo);
                    $('#addMenuGroupModel').modal('hide');
                    return ;
                }

                vc.http.post(
                    'addMenuGroup',
                    'save',
                    JSON.stringify(vc.component.addMenuGroupInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addMenuGroupModel').modal('hide');
                            vc.component.clearAddMenuGroupInfo();
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
            clearAddMenuGroupInfo:function(){
                vc.component.addMenuGroupInfo = {
                                            name:'',
icon:'',
label:'',
seq:'',
description:'',

                                        };
            }
        }
    });

})(window.vc);
