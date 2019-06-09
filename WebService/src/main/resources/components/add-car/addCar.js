/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
            callBackComponent:vc.propTypes.string
        },
        data:{
            addCarInfo:{
                carNum:'',
                carBrand:'',
                carType:'',
                carColor:'',
                carRemark:""
            }
        },
        watch:{
            addCarInfo:{
                deep: true,
                handler:function(){
                    vc.emit($props.callBackComponent,'notify',vc.component.addCarInfo);

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