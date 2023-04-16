package com.java110.report.bmo.search;

import com.java110.dto.data.SearchDataDto;

/**
 * 查询业主信息
 */
public interface ISearchOwnerBMO {

    /**
     * 查询房屋信息
     * @param searchDataDto
     * @return
     */
    SearchDataDto query(SearchDataDto searchDataDto);
}
