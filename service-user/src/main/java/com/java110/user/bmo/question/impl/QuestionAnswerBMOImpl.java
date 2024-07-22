package com.java110.user.bmo.question.impl;

import com.alibaba.fastjson.JSONArray;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.user.UserQuestionAnswerDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IUserQuestionAnswerV1InnerServiceSMO;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.po.user.UserQuestionAnswerPo;
import com.java110.user.bmo.question.IQuestionAnswerBMO;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionAnswerBMOImpl implements IQuestionAnswerBMO {

    public static final int MAX_LENGTH = 200;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IUserQuestionAnswerV1InnerServiceSMO userQuestionAnswerV1InnerServiceSMOImpl;

    @Async
    @Override
    public void saveUserQuestionAnswer(QuestionAnswerPo questionAnswerPo, JSONArray roomIds) {

        if (ListUtil.isNull(roomIds)) {
            return;
        }

        List<String> roomIdStrs = new ArrayList<>();

        for (int roomIndex = 0; roomIndex < roomIds.size(); roomIndex++) {
            roomIdStrs.add(roomIds.getString(roomIndex));
            if (roomIdStrs.size() == MAX_LENGTH) {
                doData(roomIdStrs, questionAnswerPo);
                roomIdStrs = new ArrayList<>();
            }
        }

        if (roomIdStrs.size() > 0) {
            doData(roomIdStrs, questionAnswerPo);
        }

    }

    private void doData(List<String> roomIdStrs, QuestionAnswerPo questionAnswerPo) {

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(questionAnswerPo.getCommunityId());
        roomDto.setRoomIds(roomIdStrs.toArray(new String[roomIdStrs.size()]));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        if (ListUtil.isNull(roomDtos)) {
            return;
        }

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setCommunityId(questionAnswerPo.getCommunityId());
        ownerRoomRelDto.setRoomIds(roomIdStrs.toArray(new String[roomIdStrs.size()]));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ListUtil.isNull(ownerRoomRelDtos)) {
            return;
        }

        for (RoomDto tmpRoomDto : roomDtos) {
            for (OwnerRoomRelDto tmpOwnerRoomRelDto : ownerRoomRelDtos) {
                if (tmpOwnerRoomRelDto.getRoomId().equals(tmpRoomDto.getRoomId())) {
                    tmpRoomDto.setOwnerId(tmpOwnerRoomRelDto.getOwnerId());
                    tmpRoomDto.setOwnerName(tmpOwnerRoomRelDto.getOwnerName());
                    tmpRoomDto.setLink(tmpOwnerRoomRelDto.getLink());
                }
            }
        }
        List<UserQuestionAnswerPo> userQuestionAnswerPos = new ArrayList<>();
        UserQuestionAnswerPo userQuestionAnswerPo = null;
        for (RoomDto tmpRoomDto : roomDtos) {
            if(StringUtil.isEmpty(tmpRoomDto.getOwnerId())){
                continue;
            }
            if(StringUtil.isEmpty(tmpRoomDto.getLink())){
                continue;
            }
            if(StringUtil.isEmpty(tmpRoomDto.getOwnerName())){
                continue;
            }
            userQuestionAnswerPo = new UserQuestionAnswerPo();
            userQuestionAnswerPo.setLink(tmpRoomDto.getLink());
            userQuestionAnswerPo.setOwnerId(tmpRoomDto.getOwnerId());
            userQuestionAnswerPo.setCommunityId(tmpRoomDto.getCommunityId());
            userQuestionAnswerPo.setOwnerName(tmpRoomDto.getOwnerName());
            userQuestionAnswerPo.setQaId(questionAnswerPo.getQaId());
            userQuestionAnswerPo.setScore("0");
            userQuestionAnswerPo.setUserQaId(GenerateCodeFactory.getGeneratorId("11", true));
            userQuestionAnswerPo.setState(UserQuestionAnswerDto.STATE_WAIT);
            userQuestionAnswerPo.setRoomId(tmpRoomDto.getRoomId());
            userQuestionAnswerPo.setRoomName(tmpRoomDto.getFloorNum()+"-"+tmpRoomDto.getUnitNum()+"-"+tmpRoomDto.getRoomNum());
            userQuestionAnswerPos.add(userQuestionAnswerPo);
        }

        userQuestionAnswerV1InnerServiceSMOImpl.saveUserQuestionAnswers(userQuestionAnswerPos);

    }
}
