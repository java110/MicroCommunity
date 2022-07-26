package com.java110.dto.org;

import java.io.Serializable;
import java.util.List;

/**
 * {
 * id: 'f_' + pItem.floorId,
 * floorId: pItem.floorId,
 * floorNum: pItem.floorNum,
 * icon: "/img/floor.png",
 * text: pItem.floorNum + "栋",
 * state: {
 * opened: false
 * },
 * children: []
 * }
 */
public class OrgTreeDto implements Serializable {

    public OrgTreeDto() {

    }

    public OrgTreeDto(String id, String text,String parentId,String allOrgName) {
        this.id = id;
        this.text = text;
        this.parentId = parentId;
        this.allOrgName = allOrgName;
    }

    private String id;
    private String icon ="/img/org.png";
    private String text;
    private String parentId;
    private String allOrgName;
    private List<OrgTreeDto> children;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<OrgTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<OrgTreeDto> children) {
        this.children = children;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAllOrgName() {
        return allOrgName;
    }

    public void setAllOrgName(String allOrgName) {
        this.allOrgName = allOrgName;
    }
}
