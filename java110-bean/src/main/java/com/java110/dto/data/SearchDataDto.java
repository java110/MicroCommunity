package com.java110.dto.data;

import com.java110.dto.room.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.visit.VisitDto;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索数据封装
 */
public class SearchDataDto implements Serializable {

    private String communityId;

    private String storeId;

    private String searchValue;

    private String tel;

    private List<RoomDto> rooms;

    private List<OwnerDto> owners;

    private List<OwnerDto> ownerMembers;

    private List<OwnerCarDto> cars;

    private List<OwnerCarDto> carMembers;

    private List<RepairDto> repairs;

    private List<ContractDto> contracts;

    private List<VisitDto> visitDtos;

    private List<UserDto> staffs;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public List<RoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public List<OwnerDto> getOwners() {
        return owners;
    }

    public void setOwners(List<OwnerDto> owners) {
        this.owners = owners;
    }

    public List<OwnerDto> getOwnerMembers() {
        return ownerMembers;
    }

    public void setOwnerMembers(List<OwnerDto> ownerMembers) {
        this.ownerMembers = ownerMembers;
    }

    public List<OwnerCarDto> getCars() {
        return cars;
    }

    public void setCars(List<OwnerCarDto> cars) {
        this.cars = cars;
    }

    public List<OwnerCarDto> getCarMembers() {
        return carMembers;
    }

    public void setCarMembers(List<OwnerCarDto> carMembers) {
        this.carMembers = carMembers;
    }

    public List<RepairDto> getRepairs() {
        return repairs;
    }

    public void setRepairs(List<RepairDto> repairs) {
        this.repairs = repairs;
    }

    public List<ContractDto> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractDto> contracts) {
        this.contracts = contracts;
    }

    public List<VisitDto> getVisitDtos() {
        return visitDtos;
    }

    public void setVisitDtos(List<VisitDto> visitDtos) {
        this.visitDtos = visitDtos;
    }

    public List<UserDto> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<UserDto> staffs) {
        this.staffs = staffs;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
