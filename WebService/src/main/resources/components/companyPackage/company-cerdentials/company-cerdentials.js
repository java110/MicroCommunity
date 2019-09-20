/**
    初始化 公司信息

**/

(function(vc){
    vc.extends({
        data:{
            companyCerdentialsInfo:{
                value:"",
                validityPeriod:""
            }
        },
         _initMethod:function(){
             vc.component._companyinitDate();
         },
         _initEvent:function(){
                // 证件信息
               vc.component.$on('positivePhotoEvent',function(positivePhoto){
                    for(var positivePhotoKey in positivePhoto){
                         vc.component.companyCerdentialsInfo[positivePhotoKey] = positivePhoto[positivePhotoKey];
                     }
                      vc.component.$emit('companyCerdentialsEvent',vc.component.companyCerdentialsInfo);
                });

         },
        watch:{
            companyCerdentialsInfo:{
                deep: true,
                handler:function(){
                    vc.component.$emit('companyCerdentialsEvent',vc.component.companyCerdentialsInfo);
                }
             }
        },
        methods:{
            validateCerdentials:function(){
                        if(!vc.component.validatePositivePhoto()){
                            return false;
                        }
                        return vc.validate.validate({
                                                               companyCerdentialsInfo:vc.component.companyCerdentialsInfo
                                                           },{
                                                               'companyCerdentialsInfo.value':[
                                                                   {
                                                                       limit:"required",
                                                                       param:"",
                                                                       errInfo:"证件号码不能为空"
                                                                   },
                                                                   {
                                                                       limit:"maxLength",
                                                                       param:"50",
                                                                       errInfo:"证件号码长度必须在50位之内"
                                                                   },
                                                               ],

                                                               'companyCerdentialsInfo.validityPeriod':[
                                                                   {
                                                                       limit:"required",
                                                                       param:"",
                                                                       errInfo:"成立时间不能为空"
                                                                   },
                                                                   {
                                                                       limit:"date",
                                                                       param:"",
                                                                       errInfo:"不是有效的日期，例如：2019-03-29"
                                                                   }
                                                               ]

                                                           });
                    },
            _companyinitDate:function(){
                $('.company_time').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    minView: "month",
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.company_time').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".company_time").val();
                        vc.component.companyCerdentialsInfo.validityPeriod = value;
                    });
            }

        }

    });

})(window.vc);

