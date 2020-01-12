(function(vc){

    vc.extends({
        data:{
            addDemoInfo:{
                demoName:'',
                demoValue:'',
                demoRemark:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addDemo','openAddDemoModal',function(){
                $('#addDemoModel').modal('show');
            });
        },
        methods:{
            addDemoValidate(){
                return vc.validate.validate({
                    addDemoInfo:vc.component.addDemoInfo
                },{
                    'addDemoInfo.demoName':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"名称不能为空"
                        },
                        {
                            limit:"maxin",
                            param:"2,20",
                            errInfo:"名称长度必须在2位至10位"
                        },
                    ],
                    'addDemoInfo.demoValue':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"值不能为空"
                        },
                        {
                            limit:"maxin",
                            param:"2,20",
                            errInfo:"描述不能为空"
                        },
                    ],
                    'addDemoInfo.demoRemark':[

                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注长度不能超过200位"
                        }
                    ]

                });
            },
            saveDemoInfo:function(){
                if(!vc.component.addDemoValidate()){
                    vc.toast(vc.validate.errInfo);

                    return ;
                }
                vc.http.post(
                    'addDemo',
                    'saveDemo',
                    JSON.stringify(vc.component.addDemoInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addDemoModel').modal('hide');
                            vc.component.clearAddFloorInfo();
                            vc.emit('listDemo','listDemoData',{});

                            return ;
                        }
                        vc.component.addFloorInfo.errorInfo = json;

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.addFloorInfo.errorInfo = errInfo;

                     });
            },
            clearAddFloorInfo:function(){
                vc.component.addFloorInfo = {
                                            name:'',
                                            floorNum:'',
                                            remark:'',
                                            errorInfo:''
                                        };
            }
        }
    });

})(window.vc);