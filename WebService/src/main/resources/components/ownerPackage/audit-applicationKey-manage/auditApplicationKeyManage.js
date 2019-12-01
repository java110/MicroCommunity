/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            auditApplicationKeyManageInfo:{
                applicationKeys:[],
                total:0,
                records:1,
                currentApplicationKeyId:'',
                moreCondition:false,
                conditions:{
                    name:'',
                    tel:'',
                    typeCd:'',
                    idCard:''
                }
            }
        },
        _initMethod:function(){
            vc.component._listApplicationKeys(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('auditApplicationKeyManage','listApplicationKey',function(_param){
                  vc.component._listApplicationKeys(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('auditApplicationKeyManage','notifyAuditInfo',function(_auditInfo){
                  vc.component._auditApplicationKeyState(_auditInfo);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listApplicationKeys(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listApplicationKeys:function(_page, _rows){
                var param = {
                    params:{
                        page:_page,
                        row:_rows
                    }

               }
               //发送get请求
               vc.http.get('auditApplicationKeyManage',
                            'list',
                             param,
                             function(json,res){
                                var _auditApplicationKeyManageInfo=JSON.parse(json);
                                vc.component.auditApplicationKeyManageInfo.total = _auditApplicationKeyManageInfo.total;
                                vc.component.auditApplicationKeyManageInfo.records = _auditApplicationKeyManageInfo.records;
                                vc.component.auditApplicationKeyManageInfo.applicationKeys = _auditApplicationKeyManageInfo.applicationKeys;
                                vc.emit('pagination','init',{
                                    total:vc.component.auditApplicationKeyManageInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAuditApplicationKeyModal:function(_applicationKey){
                vc.component.auditApplicationKeyManageInfo.currentApplicationKeyId = _applicationKey.applicationKeyId;
                vc.emit('audit','openAuditModal',{});
            },
            _auditApplicationKeyState:function(_auditInfo){
                _auditInfo.applicationKeyId = vc.component.auditApplicationKeyManageInfo.currentApplicationKeyId;
                vc.http.post(
                    'auditApplicationKeyManage',
                    'audit',
                    JSON.stringify(_auditInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                             vc.component._listApplicationKeys(DEFAULT_PAGE, DEFAULT_ROWS);
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(errInfo);
                });
            }

        }
    });
})(window.vc);