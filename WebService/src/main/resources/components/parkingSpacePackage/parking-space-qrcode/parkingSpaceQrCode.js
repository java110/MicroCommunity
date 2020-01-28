(function(vc){
    vc.extends({
        data:{
            parkingSpaceQrCodeInfo:{
                url:vc.getData("_sysInfo").apiUrl
            }
        },
         _initMethod:function(){
            vc.component._makeQrCode();
         },
         _initEvent:function(){

        },
        methods:{
            _makeQrCode:function(){
                var qrcode = new QRCode(document.getElementById("qrcode"), {
                	text: vc.getData("_sysInfo").logo,  //你想要填写的文本
                    width: 350, //生成的二维码的宽度
                    height: 350, //生成的二维码的高度
                    colorDark : "#000000", // 生成的二维码的深色部分
                    colorLight : "#ffffff", //生成二维码的浅色部分
                    correctLevel : QRCode.CorrectLevel.H
                });
                var _url = vc.component.parkingSpaceQrCodeInfo.url +"/tempParkingFeePay/tempParkingFeePay?communityId="+vc.getCurrentCommunity().communityId + "&communityName="+vc.getCurrentCommunity().name;
                qrcode.makeCode(_url);
            },
            _downLoadQrCode:function(){
                var qrcode = document.getElementById('qrcode');
                        var img = qrcode.getElementsByTagName('img')[0];
                        var link = document.createElement("a");
                        var url = img.getAttribute("src");
                        link.setAttribute("href",url);
                        link.setAttribute("download",vc.getData("_sysInfo").logo+'临时车支付二维码.png');
                        link.click();
            }

        }
    });

})(window.vc);