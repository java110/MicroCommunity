package com.java110.user.bmo.owner;

import org.springframework.http.ResponseEntity;

public interface IComprehensiveQuery {

    /**
     * 综合查询
     *
     * @return
     */
    ResponseEntity<String> query(String communityId, String searchValue, String searchType, String userId,String storeId);
}
