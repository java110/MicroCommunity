package com.java110.report.bmo.search;

import com.java110.dto.data.SearchDataDto;

/**
 * 查询访客信息
 */
public interface ISearchVisitBMO {

    /**
     * 查询房屋信息
     * @param searchDataDto
     * @return
     */
    SearchDataDto query(SearchDataDto searchDataDto);
}
