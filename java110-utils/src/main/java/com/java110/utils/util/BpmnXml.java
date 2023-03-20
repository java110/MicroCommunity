package com.java110.utils.util;

/**
 * 默认bpmn 工具类
 */
public class BpmnXml {

    public static final String getDefaultVisitBpmnXml(String flowId) {
        String xml = "<bpmn2:definitions xmlns:bpmn2=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\" xmlns:activiti=\"http://activiti.org/bpmn\" id=\"sample-diagram\" targetNamespace=\"http://bpmn.io/schema/bpmn\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n" +
                "  <bpmn2:process id=\"java110_"+flowId+"\" isExecutable=\"true\">\n" +
                "    <bpmn2:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn2:outgoing>Flow_0mfw6el</bpmn2:outgoing>\n" +
                "    </bpmn2:startEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_0mfw6el\" sourceRef=\"StartEvent_1\" targetRef=\"Activity_1horz27\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_1horz27\" name=\"提交者\" camunda:assignee=\"${startUserId}\">\n" +
                "      <bpmn2:incoming>Flow_0mfw6el</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_107cj2o</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_107cj2o\" sourceRef=\"Activity_1horz27\" targetRef=\"Activity_15b7rm5\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_15b7rm5\" name=\"审批人\" camunda:assignee=\"\">\n" +
                "      <bpmn2:incoming>Flow_107cj2o</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_06hzw4i</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:endEvent id=\"Event_00ichxr\">\n" +
                "      <bpmn2:incoming>Flow_06hzw4i</bpmn2:incoming>\n" +
                "    </bpmn2:endEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_06hzw4i\" sourceRef=\"Activity_15b7rm5\" targetRef=\"Event_00ichxr\"/>\n" +
                "  </bpmn2:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"java110_"+flowId+"\">\n" +
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
                "  <bpmn2:process id=\"java110_"+flowId+"\" isExecutable=\"true\">\n" +
                "    <bpmn2:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn2:outgoing>Flow_0mfw6el</bpmn2:outgoing>\n" +
                "    </bpmn2:startEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_0mfw6el\" sourceRef=\"StartEvent_1\" targetRef=\"Activity_1horz27\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_1horz27\" name=\"提交者\" camunda:assignee=\"${startUserId}\">\n" +
                "      <bpmn2:incoming>Flow_0mfw6el</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_107cj2o</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_107cj2o\" sourceRef=\"Activity_1horz27\" targetRef=\"Activity_15b7rm5\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_15b7rm5\" name=\"审批人\" camunda:assignee=\"\">\n" +
                "      <bpmn2:incoming>Flow_107cj2o</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_06hzw4i</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:endEvent id=\"Event_00ichxr\">\n" +
                "      <bpmn2:incoming>Flow_06hzw4i</bpmn2:incoming>\n" +
                "    </bpmn2:endEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_06hzw4i\" sourceRef=\"Activity_15b7rm5\" targetRef=\"Event_00ichxr\"/>\n" +
                "  </bpmn2:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"java110_"+flowId+"\">\n" +
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
                "  <bpmn2:process id=\"java110_"+flowId+"\" isExecutable=\"true\">\n" +
                "    <bpmn2:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn2:outgoing>Flow_0mfw6el</bpmn2:outgoing>\n" +
                "    </bpmn2:startEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_0mfw6el\" sourceRef=\"StartEvent_1\" targetRef=\"Activity_1horz27\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_1horz27\" name=\"提交者\" camunda:assignee=\"${startUserId}\">\n" +
                "      <bpmn2:incoming>Flow_0mfw6el</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_107cj2o</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_107cj2o\" sourceRef=\"Activity_1horz27\" targetRef=\"Activity_15b7rm5\"/>\n" +
                "    <bpmn2:userTask id=\"Activity_15b7rm5\" name=\"审批人\" camunda:assignee=\"\">\n" +
                "      <bpmn2:incoming>Flow_107cj2o</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_06hzw4i</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:endEvent id=\"Event_00ichxr\">\n" +
                "      <bpmn2:incoming>Flow_06hzw4i</bpmn2:incoming>\n" +
                "    </bpmn2:endEvent>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_06hzw4i\" sourceRef=\"Activity_15b7rm5\" targetRef=\"Event_00ichxr\"/>\n" +
                "  </bpmn2:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"java110_"+flowId+"\">\n" +
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
}
