(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addOwnerViewInfo:{
                flowComponent:'addOwnerView',
                name:'',
sex:'',
age:'',
link:'',
remark:'',

            }
        },
        watch:{
            addOwnerViewInfo:{
                deep: true,
                handler:function(){
                    vc.component.saveAddOwnerInfo();
                }
             }
        },
         _initMethod:function(){

         },
         _initEvent:function(){

            vc.on('addOwnerViewInfo', 'onIndex', function(_index){
                vc.component.addOwnerViewInfo.index = _index;
            });
        },
        methods:{
            addOwnerValidate(){
                return vc.validate.validate({
                    addOwnerViewInfo:vc.component.addOwnerViewInfo
                },{
                    'addOwnerViewInfo.name':[
{
                            limit:"required",
                            param:"",
                            errInfo:"名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"名称长度必须在2位至10位"
                        },
                    ],
'addOwnerViewInfo.sex':[
{
                            limit:"required",
                            param:"",
                            errInfo:"性别不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"性别不能为空"
                        },
                    ],
'addOwnerViewInfo.age':[
{
                            limit:"required",
                            param:"",
                            errInfo:"年龄不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"年龄不是有效的数字"
                        },
                    ],
'addOwnerViewInfo.link':[
 {
                            limit:"maxLength",
                            param:"2000",
                            errInfo:"输出模板不能超过2000"
                        },
                    ],
'addOwnerViewInfo.remark':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注长度不能超过200位"
                        },
                    ],

                });
            },
            saveAddOwnerInfo:function(){
                if(vc.component.addOwnerValidate()){
                    //侦听回传
                    vc.emit($props.callBackListener,$props.callBackFunction, vc.component.addOwnerViewInfo);
                    return ;
                }
            }
        }
    });

})(window.vc);
