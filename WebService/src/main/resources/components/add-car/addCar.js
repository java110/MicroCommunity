/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            addCarInfo:{
                carNum:'',
                carBrand:'',
                carType:'',
                carColor:'',
                remark:""
            }
        },
        watch:{
            otherInfo:{
                deep: true,
                handler:function(){
                    vc.emit('sellCar','notify',vc.component.addCarInfo);

                }
             }
        },
        _initMethod:function(){

        },
        _initEvent:function(){


        },
        methods:{


        }
    });

})(window.vc);