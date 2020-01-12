(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addMenuInfo:{
                mId:'',
                name:'',
url:'',
seq:'',
isShow:'',
description:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addMenu','openAddMenuModal',function(){
                $('#addMenuModel').modal('show');
            });
        },
        methods:{
            addMenuValidate(){
                return vc.validate.validate({
                    addMenuInfo:vc.component.addMenuInfo
                },{
                    'addMenuInfo.name':[
{
                            limit:"required",
                            param:"",
                            errInfo:"菜单名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"菜单名称必须在2至10字符之间"
                        },
                    ],
'addMenuInfo.url':[
{
                            limit:"required",
                            param:"",
                            errInfo:"菜单地址不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"菜单名称必须在2至10字符之间"
                        },
                    ],
'addMenuInfo.seq':[
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
'addMenuInfo.isShow':[
{
                            limit:"required",
                            param:"",
                            errInfo:"菜单显示不能为空"
                        },
 {
                            limit:"maxin",
                            param:"1,12",
                            errInfo:"菜单显示错误"
                        },
                    ],
'addMenuInfo.description':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注内容不能超过200"
                        },
                    ],




                });
            },
            saveMenuInfo:function(){
                if(!vc.component.addMenuValidate()){
                    vc.toast(vc.validate.errInfo);

                    return ;
                }

                vc.component.addMenuInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.addMenuInfo);
                    $('#addMenuModel').modal('hide');
                    return ;
                }

                vc.http.post(
                    'addMenu',
                    'save',
                    JSON.stringify(vc.component.addMenuInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addMenuModel').modal('hide');
                            vc.component.clearAddMenuInfo();
                            vc.emit('menuManage','listMenu',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearAddMenuInfo:function(){
                vc.component.addMenuInfo = {
                                            name:'',
url:'',
seq:'',
isShow:'',
description:'',

                                        };
            }
        }
    });

})(window.vc);
