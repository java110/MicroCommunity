package com.java110;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ContractRoot>\n" +
                "\t<TcpCont>\n" +
                "\t\t<ActionCode>0</ActionCode>\n" +
                "\t\t<TransactionID>1000000001201805072111632360</TransactionID>\n" +
                "\t\t<ServiceLevel>1</ServiceLevel>\n" +
                "\t\t<SrcOrgID>100000</SrcOrgID>\n" +
                "\t\t<SrcSysID>1000000001</SrcSysID>\n" +
                "\t\t<SrcSysSign>******</SrcSysSign>\n" +
                "\t\t<DstOrgID>600405</DstOrgID>\n" +
                "\t\t<DstSysID>6004050001</DstSysID>\n" +
                "\t\t<BusCode>BUS35011</BusCode>\n" +
                "\t\t<ServiceCode>SVC80003</ServiceCode>\n" +
                "\t\t<ServiceContractVer>SVC3300620121001</ServiceContractVer>\n" +
                "\t\t<ReqTime>20180507111632</ReqTime>\n" +
                "\t</TcpCont>\n" +
                "\t<SvcCont>\n" +
                "\t\t<SOO type=\"SAVE_CUSTOMER_ORDER_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>100</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<CUSTOMER_ORDER>\n" +
                "\t\t\t\t<LAN_ID>8630000</LAN_ID>\n" +
                "\t\t\t\t<REASON/>\n" +
                "\t\t\t\t<CUST_ID>:getCustIdByExtId(8630100,,1000018854710000,$-1$)</CUST_ID>\n" +
                "\t\t\t\t<PRE_HANDLE_FLAG>T</PRE_HANDLE_FLAG>\n" +
                "\t\t\t\t<CUST_ORDER_ID>$100000$</CUST_ORDER_ID>\n" +
                "\t\t\t\t<EXT_CUST_ORDER_ID>QH201800000031-1(1)</EXT_CUST_ORDER_ID>\n" +
                "\t\t\t\t<ATTR CD=\"140000003\" VAL=\"才金花\">发起人名称</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"140000004\" VAL=\"18997072118\">发起人联系电话</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"140000005\" VAL=\"\">发起人EMAIL</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"140000006\" VAL=\"\">原客户订单标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"140000007\" VAL=\"青海广播电视台\">客户名称</ATTR>\n" +
                "\t\t\t</CUSTOMER_ORDER>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"ADD_ORDER_ITEM_GROUP_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>200</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_ITEM_GROUP>\n" +
                "\t\t\t\t<LAN_ID>8630000</LAN_ID>\n" +
                "\t\t\t\t<REASON/>\n" +
                "\t\t\t\t<ACCEPT_DATE>20180503102650</ACCEPT_DATE>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289158</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t\t<CUST_ORDER_ID>$100000$</CUST_ORDER_ID>\n" +
                "\t\t\t\t<SERVICE_OFFER_ID>4010100000</SERVICE_OFFER_ID>\n" +
                "\t\t\t\t<SERVICE_OFFER_NAME>新装</SERVICE_OFFER_NAME>\n" +
                "\t\t\t\t<OLD_ORDER_ITEM_GROUP_ID/>\n" +
                "\t\t\t\t<ATTR CD=\"141000001\" VAL=\"F\">提供测试报告</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000006\" VAL=\"2018-05-04\">要求完成日期</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000007\" VAL=\"80050032001\">优先级别</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000008\" VAL=\"\">加急类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000009\" VAL=\"\">割接截址时间</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000012\" VAL=\"T\">是否等待起租</ATTR>\n" +
                "\t\t\t</ORDER_ITEM_GROUP>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_INST_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>300</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289158</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_INST>\n" +
                "\t\t\t\t<ACC_NBR/>\n" +
                "\t\t\t\t<LAN_ID>8630000</LAN_ID>\n" +
                "\t\t\t\t<EXT_PROD_ID>13410725</EXT_PROD_ID>\n" +
                "\t\t\t\t<ACC_PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</ACC_PROD_INST_ID>\n" +
                "\t\t\t\t<EXT_PROD_INST_ID>146748949</EXT_PROD_INST_ID>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<PRODUCT_NBR>00103022000000000000</PRODUCT_NBR>\n" +
                "\t\t\t\t<USE_CUST_ID/>\n" +
                "\t\t\t\t<OFFER_NBR>2210200301100007</OFFER_NBR>\n" +
                "\t\t\t\t<OWNER_CUST_ID>:getCustIdByExtId(8630100,,1000018854710000,$-1$)</OWNER_CUST_ID>\n" +
                "\t\t\t\t<ADDRESS_DESC/>\n" +
                "\t\t\t\t<BEGIN_RENT_TIME/>\n" +
                "\t\t\t\t<ATTR CD=\"100000103\" VAL=\"80050068001\">接入类型 </ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100001103\" VAL=\"1538\">MTU数值</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000028\" VAL=\"\">起租/退租系数</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000018\" VAL=\"80050030001\">电路类别</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000017\" VAL=\"9006005\">电路类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000025\" VAL=\"30010001\">付费方式</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000024\" VAL=\"oper0016\">受理运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000001\" VAL=\"80050002002\">租用性质</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000002\" VAL=\"3年\">租用期限</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000007\" VAL=\"\">速率</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000013\" VAL=\"80050036007\">电路维护等级</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"142000811\" VAL=\"2\">是否公免电路</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000619\" VAL=\"\">CTYUN账号</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100001109\" VAL=\"\">云资源池站点</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000026\" VAL=\"\">付费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000027\" VAL=\"\">运营商的流水号</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100010725\" VAL=\"1944662\">主调局(MAIN_ORG)</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100012368\" VAL=\"\">下单局</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000569\" VAL=\"88800130001\">业务类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"101900002\" VAL=\"\">项目编号</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"101900001\" VAL=\"\">合同编号</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"142000812\" VAL=\"\">公免文号</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"142001990\" VAL=\"2\">是否接入云</ATTR>\n" +
                "\t\t\t</PROD_INST>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_INST_ACCT_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>400</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289158</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_INST_ACCT>\n" +
                "\t\t\t\t<ACCT_ITEM_TYPE_GROUP_ID>3003000</ACCT_ITEM_TYPE_GROUP_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT>50</PAYMENT_LIMIT>\n" +
                "\t\t\t\t<PROD_INST_ACCT_ID>:getProdInstAcctIdByExtId(8630000,,148890694,$-70001$)</PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT_TYPE>5XC</PAYMENT_LIMIT_TYPE>\n" +
                "\t\t\t\t<EXT_PROD_INST_ACCT_ID>148890694</EXT_PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ACCOUNT_ID/>\n" +
                "\t\t\t\t<ATTR CD=\"109000003\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000002\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000001\" VAL=\"80050019015\">收费周期</ATTR>\n" +
                "\t\t\t</PROD_INST_ACCT>\n" +
                "\t\t\t<PROD_INST_ACCT>\n" +
                "\t\t\t\t<ACCT_ITEM_TYPE_GROUP_ID>3003000</ACCT_ITEM_TYPE_GROUP_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT>50</PAYMENT_LIMIT>\n" +
                "\t\t\t\t<PROD_INST_ACCT_ID>:getProdInstAcctIdByExtId(8630000,,148890695,$-70001$)</PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT_TYPE>5XC</PAYMENT_LIMIT_TYPE>\n" +
                "\t\t\t\t<EXT_PROD_INST_ACCT_ID>148890695</EXT_PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ACCOUNT_ID/>\n" +
                "\t\t\t\t<ATTR CD=\"109000002\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000001\" VAL=\"80050019015\">收费周期</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000003\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t</PROD_INST_ACCT>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_INST_PARTY_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>500</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289158</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_INST_PARTY>\n" +
                "\t\t\t\t<LAN_ID>8630000</LAN_ID>\n" +
                "\t\t\t\t<RELATION_TYPE_CD>1</RELATION_TYPE_CD>\n" +
                "\t\t\t\t<PARTY_NAME>祁生海</PARTY_NAME>\n" +
                "\t\t\t\t<PARTY_PHONE_NBR>17797088770</PARTY_PHONE_NBR>\n" +
                "\t\t\t\t<PROD_INST_PARTY_ID>:getProdInstPartyId(8630000,,146748949,1,489491,$-70001$)</PROD_INST_PARTY_ID>\n" +
                "\t\t\t\t<PARTY_NBR>489491</PARTY_NBR>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ATTR CD=\"106000001\" VAL=\"\">EMAIL</ATTR>\n" +
                "\t\t\t</PROD_INST_PARTY>\n" +
                "\t\t\t<PROD_INST_PARTY>\n" +
                "\t\t\t\t<LAN_ID>8630000</LAN_ID>\n" +
                "\t\t\t\t<RELATION_TYPE_CD>6</RELATION_TYPE_CD>\n" +
                "\t\t\t\t<PARTY_NAME>才金花</PARTY_NAME>\n" +
                "\t\t\t\t<PARTY_PHONE_NBR>18997072118</PARTY_PHONE_NBR>\n" +
                "\t\t\t\t<PROD_INST_PARTY_ID>:getProdInstPartyId(8630000,,146748949,6,qhdkh002,$-70003$)</PROD_INST_PARTY_ID>\n" +
                "\t\t\t\t<PARTY_NBR>qhdkh002</PARTY_NBR>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ATTR CD=\"106000001\" VAL=\"\">EMAIL</ATTR>\n" +
                "\t\t\t</PROD_INST_PARTY>\n" +
                "\t\t\t<PROD_INST_PARTY>\n" +
                "\t\t\t\t<LAN_ID>8630000</LAN_ID>\n" +
                "\t\t\t\t<RELATION_TYPE_CD>7</RELATION_TYPE_CD>\n" +
                "\t\t\t\t<PARTY_NAME>才金花</PARTY_NAME>\n" +
                "\t\t\t\t<PARTY_PHONE_NBR>18997072118</PARTY_PHONE_NBR>\n" +
                "\t\t\t\t<PROD_INST_PARTY_ID>:getProdInstPartyId(8630000,,146748949,7,qhdkh002,$-70003$)</PROD_INST_PARTY_ID>\n" +
                "\t\t\t\t<PARTY_NBR>qhdkh002</PARTY_NBR>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ATTR CD=\"106000001\" VAL=\"\">EMAIL</ATTR>\n" +
                "\t\t\t</PROD_INST_PARTY>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"ADD_ORDER_ITEM_GROUP_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>600</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_ITEM_GROUP>\n" +
                "\t\t\t\t<LAN_ID>8630100</LAN_ID>\n" +
                "\t\t\t\t<REASON/>\n" +
                "\t\t\t\t<ACCEPT_DATE>20180503102650</ACCEPT_DATE>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289159</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t\t<CUST_ORDER_ID>$100000$</CUST_ORDER_ID>\n" +
                "\t\t\t\t<SERVICE_OFFER_ID>4010100000</SERVICE_OFFER_ID>\n" +
                "\t\t\t\t<SERVICE_OFFER_NAME>新装</SERVICE_OFFER_NAME>\n" +
                "\t\t\t\t<OLD_ORDER_ITEM_GROUP_ID/>\n" +
                "\t\t\t\t<ATTR CD=\"141000001\" VAL=\"F\">提供测试报告</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000006\" VAL=\"2018-05-04\">要求完成日期</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000007\" VAL=\"80050032001\">优先级别</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000008\" VAL=\"\">加急类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000009\" VAL=\"\">割接截址时间</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000012\" VAL=\"T\">是否等待起租</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000011\" VAL=\"\">业务类型发生端</ATTR>\n" +
                "\t\t\t</ORDER_ITEM_GROUP>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_INST_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>700</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289159</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_INST>\n" +
                "\t\t\t\t<ACC_NBR/>\n" +
                "\t\t\t\t<LAN_ID>8630100</LAN_ID>\n" +
                "\t\t\t\t<EXT_PROD_ID>93410727</EXT_PROD_ID>\n" +
                "\t\t\t\t<ACC_PROD_INST_ID>:getProdInstIdByExtId(8630100,,146748950,$-30001$)</ACC_PROD_INST_ID>\n" +
                "\t\t\t\t<EXT_PROD_INST_ID>146748950</EXT_PROD_INST_ID>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630100,,146748950,$-30001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<PRODUCT_NBR>00103022000000000001</PRODUCT_NBR>\n" +
                "\t\t\t\t<USE_CUST_ID/>\n" +
                "\t\t\t\t<OFFER_NBR>2210200301100007</OFFER_NBR>\n" +
                "\t\t\t\t<OWNER_CUST_ID>:getCustIdByExtId(8630100,,1000018854710000,$-1$)</OWNER_CUST_ID>\n" +
                "\t\t\t\t<ADDRESS_DESC>青海省西宁市城西区西关大街81号</ADDRESS_DESC>\n" +
                "\t\t\t\t<BEGIN_RENT_TIME/>\n" +
                "\t\t\t\t<ATTR CD=\"100000105\" VAL=\"oper0016\">运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000106\" VAL=\"80050062024\">端口类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000560\" VAL=\"\">端口速率</ATTR>\n" +
                "\t\t\t</PROD_INST>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_INST_ACCT_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>800</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289159</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_INST_ACCT>\n" +
                "\t\t\t\t<ACCT_ITEM_TYPE_GROUP_ID>3001000</ACCT_ITEM_TYPE_GROUP_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT>100</PAYMENT_LIMIT>\n" +
                "\t\t\t\t<PROD_INST_ACCT_ID>:getProdInstAcctIdByExtId(8630100,,148890696,$-70001$)</PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT_TYPE>5XC</PAYMENT_LIMIT_TYPE>\n" +
                "\t\t\t\t<EXT_PROD_INST_ACCT_ID>148890696</EXT_PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630100,,146748950,$-30001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ACCOUNT_ID/>\n" +
                "\t\t\t\t<ATTR CD=\"109000002\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000001\" VAL=\"80050019015\">收费周期</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000003\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t</PROD_INST_ACCT>\n" +
                "\t\t\t<PROD_INST_ACCT>\n" +
                "\t\t\t\t<ACCT_ITEM_TYPE_GROUP_ID>3009000</ACCT_ITEM_TYPE_GROUP_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT>100</PAYMENT_LIMIT>\n" +
                "\t\t\t\t<PROD_INST_ACCT_ID>:getProdInstAcctIdByExtId(8630100,,148890697,$-70001$)</PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT_TYPE>5XC</PAYMENT_LIMIT_TYPE>\n" +
                "\t\t\t\t<EXT_PROD_INST_ACCT_ID>148890697</EXT_PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630100,,146748950,$-30001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ACCOUNT_ID/>\n" +
                "\t\t\t\t<ATTR CD=\"109000002\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000001\" VAL=\"80050019015\">收费周期</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000003\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t</PROD_INST_ACCT>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_INST_PARTY_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>900</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289159</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_INST_PARTY>\n" +
                "\t\t\t\t<LAN_ID>8630100</LAN_ID>\n" +
                "\t\t\t\t<RELATION_TYPE_CD>5</RELATION_TYPE_CD>\n" +
                "\t\t\t\t<PARTY_NAME>祁生海</PARTY_NAME>\n" +
                "\t\t\t\t<PARTY_PHONE_NBR> 17797088770</PARTY_PHONE_NBR>\n" +
                "\t\t\t\t<PROD_INST_PARTY_ID>:getProdInstPartyId(8630100,,146748950,5,489505,$-70003$)</PROD_INST_PARTY_ID>\n" +
                "\t\t\t\t<PARTY_NBR>489505</PARTY_NBR>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630100,,146748950,$-30001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ATTR CD=\"106000001\" VAL=\"\">EMAIL</ATTR>\n" +
                "\t\t\t</PROD_INST_PARTY>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_ACCT_ITEM_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>1000</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289159</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2008000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2009000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2002000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2006000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2005000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2012000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"ADD_ORDER_ITEM_GROUP_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>1100</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_ITEM_GROUP>\n" +
                "\t\t\t\t<LAN_ID>8110105</LAN_ID>\n" +
                "\t\t\t\t<REASON/>\n" +
                "\t\t\t\t<ACCEPT_DATE>20180503102650</ACCEPT_DATE>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289160</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t\t<CUST_ORDER_ID>$100000$</CUST_ORDER_ID>\n" +
                "\t\t\t\t<SERVICE_OFFER_ID>4010100000</SERVICE_OFFER_ID>\n" +
                "\t\t\t\t<SERVICE_OFFER_NAME>新装</SERVICE_OFFER_NAME>\n" +
                "\t\t\t\t<OLD_ORDER_ITEM_GROUP_ID/>\n" +
                "\t\t\t\t<ATTR CD=\"141000001\" VAL=\"F\">提供测试报告</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000006\" VAL=\"2018-05-04\">要求完成日期</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000007\" VAL=\"80050032001\">优先级别</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000008\" VAL=\"\">加急类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000009\" VAL=\"\">割接截址时间</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000012\" VAL=\"T\">是否等待起租</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"141000011\" VAL=\"\">业务类型发生端</ATTR>\n" +
                "\t\t\t</ORDER_ITEM_GROUP>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_INST_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>1200</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289160</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_INST>\n" +
                "\t\t\t\t<ACC_NBR/>\n" +
                "\t\t\t\t<LAN_ID>8110105</LAN_ID>\n" +
                "\t\t\t\t<EXT_PROD_ID>93410727</EXT_PROD_ID>\n" +
                "\t\t\t\t<ACC_PROD_INST_ID>:getProdInstIdByExtId(8110105,,146748951,$-20001$)</ACC_PROD_INST_ID>\n" +
                "\t\t\t\t<EXT_PROD_INST_ID>146748951</EXT_PROD_INST_ID>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8110105,,146748951,$-20001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<PRODUCT_NBR>00103022000000000001</PRODUCT_NBR>\n" +
                "\t\t\t\t<USE_CUST_ID/>\n" +
                "\t\t\t\t<OFFER_NBR>2210200301100007</OFFER_NBR>\n" +
                "\t\t\t\t<OWNER_CUST_ID>:getCustIdByExtId(8630100,,1000018854710000,$-1$)</OWNER_CUST_ID>\n" +
                "\t\t\t\t<ADDRESS_DESC>北京市朝阳区北辰西路12号数字北京大厦C座4层IPTV播控中心</ADDRESS_DESC>\n" +
                "\t\t\t\t<BEGIN_RENT_TIME/>\n" +
                "\t\t\t\t<ATTR CD=\"100000106\" VAL=\"80050062024\">端口类型 </ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000105\" VAL=\"oper0016\">运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"100000560\" VAL=\"\">端口速率</ATTR>\n" +
                "\t\t\t</PROD_INST>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_INST_ACCT_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>1300</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289160</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_INST_ACCT>\n" +
                "\t\t\t\t<ACCT_ITEM_TYPE_GROUP_ID>3001000</ACCT_ITEM_TYPE_GROUP_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT>100</PAYMENT_LIMIT>\n" +
                "\t\t\t\t<PROD_INST_ACCT_ID>:getProdInstAcctIdByExtId(8110105,,148890698,$-70001$)</PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT_TYPE>5XC</PAYMENT_LIMIT_TYPE>\n" +
                "\t\t\t\t<EXT_PROD_INST_ACCT_ID>148890698</EXT_PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8110105,,146748951,$-20001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ACCOUNT_ID/>\n" +
                "\t\t\t\t<ATTR CD=\"109000003\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000002\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000001\" VAL=\"80050019015\">收费周期</ATTR>\n" +
                "\t\t\t</PROD_INST_ACCT>\n" +
                "\t\t\t<PROD_INST_ACCT>\n" +
                "\t\t\t\t<ACCT_ITEM_TYPE_GROUP_ID>3009000</ACCT_ITEM_TYPE_GROUP_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT>100</PAYMENT_LIMIT>\n" +
                "\t\t\t\t<PROD_INST_ACCT_ID>:getProdInstAcctIdByExtId(8110105,,148890699,$-70001$)</PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PAYMENT_LIMIT_TYPE>5XC</PAYMENT_LIMIT_TYPE>\n" +
                "\t\t\t\t<EXT_PROD_INST_ACCT_ID>148890699</EXT_PROD_INST_ACCT_ID>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8110105,,146748951,$-20001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ACCOUNT_ID/>\n" +
                "\t\t\t\t<ATTR CD=\"109000003\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000002\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"109000001\" VAL=\"80050019015\">收费周期</ATTR>\n" +
                "\t\t\t</PROD_INST_ACCT>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_INST_PARTY_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>1400</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289160</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_INST_PARTY>\n" +
                "\t\t\t\t<LAN_ID>8110105</LAN_ID>\n" +
                "\t\t\t\t<RELATION_TYPE_CD>5</RELATION_TYPE_CD>\n" +
                "\t\t\t\t<PARTY_NAME>付英武</PARTY_NAME>\n" +
                "\t\t\t\t<PARTY_PHONE_NBR> 13811985150</PARTY_PHONE_NBR>\n" +
                "\t\t\t\t<PROD_INST_PARTY_ID>:getProdInstPartyId(8110105,,146748951,5,489515,$-70003$)</PROD_INST_PARTY_ID>\n" +
                "\t\t\t\t<PARTY_NBR>489515</PARTY_NBR>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8110105,,146748951,$-20001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<ATTR CD=\"106000001\" VAL=\"\">EMAIL</ATTR>\n" +
                "\t\t\t</PROD_INST_PARTY>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_ACCT_ITEM_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>1500</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289160</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2008000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2009000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2002000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2006000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2005000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t\t<ACCT_ITEM>\n" +
                "\t\t\t\t<ACCT_ITME_TYPE_ID>2012000</ACCT_ITME_TYPE_ID>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<PAYMENT_METHOD_CD>160000</PAYMENT_METHOD_CD>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<ATTR CD=\"1420000108\" VAL=\"8630000\">收费城市</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000106\" VAL=\"F\">一口价标识</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000107\" VAL=\"oper0016\">收费运营商</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1420000109\" VAL=\"\">收费形式</ATTR>\n" +
                "\t\t\t</ACCT_ITEM>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_INST_REL_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>1600</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289158</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_INST_REL>\n" +
                "\t\t\t\t<RELATED_PROD_INST_ID>:getProdInstIdByExtId(8630100,,146748950,$-30001$)</RELATED_PROD_INST_ID>\n" +
                "\t\t\t\t<RELA_PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</RELA_PROD_INST_ID>\n" +
                "\t\t\t\t<RELATION_TYPE_CD>100000</RELATION_TYPE_CD>\n" +
                "\t\t\t\t<ROLE_NAME/>\n" +
                "\t\t\t\t<ROLE_CD>1400</ROLE_CD>\n" +
                "\t\t\t</PROD_INST_REL>\n" +
                "\t\t\t<PROD_INST_REL>\n" +
                "\t\t\t\t<RELATED_PROD_INST_ID>:getProdInstIdByExtId(8110105,,146748951,$-20001$)</RELATED_PROD_INST_ID>\n" +
                "\t\t\t\t<RELA_PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</RELA_PROD_INST_ID>\n" +
                "\t\t\t\t<RELATION_TYPE_CD>100000</RELATION_TYPE_CD>\n" +
                "\t\t\t\t<ROLE_NAME/>\n" +
                "\t\t\t\t<ROLE_CD>1500</ROLE_CD>\n" +
                "\t\t\t</PROD_INST_REL>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"ADD_ORDER_ITEM_GROUP_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>1700</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_ITEM_GROUP>\n" +
                "\t\t\t\t<LAN_ID>8630000</LAN_ID>\n" +
                "\t\t\t\t<REASON/>\n" +
                "\t\t\t\t<ACCEPT_DATE>20180503102650</ACCEPT_DATE>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289161</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t\t<CUST_ORDER_ID>$100000$</CUST_ORDER_ID>\n" +
                "\t\t\t\t<SERVICE_OFFER_ID>3010100000</SERVICE_OFFER_ID>\n" +
                "\t\t\t\t<SERVICE_OFFER_NAME>订购</SERVICE_OFFER_NAME>\n" +
                "\t\t\t\t<OLD_ORDER_ITEM_GROUP_ID/>\n" +
                "\t\t\t</ORDER_ITEM_GROUP>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_OFFER_INST_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>1800</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289161</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_OFFER_INST>\n" +
                "\t\t\t\t<LAN_ID>8630000</LAN_ID>\n" +
                "\t\t\t\t<EXT_PROD_OFFER_INST_ID>272121238</EXT_PROD_OFFER_INST_ID>\n" +
                "\t\t\t\t<CUST_ID>:getCustIdByExtId(8630100,,1000018854710000,$-1$)</CUST_ID>\n" +
                "\t\t\t\t<EXT_PROD_OFFER_ID>134037637</EXT_PROD_OFFER_ID>\n" +
                "\t\t\t\t<PROD_OFFER_INST_ID>:getProdOfferInstIdByExtId(8630000,,272121238,$-50001$)</PROD_OFFER_INST_ID>\n" +
                "\t\t\t\t<OFFER_NBR>2210200301100007</OFFER_NBR>\n" +
                "\t\t\t</PROD_OFFER_INST>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_OFFER_PROD_INST_REL_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>1900</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289161</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<OFFER_PROD_INST_REL>\n" +
                "\t\t\t\t<PROD_OFFER_INST_ID>:getProdOfferInstIdByExtId(8630000,,272121238,$-50001$)</PROD_OFFER_INST_ID>\n" +
                "\t\t\t\t<ROLE_CD>2100</ROLE_CD>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</PROD_INST_ID>\n" +
                "\t\t\t</OFFER_PROD_INST_REL>\n" +
                "\t\t\t<OFFER_PROD_INST_REL>\n" +
                "\t\t\t\t<PROD_OFFER_INST_ID>:getProdOfferInstIdByExtId(8630000,,272121238,$-50001$)</PROD_OFFER_INST_ID>\n" +
                "\t\t\t\t<ROLE_CD>2101</ROLE_CD>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630100,,146748950,$-30001$)</PROD_INST_ID>\n" +
                "\t\t\t</OFFER_PROD_INST_REL>\n" +
                "\t\t\t<OFFER_PROD_INST_REL>\n" +
                "\t\t\t\t<PROD_OFFER_INST_ID>:getProdOfferInstIdByExtId(8630000,,272121238,$-50001$)</PROD_OFFER_INST_ID>\n" +
                "\t\t\t\t<ROLE_CD>2102</ROLE_CD>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8110105,,146748951,$-20001$)</PROD_INST_ID>\n" +
                "\t\t\t</OFFER_PROD_INST_REL>\n" +
                "\t\t</SOO>\n" +
                "\t\t<SOO type=\"SAVE_PROD_OFFER_FEE_INFO_REQ_TYPE\">\n" +
                "\t\t\t<PUB_REQ>\n" +
                "\t\t\t\t<SOO_ID>2000</SOO_ID>\n" +
                "\t\t\t</PUB_REQ>\n" +
                "\t\t\t<ORDER_REQ>\n" +
                "\t\t\t\t<ORDER_ITEM_GROUP_ID>453289161</ORDER_ITEM_GROUP_ID>\n" +
                "\t\t\t</ORDER_REQ>\n" +
                "\t\t\t<PROD_OFFER_FEE_INFO>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<FEE_TYPE>3003000</FEE_TYPE>\n" +
                "\t\t\t\t<AMOUNT>208330</AMOUNT>\n" +
                "\t\t\t\t<PROD_OFFER_INST_ID>:getProdOfferInstIdByExtId(8630000,,272121238,$-50001$)</PROD_OFFER_INST_ID>\n" +
                "\t\t\t\t<REAL_AMOUNT>208330</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630000,,146748949,$-40001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<PROD_OFFER_FEE_ID>:getProdOfferFeeIdByExtId(8630000,,272121238,146748949,$-50002$)</PROD_OFFER_FEE_ID>\n" +
                "\t\t\t\t<ATTR CD=\"1100000007\" VAL=\"\">结出单位</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000006\" VAL=\"F\">一口价标志</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000009\" VAL=\"\">结出金额币种</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000008\" VAL=\"\">结出金额</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000005\" VAL=\"\">返点比例</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000002\" VAL=\"\">返点客户</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000001\" VAL=\"\">返点类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000004\" VAL=\"0\">SLA加收比例</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000003\" VAL=\"\">返点单位</ATTR>\n" +
                "\t\t\t</PROD_OFFER_FEE_INFO>\n" +
                "\t\t\t<PROD_OFFER_FEE_INFO>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<FEE_TYPE>3001000</FEE_TYPE>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<PROD_OFFER_INST_ID>:getProdOfferInstIdByExtId(8630000,,272121238,$-50001$)</PROD_OFFER_INST_ID>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630100,,146748950,$-30001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<PROD_OFFER_FEE_ID>:getProdOfferFeeIdByExtId(8630000,,272121238,146748949,$-50002$)</PROD_OFFER_FEE_ID>\n" +
                "\t\t\t\t<ATTR CD=\"1100000007\" VAL=\"\">结出单位</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000006\" VAL=\"F\">一口价标志</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000009\" VAL=\"\">结出金额币种</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000008\" VAL=\"\">结出金额</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000005\" VAL=\"\">返点比例</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000002\" VAL=\"\">返点客户</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000001\" VAL=\"\">返点类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000004\" VAL=\"0\">SLA加收比例</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000003\" VAL=\"\">返点单位</ATTR>\n" +
                "\t\t\t</PROD_OFFER_FEE_INFO>\n" +
                "\t\t\t<PROD_OFFER_FEE_INFO>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<FEE_TYPE>3009000</FEE_TYPE>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<PROD_OFFER_INST_ID>:getProdOfferInstIdByExtId(8630000,,272121238,$-50001$)</PROD_OFFER_INST_ID>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8630100,,146748950,$-30001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<PROD_OFFER_FEE_ID>:getProdOfferFeeIdByExtId(8630000,,272121238,146748949,$-50002$)</PROD_OFFER_FEE_ID>\n" +
                "\t\t\t\t<ATTR CD=\"1100000007\" VAL=\"\">结出单位</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000006\" VAL=\"F\">一口价标志</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000009\" VAL=\"\">结出金额币种</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000008\" VAL=\"\">结出金额</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000005\" VAL=\"\">返点比例</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000002\" VAL=\"\">返点客户</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000001\" VAL=\"\">返点类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000004\" VAL=\"0\">SLA加收比例</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000003\" VAL=\"\">返点单位</ATTR>\n" +
                "\t\t\t</PROD_OFFER_FEE_INFO>\n" +
                "\t\t\t<PROD_OFFER_FEE_INFO>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<FEE_TYPE>3001000</FEE_TYPE>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<PROD_OFFER_INST_ID>:getProdOfferInstIdByExtId(8630000,,272121238,$-50001$)</PROD_OFFER_INST_ID>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8110105,,146748951,$-20001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<PROD_OFFER_FEE_ID>:getProdOfferFeeIdByExtId(8630000,,272121238,146748949,$-50002$)</PROD_OFFER_FEE_ID>\n" +
                "\t\t\t\t<ATTR CD=\"1100000007\" VAL=\"\">结出单位</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000006\" VAL=\"F\">一口价标志</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000009\" VAL=\"\">结出金额币种</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000008\" VAL=\"\">结出金额</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000005\" VAL=\"\">返点比例</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000002\" VAL=\"\">返点客户</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000001\" VAL=\"\">返点类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000004\" VAL=\"0\">SLA加收比例</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000003\" VAL=\"\">返点单位</ATTR>\n" +
                "\t\t\t</PROD_OFFER_FEE_INFO>\n" +
                "\t\t\t<PROD_OFFER_FEE_INFO>\n" +
                "\t\t\t\t<CURRENCY>11</CURRENCY>\n" +
                "\t\t\t\t<FEE_TYPE>3009000</FEE_TYPE>\n" +
                "\t\t\t\t<AMOUNT>0</AMOUNT>\n" +
                "\t\t\t\t<PROD_OFFER_INST_ID>:getProdOfferInstIdByExtId(8630000,,272121238,$-50001$)</PROD_OFFER_INST_ID>\n" +
                "\t\t\t\t<REAL_AMOUNT>0</REAL_AMOUNT>\n" +
                "\t\t\t\t<RATIO_METHDOD>1</RATIO_METHDOD>\n" +
                "\t\t\t\t<RATIO>0</RATIO>\n" +
                "\t\t\t\t<PROD_INST_ID>:getProdInstIdByExtId(8110105,,146748951,$-20001$)</PROD_INST_ID>\n" +
                "\t\t\t\t<PROD_OFFER_FEE_ID>:getProdOfferFeeIdByExtId(8630000,,272121238,146748949,$-50002$)</PROD_OFFER_FEE_ID>\n" +
                "\t\t\t\t<ATTR CD=\"1100000007\" VAL=\"\">结出单位</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000006\" VAL=\"F\">一口价标志</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000009\" VAL=\"\">结出金额币种</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000008\" VAL=\"\">结出金额</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000005\" VAL=\"\">返点比例</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000002\" VAL=\"\">返点客户</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000001\" VAL=\"\">返点类型</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000004\" VAL=\"0\">SLA加收比例</ATTR>\n" +
                "\t\t\t\t<ATTR CD=\"1100000003\" VAL=\"\">返点单位</ATTR>\n" +
                "\t\t\t</PROD_OFFER_FEE_INFO>\n" +
                "\t\t</SOO>\n" +
                "\t</SvcCont>\n" +
                "</ContractRoot>\n";
        String logic = "<BusCode>BUS35011</BusCode> ||{<BusCode>BUS50001</BusCode> && {<SERVICE_OFFER_ID>3010100002</SERVICE_OFFER_ID>|| <SERVICE_OFFER_ID>4040100043</SERVICE_OFFER_ID>|| <SERVICE_OFFER_ID>4041000001</SERVICE_OFFER_ID>|| <SERVICE_OFFER_ID>4040100027</SERVICE_OFFER_ID>|| <SERVICE_OFFER_ID>3020100000</SERVICE_OFFER_ID>|| <SERVICE_OFFER_ID>4040100005</SERVICE_OFFER_ID>|| <SERVICE_OFFER_ID>4020100000</SERVICE_OFFER_ID>|| <SERVICE_OFFER_ID>5010200000</SERVICE_OFFER_ID>|| <SERVICE_OFFER_ID>5010100001</SERVICE_OFFER_ID>}}";
        System.out.println(logicCalculus(xml,logic));
    }


    /**
     * 对逻辑字符串进行分析和运算
     *
     * @param sooXml
     * @param keyIdentifyLogicInfo
     * @return
     * @throws
     */
    public static boolean logicCalculus(String sooXml, String keyIdentifyLogicInfo)  {
        final String PART_STRING_DBL_AMPERSAND = "&&"; //表示字符串：&&
        final String PART_STRING_DBL_VERTICAL_BAR = "||"; //表示字符串：||
        final String PART_STRING_L_BIG_BRACKET = "{"; //表示字符串：{
        final String PART_STRING_R_BIG_BRACKET = "}"; //表示字符串：}
        final String PRVT_TRUE_STR = "1";
        final String PRVT_FALSE_STR = "0";
        final String PRVT_OR_BLANK_STR = " || ";
        String keyIdentifyLogicStr = keyIdentifyLogicInfo; //动态变化的关键判断逻辑信息
        while (keyIdentifyLogicStr.contains(PART_STRING_L_BIG_BRACKET)
                && keyIdentifyLogicStr.contains(PART_STRING_R_BIG_BRACKET)
                || keyIdentifyLogicStr.contains(PART_STRING_DBL_AMPERSAND)
                || keyIdentifyLogicStr.contains(PART_STRING_DBL_VERTICAL_BAR)
                || !PRVT_TRUE_STR.equals(keyIdentifyLogicStr) && !PRVT_FALSE_STR.equals(keyIdentifyLogicStr)) {
            int leftBigBracketPosition = 0; //最内左大括号的位置
            int rightBigBracketPosition = 0; //最内右大括号的位置
            String curProcessStr = keyIdentifyLogicStr; //当前要处理的字符串片断
            if (keyIdentifyLogicStr.contains(PART_STRING_L_BIG_BRACKET)
                    && keyIdentifyLogicStr.contains(PART_STRING_R_BIG_BRACKET)) {
                leftBigBracketPosition = keyIdentifyLogicStr.lastIndexOf(PART_STRING_L_BIG_BRACKET);
                rightBigBracketPosition = keyIdentifyLogicStr
                        .indexOf(PART_STRING_R_BIG_BRACKET, leftBigBracketPosition);
                curProcessStr = keyIdentifyLogicStr.substring(leftBigBracketPosition + 1, rightBigBracketPosition);
            }
            String rsltLogicStr = "";
            if (curProcessStr.contains(PART_STRING_DBL_VERTICAL_BAR)) {
                String[] orLogicStrs = curProcessStr.split("\\|\\|"); //“或”逻辑信息
                int orLogicStrNum = orLogicStrs.length;
                String curOrLogicStr = "";
                String curAndLogicStr = "";
                String orLogicStr = "";
                String andLogicStr = "";
                boolean hasTrue = false;
                for (int i = 0; i < orLogicStrNum; i++) {
                    curOrLogicStr = orLogicStrs[i];
                    if (null != curOrLogicStr) {
                        curOrLogicStr = curOrLogicStr.trim();
                    }
                    if (null == curOrLogicStr || "".equals(curOrLogicStr)) {
                        System.out.println("字段配置错误，两个||之间不能没有信息");
                        return false;
                    }
                    orLogicStr = PRVT_FALSE_STR;
                    if (curOrLogicStr.contains(PART_STRING_DBL_AMPERSAND)) {
                        String[] andLogicStrs = curOrLogicStr.split(PART_STRING_DBL_AMPERSAND); //“与”逻辑信息
                        int andLogicStrNum = andLogicStrs.length;
                        andLogicStr = PRVT_TRUE_STR;
                        for (int j = 0; j < andLogicStrNum; j++) {
                            curAndLogicStr = andLogicStrs[j];
                            if (null != curAndLogicStr) {
                                curAndLogicStr = curAndLogicStr.trim();
                            }
                            if (null == curAndLogicStr || "".equals(curAndLogicStr)) {
                                System.out.println("字段配置错误，两个&&之间不能没有信息");
                                return false;
                            }
                            if (!sooXml.contains(curAndLogicStr) || PRVT_FALSE_STR.equals(curAndLogicStr)) {
                                andLogicStr = PRVT_FALSE_STR;
                                break;
                            }
                        }
                        if ("".equals(rsltLogicStr)) {
                            rsltLogicStr = andLogicStr;
                        } else {
                            rsltLogicStr += PRVT_OR_BLANK_STR + andLogicStr;
                        }
                    } else {
                        if (sooXml.contains(curOrLogicStr) && !"0".equals(curOrLogicStr) || PRVT_TRUE_STR.equals(curOrLogicStr)) {
                            if (!hasTrue) {
                                orLogicStr = PRVT_TRUE_STR;
                                if ("".equals(rsltLogicStr)) {
                                    rsltLogicStr = orLogicStr;
                                } else {
                                    rsltLogicStr += PRVT_OR_BLANK_STR + orLogicStr;
                                }
                                hasTrue = true;
                            }
                        }
                    }
                }
                if (!hasTrue) {
                    if ("".equals(rsltLogicStr)) {
                        rsltLogicStr = orLogicStr;
                    } else {
                        rsltLogicStr += PRVT_OR_BLANK_STR + orLogicStr;
                    }
                }
            } else if (curProcessStr.contains(PART_STRING_DBL_AMPERSAND)) {
                String[] andLogicStrs = curProcessStr.split(PART_STRING_DBL_AMPERSAND); //“与”逻辑信息
                int andLogicStrNum = andLogicStrs.length;
                String curAndLogicStr = "";
                String andLogicStr = PRVT_TRUE_STR;
                for (int j = 0; j < andLogicStrNum; j++) {
                    curAndLogicStr = andLogicStrs[j];
                    if (null != curAndLogicStr) {
                        curAndLogicStr = curAndLogicStr.trim();
                    }
                    if (null == curAndLogicStr || "".equals(curAndLogicStr)) {
                        System.out.println("字段配置错误，两个&&之间不能没有信息");
                        return false;
                    }
                    if (!sooXml.contains(curAndLogicStr) || PRVT_FALSE_STR.equals(curAndLogicStr)) {
                        andLogicStr = PRVT_FALSE_STR;
                        break;
                    }
                }
                rsltLogicStr = andLogicStr;
            } else {
                String andLogicStr = PRVT_TRUE_STR;
                if (!sooXml.contains(curProcessStr) || PRVT_FALSE_STR.equals(curProcessStr)) {
                    andLogicStr = PRVT_FALSE_STR;
                }
                rsltLogicStr = andLogicStr;
            }
            System.out.println("替换前判断逻辑字符串（keyIdentifyLogicStr） ：{}"+ keyIdentifyLogicStr);
            System.out.println("当前用于替换的逻辑字符串（rsltLogicStr） ：{}"+rsltLogicStr);
            if (0 != rightBigBracketPosition) {
                keyIdentifyLogicStr = keyIdentifyLogicStr.substring(0, leftBigBracketPosition) + rsltLogicStr
                        + keyIdentifyLogicStr.substring(rightBigBracketPosition + 1);
            } else {
                keyIdentifyLogicStr = rsltLogicStr;
            }
            System.out.println("替换后判断逻辑字符串（keyIdentifyLogicStr） ：{}"+keyIdentifyLogicStr);
        }
        if (PRVT_TRUE_STR.equals(keyIdentifyLogicStr)) {
            return true;
        }
        return false;
    }
}
