package com.java110.report.bmo.search;

import com.java110.dto.data.SearchDataDto;

/**
 * 查询报修信息
 */
public interface ISearchRepairBMO {

    /**
     * 查询房屋信息
     * @param searchDataDto
     * @return
     */
    SearchDataDto query(SearchDataDto searchDataDto);
}
