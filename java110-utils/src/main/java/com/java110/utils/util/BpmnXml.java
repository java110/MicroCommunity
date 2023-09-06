package com.java110.utils.util;

/**
 * 默认bpmn 工具类
 */
public class BpmnXml {

    public static final String getDefaultVisitBpmnXml(String flowId) {
        String xml = "<bpmn2:definitions xmlns:bpmn2=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\" xmlns:activiti=\"http://activiti.org/bpmn\" id=\"sample-diagram\" targetNamespace=\"http://bpmn.io/schema/bpmn\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n" +
                "  <bpmn2:process id=\"java110_" + flowId + "\" isExecutable=\"true\">\n" +
                "    <bpmn2:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn2:outgoing>Flow_0mfw6el</bpmn2:outgoing>\n" +
                "    </bpmn2:startEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_0mfw6el\" sourceRef=\"StartEvent_1\" targetRef=\"Activity_1horz27\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_1horz27\" name=\"提交者\" camunda:assignee=\"${startUserId}\">\n" +
                "      <bpmn2:incoming>Flow_0mfw6el</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_107cj2o</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_107cj2o\" sourceRef=\"Activity_1horz27\" targetRef=\"Activity_15b7rm5\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_15b7rm5\" name=\"审批人\" camunda:assignee=\"${nextUserId}\">\n" +
                "      <bpmn2:incoming>Flow_107cj2o</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_06hzw4i</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:endEvent id=\"Event_00ichxr\">\n" +
                "      <bpmn2:incoming>Flow_06hzw4i</bpmn2:incoming>\n" +
                "    </bpmn2:endEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_06hzw4i\" sourceRef=\"Activity_15b7rm5\" targetRef=\"Event_00ichxr\"/>\n" +
                "  </bpmn2:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"java110_" + flowId + "\">\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_06hzw4i_di\" bpmnElement=\"Flow_06hzw4i\">\n" +
                "        <di:waypoint x=\"760\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"822\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_107cj2o_di\" bpmnElement=\"Flow_107cj2o\">\n" +
                "        <di:waypoint x=\"600\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"660\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0mfw6el_di\" bpmnElement=\"Flow_0mfw6el\">\n" +
                "        <di:waypoint x=\"448\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"500\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"412\" y=\"240\" width=\"36\" height=\"36\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_0udbuzc_di\" bpmnElement=\"Activity_1horz27\">\n" +
                "        <dc:Bounds x=\"500\" y=\"218\" width=\"100\" height=\"80\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1frcabp_di\" bpmnElement=\"Activity_15b7rm5\">\n" +
                "        <dc:Bounds x=\"660\" y=\"218\" width=\"100\" height=\"80\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_00ichxr_di\" bpmnElement=\"Event_00ichxr\">\n" +
                "        <dc:Bounds x=\"822\" y=\"240\" width=\"36\" height=\"36\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn2:definitions>";
        return xml;
    }

    public static final String getDefaultOwnerSettledBpmnXml(String flowId) {
        String xml = "<bpmn2:definitions xmlns:bpmn2=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\" xmlns:activiti=\"http://activiti.org/bpmn\" id=\"sample-diagram\" targetNamespace=\"http://bpmn.io/schema/bpmn\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n" +
                "  <bpmn2:process id=\"java110_" + flowId + "\" isExecutable=\"true\">\n" +
                "    <bpmn2:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn2:outgoing>Flow_0mfw6el</bpmn2:outgoing>\n" +
                "    </bpmn2:startEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_0mfw6el\" sourceRef=\"StartEvent_1\" targetRef=\"Activity_1horz27\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_1horz27\" name=\"提交者\" camunda:assignee=\"${startUserId}\">\n" +
                "      <bpmn2:incoming>Flow_0mfw6el</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_107cj2o</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_107cj2o\" sourceRef=\"Activity_1horz27\" targetRef=\"Activity_15b7rm5\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_15b7rm5\" name=\"审批人\" camunda:assignee=\"${nextUserId}\">\n" +
                "      <bpmn2:incoming>Flow_107cj2o</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_06hzw4i</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:endEvent id=\"Event_00ichxr\">\n" +
                "      <bpmn2:incoming>Flow_06hzw4i</bpmn2:incoming>\n" +
                "    </bpmn2:endEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_06hzw4i\" sourceRef=\"Activity_15b7rm5\" targetRef=\"Event_00ichxr\"/>\n" +
                "  </bpmn2:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"java110_" + flowId + "\">\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_06hzw4i_di\" bpmnElement=\"Flow_06hzw4i\">\n" +
                "        <di:waypoint x=\"760\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"822\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_107cj2o_di\" bpmnElement=\"Flow_107cj2o\">\n" +
                "        <di:waypoint x=\"600\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"660\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0mfw6el_di\" bpmnElement=\"Flow_0mfw6el\">\n" +
                "        <di:waypoint x=\"448\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"500\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"412\" y=\"240\" width=\"36\" height=\"36\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_0udbuzc_di\" bpmnElement=\"Activity_1horz27\">\n" +
                "        <dc:Bounds x=\"500\" y=\"218\" width=\"100\" height=\"80\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1frcabp_di\" bpmnElement=\"Activity_15b7rm5\">\n" +
                "        <dc:Bounds x=\"660\" y=\"218\" width=\"100\" height=\"80\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_00ichxr_di\" bpmnElement=\"Event_00ichxr\">\n" +
                "        <dc:Bounds x=\"822\" y=\"240\" width=\"36\" height=\"36\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn2:definitions>";
        return xml;
    }

