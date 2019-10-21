/**
    初始化 公司信息

**/

(function(vc){
    vc.extends({
        data:{
            storeTypes:[],
            companyBaseInfo:{
                name:"",
                address:"",
                tel:"",
                storeTypeCd:"",
                nearbyLandmarks:""
            },
            areas:[],
            provs:[],
            citys: [],
            selectProv: '',
            selectCity: '',
            allCity:[]
        },
         _initMethod:function(){
             vc.component.initStoreType();
         },
         _initEvent:function(){
//              vc.component.$on('errorInfoEvent',function(_errorInfo){
//                     vc.component.registerInfo.errorInfo = _errorInfo;
//                     console.log('errorInfoEvent 事件被监听',_errorInfo)
//                 });

         },
        watch:{
            companyBaseInfo:{
                deep: true,
                handler:function(){
                    vc.component.$emit('companyBaseEvent',vc.component.companyBaseInfo);
                }
             }
        },
        methods:{
            getProv: function (prov) {
                let tempCity=[];
                this.citys=[];
                this.selectCity='';
                for (var val of this.allCity){
                    if (prov.currentTarget.value == val.pid){
                        tempCity.push({label: val.label, id: val.id})
                    }
                }
                this.citys = tempCity;
            },
            getCity: function (city) {
                console.log(this.selectCity);
                console.log(this.selectProv);
            },
            initStoreType:function(){
                var param = {
                                    params:{
                                        msg:"123"
                                    }

                               }
                vc.http.get('company','getStoreType',
                             JSON.stringify(param),
                             function(json,res){
                                if(res.status == 200){
                                    vc.component.storeTypes = JSON.parse(json);
                                    return ;
                                }
                                //vc.component.$emit('errorInfoEvent',json);
                             },function(errInfo,error){
                                console.log('请求失败处理',errInfo,error);
                                vc.component.$emit('errorInfoEvent',errInfo);
                             });
                vc.http.get('company','getAreas',
                    JSON.stringify(param),
                    function(json,res){
                        if(res.status == 200){
                            vc.component.areas = JSON.parse(json);
                            for (var i=0;i<vc.component.areas.length;i++){
                                if(vc.component.areas[i].level==1){
                                    vc.component.provs.push(vc.component.areas[i])
                                }
                                if(vc.component.areas[i].level==2){
                                    vc.component.allCity.push(vc.component.areas[i])
                                }
                            }
                            return ;
                        }
                        //vc.component.$emit('errorInfoEvent',json);
                    },function(errInfo,error){
                        console.log('请求失败处理',errInfo,error);
                        vc.component.$emit('errorInfoEvent',errInfo);
                    });
            },
            validateBase:function(){
                return vc.validate.validate({
                                    companyBaseInfo:vc.component.companyBaseInfo
                                },{
                                    'companyBaseInfo.name':[
                                        {
                                            limit:"required",
                                            param:"",
                                            errInfo:"公司名不能为空"
                                        },
                                        {
                                            limit:"maxLength",
                                            param:"100",
                                            errInfo:"用户名长度必须在100位之内"
                                        },
                                    ],
                                    'companyBaseInfo.address':[
                                        {
                                            limit:"required",
                                            param:"",
                                            errInfo:"地址不能为空"
                                        },
                                        {
                                            limit:"maxLength",
                                            param:"200",
                                            errInfo:"地址长度必须在200位之内"
                                        },
                                    ],
                                    'companyBaseInfo.tel':[
                                        {
                                            limit:"required",
                                            param:"",
                                            errInfo:"手机号不能为空"
                                        },
                                        {
                                            limit:"phone",
                                            param:"",
                                            errInfo:"不是有效的手机号"
                                        }
                                    ],
                                    'companyBaseInfo.storeTypeCd':[
                                        {
                                            limit:"required",
                                            param:"",
                                            errInfo:"商户类型不能为空"
                                        }
                                    ],
                                    'companyBaseInfo.nearbyLandmarks':[
                                        {
                                            limit:"required",
                                            param:"",
                                            errInfo:"附近建筑不能为空"
                                        },
                                        {
                                            limit:"maxLength",
                                            param:"200",
                                            errInfo:"地址长度必须在200位之内"
                                        }
                                    ],

                                });
            }
        }

    });

})(window.vc);

