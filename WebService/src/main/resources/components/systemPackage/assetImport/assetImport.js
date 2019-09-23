/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            assetImportInfo:{
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
                            ]
                        });
            },
            saveAssetImportInfo:function(){
                if(!vc.component.assetImportValidate()){
                    return ;
                }
            },
            _openDownloadHcExcelTemplate:function(){
                //下载 模板
                vc.jumpToPage('/import/hc.xlsx')
            }

        }
    });

})(window.vc);