(function(vc,vm){

    vc.extends({
        data:{
            editResourceStoreInfo:{
                resId:'',
                resName:'',
                resCode:'',
                price:'',
                description:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editResourceStore','openEditResourceStoreModal',function(_params){
                vc.component.refreshEditResourceStoreInfo();
                $('#editResourceStoreModel').modal('show');
                vc.copyObject(_params, vc.component.editResourceStoreInfo );
                vc.component.editResourceStoreInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editResourceStoreValidate:function(){
                        return vc.validate.validate({
                            editResourceStoreInfo:vc.component.editResourceStoreInfo
                        },{
                            'editResourceStoreInfo.resName':[
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
'editResourceStoreInfo.resCode':[
 {
                            limit:"maxLength",
                            param:"50",
                            errInfo:"物品编码不能超过50位"
                        },
                    ],
'editResourceStoreInfo.price':[
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

'editResourceStoreInfo.description':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"描述不能为空"
                        },
                    ],
'editResourceStoreInfo.resId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"物品ID不能为空"
                        }]

                        });
             },
            editResourceStore:function(){
                if(!vc.component.editResourceStoreValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editResourceStore',
                    'update',
                    JSON.stringify(vc.component.editResourceStoreInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editResourceStoreModel').modal('hide');
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
            refreshEditResourceStoreInfo:function(){
                vc.component.editResourceStoreInfo= {
                  resId:'',
                    resName:'',
                    resCode:'',
                    price:'',
                    description:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
