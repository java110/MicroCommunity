/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
            callBackComponent:vc.propTypes.string,
            callBackFunction:vc.propTypes.string
        },
        data:{
            addCarInfo:{
                flowComponent:'addCar',
                carNum:'',
                carBrand:'',
                carType:'',
                carColor:'',
                carRemark:""
            },
            carTypes:[
                {
                    key:'9901',
                    value:'家用小汽车'
                },
                {
                    key:'9902',
                    value:'客车'
                },
                {
                    key:'9903',
                    value:'货车'
                }
            ]
        },
        watch:{
            addCarInfo:{
                deep: true,
                handler:function(){
                    vc.component.saveAddCarInfo();
                }
             }
        },
        _initMethod:function(){
            var param={
                params: {
                    name: 'owner_car',
                    type: 'car_type'
                }
            }
            //发送get请求
            vc.http.get('hireParkingSpace',
                'listCarType',
                param,
                function (json, res) {
                    var carTypes = JSON.parse(json);

                    vc.component.carTypes = carTypes;
                }, function (errInfo, error) {
                    console.log('请求失败处理');
                }
            );
        },
        _initEvent:function(){
            vc.on('addCar', 'onIndex', function(_index){
                vc.component.addCarInfo.index = _index;
            });

        },
        methods:{
            addCarValidate:function(){
                    return vc.validate.validate({
                            addCarInfo:vc.component.addCarInfo
                        },{

                            'addCarInfo.carNum':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"车牌号不能为空"
                                },
                                {
                                    limit:"maxin",
                                    param:"2,12",
                                    errInfo:"车牌号不正确"
                                }
                            ],
                            'addCarInfo.carBrand':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"车品牌不能为空"
                                },
                                {
                                    limit:"maxLength",
                                    param:"50",
                                    errInfo:"车品牌超出限制"
                                }
                            ],
                            'addCarInfo.carType':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"车类型不能为空"
                                }
                            ],
                            'addCarInfo.carColor':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"车颜色不能为空"
                                },
                                {
                                    limit:"maxLength",
                                    param:"12",
                                    errInfo:"车颜色超出限制"
                                }
                            ]
                        });
            },
            saveAddCarInfo:function(){
                if(vc.component.addCarValidate()){
                    //侦听回传
                    vc.emit($props.callBackComponent,$props.callBackFunction, vc.component.addCarInfo);
                    return ;
                }
            }

        }
    });

})(window.vc);