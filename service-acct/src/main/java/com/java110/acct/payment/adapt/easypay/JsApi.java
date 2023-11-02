package com.java110.acct.payment.adapt.easypay;

import java.io.Serializable;
import java.util.List;

public class JsApi implements Serializable {

    /**
     * orgId :
     * orgMercode :
     * orgTermno :
     * orgTrace :
     * sign :
     * signType :
     * prodTrace :
     * appendData : {"orgBasestation":"","orgDeviceIp":"","orgGpstype":"","orgLatitude":"","orgLongitude":"","dgtlEnvlp":"","identity":{"encryptInfo":"","fixBuyer":"","minage":"","needCheckInfo":"","type":"身份证：01；护照：02;军官证：03;士兵证：04;户口本：05 （微信只支持身份证）"},"wxLimitPay":"no_credit","invoiceSt":false,"wxDetail":{"cost_price":0,"goods_detail":[{"goods_id":"","goods_name":"","price":0,"quantity":0,"wxpay_goods_id":""}],"receipt_id":""},"wxGoodsTag":"","wxSceneInfo":{"address":"","area_code":"","id":"","name":""},"aliDisablePayChannels":"","aliEnablePayChannels":"","aliExtendParams":{"card_type":"","hb_fq_num":"","hb_fq_seller_percent":"","industry_refluxString_info":"","sys_service_provider_id":""},"aliGoodsDetail":[{"alipay_goods_id":"","body":"","categories_tree":"","goods_category":"","goods_id":"","goods_name":"","price":0,"quantity":0,"show_url":""}],"aliBusinessParams":"{\n  \"data\": \"123\"\n}","aliDiscountableAmount":1,"aliOperatorId":"","aliSellerId":"","aliStoreId":"","aliTerminalParams":"{\n  \"credential\": \"28763443825664394:20180207192030954:abcdefGHIJKLMN\",\n  \"signature\": \"xxxxxxx\",\n  \"terminalType\": \"IOT\"\n}","aliUndiscountableAmount":1,"alipayStoreId":"","qrAcqAddnData":{"customData":"","goodsInfos":[{"addnInfo":"","category":"","id":"","name":"","price":"","quantity":""}],"orderInfo":{"addnInfo":"","dctAmount":"","description":"","title":""}},"qrSpecFeeInfo":""}
     * data : {"tradeCode":"","tradeAmt":1,"timeoutMinutes":60,"orderInfo":"","infoAttach":"","delaySettleFlag":"","patnerSettleFlag":"","splitSettleFlag":"","investor":"","orgPreferentialAmt":"","orgBackUrl":"","payerId":"","payerOtherInfo":"","wxSubAppid":"","attach":"","handingFee":"","orgSmercode":"","otherfee":""}
     * orgInfo :
     */

    private String orgId;
    private String orgMercode;
    private String orgTermno;
    private String orgTrace;
    private String sign;
    private String signType;
    private String prodTrace;
    private AppendDataBean appendData;
    private DataBean data;
    private String orgInfo;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgMercode() {
        return orgMercode;
    }

    public void setOrgMercode(String orgMercode) {
        this.orgMercode = orgMercode;
    }

    public String getOrgTermno() {
        return orgTermno;
    }

    public void setOrgTermno(String orgTermno) {
        this.orgTermno = orgTermno;
    }

    public String getOrgTrace() {
        return orgTrace;
    }

