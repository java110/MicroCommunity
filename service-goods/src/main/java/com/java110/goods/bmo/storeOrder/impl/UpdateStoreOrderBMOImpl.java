package com.java110.goods.bmo.storeOrder.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.groupBuy.GroupBuyDto;
import com.java110.dto.groupBuyProduct.GroupBuyProductDto;
import com.java110.dto.productSpecValue.ProductSpecValueDto;
import com.java110.dto.storeCart.StoreCartDto;
import com.java110.dto.storeOrder.StoreOrderDto;
import com.java110.dto.storeOrderCart.StoreOrderCartDto;
import com.java110.goods.bmo.storeOrder.IUpdateStoreOrderBMO;
import com.java110.intf.IStoreOrderCartInnerServiceSMO;
import com.java110.intf.IStoreOrderInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.goods.IGroupBuyInnerServiceSMO;
import com.java110.intf.goods.IGroupBuyProductInnerServiceSMO;
import com.java110.intf.goods.IProductSpecValueInnerServiceSMO;
import com.java110.intf.goods.IStoreCartInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.groupBuy.GroupBuyPo;
import com.java110.po.storeCart.StoreCartPo;
import com.java110.po.storeOrder.StoreOrderPo;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("updateStoreOrderBMOImpl")
public class UpdateStoreOrderBMOImpl implements IUpdateStoreOrderBMO {

    @Autowired
    private IStoreOrderInnerServiceSMO storeOrderInnerServiceSMOImpl;

    @Autowired
    private IStoreOrderCartInnerServiceSMO storeOrderCartInnerServiceSMOImpl;

    @Autowired
    private IStoreCartInnerServiceSMO storeCartInnerServiceSMOImpl;

    @Autowired
    private IGroupBuyInnerServiceSMO groupBuyInnerServiceSMOImpl;

    @Autowired
    private IGroupBuyProductInnerServiceSMO groupBuyProductInnerServiceSMOImpl;

    @Autowired
    private IProductSpecValueInnerServiceSMO productSpecValueInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    /**
     * @param storeOrderPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(StoreOrderPo storeOrderPo) {

        StoreOrderDto storeOrderDto = new StoreOrderDto();
        storeOrderDto.setOrderId(storeOrderPo.getOrderId());
        storeOrderDto.setState(StoreOrderDto.STATE_WAIT_PAY);

        List<StoreOrderDto> storeOrderDtos = storeOrderInnerServiceSMOImpl.queryStoreOrders(storeOrderDto);

        Assert.listOnlyOne(storeOrderDtos, "当前未找到相应订单");

        storeOrderPo.setState(StoreOrderDto.STATE_FINISH_PAY);
        int flag = storeOrderInnerServiceSMOImpl.updateStoreOrder(storeOrderPo);
        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        StoreOrderCartDto storeOrderCartDto = new StoreOrderCartDto();
        storeOrderCartDto.setOrderId(storeOrderPo.getOrderId());
        storeOrderCartDto.setState(StoreOrderCartDto.STATE_WAIT_BUY);
        List<StoreOrderCartDto> storeOrderCartDtos = storeOrderCartInnerServiceSMOImpl.queryStoreOrderCarts(storeOrderCartDto);

        if (storeOrderCartDtos == null || storeOrderCartDtos.size() < 1) {
            throw new IllegalArgumentException("未找到需要购买商品");
        }

        StoreOrderCartPo storeOrderCartPo = new StoreOrderCartPo();
        storeOrderCartPo.setOrderId(storeOrderPo.getOrderId());
        storeOrderCartPo.setState(StoreOrderCartDto.STATE_WAIT_SEND);
        flag = storeOrderCartInnerServiceSMOImpl.updateStoreOrderCart(storeOrderCartPo);
        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存购物车失败");
        }


        for (StoreOrderCartDto tmpStoreOrderCartDto : storeOrderCartDtos) {
            doDealStoreCart(tmpStoreOrderCartDto);
            doDealGroupBuyProduct(tmpStoreOrderCartDto, storeOrderDtos.get(0));
            doDealStoreCartFee(tmpStoreOrderCartDto, storeOrderDtos.get(0));
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");


    }

    /**
     * 购物车收费
     *
     * @param tmpStoreOrderCartDto
     * @param storeOrderDto
     */
    private void doDealStoreCartFee(StoreOrderCartDto tmpStoreOrderCartDto, StoreOrderDto storeOrderDto) {

        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        payFeeDetailPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeeDetailPo.setPrimeRate("1.0");
        payFeeDetailPo.setCycles("1");
        payFeeDetailPo.setReceivableAmount(tmpStoreOrderCartDto.getPayPrice() + "");
        payFeeDetailPo.setReceivedAmount(tmpStoreOrderCartDto.getPayPrice() + "");
        payFeeDetailPo.setCommunityId("-1");

        int flag = feeDetailInnerServiceSMOImpl.saveFeeDetail(payFeeDetailPo);
        if (flag < 1) {
            throw new IllegalArgumentException("添加费用明细失败");
        }


        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(payFeeDetailPo.getFeeId());
        payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setIncomeObjId(tmpStoreOrderCartDto.getStoreId());
        payFeePo.setCommunityId("-1");
        payFeePo.setUserId("-1");
        payFeePo.setPayerObjId(tmpStoreOrderCartDto.getPersonId());
        payFeePo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeePo.setFeeTypeCd(FeeConfigDto.FEE_TYPE_CD_SYSTEM);
        payFeePo.setAmount(tmpStoreOrderCartDto.getPayPrice() + "");
        payFeePo.setState(FeeDto.STATE_FINISH);
        payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_RENTING);
        payFeePo.setConfigId(FeeConfigDto.CONFIG_ID_GOODS);
        List<PayFeePo> payFeePos = new ArrayList<>();
        payFeePos.add(payFeePo);
        flag = feeInnerServiceSMOImpl.saveFee(payFeePos);