    public static final String getDefaultBpmnXml(String flowId) {
        String xml = "<bpmn2:definitions xmlns:bpmn2=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\" xmlns:activiti=\"http://activiti.org/bpmn\" id=\"sample-diagram\" targetNamespace=\"http://bpmn.io/schema/bpmn\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n" +
                "  <bpmn2:process id=\"java110_" + flowId + "\" isExecutable=\"true\">\n" +
                "    <bpmn2:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn2:outgoing>Flow_0mfw6el</bpmn2:outgoing>\n" +
                "    </bpmn2:startEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_0mfw6el\" sourceRef=\"StartEvent_1\" targetRef=\"Activity_1horz27\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_1horz27\" name=\"提交者\" camunda:assignee=\"${startUserId}\">\n" +
                "      <bpmn2:incoming>Flow_0mfw6el</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_107cj2o</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_107cj2o\" sourceRef=\"Activity_1horz27\" targetRef=\"Activity_15b7rm5\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_15b7rm5\" name=\"审批人\" camunda:assignee=\"${nextUserId}\">\n" +
                "      <bpmn2:incoming>Flow_107cj2o</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_06hzw4i</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:endEvent id=\"Event_00ichxr\">\n" +
                "      <bpmn2:incoming>Flow_06hzw4i</bpmn2:incoming>\n" +
                "    </bpmn2:endEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_06hzw4i\" sourceRef=\"Activity_15b7rm5\" targetRef=\"Event_00ichxr\"/>\n" +
                "  </bpmn2:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"java110_" + flowId + "\">\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_06hzw4i_di\" bpmnElement=\"Flow_06hzw4i\">\n" +
                "        <di:waypoint x=\"760\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"822\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_107cj2o_di\" bpmnElement=\"Flow_107cj2o\">\n" +
                "        <di:waypoint x=\"600\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"660\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0mfw6el_di\" bpmnElement=\"Flow_0mfw6el\">\n" +
                "        <di:waypoint x=\"448\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"500\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"412\" y=\"240\" width=\"36\" height=\"36\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_0udbuzc_di\" bpmnElement=\"Activity_1horz27\">\n" +
                "        <dc:Bounds x=\"500\" y=\"218\" width=\"100\" height=\"80\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1frcabp_di\" bpmnElement=\"Activity_15b7rm5\">\n" +
                "        <dc:Bounds x=\"660\" y=\"218\" width=\"100\" height=\"80\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_00ichxr_di\" bpmnElement=\"Event_00ichxr\">\n" +
                "        <dc:Bounds x=\"822\" y=\"240\" width=\"36\" height=\"36\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn2:definitions>";
        return xml;
    }

