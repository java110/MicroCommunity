package com.java110.common.cmd.area;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.area.AreaDto;
import com.java110.intf.common.IAreaInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 查询地区树形结构
 */
@Java110Cmd(serviceCode = "area.queryAreaTree")
public class QueryAreaTreeCmd extends Cmd {

    @Autowired
    private IAreaInnerServiceSMO areaInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        AreaDto areaDto = new AreaDto();
        List<AreaDto> areaDtos = areaInnerServiceSMOImpl.getArea(areaDto);

        JSONObject areaData = new JSONObject();
        areaData.put("id", "0");
        areaData.put("areaCode", "0");

        areaData.put("text", "地区");
        areaData.put("icon", "/img/org.png");
        areaData.put("children", new JSONArray());
        JSONArray areas = areaData.getJSONArray("children");
        if (ListUtil.isNull(areaDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(areas));
            return;
        }

        JSONObject areaInfo = null;
        for (AreaDto tmpAreaDto : areaDtos) {
            if (AreaDto.AREA_LEVEL_ONE.equals(tmpAreaDto.getAreaLevel())) {
                areaInfo = new JSONObject();
                areaInfo.put("id", tmpAreaDto.getId());
                areaInfo.put("areaCode", tmpAreaDto.getAreaCode());
                areaInfo.put("areaLevel", tmpAreaDto.getAreaLevel());
                areaInfo.put("text", tmpAreaDto.getAreaName());
                areaInfo.put("icon", "/img/org.png");
                areaInfo.put("children", new JSONArray());
                areas.add(areaInfo);
            }
        }

        JSONObject area = null;
        for (int cIndex = 0; cIndex < areas.size(); cIndex++) {
            area = areas.getJSONObject(cIndex);
            // find floor data in unitDtos
            findTwoLevel(area, areaDtos);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(areaData));

    }

    private void findTwoLevel(JSONObject area, List<AreaDto> areaDtos) {

        JSONArray childrens = area.getJSONArray("children");
        JSONObject childrenInfo = null;
        for (AreaDto tmpAreaDto : areaDtos) {
            if (!AreaDto.AREA_LEVEL_TWO.equals(tmpAreaDto.getAreaLevel())) {
                continue;
            }
            if (area.getString("areaCode").equals(tmpAreaDto.getParentAreaCode())) {
                childrenInfo = new JSONObject();
                childrenInfo.put("id", tmpAreaDto.getId());
                childrenInfo.put("areaCode", tmpAreaDto.getAreaCode());
                childrenInfo.put("areaLevel", tmpAreaDto.getAreaLevel());
                childrenInfo.put("text", tmpAreaDto.getAreaName());
                childrenInfo.put("children", new JSONArray());
                childrenInfo.put("icon", "/img/floor.png");
                childrens.add(childrenInfo);
            }
        }


        JSONObject floor = null;
        for (int cIndex = 0; cIndex < childrens.size(); cIndex++) {
            floor = childrens.getJSONObject(cIndex);
            // find floor data in unitDtos
            findThreeLevel(floor, areaDtos);
        }
    }

    private void findThreeLevel(JSONObject twoArea, List<AreaDto> areaDtos) {
        JSONArray childrens = twoArea.getJSONArray("children");
        JSONObject childrenInfo = null;
        for (AreaDto tmpAreaDto : areaDtos) {
            if (!AreaDto.AREA_LEVEL_THREE.equals(tmpAreaDto.getAreaLevel())) {
                continue;
            }
            if (twoArea.getString("areaCode").equals(tmpAreaDto.getParentAreaCode())) {
                childrenInfo = new JSONObject();
                childrenInfo.put("id", tmpAreaDto.getId());
                childrenInfo.put("areaCode", tmpAreaDto.getAreaCode());
                childrenInfo.put("areaLevel", tmpAreaDto.getAreaLevel());
                childrenInfo.put("text", tmpAreaDto.getAreaName());
                childrenInfo.put("children", new JSONArray());
                childrenInfo.put("icon", "/img/unit.png");
                childrens.add(childrenInfo);
            }
        }
    }
}
