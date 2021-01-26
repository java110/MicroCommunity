package com.java110.store.api;

import com.java110.dto.Dto;.@@TemplateCode@@Dto;
import com.java110.po.@@templateCode@@.@@TemplateCode@@Po;
import com.java110.store.bmo.@@TemplateCode@@.IDelete@@TemplateCode@@BMO;
import com.java110.store.bmo.@@TemplateCode@@.IGet@@TemplateCode@@BMO;
import com.java110.store.bmo.@@TemplateCode@@.ISave@@TemplateCode@@BMO;
import com.java110.store.bmo.@@TemplateCode@@.IUpdate@@TemplateCode@@BMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/@@templateCode@@")
public class @@TemplateCode@@Api {

    @Autowired
    private ISave@@TemplateCode@@BMO save@@TemplateCode@@BMOImpl;
    @Autowired
    private IUpdate@@TemplateCode@@BMO update@@TemplateCode@@BMOImpl;
    @Autowired
    private IDelete@@TemplateCode@@BMO delete@@TemplateCode@@BMOImpl;

    @Autowired
    private IGet@@TemplateCode@@BMO get@@TemplateCode@@BMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /@@templateCode@@/save@@TemplateCode@@
     * @path /app/@@templateCode@@/save@@TemplateCode@@
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/save@@TemplateCode@@", method = RequestMethod.POST)
    public ResponseEntity<String> save@@TemplateCode@@(@RequestBody JSONObject reqJson) {

        @@validateSaveTemplateColumns@@

        @@TemplateCode@@Po @@templateCode@@Po = BeanConvertUtil.covertBean(reqJson, @@TemplateCode@@Po.class);
        return save@@TemplateCode@@BMOImpl.save(@@templateCode@@Po);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /@@templateCode@@/update@@TemplateCode@@
     * @path /app/@@templateCode@@/update@@TemplateCode@@
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/update@@TemplateCode@@", method = RequestMethod.POST)
    public ResponseEntity<String> update@@TemplateCode@@(@RequestBody JSONObject reqJson) {

        @@validateUpdateTemplateColumns@@

        @@TemplateCode@@Po @@templateCode@@Po = BeanConvertUtil.covertBean(reqJson, @@TemplateCode@@Po.class);
        return update@@TemplateCode@@BMOImpl.update(@@templateCode@@Po);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /@@templateCode@@/delete@@TemplateCode@@
     * @path /app/@@templateCode@@/delete@@TemplateCode@@
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/delete@@TemplateCode@@", method = RequestMethod.POST)
    public ResponseEntity<String> delete@@TemplateCode@@(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        @@validateDeleteTemplateColumns@@

        @@TemplateCode@@Po @@templateCode@@Po = BeanConvertUtil.covertBean(reqJson, @@TemplateCode@@Po.class);
        return delete@@TemplateCode@@BMOImpl.delete(@@templateCode@@Po);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /@@templateCode@@/query@@TemplateCode@@
     * @path /app/@@templateCode@@/query@@TemplateCode@@
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/query@@TemplateCode@@", method = RequestMethod.GET)
    public ResponseEntity<String> query@@TemplateCode@@(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        @@TemplateCode@@Dto @@templateCode@@Dto = new @@TemplateCode@@Dto();
        @@templateCode@@Dto.setPage(page);
        @@templateCode@@Dto.setRow(row);
        @@templateCode@@Dto.setCommunityId(communityId);
        return get@@TemplateCode@@BMOImpl.get(@@templateCode@@Dto);
    }
}
