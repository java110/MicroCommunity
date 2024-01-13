package com.java110.store.bmo.allocation;

import com.java110.dto.purchase.AllocationStorehouseDto;

public interface IAllocationBMO {

    /**
     * 调拨
     * @param tmpAllocationStorehouseDto
     * @param allocationStock
     */
    void doToAllocationStorehouse(AllocationStorehouseDto tmpAllocationStorehouseDto, double allocationStock);
}