    public void setOrgTrace(String orgTrace) {
        this.orgTrace = orgTrace;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getProdTrace() {
        return prodTrace;
    }

    public void setProdTrace(String prodTrace) {
        this.prodTrace = prodTrace;
    }

    public AppendDataBean getAppendData() {
        return appendData;
    }

    public void setAppendData(AppendDataBean appendData) {
        this.appendData = appendData;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getOrgInfo() {
        return orgInfo;
    }

    public void setOrgInfo(String orgInfo) {
        this.orgInfo = orgInfo;
    }

    public static class AppendDataBean implements Serializable {
        /**
         * orgBasestation :
         * orgDeviceIp :
         * orgGpstype :
         * orgLatitude :
         * orgLongitude :
         * dgtlEnvlp :
         * identity : {"encryptInfo":"","fixBuyer":"","minage":"","needCheckInfo":"","type":"身份证：01；护照：02;军官证：03;士兵证：04;户口本：05 （微信只支持身份证）"}
         * wxLimitPay : no_credit
         * invoiceSt : false
         * wxDetail : {"cost_price":0,"goods_detail":[{"goods_id":"","goods_name":"","price":0,"quantity":0,"wxpay_goods_id":""}],"receipt_id":""}
         * wxGoodsTag :
         * wxSceneInfo : {"address":"","area_code":"","id":"","name":""}
         * aliDisablePayChannels :
         * aliEnablePayChannels :
         * aliExtendParams : {"card_type":"","hb_fq_num":"","hb_fq_seller_percent":"","industry_refluxString_info":"","sys_service_provider_id":""}
         * aliGoodsDetail : [{"alipay_goods_id":"","body":"","categories_tree":"","goods_category":"","goods_id":"","goods_name":"","price":0,"quantity":0,"show_url":""}]
         * aliBusinessParams : {
         "data": "123"
         }
         * aliDiscountableAmount : 1
         * aliOperatorId :
         * aliSellerId :
         * aliStoreId :
         * aliTerminalParams : {
         "credential": "28763443825664394:20180207192030954:abcdefGHIJKLMN",
         "signature": "xxxxxxx",
         "terminalType": "IOT"
         }
         * aliUndiscountableAmount : 1
         * alipayStoreId :
         * qrAcqAddnData : {"customData":"","goodsInfos":[{"addnInfo":"","category":"","id":"","name":"","price":"","quantity":""}],"orderInfo":{"addnInfo":"","dctAmount":"","description":"","title":""}}
         * qrSpecFeeInfo :
         */

        private String orgBasestation;
        private String orgDeviceIp;
        private String orgGpstype;
        private String orgLatitude;
        private String orgLongitude;
        private String dgtlEnvlp;
        private IdentityBean identity;
        private String wxLimitPay;
        private boolean invoiceSt;
        private WxDetailBean wxDetail;
        private String wxGoodsTag;
        private WxSceneInfoBean wxSceneInfo;
        private String aliDisablePayChannels;
        private String aliEnablePayChannels;
        private AliExtendParamsBean aliExtendParams;
        private String aliBusinessParams;
        private String aliDiscountableAmount;
        private String aliOperatorId;
        private String aliSellerId;
        private String aliStoreId;
        private String aliTerminalParams;
        private String aliUndiscountableAmount;
        private String alipayStoreId;
        private QrAcqAddnDataBean qrAcqAddnData;
        private String qrSpecFeeInfo;
        private List<AliGoodsDetailBean> aliGoodsDetail;

        /**
         * 259号文改造增加字段
         */
        private TerminalInfo terminalinfo;

        public String getOrgBasestation() {
            return orgBasestation;
        }

        public void setOrgBasestation(String orgBasestation) {
            this.orgBasestation = orgBasestation;
        }

        public String getOrgDeviceIp() {
            return orgDeviceIp;
        }

        public void setOrgDeviceIp(String orgDeviceIp) {
            this.orgDeviceIp = orgDeviceIp;
        }

        public String getOrgGpstype() {
            return orgGpstype;
        }

        public void setOrgGpstype(String orgGpstype) {
            this.orgGpstype = orgGpstype;
        }

        public String getOrgLatitude() {
            return orgLatitude;
        }

        public void setOrgLatitude(String orgLatitude) {
            this.orgLatitude = orgLatitude;
        }

        public String getOrgLongitude() {
            return orgLongitude;
        }

        public void setOrgLongitude(String orgLongitude) {
            this.orgLongitude = orgLongitude;
        }

        public String getDgtlEnvlp() {
            return dgtlEnvlp;
        }

        public void setDgtlEnvlp(String dgtlEnvlp) {
            this.dgtlEnvlp = dgtlEnvlp;
        }

        public IdentityBean getIdentity() {
            return identity;
        }

        public void setIdentity(IdentityBean identity) {
            this.identity = identity;
        }

        public String getWxLimitPay() {
            return wxLimitPay;
        }

        public void setWxLimitPay(String wxLimitPay) {
            this.wxLimitPay = wxLimitPay;
        }

        public boolean isInvoiceSt() {
            return invoiceSt;
        }

        public void setInvoiceSt(boolean invoiceSt) {
            this.invoiceSt = invoiceSt;
        }

        public WxDetailBean getWxDetail() {
            return wxDetail;
        }

        public void setWxDetail(WxDetailBean wxDetail) {
            this.wxDetail = wxDetail;
        }

        public String getWxGoodsTag() {
            return wxGoodsTag;
        }

        public void setWxGoodsTag(String wxGoodsTag) {
            this.wxGoodsTag = wxGoodsTag;
        }

        public WxSceneInfoBean getWxSceneInfo() {
            return wxSceneInfo;
        }

        public void setWxSceneInfo(WxSceneInfoBean wxSceneInfo) {
            this.wxSceneInfo = wxSceneInfo;
        }

        public String getAliDisablePayChannels() {
            return aliDisablePayChannels;
        }

        public void setAliDisablePayChannels(String aliDisablePayChannels) {
            this.aliDisablePayChannels = aliDisablePayChannels;
        }

        public String getAliEnablePayChannels() {
            return aliEnablePayChannels;
        }

        public void setAliEnablePayChannels(String aliEnablePayChannels) {
            this.aliEnablePayChannels = aliEnablePayChannels;
        }

        public AliExtendParamsBean getAliExtendParams() {
            return aliExtendParams;
        }

        public void setAliExtendParams(AliExtendParamsBean aliExtendParams) {
            this.aliExtendParams = aliExtendParams;
        }

        public String getAliBusinessParams() {
            return aliBusinessParams;
        }

        public void setAliBusinessParams(String aliBusinessParams) {
            this.aliBusinessParams = aliBusinessParams;
        }

        public String getAliDiscountableAmount() {
            return aliDiscountableAmount;
        }

        public void setAliDiscountableAmount(String aliDiscountableAmount) {
            this.aliDiscountableAmount = aliDiscountableAmount;
        }

        public String getAliOperatorId() {
            return aliOperatorId;
        }

        public void setAliOperatorId(String aliOperatorId) {
            this.aliOperatorId = aliOperatorId;
        }

        public String getAliSellerId() {
            return aliSellerId;
        }

        public void setAliSellerId(String aliSellerId) {
            this.aliSellerId = aliSellerId;
        }

        public String getAliStoreId() {
            return aliStoreId;
        }

        public void setAliStoreId(String aliStoreId) {
            this.aliStoreId = aliStoreId;
        }

        public String getAliTerminalParams() {
            return aliTerminalParams;
        }

        public void setAliTerminalParams(String aliTerminalParams) {
            this.aliTerminalParams = aliTerminalParams;
        }

        public String getAliUndiscountableAmount() {
            return aliUndiscountableAmount;
        }

        public void setAliUndiscountableAmount(String aliUndiscountableAmount) {
            this.aliUndiscountableAmount = aliUndiscountableAmount;
        }

        public String getAlipayStoreId() {
            return alipayStoreId;
        }

        public void setAlipayStoreId(String alipayStoreId) {
            this.alipayStoreId = alipayStoreId;
        }

        public QrAcqAddnDataBean getQrAcqAddnData() {
            return qrAcqAddnData;
        }

        public void setQrAcqAddnData(QrAcqAddnDataBean qrAcqAddnData) {
            this.qrAcqAddnData = qrAcqAddnData;
        }

        public String getQrSpecFeeInfo() {
            return qrSpecFeeInfo;
        }

        public void setQrSpecFeeInfo(String qrSpecFeeInfo) {
            this.qrSpecFeeInfo = qrSpecFeeInfo;
        }

        public List<AliGoodsDetailBean> getAliGoodsDetail() {
            return aliGoodsDetail;
        }

        public void setAliGoodsDetail(List<AliGoodsDetailBean> aliGoodsDetail) {
            this.aliGoodsDetail = aliGoodsDetail;
        }

        public TerminalInfo getTerminalinfo() {
            return terminalinfo;
        }

        public void setTerminalinfo(TerminalInfo terminalinfo) {
            this.terminalinfo = terminalinfo;
        }

        public static class IdentityBean implements Serializable {
            /**
             * encryptInfo :
             * fixBuyer :
             * minage :
             * needCheckInfo :
             * type : 身份证：01；护照：02;军官证：03;士兵证：04;户口本：05 （微信只支持身份证）
             */

            private String encryptInfo;
            private String fixBuyer;
            private String minage;
            private String needCheckInfo;
            private String type;

            public String getEncryptInfo() {
                return encryptInfo;
            }

            public void setEncryptInfo(String encryptInfo) {
                this.encryptInfo = encryptInfo;
            }

            public String getFixBuyer() {
                return fixBuyer;
            }

            public void setFixBuyer(String fixBuyer) {
                this.fixBuyer = fixBuyer;
            }

            public String getMinage() {
                return minage;
            }

            public void setMinage(String minage) {
                this.minage = minage;
            }

            public String getNeedCheckInfo() {
                return needCheckInfo;
            }

            public void setNeedCheckInfo(String needCheckInfo) {
                this.needCheckInfo = needCheckInfo;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class WxDetailBean implements Serializable {
            /**
             * cost_price : 0
             * goods_detail : [{"goods_id":"","goods_name":"","price":0,"quantity":0,"wxpay_goods_id":""}]
             * receipt_id :
             */

            private int cost_price;
            private String receipt_id;
            private List<GoodsDetailBean> goods_detail;

            public int getCost_price() {
                return cost_price;
            }

            public void setCost_price(int cost_price) {
                this.cost_price = cost_price;
            }

            public String getReceipt_id() {
                return receipt_id;
            }

            public void setReceipt_id(String receipt_id) {
                this.receipt_id = receipt_id;
            }

            public List<GoodsDetailBean> getGoods_detail() {
                return goods_detail;
            }

            public void setGoods_detail(List<GoodsDetailBean> goods_detail) {
                this.goods_detail = goods_detail;
            }

            public static class GoodsDetailBean implements Serializable {
                /**
                 * goods_id :
                 * goods_name :
                 * price : 0
                 * quantity : 0
                 * wxpay_goods_id :
                 */

                private String goods_id;
                private String goods_name;
                private int price;
                private int quantity;
                private String wxpay_goods_id;

                public String getGoods_id() {
                    return goods_id;
                }

                public void setGoods_id(String goods_id) {
                    this.goods_id = goods_id;
                }

                public String getGoods_name() {
                    return goods_name;
                }

                public void setGoods_name(String goods_name) {
                    this.goods_name = goods_name;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
                }

                public int getQuantity() {
                    return quantity;
                }

                public void setQuantity(int quantity) {
                    this.quantity = quantity;
                }

                public String getWxpay_goods_id() {
                    return wxpay_goods_id;
                }

                public void setWxpay_goods_id(String wxpay_goods_id) {
                    this.wxpay_goods_id = wxpay_goods_id;
                }
            }
        }


        public static class WxSceneInfoBean implements Serializable {
            /**
             * address :
             * area_code :
             * id :
             * name :
             */

            private String address;
            private String area_code;
            private String id;
            private String name;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getArea_code() {
                return area_code;
            }

            public void setArea_code(String area_code) {
                this.area_code = area_code;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }


        public static class AliExtendParamsBean implements Serializable {
            /**
             * card_type :
             * hb_fq_num :
             * hb_fq_seller_percent :
             * industry_refluxString_info :
             * sys_service_provider_id :
             */

            private String card_type;
            private String hb_fq_num;
            private String hb_fq_seller_percent;
            private String industry_refluxString_info;
            private String sys_service_provider_id;

            public String getCard_type() {
                return card_type;
            }

            public void setCard_type(String card_type) {
                this.card_type = card_type;
            }

            public String getHb_fq_num() {
                return hb_fq_num;
            }

            public void setHb_fq_num(String hb_fq_num) {
                this.hb_fq_num = hb_fq_num;
            }

            public String getHb_fq_seller_percent() {
                return hb_fq_seller_percent;
            }

            public void setHb_fq_seller_percent(String hb_fq_seller_percent) {
                this.hb_fq_seller_percent = hb_fq_seller_percent;
            }

            public String getIndustry_refluxString_info() {
                return industry_refluxString_info;
            }

            public void setIndustry_refluxString_info(String industry_refluxString_info) {
                this.industry_refluxString_info = industry_refluxString_info;
            }

            public String getSys_service_provider_id() {
                return sys_service_provider_id;
            }

            public void setSys_service_provider_id(String sys_service_provider_id) {
                this.sys_service_provider_id = sys_service_provider_id;
            }
        }

        public static class QrAcqAddnDataBean implements Serializable {
            /**
             * customData :
             * goodsInfos : [{"addnInfo":"","category":"","id":"","name":"","price":"","quantity":""}]
             * orderInfo : {"addnInfo":"","dctAmount":"","description":"","title":""}
             */

            private String customData;
            private OrderInfoBean orderInfo;
            private List<GoodsInfosBean> goodsInfos;

            public String getCustomData() {
                return customData;
            }

            public void setCustomData(String customData) {
                this.customData = customData;
            }

            public OrderInfoBean getOrderInfo() {
                return orderInfo;
            }

            public void setOrderInfo(OrderInfoBean orderInfo) {
                this.orderInfo = orderInfo;
            }

            public List<GoodsInfosBean> getGoodsInfos() {
                return goodsInfos;
            }

            public void setGoodsInfos(List<GoodsInfosBean> goodsInfos) {
                this.goodsInfos = goodsInfos;
            }


            public static class OrderInfoBean implements Serializable {
                /**
                 * addnInfo :
                 * dctAmount :
                 * description :
                 * title :
                 */

                private String addnInfo;
                private String dctAmount;
                private String description;
                private String title;

                public String getAddnInfo() {
                    return addnInfo;
                }

                public void setAddnInfo(String addnInfo) {
                    this.addnInfo = addnInfo;
                }

                public String getDctAmount() {
                    return dctAmount;
                }

                public void setDctAmount(String dctAmount) {
                    this.dctAmount = dctAmount;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }


            public static class GoodsInfosBean implements Serializable {
                /**
                 * addnInfo :
                 * category :
                 * id :
                 * name :
                 * price :
                 * quantity :
                 */

                private String addnInfo;
                private String category;
                private String id;
                private String name;
                private String price;
                private String quantity;

                public String getAddnInfo() {
                    return addnInfo;
                }

                public void setAddnInfo(String addnInfo) {
                    this.addnInfo = addnInfo;
                }

                public String getCategory() {
                    return category;
                }

                public void setCategory(String category) {
                    this.category = category;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getQuantity() {
                    return quantity;
                }

                public void setQuantity(String quantity) {
                    this.quantity = quantity;
                }
            }
        }


        public static class AliGoodsDetailBean implements Serializable {
            /**
             * alipay_goods_id :
             * body :
             * categories_tree :
             * goods_category :
             * goods_id :
             * goods_name :
             * price : 0
             * quantity : 0
             * show_url :
             */

            private String alipay_goods_id;
            private String body;
            private String categories_tree;
            private String goods_category;
            private String goods_id;
            private String goods_name;
            private int price;
            private int quantity;
            private String show_url;

            public String getAlipay_goods_id() {
                return alipay_goods_id;
            }

            public void setAlipay_goods_id(String alipay_goods_id) {
                this.alipay_goods_id = alipay_goods_id;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getCategories_tree() {
                return categories_tree;
            }

            public void setCategories_tree(String categories_tree) {
                this.categories_tree = categories_tree;
            }

            public String getGoods_category() {
                return goods_category;
            }

            public void setGoods_category(String goods_category) {
                this.goods_category = goods_category;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public String getShow_url() {
                return show_url;
            }

            public void setShow_url(String show_url) {
                this.show_url = show_url;
            }
        }
    }


    public static class DataBean implements Serializable {
        /**
         * tradeCode :
         * tradeAmt : 1
         * timeoutMinutes : 60
         * orderInfo :
         * infoAttach :
         * delaySettleFlag :
         * patnerSettleFlag :
         * splitSettleFlag :
         * investor :
         * orgPreferentialAmt :
         * orgBackUrl :
         * payerId :
         * payerOtherInfo :
         * wxSubAppid :
         * attach :
         * handingFee :
         * orgSmercode :
         * otherfee :
         */

        private String tradeCode;
        private int tradeAmt;
        private int timeoutMinutes;
        private String orderInfo;
        private String infoAttach;
        private String delaySettleFlag;
        private String patnerSettleFlag;
        private String splitSettleFlag;
        private String investor;
        private String orgPreferentialAmt;
        private String orgBackUrl;
        private String orgFrontUrl;
        private String payerId;
        private String payerOtherInfo;
        private String wxSubAppid;
        private String attach;
        private String handingFee;
        private String orgSmercode;
        private String otherfee;

        public String getTradeCode() {
            return tradeCode;
        }

        public void setTradeCode(String tradeCode) {
            this.tradeCode = tradeCode;
        }

        public int getTradeAmt() {
            return tradeAmt;
        }

        public void setTradeAmt(int tradeAmt) {
            this.tradeAmt = tradeAmt;
        }

        public int getTimeoutMinutes() {
            return timeoutMinutes;
        }

        public void setTimeoutMinutes(int timeoutMinutes) {
            this.timeoutMinutes = timeoutMinutes;
        }

        public String getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(String orderInfo) {
            this.orderInfo = orderInfo;
        }

        public String getInfoAttach() {
            return infoAttach;
        }

        public void setInfoAttach(String infoAttach) {
            this.infoAttach = infoAttach;
        }

        public String getDelaySettleFlag() {
            return delaySettleFlag;
        }

        public void setDelaySettleFlag(String delaySettleFlag) {
            this.delaySettleFlag = delaySettleFlag;
        }

        public String getPatnerSettleFlag() {
            return patnerSettleFlag;
        }

        public void setPatnerSettleFlag(String patnerSettleFlag) {
            this.patnerSettleFlag = patnerSettleFlag;
        }

        public String getSplitSettleFlag() {
            return splitSettleFlag;
        }

        public void setSplitSettleFlag(String splitSettleFlag) {
            this.splitSettleFlag = splitSettleFlag;
        }

        public String getInvestor() {
            return investor;
        }

        public void setInvestor(String investor) {
            this.investor = investor;
        }

        public String getOrgPreferentialAmt() {
            return orgPreferentialAmt;
        }

        public void setOrgPreferentialAmt(String orgPreferentialAmt) {
            this.orgPreferentialAmt = orgPreferentialAmt;
        }

        public String getOrgBackUrl() {
            return orgBackUrl;
        }

        public void setOrgBackUrl(String orgBackUrl) {
            this.orgBackUrl = orgBackUrl;
        }

        public String getOrgFrontUrl() {
            return orgFrontUrl;
        }

        public void setOrgFrontUrl(String orgFrontUrl) {
            this.orgFrontUrl = orgFrontUrl;
        }

        public String getPayerId() {
            return payerId;
        }

        public void setPayerId(String payerId) {
            this.payerId = payerId;
        }

        public String getPayerOtherInfo() {
            return payerOtherInfo;
        }

        public void setPayerOtherInfo(String payerOtherInfo) {
            this.payerOtherInfo = payerOtherInfo;
        }

        public String getWxSubAppid() {
            return wxSubAppid;
        }

        public void setWxSubAppid(String wxSubAppid) {
            this.wxSubAppid = wxSubAppid;
        }

        public String getAttach() {
            return attach;
        }

        public void setAttach(String attach) {
            this.attach = attach;
        }

        public String getHandingFee() {
            return handingFee;
        }

        public void setHandingFee(String handingFee) {
            this.handingFee = handingFee;
        }

        public String getOrgSmercode() {
            return orgSmercode;
        }

        public void setOrgSmercode(String orgSmercode) {
            this.orgSmercode = orgSmercode;
        }

        public String getOtherfee() {
            return otherfee;
        }

        public void setOtherfee(String otherfee) {
            this.otherfee = otherfee;
        }
    }
}