    /**
     * 采购相关默认流程
     *
     * @param flowId
     * @return
     */
    public static final String getResourceBpmnXml(String flowId) {
        String xml = "<bpmn2:definitions xmlns:bpmn2=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\" xmlns:activiti=\"http://activiti.org/bpmn\" id=\"sample-diagram\" targetNamespace=\"http://bpmn.io/schema/bpmn\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n" +
                "  <bpmn2:process id=\"java110_"+flowId+"\" isExecutable=\"true\">\n" +
                "    <bpmn2:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn2:outgoing>Flow_1sqmp2q</bpmn2:outgoing>\n" +
                "    </bpmn2:startEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_1sqmp2q\" sourceRef=\"StartEvent_1\" targetRef=\"Activity_1ukl8gh\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_1ukl8gh\" name=\"提交人\" camunda:assignee=\"${startUserId}\">\n" +
                "      <bpmn2:incoming>Flow_1sqmp2q</bpmn2:incoming>\n" +
                "      <bpmn2:incoming>Flow_0br4lc7</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_1qsbcm5</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_1qsbcm5\" sourceRef=\"Activity_1ukl8gh\" targetRef=\"Activity_0oru23p\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_0oru23p\" name=\"审核人\" camunda:assignee=\"${nextUserId}\">\n" +
                "      <bpmn2:incoming>Flow_1qsbcm5</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_04yuz9d</bpmn2:outgoing>\n" +
                "      <bpmn2:outgoing>Flow_0br4lc7</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_04yuz9d\" sourceRef=\"Activity_0oru23p\" targetRef=\"Activity_1hy05pv\">\n" +
                "      <bpmn2:conditionExpression xsi:type=\"bpmn2:tFormalExpression\">${auditCode=='1100'}</bpmn2:conditionExpression>\n" +
                "    </bpmn2:sequenceFlow>\n" +
                "    <bpmn2:userTask id=\"Activity_1hy05pv\" name=\"仓库管理员\" camunda:assignee=\"${nextUserId}\">\n" +
                "      <bpmn2:incoming>Flow_04yuz9d</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_1ciz70a</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:endEvent id=\"Event_0i3sdca\">\n" +
                "      <bpmn2:incoming>Flow_1ciz70a</bpmn2:incoming>\n" +
                "    </bpmn2:endEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_1ciz70a\" sourceRef=\"Activity_1hy05pv\" targetRef=\"Event_0i3sdca\"/>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_0br4lc7\" sourceRef=\"Activity_0oru23p\" targetRef=\"Activity_1ukl8gh\">\n" +
                "      <bpmn2:conditionExpression xsi:type=\"bpmn2:tFormalExpression\">${auditCode=='1400'}</bpmn2:conditionExpression>\n" +
                "    </bpmn2:sequenceFlow>\n" +
                "  </bpmn2:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"java110_752023082431250005\">\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1ciz70a_di\" bpmnElement=\"Flow_1ciz70a\">\n" +
                "        <di:waypoint x=\"920\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"982\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_04yuz9d_di\" bpmnElement=\"Flow_04yuz9d\">\n" +
                "        <di:waypoint x=\"760\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"820\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1qsbcm5_di\" bpmnElement=\"Flow_1qsbcm5\">\n" +
                "        <di:waypoint x=\"600\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"660\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1sqmp2q_di\" bpmnElement=\"Flow_1sqmp2q\">\n" +
                "        <di:waypoint x=\"448\" y=\"258\"/>\n" +
                "        <di:waypoint x=\"500\" y=\"258\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0br4lc7_di\" bpmnElement=\"Flow_0br4lc7\">\n" +
                "        <di:waypoint x=\"710\" y=\"298\"/>\n" +
                "        <di:waypoint x=\"710\" y=\"420\"/>\n" +
                "        <di:waypoint x=\"550\" y=\"420\"/>\n" +
                "        <di:waypoint x=\"550\" y=\"298\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"412\" y=\"240\" width=\"36\" height=\"36\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1snbikp_di\" bpmnElement=\"Activity_1ukl8gh\">\n" +
                "        <dc:Bounds x=\"500\" y=\"218\" width=\"100\" height=\"80\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_0szddqt_di\" bpmnElement=\"Activity_0oru23p\">\n" +
                "        <dc:Bounds x=\"660\" y=\"218\" width=\"100\" height=\"80\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1qb2w4x_di\" bpmnElement=\"Activity_1hy05pv\">\n" +
                "        <dc:Bounds x=\"820\" y=\"218\" width=\"100\" height=\"80\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_0i3sdca_di\" bpmnElement=\"Event_0i3sdca\">\n" +
                "        <dc:Bounds x=\"982\" y=\"240\" width=\"36\" height=\"36\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn2:definitions>";
        return xml;
    }
}
