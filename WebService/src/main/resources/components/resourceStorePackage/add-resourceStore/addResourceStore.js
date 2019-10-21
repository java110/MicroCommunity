(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addResourceStoreInfo:{
                resId:'',
                resName:'',
resCode:'',
price:'',
stock:'',
description:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addResourceStore','openAddResourceStoreModal',function(){
                $('#addResourceStoreModel').modal('show');
            });
        },
        methods:{
            addResourceStoreValidate(){
                return vc.validate.validate({
                    addResourceStoreInfo:vc.component.addResourceStoreInfo
                },{
                    'addResourceStoreInfo.resName':[
{
                            limit:"required",
                            param:"",
                            errInfo:"物品名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,100",
                            errInfo:"物品名称长度为2至100"
                        },
                    ],
'addResourceStoreInfo.resCode':[
{
                            limit:"required",
                            param:"",
                            errInfo:"物品编码不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,50",
                            errInfo:"物品编码必须在2至50字符之间"
                        },
                    ],
'addResourceStoreInfo.price':[
{
                            limit:"required",
                            param:"",
                            errInfo:"物品价格不能为空"
                        },
 {
                            limit:"money",
                            param:"",
                            errInfo:"物品价格格式错误"
                        },
                    ],
'addResourceStoreInfo.stock':[
{
                            limit:"required",
                            param:"",
                            errInfo:"物品库存不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"物品库存不是有效数字"
                        },
                    ],
'addResourceStoreInfo.description':[
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
            saveResourceStoreInfo:function(){
                if(!vc.component.addResourceStoreValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.addResourceStoreInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.addResourceStoreInfo);
                    $('#addResourceStoreModel').modal('hide');
                    return ;
                }

                vc.http.post(
                    'addResourceStore',
                    'save',
                    JSON.stringify(vc.component.addResourceStoreInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addResourceStoreModel').modal('hide');
                            vc.component.clearAddResourceStoreInfo();
                            vc.emit('resourceStoreManage','listResourceStore',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearAddResourceStoreInfo:function(){
                vc.component.addResourceStoreInfo = {
                                            resName:'',
resCode:'',
price:'',
stock:'',
description:'',

                                        };
            }
        }
    });

})(window.vc);
