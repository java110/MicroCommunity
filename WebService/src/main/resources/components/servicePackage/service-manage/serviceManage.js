/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            serviceManageInfo:{
                services:[],
                total:0,
                records:1,
                moreCondition:false,
                name:'',
                conditions:{
                    appName:'',
appId:'',
serviceName:'',
serviceCode:'',
serviceUrl:'',

                }
            }
        },
        _initMethod:function(){
            vc.component._listServices(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('serviceManage','chooseApp',function(_param){
              vc.copyObject(_param,vc.component.serviceManageInfo.conditions);
            });

            vc.on('serviceManage','listService',function(_param){
                  vc.component._listServices(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listServices(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listServices:function(_page, _rows){

                vc.component.serviceManageInfo.conditions.page = _page;
                vc.component.serviceManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.serviceManageInfo.conditions
               };

               //发送get请求
               vc.http.get('serviceManage',
                            'list',
                             param,
                             function(json,res){
                                var _serviceManageInfo=JSON.parse(json);
                                vc.component.serviceManageInfo.total = _serviceManageInfo.total;
                                vc.component.serviceManageInfo.records = _serviceManageInfo.records;
                                vc.component.serviceManageInfo.services = _serviceManageInfo.services;
                                vc.emit('pagination','init',{
                                     total:vc.component.serviceManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddServiceModal:function(){
                vc.emit('addService','openAddServiceModal',{});
            },
            _openEditServiceModel:function(_service){
                vc.emit('editService','openEditServiceModal',_service);
            },
            _openDeleteServiceModel:function(_service){
                vc.emit('deleteService','openDeleteServiceModal',_service);
            },
            _queryServiceMethod:function(){
                vc.component._listServices(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.serviceManageInfo.moreCondition){
                    vc.component.serviceManageInfo.moreCondition = false;
                }else{
                    vc.component.serviceManageInfo.moreCondition = true;
                }
            }

             ,
 _openChooseAppMethod:function(){
                vc.emit('chooseApp','openChooseAppModel',{});

            }
        }
    });
})(window.vc);
