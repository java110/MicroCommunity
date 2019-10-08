/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            addVisitCase:{
                visitCase:""
            }
        },
        watch:{
            addVisitCase:{
                handler:function (val,oldval) {

                },
                deep:true,
            },
            'addVisitCase.visitCase':function (val,oldval) {
                vc.emit('addVisitSpace', 'visitCase',val);
            },

        },

        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('addVisitCase', 'onIndex', function(_index){
                vc.emit('addVisitSpace', 'notify', _index);
            });

        },
        methods:{
            addCarValidate:function(){

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