/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            assetImportInfo:{
                communityId: vc.getCurrentCommunity().communityId,
                excelTemplate:'',
                remark:""
            }
        },

        _initMethod:function(){

        },
        _initEvent:function(){

        },
        methods:{
            assetImportValidate:function(){
                    return vc.validate.validate({
                            assetImportInfo:vc.component.assetImportInfo
                        },{

                            'assetImportInfo.excelTemplate':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"文件不能为空"
                                }
                            ],
                            'assetImportInfo.communityId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"还未入驻小区，请先入驻小区"
                                }
                            ]
                        });
            },
            _openDownloadHcExcelTemplate:function(){
                //下载 模板
                vc.jumpToPage('/import/hc.xlsx')
            },
            getExcelTemplate:function(e){
                //console.log("getExcelTemplate 开始调用")
               vc.component.assetImportInfo.excelTemplate = e.target.files[0]
            },
            _importData:function(){

                if(!vc.component.assetImportValidate()){
                    return ;
                }
                // 导入数据
                if (!vc.component.checkFileType(vc.component.assetImportInfo.excelTemplate.name.split('.')[1])) {
                    vc.message('不是有效的Excel格式');
                    return ;
                }
                if (!vc.component.checkFileSize(vc.component.assetImportInfo.excelTemplate.size)) {
                    vc.message('Excel文件大小不能超过2M');
                    return ;
                }
                var param = new FormData();
                param.append("excelTemplate", vc.component.assetImportInfo.excelTemplate);
                param.append('communityId',vc.component.assetImportInfo.communityId);


                vc.http.upload(
                    'assetImport',
                    'importData',
                    param,
                    {
                        emulateJSON:true,
                        //添加请求头
                        headers: {
                            "Content-Type": "multipart/form-data"
                        }
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            vc.message("处理成功");
                            vc.jumpToPage('/flow/ownerFlow')
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(errInfo);
                     });
            },
            checkFileType: function (fileType) {
                const acceptTypes = ['xls', 'xlsx'];
                for (var i = 0; i < acceptTypes.length; i++) {
                    if (fileType === acceptTypes[i]) {
                        return true;
                    }
                }
                return false;
            },
            checkFileSize: function (fileSize) {
                //2M
                const MAX_SIZE = 2 * 1024 * 1024;
                if (fileSize > MAX_SIZE) {
                    return false;
                }
                return true;
            }

        }
    });

})(window.vc);