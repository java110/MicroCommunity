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
            addVisitCase(val){
                vc.emit('addVisitSpace', 'visitCase', this.addVisitCase.visitCase);
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('addVisitCase', 'onIndex', function(_index){
                console.log("侦听到addVisitCase的index为  "+_index);
                // vc.component.addCarInfo.index = _index;
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