package com.java110.api.smo.inspection;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

public interface IInspectionPlanStateSMO {

    ResponseEntity<String> updateInspectionPlanState(IPageData pd);
}
