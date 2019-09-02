/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            serviceProvideManageInfo:{
                serviceProvides:[],
                total:0,
                records:1,
                moreCondition:false,
                name:'',
                conditions:{
                    id:'',
serviceCode:'',
name:'',
queryModel:'',

                }
            }
        },
        _initMethod:function(){
            vc.component._listServiceProvides(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            
            vc.on('serviceProvideManage','listServiceProvide',function(_param){
                  vc.component._listServiceProvides(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listServiceProvides(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listServiceProvides:function(_page, _rows){

                vc.component.serviceProvideManageInfo.conditions.page = _page;
                vc.component.serviceProvideManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.serviceProvideManageInfo.conditions
               };

               //发送get请求
               vc.http.get('serviceProvideManage',
                            'list',
                             param,
                             function(json,res){
                                var _serviceProvideManageInfo=JSON.parse(json);
                                vc.component.serviceProvideManageInfo.total = _serviceProvideManageInfo.total;
                                vc.component.serviceProvideManageInfo.records = _serviceProvideManageInfo.records;
                                vc.component.serviceProvideManageInfo.serviceProvides = _serviceProvideManageInfo.serviceProvides;
                                vc.emit('pagination','init',{
                                     total:vc.component.serviceProvideManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddServiceProvideModal:function(){
                vc.emit('addServiceProvide','openAddServiceProvideModal',{});
            },
            _openEditServiceProvideModel:function(_serviceProvide){
                vc.emit('editServiceProvide','openEditServiceProvideModal',_serviceProvide);
            },
            _openDeleteServiceProvideModel:function(_serviceProvide){
                vc.emit('deleteServiceProvide','openDeleteServiceProvideModal',_serviceProvide);
            },
            _queryServiceProvideMethod:function(){
                vc.component._listServiceProvides(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.serviceProvideManageInfo.moreCondition){
                    vc.component.serviceProvideManageInfo.moreCondition = false;
                }else{
                    vc.component.serviceProvideManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);
