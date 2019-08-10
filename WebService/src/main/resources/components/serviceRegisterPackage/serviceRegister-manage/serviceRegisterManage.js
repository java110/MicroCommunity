/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            serviceRegisterManageInfo:{
                serviceRegisters:[],
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
            vc.component._listServiceRegisters(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('serviceRegisterManage','chooseApp',function(_param){
              vc.copyObject(_param,vc.component.serviceRegisterManageInfo.conditions);
            });

            vc.on('serviceRegisterManage','listServiceRegister',function(_param){
                  vc.component._listServiceRegisters(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listServiceRegisters(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listServiceRegisters:function(_page, _rows){

                vc.component.serviceRegisterManageInfo.conditions.page = _page;
                vc.component.serviceRegisterManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.serviceRegisterManageInfo.conditions
               };

               //发送get请求
               vc.http.get('serviceRegisterManage',
                            'list',
                             param,
                             function(json,res){
                                var _serviceRegisterManageInfo=JSON.parse(json);
                                vc.component.serviceRegisterManageInfo.total = _serviceRegisterManageInfo.total;
                                vc.component.serviceRegisterManageInfo.records = _serviceRegisterManageInfo.records;
                                vc.component.serviceRegisterManageInfo.serviceRegisters = _serviceRegisterManageInfo.serviceRegisters;
                                vc.emit('pagination','init',{
                                     total:vc.component.serviceRegisterManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddServiceRegisterModal:function(){
                //vc.emit('addServiceRegister','openAddServiceRegisterModal',{});
                vc.jumpToPage("/flow/serviceBindingFlow");
            },
            _openEditServiceRegisterModel:function(_serviceRegister){
                vc.emit('editServiceRegister','openEditServiceRegisterModal',_serviceRegister);
            },
            _openDeleteServiceRegisterModel:function(_serviceRegister){
                vc.emit('deleteServiceRegister','openDeleteServiceRegisterModal',_serviceRegister);
            },
            _queryServiceRegisterMethod:function(){
                vc.component._listServiceRegisters(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.serviceRegisterManageInfo.moreCondition){
                    vc.component.serviceRegisterManageInfo.moreCondition = false;
                }else{
                    vc.component.serviceRegisterManageInfo.moreCondition = true;
                }
            }

             ,
             _openChooseAppMethod:function(){
                            vc.emit('chooseApp','openChooseAppModel',{});

            },
            _loadDataByParam: function(){
                    vc.component.serviceManageInfo.conditions.appId = vc.getParam("appId");
                    //如果 floodId 没有传 则，直接结束
                    if(vc.component.serviceManageInfo.conditions.appId == null
                        || vc.component.serviceManageInfo.conditions.appId == undefined
                        || vc.component.serviceManageInfo.conditions.appId == ''){
                        vc.component._listServices(DEFAULT_PAGE, DEFAULT_ROWS);
                        return ;
                    }

                    var param = {
                        params:{
                            page:DEFAULT_PAGE,
                            row:DEFAULT_ROWS,
                            communityId:vc.getCurrentCommunity().communityId,
                            appId:vc.component.serviceManageInfo.conditions.appId
                        }
                    }

                    vc.http.get(
                        'serviceRegisterManage',
                        'loadApp',
                         param,
                         function(json,res){
                            if(res.status == 200){
                                var _appInfo = JSON.parse(json);
                                var _tmpApp = _appInfo.apps[0];
                                vc.component.serviceManageInfo.conditions.appName = _tmpApp.name;
                                return ;
                            }
                            vc.message(json);
                         },
                         function(errInfo,error){
                            console.log('请求失败处理');

                            vc.message(errInfo);
                         });

                    vc.component._listServiceRegisters(DEFAULT_PAGE, DEFAULT_ROWS);
            }
        }
    });
})(window.vc);