        if (flag < 1) {
            throw new IllegalArgumentException("添加费用明细失败");
        }
    }

    private void doDealGroupBuyProduct(StoreOrderCartDto tmpStoreOrderCartDto, StoreOrderDto storeOrderDto) {

        GroupBuyProductDto groupBuyProductDto = new GroupBuyProductDto();
        groupBuyProductDto.setProductId(tmpStoreOrderCartDto.getProductId());
        groupBuyProductDto.setStoreId(tmpStoreOrderCartDto.getStoreId());
        List<GroupBuyProductDto> groupBuyProductDtos = groupBuyProductInnerServiceSMOImpl.queryGroupBuyProducts(groupBuyProductDto);

        if (groupBuyProductDtos == null || groupBuyProductDtos.size() < 1) {
            return;
        }

        ProductSpecValueDto productSpecValueDto = new ProductSpecValueDto();
        productSpecValueDto.setProductId(tmpStoreOrderCartDto.getProductId());
        productSpecValueDto.setValueId(tmpStoreOrderCartDto.getValueId());
        productSpecValueDto.setStoreId(tmpStoreOrderCartDto.getStoreId());
        List<ProductSpecValueDto> productSpecValueDtos = productSpecValueInnerServiceSMOImpl.queryProductSpecValues(productSpecValueDto);
        if (productSpecValueDtos == null || productSpecValueDtos.size() < 1) {
            throw new IllegalArgumentException("未找到需要购买商品规格");
        }
        GroupBuyPo groupBuyPo = new GroupBuyPo();
        groupBuyPo.setAmount(tmpStoreOrderCartDto.getPayPrice());
        groupBuyPo.setBatchId(groupBuyProductDtos.get(0).getBatchId());
        groupBuyPo.setBuyCount(tmpStoreOrderCartDto.getCartNum());
        groupBuyPo.setBuyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_buyId));
        groupBuyPo.setGroupId(groupBuyProductDtos.get(0).getGroupId());
        groupBuyPo.setGroupPrice(tmpStoreOrderCartDto.getPayPrice());
        groupBuyPo.setPersionId(tmpStoreOrderCartDto.getPersonId());
        groupBuyPo.setPersionName(storeOrderDto.getPersonName());
        groupBuyPo.setProductId(tmpStoreOrderCartDto.getProductId());
        groupBuyPo.setSpecId(productSpecValueDtos.get(0).getSpecId());
        groupBuyPo.setState(GroupBuyDto.STATE_SUCCESS);
        groupBuyPo.setStoreId(tmpStoreOrderCartDto.getStoreId());

        int flag = groupBuyInnerServiceSMOImpl.saveGroupBuy(groupBuyPo);

        if (flag < 1) {
            throw new IllegalArgumentException("添加拼团失败");
        }

    }

    /**
     * 处理订单 购物车 和  拼团
     *
     * @param tmpStoreOrderCartDto
     */
    private void doDealStoreCart(StoreOrderCartDto tmpStoreOrderCartDto) {
        StoreCartDto storeCartDto = new StoreCartDto();
        storeCartDto.setCartId(tmpStoreOrderCartDto.getCartId());
        storeCartDto.setStoreId(tmpStoreOrderCartDto.getStoreId());
        List<StoreCartDto> storeCarts = storeCartInnerServiceSMOImpl.queryStoreCarts(storeCartDto);

        if (storeCarts == null || storeCarts.size() < 1) {
            return;
        }

        StoreCartPo storeCartPo = new StoreCartPo();
        storeCartPo.setCartId(tmpStoreOrderCartDto.getCartId());
        storeCartPo.setStoreId(tmpStoreOrderCartDto.getStoreId());
        storeCartPo.setState(StoreCartDto.STATE_FINISH_BUY);
        int flag = storeCartInnerServiceSMOImpl.updateStoreCart(storeCartPo);

        if (flag < 1) {
            throw new IllegalArgumentException("修改购物车失败");
        }

    }

}
