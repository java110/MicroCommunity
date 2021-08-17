/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.dto.oaWorkflow;

/**
 * @desc add by 吴学文 11:31
 */
public class StencilsetJson {
    public static final String JSON = "{\n" +
            "    \"title\" : \"BPMN 2.0标准工具\",\n" +
            "    \"namespace\" : \"http://b3mn.org/stencilset/bpmn2.0#\",\n" +
            "    \"description\" : \"BPMN process editor\",\n" +
            "    \"propertyPackages\" : [ {\n" +
            "        \"name\" : \"process_idpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"process_id\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"流程名称\",\n" +
            "            \"value\" : \"process\",\n" +
            "            \"description\" : \"流程的特殊唯一的名称标识\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"overrideidpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"overrideid\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"Id\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Unique identifier of the element.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"namepackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"name\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"名称\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"元素名称\",\n" +
            "            \"popular\" : true,\n" +
            "            \"refToView\" : \"text_name\"\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"documentationpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"documentation\",\n" +
            "            \"type\" : \"Text\",\n" +
            "            \"title\" : \"描述\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"元素描述\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"process_authorpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"process_author\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"流程作者\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"流程定义者姓名\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"process_versionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"process_version\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"流程版本\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"标识文档版本\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"process_namespacepackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"process_namespace\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"目标命名空间\",\n" +
            "            \"value\" : \"http://www.activiti.org/processdef\",\n" +
            "            \"description\" : \"工作流目标命名空间\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"asynchronousdefinitionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"asynchronousdefinition\",\n" +
            "            \"type\" : \"Boolean\",\n" +
            "            \"title\" : \"异步\",\n" +
            "            \"value\" : \"false\",\n" +
            "            \"description\" : \"Define the activity as asynchronous.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"exclusivedefinitionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"exclusivedefinition\",\n" +
            "            \"type\" : \"Boolean\",\n" +
            "            \"title\" : \"单独\",\n" +
            "            \"value\" : \"false\",\n" +
            "            \"description\" : \"Define the activity as exclusive.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"executionlistenerspackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"executionlisteners\",\n" +
            "            \"type\" : \"multiplecomplex\",\n" +
            "            \"title\" : \"执行监听器\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Listeners for an activity, process, sequence flow, start and end event.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"tasklistenerspackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"tasklisteners\",\n" +
            "            \"type\" : \"multiplecomplex\",\n" +
            "            \"title\" : \"任务监听器\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Listeners for a user task\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"eventlistenerspackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"eventlisteners\",\n" +
            "            \"type\" : \"multiplecomplex\",\n" +
            "            \"title\" : \"事件监听器\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Listeners for any event happening in the Activiti Engine. It's also possible to rethrow the event as a signal, message or error event\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"usertaskassignmentpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"usertaskassignment\",\n" +
            "            \"type\" : \"Complex\",\n" +
            "            \"title\" : \"代理\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Assignment definition for the user task\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"formpropertiespackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"formproperties\",\n" +
            "            \"type\" : \"Complex\",\n" +
            "            \"title\" : \"动态表单属性\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Definition of the form with a list of form properties\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"formkeydefinitionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"formkeydefinition\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"自定义表单\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"用户任务表单编号\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"duedatedefinitionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"duedatedefinition\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"到期日期\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"用户任务到期时间\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"prioritydefinitionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"prioritydefinition\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"优先级\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"用户任务优先级\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"duedatedefinitionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"duedatedefinition\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"到期日期\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Due date of the user task.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"servicetaskclasspackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"servicetaskclass\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"监听类\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Class that implements the service task logic.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"servicetaskexpressionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"servicetaskexpression\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"表达式\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Service task logic defined with an expression.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"servicetaskdelegateexpressionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"servicetaskdelegateexpression\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"委托表达式\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Service task logic defined with a delegate expression.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"servicetaskfieldspackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"servicetaskfields\",\n" +
            "            \"type\" : \"Complex\",\n" +
            "            \"title\" : \"字段\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Field extensions\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"servicetaskresultvariablepackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"servicetaskresultvariable\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"Result variable name\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Process variable name to store the service task result.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"scriptformatpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"scriptformat\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"脚本格式\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Script format of the script task.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"scripttextpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"scripttext\",\n" +
            "            \"type\" : \"Text\",\n" +
            "            \"title\" : \"脚本\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Script text of the script task.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"ruletask_rulespackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"ruletask_rules\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"规则\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Rules of the rule task.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"ruletask_variables_inputpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"ruletask_variables_input\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"输入变量\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Input variables of the rule task.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"ruletask_excludepackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"ruletask_exclude\",\n" +
            "            \"type\" : \"Boolean\",\n" +
            "            \"title\" : \"除外\",\n" +
            "            \"value\" : \"false\",\n" +
            "            \"description\" : \"Use the rules property as exclusion.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"ruletask_resultpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"ruletask_result\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"返回变量\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Result variable of the rule task.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"mailtasktopackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"mailtaskto\",\n" +
            "            \"type\" : \"Text\",\n" +
            "            \"title\" : \"接收人\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"The recipients if the e-mail. Multiple recipients are defined in a comma-separated list.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"mailtaskfrompackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"mailtaskfrom\",\n" +
            "            \"type\" : \"Text\",\n" +
            "            \"title\" : \"发件人\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"The sender e-mail address. If not provided, the default configured from address is used.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"mailtasksubjectpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"mailtasksubject\",\n" +
            "            \"type\" : \"Text\",\n" +
            "            \"title\" : \"主题\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"The subject of the e-mail.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"mailtaskccpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"mailtaskcc\",\n" +
            "            \"type\" : \"Text\",\n" +
            "            \"title\" : \"转发\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"The cc's of the e-mail. Multiple recipients are defined in a comma-separated list\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"mailtaskbccpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"mailtaskbcc\",\n" +
            "            \"type\" : \"Text\",\n" +
            "            \"title\" : \"密送\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"The bcc's of the e-mail. Multiple recipients are defined in a comma-separated list\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"mailtasktextpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"mailtasktext\",\n" +
            "            \"type\" : \"Text\",\n" +
            "            \"title\" : \"内容\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"The content of the e-mail, in case one needs to send plain none-rich e-mails. Can be used in combination with html, for e-mail clients that don't support rich content. The client will then fall back to this text-only alternative.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"mailtaskhtmlpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"mailtaskhtml\",\n" +
            "            \"type\" : \"Text\",\n" +
            "            \"title\" : \"Html\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"A piece of HTML that is the content of the e-mail.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"mailtaskcharsetpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"mailtaskcharset\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"Charset\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Allows to change the charset of the email, which is necessary for many non-English languages. \",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"callactivitycalledelementpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"callactivitycalledelement\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"被调用元素\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Process reference.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"callactivityinparameterspackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"callactivityinparameters\",\n" +
            "            \"type\" : \"Complex\",\n" +
            "            \"title\" : \"输入参数\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Definition of the input parameters\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"callactivityoutparameterspackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"callactivityoutparameters\",\n" +
            "            \"type\" : \"Complex\",\n" +
            "            \"title\" : \"输出参数\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Definition of the output parameters\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"cameltaskcamelcontextpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"cameltaskcamelcontext\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"Camel内容\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"An optional camel context definition, if left empty the default is used.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"muletaskendpointurlpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"muletaskendpointurl\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"终端url\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"A required endpoint url to sent the message to Mule.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"muletasklanguagepackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"muletasklanguage\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"语言\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"A required definition for the language to resolve the payload expression, like juel.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"muletaskpayloadexpressionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"muletaskpayloadexpression\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"有效载荷表达式\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"A required definition for the payload of the message sent to Mule.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"muletaskresultvariablepackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"muletaskresultvariable\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"返回变量\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"An optional result variable for the payload returned.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"conditionsequenceflowpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"conditionsequenceflow\",\n" +
            "            \"type\" : \"Complex\",\n" +
            "            \"title\" : \"流转条件\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"The condition of the sequence flow\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"defaultflowpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"defaultflow\",\n" +
            "            \"type\" : \"Boolean\",\n" +
            "            \"title\" : \"默认流转\",\n" +
            "            \"value\" : \"false\",\n" +
            "            \"description\" : \"Define the sequence flow as default\",\n" +
            "            \"popular\" : true,\n" +
            "            \"refToView\" : \"default\"\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"conditionalflowpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"conditionalflow\",\n" +
            "            \"type\" : \"Boolean\",\n" +
            "            \"title\" : \"Conditional flow\",\n" +
            "            \"value\" : \"false\",\n" +
            "            \"description\" : \"Define the sequence flow with a condition\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"timercycledefinitionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"timercycledefinition\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"循环时间(例：R3/PT10H)\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the timer with a ISO-8601 cycle.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"timerdatedefinitionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"timerdatedefinition\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"开始时间（ISO-8601）\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the timer with a ISO-8601 date definition.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"timerdurationdefinitionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"timerdurationdefinition\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"持续时间(例：PT5M)\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the timer with a ISO-8601 duration.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"timerenddatedefinitionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"timerenddatedefinition\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"结束时间（ISO-8601）\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the timer with a ISO-8601 duration.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"messagerefpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"messageref\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"消息引用\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the message name.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"signalrefpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"signalref\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"信号引用\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the signal name.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"errorrefpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"errorref\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"错误引用\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the error name.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"cancelactivitypackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"cancelactivity\",\n" +
            "            \"type\" : \"Boolean\",\n" +
            "            \"title\" : \"取消活动\",\n" +
            "            \"value\" : \"true\",\n" +
            "            \"description\" : \"Should the activity be cancelled\",\n" +
            "            \"popular\" : true,\n" +
            "            \"refToView\" : [ \"frame\", \"frame2\" ]\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"initiatorpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"initiator\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"发起人\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Initiator of the process.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"textpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"text\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"Text\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"The text of the text annotation.\",\n" +
            "            \"popular\" : true,\n" +
            "            \"refToView\" : \"text\"\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"multiinstance_typepackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"multiinstance_type\",\n" +
            "            \"type\" : \"kisbpm-multiinstance\",\n" +
            "            \"title\" : \"多实例类型\",\n" +
            "            \"value\" : \"None\",\n" +
            "            \"description\" : \"Repeated activity execution (parallel or sequential) can be displayed through different loop types\",\n" +
            "            \"popular\" : true,\n" +
            "            \"refToView\" : \"multiinstance\"\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"multiinstance_cardinalitypackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"multiinstance_cardinality\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"基数(多实例)\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the cardinality of multi instance.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"multiinstance_collectionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"multiinstance_collection\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"集合(多实例)\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the collection for the multi instance.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"multiinstance_variablepackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"multiinstance_variable\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"元素变量(多实例)\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the element variable for the multi instance.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"multiinstance_conditionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"multiinstance_condition\",\n" +
            "            \"type\" : \"String\",\n" +
            "            \"title\" : \"完成条件(多实例)\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Define the completion condition for the multi instance.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"isforcompensationpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"isforcompensation\",\n" +
            "            \"type\" : \"Boolean\",\n" +
            "            \"title\" : \"是否为补偿\",\n" +
            "            \"value\" : \"false\",\n" +
            "            \"description\" : \"一个标志,标识是否这个活动的目的是为了补偿.\",\n" +
            "            \"popular\" : true,\n" +
            "            \"refToView\" : \"compensation\"\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"sequencefloworderpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"sequencefloworder\",\n" +
            "            \"type\" : \"Complex\",\n" +
            "            \"title\" : \"流动顺序\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Order outgoing sequence flows.\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"signaldefinitionspackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"signaldefinitions\",\n" +
            "            \"type\" : \"multiplecomplex\",\n" +
            "            \"title\" : \"信号定义\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Signal definitions\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"messagedefinitionspackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"messagedefinitions\",\n" +
            "            \"type\" : \"multiplecomplex\",\n" +
            "            \"title\" : \"消息定义\",\n" +
            "            \"value\" : \"\",\n" +
            "            \"description\" : \"Message definitions\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"istransactionpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"istransaction\",\n" +
            "            \"type\" : \"Boolean\",\n" +
            "            \"title\" : \"是否事务处理子过程\",\n" +
            "            \"value\" : \"false\",\n" +
            "            \"description\" : \"A flag that identifies whether this sub process is of type transaction.\",\n" +
            "            \"popular\" : true,\n" +
            "            \"refToView\" : \"border\"\n" +
            "        } ]\n" +
            "    }, {\n" +
            "        \"name\" : \"terminateAllpackage\",\n" +
            "        \"properties\" : [ {\n" +
            "            \"id\" : \"terminateAll\",\n" +
            "            \"type\" : \"Boolean\",\n" +
            "            \"title\" : \"终止全部\",\n" +
            "            \"value\" : \"false\",\n" +
            "            \"description\" : \"Enable to terminate the process instance\",\n" +
            "            \"popular\" : true\n" +
            "        } ]\n" +
            "    } ],\n" +
            "    \"stencils\" : [ {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"BPMNDiagram\",\n" +
            "        \"title\" : \"BPMN-Diagram\",\n" +
            "        \"description\" : \"A BPMN 2.0 diagram.\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n   width=\\\"800\\\"\\n   height=\\\"600\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <g pointer-events=\\\"fill\\\" >\\n    <polygon stroke=\\\"black\\\" fill=\\\"black\\\" stroke-width=\\\"1\\\" points=\\\"0,0 0,590 9,599 799,599 799,9 790,0\\\" stroke-linecap=\\\"butt\\\" stroke-linejoin=\\\"miter\\\" stroke-miterlimit=\\\"10\\\" />\\n    <rect id=\\\"diagramcanvas\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"790\\\" height=\\\"590\\\" stroke=\\\"black\\\" stroke-width=\\\"2\\\" fill=\\\"white\\\" />\\n    \\t<text font-size=\\\"22\\\" id=\\\"diagramtext\\\" x=\\\"400\\\" y=\\\"25\\\" oryx:align=\\\"top center\\\" stroke=\\\"#373e48\\\"></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"diagram.png\",\n" +
            "        \"groups\" : [ \"Diagram\" ],\n" +
            "        \"mayBeRoot\" : true,\n" +
            "        \"hide\" : true,\n" +
            "        \"propertyPackages\" : [ \"process_idpackage\", \"namepackage\", \"documentationpackage\", \"process_authorpackage\", \"process_versionpackage\", \"process_namespacepackage\", \"executionlistenerspackage\", \"eventlistenerspackage\", \"signaldefinitionspackage\", \"messagedefinitionspackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"StartNoneEvent\",\n" +
            "        \"title\" : \"开始\",\n" +
            "        \"description\" : \"A start event without a specific trigger\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\">\\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"1\\\"/>\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"startevent/none.png\",\n" +
            "        \"groups\" : [ \"启动事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"initiatorpackage\", \"formkeydefinitionpackage\", \"formpropertiespackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"Startevents_all\", \"StartEventsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"StartTimerEvent\",\n" +
            "        \"title\" : \"定时事件\",\n" +
            "        \"description\" : \"A start event with a timer trigger\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\">\\n    <circle \\n    \\tid=\\\"bg_frame\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"15\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"#ffffff\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 5.5, 3\\\" />\\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    \\n    <path id=\\\"path1\\\" transform=\\\"translate(6,6)\\\"\\n    \\td=\\\"M 10 0 C 4.4771525 0 0 4.4771525 0 10 C 0 15.522847 4.4771525 20 10 20 C 15.522847 20 20 15.522847 20 10 C 20 4.4771525 15.522847 1.1842379e-15 10 0 z M 9.09375 1.03125 C 9.2292164 1.0174926 9.362825 1.0389311 9.5 1.03125 L 9.5 3.5 L 10.5 3.5 L 10.5 1.03125 C 15.063526 1.2867831 18.713217 4.9364738 18.96875 9.5 L 16.5 9.5 L 16.5 10.5 L 18.96875 10.5 C 18.713217 15.063526 15.063526 18.713217 10.5 18.96875 L 10.5 16.5 L 9.5 16.5 L 9.5 18.96875 C 4.9364738 18.713217 1.2867831 15.063526 1.03125 10.5 L 3.5 10.5 L 3.5 9.5 L 1.03125 9.5 C 1.279102 5.0736488 4.7225326 1.4751713 9.09375 1.03125 z M 9.5 5 L 9.5 8.0625 C 8.6373007 8.2844627 8 9.0680195 8 10 C 8 11.104569 8.8954305 12 10 12 C 10.931981 12 11.715537 11.362699 11.9375 10.5 L 14 10.5 L 14 9.5 L 11.9375 9.5 C 11.756642 8.7970599 11.20294 8.2433585 10.5 8.0625 L 10.5 5 L 9.5 5 z \\\"  \\n    \\tfill=\\\"#585858\\\" stroke=\\\"none\\\" />\\n   \\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"startevent/timer.png\",\n" +
            "        \"groups\" : [ \"启动事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"timercycledefinitionpackage\", \"timerdatedefinitionpackage\", \"timerdurationdefinitionpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"Startevents_all\", \"StartEventsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"StartSignalEvent\",\n" +
            "        \"title\" : \"信号事件\",\n" +
            "        \"description\" : \"A start event with a signal trigger\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\">\\n\\n    <circle \\n    \\tid=\\\"bg_frame\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"15\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"#ffffff\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 5.5, 3\\\" />\\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    <path\\n       d=\\\"M 8.7124971,21.247342 L 23.333334,21.247342 L 16.022915,8.5759512 L 8.7124971,21.247342 z\\\"\\n       id=\\\"triangle\\\"\\n       stroke=\\\"#585858\\\"\\n       style=\\\"fill:none;stroke-width:1.4;stroke-miterlimit:4;stroke-dasharray:none\\\" />\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"startevent/signal.png\",\n" +
            "        \"groups\" : [ \"启动事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"signalrefpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"Startevents_all\", \"StartEventsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"StartMessageEvent\",\n" +
            "        \"title\" : \"消息事件\",\n" +
            "        \"description\" : \"A start event with a message trigger\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\">\\n    <circle \\n    \\tid=\\\"bg_frame\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"15\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"#ffffff\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 5.5, 3\\\" />\\n    \\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    \\n    <path transform=\\\"translate(7,7)\\\" id=\\\"path1\\\" stroke=\\\"none\\\" fill=\\\"#585858\\\" stroke-width=\\\"1\\\" d=\\\"m 0.5,2.5 0,13 17,0 0,-13 z M 2,4 6.5,8.5 2,13 z M 4,4 14,4 9,9 z m 12,0 0,9 -4.5,-4.5 z M 7.5,9.5 9,11 10.5,9.5 15,14 3,14 z\\\"/>\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"startevent/message.png\",\n" +
            "        \"groups\" : [ \"启动事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"messagerefpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"Startevents_all\", \"StartEventsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"StartErrorEvent\",\n" +
            "        \"title\" : \"异常事件\",\n" +
            "        \"description\" : \"A start event that catches a thrown BPMN error\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"1\\\"/>\\n    \\n    <path\\n         stroke=\\\"#585858\\\"\\n         style=\\\"fill:none;stroke-width:1.5;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:10\\\"\\n         d=\\\"M 22.820839,11.171502 L 19.36734,24.58992 L 13.54138,14.281819 L 9.3386512,20.071607 L 13.048949,6.8323057 L 18.996148,16.132659 L 22.820839,11.171502 z\\\"\\n         id=\\\"errorPolygon\\\" />\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"startevent/error.png\",\n" +
            "        \"groups\" : [ \"启动事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"errorrefpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"Startevents_all\", \"StartEventsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"UserTask\",\n" +
            "        \"title\" : \"用户活动\",\n" +
            "        \"description\" : \"分配给特定人的任务 \",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n\\t\\n\\t<g id=\\\"userTask\\\" transform=\\\"translate(3,3)\\\">\\n\\t\\t<path oryx:anchors=\\\"top left\\\"\\n       \\t\\tstyle=\\\"fill:#d1b575;stroke:none;\\\"\\n       \\t\\t d=\\\"m 1,17 16,0 0,-1.7778 -5.333332,-3.5555 0,-1.7778 c 1.244444,0 1.244444,-2.3111 1.244444,-2.3111 l 0,-3.0222 C 12.555557,0.8221 9.0000001,1.0001 9.0000001,1.0001 c 0,0 -3.5555556,-0.178 -3.9111111,3.5555 l 0,3.0222 c 0,0 0,2.3111 1.2444443,2.3111 l 0,1.7778 L 1,15.2222 1,17 17,17\\\" \\n         />\\n\\t\\t\\n\\t</g>\\n  \\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\t\\n\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/list/type.user.png\",\n" +
            "        \"groups\" : [ \"活动列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\", \"usertaskassignmentpackage\", \"formkeydefinitionpackage\", \"duedatedefinitionpackage\", \"prioritydefinitionpackage\", \"formpropertiespackage\", \"tasklistenerspackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"ActivitiesMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"ServiceTask\",\n" +
            "        \"title\" : \"服务任务\",\n" +
            "        \"description\" : \"An automatic task with service logic\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n\\t\\n\\t<g id=\\\"serviceTask\\\" transform=\\\"translate(3,3)\\\">\\n\\t<path oryx:anchors=\\\"top left\\\"\\n\\t\\tstyle=\\\"fill:#72a7d0;stroke:none\\\"\\n     d=\\\"M 8,1 7.5,2.875 c 0,0 -0.02438,0.250763 -0.40625,0.4375 C 7.05724,3.330353 7.04387,3.358818 7,3.375 6.6676654,3.4929791 6.3336971,3.6092802 6.03125,3.78125 6.02349,3.78566 6.007733,3.77681 6,3.78125 5.8811373,3.761018 5.8125,3.71875 5.8125,3.71875 l -1.6875,-1 -1.40625,1.4375 0.96875,1.65625 c 0,0 0.065705,0.068637 0.09375,0.1875 0.002,0.00849 -0.00169,0.022138 0,0.03125 C 3.6092802,6.3336971 3.4929791,6.6676654 3.375,7 3.3629836,7.0338489 3.3239228,7.0596246 3.3125,7.09375 3.125763,7.4756184 2.875,7.5 2.875,7.5 L 1,8 l 0,2 1.875,0.5 c 0,0 0.250763,0.02438 0.4375,0.40625 0.017853,0.03651 0.046318,0.04988 0.0625,0.09375 0.1129372,0.318132 0.2124732,0.646641 0.375,0.9375 -0.00302,0.215512 -0.09375,0.34375 -0.09375,0.34375 L 2.6875,13.9375 4.09375,15.34375 5.78125,14.375 c 0,0 0.1229911,-0.09744 0.34375,-0.09375 0.2720511,0.147787 0.5795915,0.23888 0.875,0.34375 0.033849,0.01202 0.059625,0.05108 0.09375,0.0625 C 7.4756199,14.874237 7.5,15.125 7.5,15.125 L 8,17 l 2,0 0.5,-1.875 c 0,0 0.02438,-0.250763 0.40625,-0.4375 0.03651,-0.01785 0.04988,-0.04632 0.09375,-0.0625 0.332335,-0.117979 0.666303,-0.23428 0.96875,-0.40625 0.177303,0.0173 0.28125,0.09375 0.28125,0.09375 l 1.65625,0.96875 1.40625,-1.40625 -0.96875,-1.65625 c 0,0 -0.07645,-0.103947 -0.09375,-0.28125 0.162527,-0.290859 0.262063,-0.619368 0.375,-0.9375 0.01618,-0.04387 0.04465,-0.05724 0.0625,-0.09375 C 14.874237,10.52438 15.125,10.5 15.125,10.5 L 17,10 17,8 15.125,7.5 c 0,0 -0.250763,-0.024382 -0.4375,-0.40625 C 14.669647,7.0572406 14.641181,7.0438697 14.625,7 14.55912,6.8144282 14.520616,6.6141566 14.4375,6.4375 c -0.224363,-0.4866 0,-0.71875 0,-0.71875 L 15.40625,4.0625 14,2.625 l -1.65625,1 c 0,0 -0.253337,0.1695664 -0.71875,-0.03125 l -0.03125,0 C 11.405359,3.5035185 11.198648,3.4455201 11,3.375 10.95613,3.3588185 10.942759,3.3303534 10.90625,3.3125 10.524382,3.125763 10.5,2.875 10.5,2.875 L 10,1 8,1 z m 1,5 c 1.656854,0 3,1.3431458 3,3 0,1.656854 -1.343146,3 -3,3 C 7.3431458,12 6,10.656854 6,9 6,7.3431458 7.3431458,6 9,6 z\\\" />\\n\\t</g>\\n  \\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\t\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/list/type.service.png\",\n" +
            "        \"groups\" : [ \"活动列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\", \"servicetaskclasspackage\", \"servicetaskexpressionpackage\", \"servicetaskdelegateexpressionpackage\", \"servicetaskfieldspackage\", \"servicetaskresultvariablepackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"ActivitiesMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"ScriptTask\",\n" +
            "        \"title\" : \"脚本任务\",\n" +
            "        \"description\" : \"An automatic task with script logic\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n\\t\\n\\t<g id=\\\"scriptTask\\\" transform=\\\"translate(2,2)\\\">\\n\\t\\t<path oryx:anchors=\\\"top left\\\"\\n\\t\\t\\td=\\\"m 5,2 0,0.094 c 0.23706,0.064 0.53189,0.1645 0.8125,0.375 0.5582,0.4186 1.05109,1.228 1.15625,2.5312 l 8.03125,0 1,0 1,0 c 0,-3 -2,-3 -2,-3 l -10,0 z M 4,3 4,13 2,13 c 0,3 2,3 2,3 l 9,0 c 0,0 2,0 2,-3 L 15,6 6,6 6,5.5 C 6,4.1111 5.5595,3.529 5.1875,3.25 4.8155,2.971 4.5,3 4.5,3 L 4,3 z\\\"\\n     \\t\\tstyle=\\\"fill:#72a7d0;stroke:none\\\"\\n\\t\\t/>\\n\\t</g>\\n  \\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\t\\n\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/list/type.script.png\",\n" +
            "        \"groups\" : [ \"活动列表\" ],\n" +
            "        \"propertyPackages\" : [ \"scriptformatpackage\", \"scripttextpackage\", \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"ActivitiesMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"BusinessRule\",\n" +
            "        \"title\" : \"规则任务\",\n" +
            "        \"description\" : \"An automatic task with rule logic\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n  \\t<defs>\\n\\t\\t<radialGradient id=\\\"background\\\" cx=\\\"10%\\\" cy=\\\"10%\\\" r=\\\"100%\\\" fx=\\\"10%\\\" fy=\\\"10%\\\">\\n\\t\\t\\t<stop offset=\\\"0%\\\" stop-color=\\\"#ffffff\\\" stop-opacity=\\\"1\\\"/>\\n\\t\\t\\t<stop id=\\\"fill_el\\\" offset=\\\"100%\\\" stop-color=\\\"#ffffcc\\\" stop-opacity=\\\"1\\\"/>\\n\\t\\t</radialGradient>\\n\\t</defs>\\n\\t\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n    \\n\\t<g id=\\\"businessRuleTask\\\" transform=\\\"translate(4,3)\\\">\\n\\t\\t<path oryx:anchors=\\\"top left\\\" \\n\\t\\t\\t d=\\\"m 1,2 0,14 16,0 0,-14 z m 1.45458,5.6000386 2.90906,0 0,2.7999224 -2.90906,0 z m 4.36364,0 8.72718,0 0,2.7999224 -8.72718,0 z m -4.36364,4.1998844 2.90906,0 0,2.800116 -2.90906,0 z m 4.36364,0 8.72718,0 0,2.800116 -8.72718,0 z\\\"\\n     \\t\\tstyle=\\\"fill:#72a7d0;stroke:none\\\"\\n\\t\\t/>\\n\\t</g>\\n\\t\\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/list/type.business.rule.png\",\n" +
            "        \"groups\" : [ \"活动列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\", \"ruletask_rulespackage\", \"ruletask_variables_inputpackage\", \"ruletask_excludepackage\", \"ruletask_resultpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"ActivitiesMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"ReceiveTask\",\n" +
            "        \"title\" : \"接受任务\",\n" +
            "        \"description\" : \"An task that waits for a signal\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n    \\n\\t<g id=\\\"receiveTask\\\" transform=\\\"translate(4,3)\\\">\\n\\t\\t<path oryx:anchors=\\\"left top\\\" \\n\\t\\t\\t style=\\\"fill:#16964d;stroke:none;\\\"\\n     \\t\\t d=\\\"m 0.5,2.5 0,13 17,0 0,-13 z M 2,4 6.5,8.5 2,13 z M 4,4 14,4 9,9 z m 12,0 0,9 -4.5,-4.5 z M 7.5,9.5 9,11 10.5,9.5 15,14 3,14 z\\\"\\n\\t\\t />\\n\\t</g>\\n\\t\\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/list/type.receive.png\",\n" +
            "        \"groups\" : [ \"活动列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"ActivitiesMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"ManualTask\",\n" +
            "        \"title\" : \"手工任务\",\n" +
            "        \"description\" : \"An automatic task with no logic\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n    <g id=\\\"manualTask\\\" transform=\\\"translate(3,1)\\\">\\n    \\t<path oryx:anchors=\\\"top left\\\"\\n    \\t\\tstyle=\\\"fill:#d1b575;stroke=none\\\"\\n     \\t\\td=\\\"m 17,9.3290326 c -0.0069,0.5512461 -0.455166,1.0455894 -0.940778,1.0376604 l -5.792746,0 c 0.0053,0.119381 0.0026,0.237107 0.0061,0.355965 l 5.154918,0 c 0.482032,-0.0096 0.925529,0.49051 0.919525,1.037574 -0.0078,0.537128 -0.446283,1.017531 -0.919521,1.007683 l -5.245273,0 c -0.01507,0.104484 -0.03389,0.204081 -0.05316,0.301591 l 2.630175,0 c 0.454137,-0.0096 0.872112,0.461754 0.866386,0.977186 C 13.619526,14.554106 13.206293,15.009498 12.75924,15 L 3.7753054,15 C 3.6045812,15 3.433552,14.94423 3.2916363,14.837136 c -0.00174,0 -0.00436,0 -0.00609,0 C 1.7212035,14.367801 0.99998255,11.458641 1,11.458641 L 1,7.4588393 c 0,0 0.6623144,-1.316333 1.8390583,-2.0872584 1.1767614,-0.7711868 6.8053358,-2.40497 7.2587847,-2.8052901 0.453484,-0.40032 1.660213,1.4859942 0.04775,2.4010487 C 8.5332315,5.882394 8.507351,5.7996113 8.4370292,5.7936859 l 6.3569748,-0.00871 c 0.497046,-0.00958 0.952273,0.5097676 0.94612,1.0738232 -0.0053,0.556126 -0.456176,1.0566566 -0.94612,1.0496854 l -4.72435,0 c 0.01307,0.1149374 0.0244,0.2281319 0.03721,0.3498661 l 5.952195,0 c 0.494517,-0.00871 0.947906,0.5066305 0.940795,1.0679848 z\\\"\\n    \\t/>\\n\\t</g>\\n\\t\\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/list/type.manual.png\",\n" +
            "        \"groups\" : [ \"活动列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"ActivitiesMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"MailTask\",\n" +
            "        \"title\" : \"邮件任务\",\n" +
            "        \"description\" : \"An mail task\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n    \\n\\t<g id=\\\"sendTask\\\" transform=\\\"translate(4,3)\\\">\\n\\t\\n\\t<!-- path here -->\\n\\t\\t<path oryx:anchors=\\\"top left\\\"\\n\\t\\t\\tstyle=\\\"fill:#16964d;stroke:none;\\\"\\n     \\t\\td=\\\"M 1 3 L 9 11 L 17 3 L 1 3 z M 1 5 L 1 13 L 5 9 L 1 5 z M 17 5 L 13 9 L 17 13 L 17 5 z M 6 10 L 1 15 L 17 15 L 12 10 L 9 13 L 6 10 z \\\"\\n     \\t/>\\n\\t</g>\\n\\t\\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/list/type.send.png\",\n" +
            "        \"groups\" : [ \"活动列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\", \"mailtasktopackage\", \"mailtaskfrompackage\", \"mailtasksubjectpackage\", \"mailtaskccpackage\", \"mailtaskbccpackage\", \"mailtasktextpackage\", \"mailtaskhtmlpackage\", \"mailtaskcharsetpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"ActivitiesMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"CamelTask\",\n" +
            "        \"title\" : \"Camel任务\",\n" +
            "        \"description\" : \"An task that sends a message to Camel\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n\\t\\n\\t<g id=\\\"camelTask\\\" transform=\\\"translate(4,4)\\\">\\n\\t\\t<path\\n     style=\\\"fill:#bd4848;fill-opacity:1\\\"\\n     d=\\\"m 8.1878027,15.383782 c -0.824818,-0.3427 0.375093,-1.1925 0.404055,-1.7743 0.230509,-0.8159 -0.217173,-1.5329 -0.550642,-2.2283 -0.106244,-0.5273 -0.03299,-1.8886005 -0.747194,-1.7818005 -0.712355,0.3776 -0.9225,1.2309005 -1.253911,1.9055005 -0.175574,1.0874 -0.630353,2.114 -0.775834,3.2123 -0.244009,0.4224 -1.741203,0.3888 -1.554386,-0.1397 0.651324,-0.3302 1.13227,-0.9222 1.180246,-1.6705 0.0082,-0.7042 -0.133578,-1.3681 0.302178,-2.0083 0.08617,-0.3202 0.356348,-1.0224005 -0.218996,-0.8051 -0.694517,0.2372 -1.651062,0.6128 -2.057645,-0.2959005 -0.696769,0.3057005 -1.102947,-0.611 -1.393127,-1.0565 -0.231079,-0.6218 -0.437041,-1.3041 -0.202103,-1.9476 -0.185217,-0.7514 -0.39751099,-1.5209 -0.35214999,-2.301 -0.243425,-0.7796 0.86000899,-1.2456 0.08581,-1.8855 -0.76078999,0.1964 -1.41630099,-0.7569 -0.79351899,-1.2877 0.58743,-0.52829998 1.49031699,-0.242 2.09856399,-0.77049998 0.816875,-0.3212 1.256619,0.65019998 1.923119,0.71939998 0.01194,0.7333 -0.0031,1.5042 -0.18417,2.2232 -0.194069,0.564 -0.811196,1.6968 0.06669,1.9398 0.738382,-0.173 1.095723,-0.9364 1.659041,-1.3729 0.727298,-0.3962 1.093982,-1.117 1.344137,-1.8675 0.400558,-0.8287 1.697676,-0.6854 1.955367,0.1758 0.103564,0.5511 0.9073983,1.7538 1.2472763,0.6846 0.121868,-0.6687 0.785541,-1.4454 1.518183,-1.0431 0.813587,0.4875 0.658233,1.6033 1.285504,2.2454 0.768715,0.8117 1.745394,1.4801 2.196633,2.5469 0.313781,0.8074 0.568552,1.707 0.496624,2.5733 -0.35485,0.8576005 -1.224508,-0.216 -0.64725,-0.7284 0.01868,-0.3794 -0.01834,-1.3264 -0.370249,-1.3272 -0.123187,0.7586 -0.152778,1.547 -0.10869,2.3154 0.270285,0.6662005 1.310741,0.7653005 1.060553,1.6763005 -0.03493,0.9801 0.294343,1.9505 0.148048,2.9272 -0.320479,0.2406 -0.79575,0.097 -1.185062,0.1512 -0.165725,0.3657 -0.40138,0.921 -1.020848,0.6744 -0.564671,0.1141 -1.246404,-0.266 -0.578559,-0.7715 0.679736,-0.5602 0.898618,-1.5362 0.687058,-2.3673 -0.529674,-1.108 -1.275984,-2.0954005 -1.839206,-3.1831005 -0.634619,-0.1004 -1.251945,0.6779 -1.956789,0.7408 -0.6065893,-0.038 -1.0354363,-0.06 -0.8495673,0.6969005 0.01681,0.711 0.152396,1.3997 0.157345,2.1104 0.07947,0.7464 0.171287,1.4944 0.238271,2.2351 0.237411,1.0076 -0.687542,1.1488 -1.414811,0.8598 z m 6.8675483,-1.8379 c 0.114364,-0.3658 0.206751,-1.2704 -0.114466,-1.3553 -0.152626,0.5835 -0.225018,1.1888 -0.227537,1.7919 0.147087,-0.1166 0.265559,-0.2643 0.342003,-0.4366 z\\\"\\n     />\\n\\t</g>\\n  \\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\t\\n\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/list/type.camel.png\",\n" +
            "        \"groups\" : [ \"活动列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\", \"cameltaskcamelcontextpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"ActivitiesMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"MuleTask\",\n" +
            "        \"title\" : \"Mule任务\",\n" +
            "        \"description\" : \"An task that sends a message to Mule\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n\\t\\n\\t<g id=\\\"muleTask\\\" transform=\\\"translate(4,4)\\\">\\n\\t\\t<path\\n     style=\\\"fill:#bd4848;fill-opacity:1\\\"\\n     d=\\\"M 8,0 C 3.581722,0 0,3.5817 0,8 c 0,4.4183 3.581722,8 8,8 4.418278,0 8,-3.5817 8,-8 L 16,7.6562 C 15.813571,3.3775 12.282847,0 8,0 z M 5.1875,2.7812 8,7.3437 10.8125,2.7812 c 1.323522,0.4299 2.329453,1.5645 2.8125,2.8438 1.136151,2.8609 -0.380702,6.4569 -3.25,7.5937 -0.217837,-0.6102 -0.438416,-1.2022 -0.65625,-1.8125 0.701032,-0.2274 1.313373,-0.6949 1.71875,-1.3125 0.73624,-1.2317 0.939877,-2.6305 -0.03125,-4.3125 l -2.75,4.0625 -0.65625,0 -0.65625,0 -2.75,-4 C 3.5268433,7.6916 3.82626,8.862 4.5625,10.0937 4.967877,10.7113 5.580218,11.1788 6.28125,11.4062 6.063416,12.0165 5.842837,12.6085 5.625,13.2187 2.755702,12.0819 1.238849,8.4858 2.375,5.625 2.858047,4.3457 3.863978,3.2112 5.1875,2.7812 z\\\"\\n     />\\n\\t</g>\\n  \\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\t\\n\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/list/type.mule.png\",\n" +
            "        \"groups\" : [ \"活动列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\", \"muletaskendpointurlpackage\", \"muletasklanguagepackage\", \"muletaskpayloadexpressionpackage\", \"muletaskresultvariablepackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"ActivitiesMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"SendTask\",\n" +
            "        \"title\" : \"Send task\",\n" +
            "        \"description\" : \"An task that sends a message\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n    \\n\\t<g id=\\\"sendTask\\\" transform=\\\"translate(4,3)\\\">\\n\\t\\n\\t<!-- path here -->\\n\\t\\t<path oryx:anchors=\\\"top left\\\"\\n\\t\\t\\tstyle=\\\"fill:#16964d;stroke:none;\\\"\\n     \\t\\td=\\\"M 1 3 L 9 11 L 17 3 L 1 3 z M 1 5 L 1 13 L 5 9 L 1 5 z M 17 5 L 13 9 L 17 13 L 17 5 z M 6 10 L 1 15 L 17 15 L 12 10 L 9 13 L 6 10 z \\\"\\n     \\t/>\\n\\t</g>\\n\\t\\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/list/type.send.png\",\n" +
            "        \"groups\" : [ \"活动列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"ActivitiesMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"SubProcess\",\n" +
            "        \"title\" : \"子流程\",\n" +
            "        \"description\" : \"子流程范围\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n   width=\\\"200\\\"\\n   height=\\\"160\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"50\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"80\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"110\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"70\\\" oryx:cy=\\\"159\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"100\\\" oryx:cy=\\\"159\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"130\\\" oryx:cy=\\\"159\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"199\\\" oryx:cy=\\\"50\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"199\\\" oryx:cy=\\\"80\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"199\\\" oryx:cy=\\\"110\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"70\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"100\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"130\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"100\\\" oryx:cy=\\\"80\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"120 100\\\" oryx:maximumSize=\\\"\\\" >\\n    <rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"190\\\" height=\\\"160\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"200\\\" height=\\\"160\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#ffffff\\\" />\\n\\t<rect id=\\\"border\\\" oryx:anchors=\\\"top bottom left right\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"2.5\\\" y=\\\"2.5\\\" width=\\\"195\\\" height=\\\"155\\\" rx=\\\"8\\\" ry=\\\"8\\\" stroke=\\\"black\\\" stroke-width=\\\"1\\\" fill=\\\"none\\\" />\\n\\t<text \\n\\t\\tfont-size=\\\"12\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"8\\\" \\n\\t\\ty=\\\"10\\\" \\n\\t\\toryx:align=\\\"top left\\\"\\n\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\toryx:anchors=\\\"left top\\\" \\n\\t\\tstroke=\\\"#373e48\\\">\\n\\t</text>\\n\\t\\n\\t<g \\tid=\\\"parallel\\\"\\n\\t\\ttransform=\\\"translate(1)\\\">\\n\\t\\t<path \\n\\t\\t\\tid=\\\"parallelpath\\\"\\n\\t\\t\\toryx:anchors=\\\"bottom\\\" \\n\\t\\t\\tfill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M96 145 v10 M100 145 v10 M104 145 v10\\\" \\n\\t\\t\\tstroke-width=\\\"2\\\"\\n\\t\\t/>\\n\\t</g>\\n\\t<g \\tid=\\\"sequential\\\"\\n\\t\\ttransform=\\\"translate(1)\\\">\\n\\t\\t<path \\n\\t\\t\\tid=\\\"sequentialpath\\\"\\n\\t\\t\\toryx:anchors=\\\"bottom\\\" \\n\\t\\t\\tfill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M95,154h10 M95,150h10 M95,146h10\\\"\\n\\t\\t/>\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/expanded.subprocess.png\",\n" +
            "        \"groups\" : [ \"结构列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"istransactionpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"EventSubProcess\",\n" +
            "        \"title\" : \"事件子流程\",\n" +
            "        \"description\" : \"一个事件周期的子流程\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n   width=\\\"200\\\"\\n   height=\\\"160\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"0\\\" oryx:cy=\\\"80\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"100\\\" oryx:cy=\\\"160\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"200\\\" oryx:cy=\\\"80\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"100\\\" oryx:cy=\\\"0\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"100\\\" oryx:cy=\\\"80\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"120 100\\\" oryx:maximumSize=\\\"\\\" >\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"190\\\" height=\\\"160\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:anchors=\\\"bottom top right left\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"200\\\" height=\\\"160\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" stroke-dasharray=\\\"2,2,2\\\" fill=\\\"#ffffff\\\" />\\n    \\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"8\\\" \\n\\t\\t\\ty=\\\"10\\\" \\n\\t\\t\\toryx:align=\\\"top left\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\toryx:anchors=\\\"left top\\\" \\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n    \\t\\n\\t<g id=\\\"none\\\"></g>\\n\\t\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/event.subprocess.png\",\n" +
            "        \"groups\" : [ \"结构列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"CallActivity\",\n" +
            "        \"title\" : \"调用活动\",\n" +
            "        \"description\" : \"一个调用活动\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n\\n   width=\\\"102\\\"\\n   height=\\\"82\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"1\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"left\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"79\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"20\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"40\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"99\\\" oryx:cy=\\\"60\\\" oryx:anchors=\\\"right\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"25\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"75\\\" oryx:cy=\\\"1\\\" oryx:anchors=\\\"top\\\" />\\n  \\t\\n  \\t<oryx:magnet oryx:cx=\\\"50\\\" oryx:cy=\\\"40\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\" oryx:minimumSize=\\\"50 40\\\">\\n\\t<rect id=\\\"text_frame\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"1\\\" y=\\\"1\\\" width=\\\"94\\\" height=\\\"79\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"none\\\" stroke-width=\\\"0\\\" fill=\\\"none\\\" />\\n    <rect oryx:resize=\\\"vertical horizontal\\\" oryx:anchors=\\\"bottom top right left\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"4\\\" fill=\\\"none\\\" />\\n\\t<rect id=\\\"bg_frame\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"100\\\" height=\\\"80\\\" rx=\\\"10\\\" ry=\\\"10\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"1\\\" fill=\\\"#f9f9f9\\\" />\\n\\t\\t<text \\n\\t\\t\\tfont-size=\\\"12\\\" \\n\\t\\t\\tid=\\\"text_name\\\" \\n\\t\\t\\tx=\\\"50\\\" \\n\\t\\t\\ty=\\\"40\\\" \\n\\t\\t\\toryx:align=\\\"middle center\\\"\\n\\t\\t\\toryx:fittoelem=\\\"text_frame\\\"\\n\\t\\t\\tstroke=\\\"#373e48\\\">\\n\\t\\t</text>\\n    \\n\\t<g id=\\\"parallel\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M46 70 v8 M50 70 v8 M54 70 v8\\\" stroke-width=\\\"2\\\" />\\n\\t</g>\\n\\t\\n\\t<g id=\\\"sequential\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" stroke-width=\\\"2\\\" d=\\\"M46,76h10M46,72h10 M46,68h10\\\"/>\\n\\t</g>\\n\\n\\t<g id=\\\"compensation\\\">\\n\\t\\t<path oryx:anchors=\\\"bottom\\\" fill=\\\"none\\\" stroke=\\\"#bbbbbb\\\" d=\\\"M 62 74 L 66 70 L 66 78 L 62 74 L 62 70 L 58 74 L 62 78 L 62 74\\\" stroke-width=\\\"1\\\" />\\n\\t</g>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"activity/task.png\",\n" +
            "        \"groups\" : [ \"结构列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"executionlistenerspackage\", \"callactivitycalledelementpackage\", \"callactivityinparameterspackage\", \"callactivityoutparameterspackage\", \"multiinstance_typepackage\", \"multiinstance_cardinalitypackage\", \"multiinstance_collectionpackage\", \"multiinstance_variablepackage\", \"multiinstance_conditionpackage\", \"isforcompensationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"Activity\", \"sequence_start\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"ExclusiveGateway\",\n" +
            "        \"title\" : \"互斥网关\",\n" +
            "        \"description\" : \"一个选择的网关\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   version=\\\"1.0\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\">\\n  <defs\\n     id=\\\"defs4\\\" />\\n  <oryx:magnets>\\n    <oryx:magnet\\n       oryx:default=\\\"yes\\\"\\n       oryx:cy=\\\"16\\\"\\n       oryx:cx=\\\"16\\\" />\\n  </oryx:magnets>\\t\\t\\t\\t\\t\\n  <g>\\n  \\n    <path\\n       d=\\\"M -4.5,16 L 16,-4.5 L 35.5,16 L 16,35.5z\\\"\\n       id=\\\"bg_frame\\\"\\n       fill=\\\"#ffffff\\\"\\n       stroke=\\\"#585858\\\"\\n       style=\\\"stroke-width:1\\\" />\\n    <g\\n       id=\\\"cross\\\">\\n      <path\\n      \\tid=\\\"crosspath\\\"\\n      \\tstroke=\\\"#585858\\\"\\n      \\tfill=\\\"#585858\\\"\\n        d=\\\"M 8.75,7.55 L 12.75,7.55 L 23.15,24.45 L 19.25,24.45 z\\\"\\n        style=\\\"stroke-width:1\\\" />\\n      <path\\n      \\tid=\\\"crosspath2\\\"\\n      \\tstroke=\\\"#585858\\\"\\n      \\tfill=\\\"#585858\\\"\\n        d=\\\"M 8.75,24.45 L 19.25,7.55 L 23.15,7.55 L 12.75,24.45 z\\\"\\n        style=\\\"stroke-width:1\\\" />\\n    </g>\\n\\t\\n\\t<text id=\\\"text_name\\\" x=\\\"26\\\" y=\\\"26\\\" oryx:align=\\\"left top\\\"/>\\n\\t\\n  </g>\\n</svg>\\n\",\n" +
            "        \"icon\" : \"gateway/exclusive.databased.png\",\n" +
            "        \"groups\" : [ \"网关列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"sequencefloworderpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"GatewaysMorph\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"ParallelGateway\",\n" +
            "        \"title\" : \"并行网关\",\n" +
            "        \"description\" : \"一个并行的网关\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   version=\\\"1.0\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\">\\n   \\n  <oryx:magnets>\\n    <oryx:magnet\\n       oryx:default=\\\"yes\\\"\\n       oryx:cy=\\\"16\\\"\\n       oryx:cx=\\\"16\\\" />\\n  </oryx:magnets>\\n  <g>\\n    <path\\n       d=\\\"M -4.5,16 L 16,-4.5 L 35.5,16 L 16,35.5z\\\"\\n       id=\\\"bg_frame\\\"\\n       fill=\\\"#ffffff\\\"\\n       stroke=\\\"#585858\\\"\\n       style=\\\"stroke-width:1\\\" />\\n    <path\\n       d=\\\"M 6.75,16 L 25.75,16 M 16,6.75 L 16,25.75\\\"\\n       id=\\\"path9\\\"\\n       stroke=\\\"#585858\\\"\\n       style=\\\"fill:none;stroke-width:3\\\" />\\n    \\n\\t<text id=\\\"text_name\\\" x=\\\"26\\\" y=\\\"26\\\" oryx:align=\\\"left top\\\"/>\\n\\t\\n  </g>\\n</svg>\\n\",\n" +
            "        \"icon\" : \"gateway/parallel.png\",\n" +
            "        \"groups\" : [ \"网关列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"sequencefloworderpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"GatewaysMorph\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"InclusiveGateway\",\n" +
            "        \"title\" : \"包容性网关\",\n" +
            "        \"description\" : \"一个包容性网关\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   version=\\\"1.0\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\">\\n  <oryx:magnets>\\n    <oryx:magnet\\n       oryx:default=\\\"yes\\\"\\n       oryx:cy=\\\"16\\\"\\n       oryx:cx=\\\"16\\\" />\\n  </oryx:magnets>\\n  <g>\\n\\n    <path\\n       d=\\\"M -4.5,16 L 16,-4.5 L 35.5,16 L 16,35.5z\\\"\\n       id=\\\"bg_frame\\\"\\n       fill=\\\"#ffffff\\\"\\n       stroke=\\\"#585858\\\"\\n       style=\\\"stroke-width:1\\\" />\\n    <circle\\n    \\tid=\\\"circle\\\"\\n    \\tstroke=\\\"#585858\\\"\\n\\t\\tcx=\\\"16\\\"\\n\\t\\tcy=\\\"16\\\"\\n\\t\\tr=\\\"9.75\\\"\\n\\t\\tstyle=\\\"fill:none;stroke-width:2.5\\\" />\\n    \\n\\t<text id=\\\"text_name\\\" x=\\\"26\\\" y=\\\"26\\\" oryx:align=\\\"left top\\\"/>\\n\\t\\n  </g>\\n</svg>\\n\",\n" +
            "        \"icon\" : \"gateway/inclusive.png\",\n" +
            "        \"groups\" : [ \"网关列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"sequencefloworderpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"GatewaysMorph\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"EventGateway\",\n" +
            "        \"title\" : \"事件网关\",\n" +
            "        \"description\" : \"一个事件网关\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   version=\\\"1.0\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\">\\n  <oryx:magnets>\\n    <oryx:magnet\\n       oryx:default=\\\"yes\\\"\\n       oryx:cy=\\\"16\\\"\\n       oryx:cx=\\\"16\\\" />\\n  </oryx:magnets>\\n  \\n  <g> \\n  \\t\\n\\t<path\\n\\t\\td=\\\"M -4.5,16 L 16,-4.5 L 35.5,16 L 16,35.5z\\\"\\n\\t\\tid=\\\"bg_frame\\\"\\n\\t\\tfill=\\\"#ffffff\\\"\\n\\t\\tstroke=\\\"#585858\\\"\\n\\t\\tstyle=\\\"stroke-width:1\\\" />\\n\\t<circle\\n\\t\\tid=\\\"circle\\\"\\n\\t\\tcx=\\\"16\\\"\\n\\t\\tcy=\\\"16\\\"\\n\\t\\tr=\\\"10.4\\\"\\n\\t\\tstroke=\\\"#585858\\\"\\n\\t\\tstyle=\\\"fill:none;stroke-width:0.5\\\" />\\n\\t<circle\\n\\t\\tid=\\\"circle2\\\"\\n\\t\\tcx=\\\"16\\\"\\n\\t\\tcy=\\\"16\\\"\\n\\t\\tr=\\\"11.7\\\"\\n\\t\\tstroke=\\\"#585858\\\"\\n\\t\\tstyle=\\\"fill:none;stroke-width:0.5\\\" />\\n\\t<path\\n\\t\\td=\\\"M 20.327514,22.344972 L 11.259248,22.344216 L 8.4577203,13.719549 L 15.794545,8.389969 L 23.130481,13.720774 L 20.327514,22.344972 z\\\"\\n\\t\\tid=\\\"middlePolygon\\\"\\n\\t\\tstroke=\\\"#585858\\\"\\n\\t\\tstyle=\\\"fill:none;fill-opacity:1;stroke-width:1.39999998;stroke-linejoin:bevel;stroke-opacity:1\\\" />\\n\\t\\n\\t\\n\\t<g id=\\\"instantiate\\\">\\n\\t\\n\\t\\t<path\\n\\t\\t\\td=\\\"M -4.5,16 L 16,-4.5 L 35.5,16 L 16,35.5z\\\"\\n\\t\\t\\tid=\\\"bg_frame2\\\"\\n\\t\\t\\tfill=\\\"#ffffff\\\"\\n\\t\\t\\tstroke=\\\"#585858\\\"\\n\\t\\t\\tstyle=\\\"stroke-width:1\\\" />\\n\\t\\t<circle\\n\\t\\t\\tid=\\\"circle3\\\"\\n\\t\\t\\tcx=\\\"16\\\"\\n\\t\\t\\tcy=\\\"16\\\"\\n\\t\\t\\tr=\\\"11\\\"\\n\\t\\t\\tstroke=\\\"#585858\\\"\\n\\t\\t\\tstyle=\\\"fill:none;stroke-width:1\\\" />\\n\\t\\t<path\\n\\t\\t\\td=\\\"M 20.327514,22.344972 L 11.259248,22.344216 L 8.4577203,13.719549 L 15.794545,8.389969 L 23.130481,13.720774 L 20.327514,22.344972 z\\\"\\n\\t\\t\\tid=\\\"middlePolygon2\\\"\\n\\t\\t\\tstroke=\\\"#585858\\\"\\n\\t\\t\\tstyle=\\\"fill:none;fill-opacity:1;stroke-width:1.39999998;stroke-linejoin:bevel;stroke-opacity:1\\\" />\\n\\t\\n\\t\\n\\t\\t<g id=\\\"parallel\\\">\\n\\t\\t\\t<path\\n\\t\\t\\t\\td=\\\"M -4.5,16 L 16,-4.5 L 35.5,16 L 16,35.5z\\\"\\n\\t\\t\\t\\tid=\\\"bg_frame3\\\"\\n\\t\\t\\t\\tfill=\\\"#ffffff\\\"\\n\\t\\t\\t\\tstroke=\\\"#585858\\\"\\n\\t\\t\\t\\tstyle=\\\"stroke-width:1\\\" />\\n\\t\\t\\t<circle id=\\\"frame5\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n\\t\\t\\t\\n\\t\\t\\t<path\\n\\t\\t\\t\\td=\\\"M 6.75,14 L6.75,18 L14,18 L14,24.75 L18,24.75 L18,18 L24.75,18 L24.75,14 L18,14 L18,6.75 L14,6.75 L14,14z\\\"\\n\\t\\t\\t\\tid=\\\"path92\\\"\\n\\t\\t\\t\\tstroke=\\\"#585858\\\"\\n\\t\\t\\t\\tstyle=\\\"fill:none;stroke-width:1\\\" />\\n\\t\\t\\n\\t\\t</g>\\n\\t\\n\\t</g>\\n\\t\\n\\t<text id=\\\"text_name\\\" x=\\\"26\\\" y=\\\"26\\\" oryx:align=\\\"left top\\\"/>\\n\\t\\n  </g>\\t\\n\\t\\n</svg>\\n\",\n" +
            "        \"icon\" : \"gateway/eventbased.png\",\n" +
            "        \"groups\" : [ \"网关列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"asynchronousdefinitionpackage\", \"exclusivedefinitionpackage\", \"sequencefloworderpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"GatewaysMorph\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"BoundaryErrorEvent\",\n" +
            "        \"title\" : \"边界错误事件\",\n" +
            "        \"description\" : \"一个捕捉BPMN异常的边界事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    \\n    <path\\n         stroke=\\\"#585858\\\"\\n         style=\\\"fill:none;stroke-width:1.5;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:10\\\"\\n         d=\\\"M 22.820839,11.171502 L 19.36734,24.58992 L 13.54138,14.281819 L 9.3386512,20.071607 L 13.048949,6.8323057 L 18.996148,16.132659 L 22.820839,11.171502 z\\\"\\n         id=\\\"errorPolygon\\\" />\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"catching/error.png\",\n" +
            "        \"groups\" : [ \"边界事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"errorrefpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"BoundaryEventsMorph\", \"IntermediateEventOnActivityBoundary\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"BoundaryTimerEvent\",\n" +
            "        \"title\" : \"定时边界事件\",\n" +
            "        \"description\" : \"一个定时触发的边界事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n    <circle \\n    \\tid=\\\"bg_frame\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"15\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"#ffffff\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 5.5, 3\\\" />\\n    \\t\\n    <circle \\n    \\tid=\\\"frame2_non_interrupting\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"12\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"none\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 4.5, 3\\\" />\\n    \\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame2\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    \\n    <path id=\\\"path1\\\" transform=\\\"translate(6,6)\\\"\\n    \\td=\\\"M 10 0 C 4.4771525 0 0 4.4771525 0 10 C 0 15.522847 4.4771525 20 10 20 C 15.522847 20 20 15.522847 20 10 C 20 4.4771525 15.522847 1.1842379e-15 10 0 z M 9.09375 1.03125 C 9.2292164 1.0174926 9.362825 1.0389311 9.5 1.03125 L 9.5 3.5 L 10.5 3.5 L 10.5 1.03125 C 15.063526 1.2867831 18.713217 4.9364738 18.96875 9.5 L 16.5 9.5 L 16.5 10.5 L 18.96875 10.5 C 18.713217 15.063526 15.063526 18.713217 10.5 18.96875 L 10.5 16.5 L 9.5 16.5 L 9.5 18.96875 C 4.9364738 18.713217 1.2867831 15.063526 1.03125 10.5 L 3.5 10.5 L 3.5 9.5 L 1.03125 9.5 C 1.279102 5.0736488 4.7225326 1.4751713 9.09375 1.03125 z M 9.5 5 L 9.5 8.0625 C 8.6373007 8.2844627 8 9.0680195 8 10 C 8 11.104569 8.8954305 12 10 12 C 10.931981 12 11.715537 11.362699 11.9375 10.5 L 14 10.5 L 14 9.5 L 11.9375 9.5 C 11.756642 8.7970599 11.20294 8.2433585 10.5 8.0625 L 10.5 5 L 9.5 5 z \\\"  \\n    \\tfill=\\\"#585858\\\" stroke=\\\"none\\\" />\\n    \\t\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"catching/timer.png\",\n" +
            "        \"groups\" : [ \"边界事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"timercycledefinitionpackage\", \"timerdatedefinitionpackage\", \"timerdurationdefinitionpackage\", \"timerenddatedefinitionpackage\", \"cancelactivitypackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"BoundaryEventsMorph\", \"IntermediateEventOnActivityBoundary\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"BoundarySignalEvent\",\n" +
            "        \"title\" : \"边界信号事件\",\n" +
            "        \"description\" : \"一个信号触发的边界事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n    <circle \\n    \\tid=\\\"bg_frame\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"15\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"#ffffff\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 5.5, 3\\\" />\\n    \\t\\n    <circle \\n    \\tid=\\\"frame2_non_interrupting\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"12\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"none\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 4.5, 3\\\" />\\n    \\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame2\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n\\t<path\\n\\t   id=\\\"signalCatching\\\"\\n\\t   stroke=\\\"#585858\\\"\\n       d=\\\"M 8.7124971,21.247342 L 23.333334,21.247342 L 16.022915,8.5759512 L 8.7124971,21.247342 z\\\"\\n       style=\\\"fill:none;stroke-width:1.4;stroke-miterlimit:4;stroke-dasharray:none\\\" />\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"catching/signal.png\",\n" +
            "        \"groups\" : [ \"边界事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"signalrefpackage\", \"cancelactivitypackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"BoundaryEventsMorph\", \"IntermediateEventOnActivityBoundary\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"BoundaryMessageEvent\",\n" +
            "        \"title\" : \"边界消息事件\",\n" +
            "        \"description\" : \"一个边界消息事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n    <circle \\n    \\tid=\\\"bg_frame\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"15\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"#ffffff\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 5.5, 3\\\" />\\n    \\t\\n    <circle \\n    \\tid=\\\"frame2_non_interrupting\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"12\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"none\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 4.5, 3\\\" />\\n    \\t\\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame2\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    \\n\\t<g id=\\\"messageCatching\\\">\\n\\t\\t<path transform=\\\"translate(7,7)\\\" id=\\\"path1\\\" stroke=\\\"none\\\" fill=\\\"#585858\\\" stroke-width=\\\"1\\\" d=\\\"M 1 3 L 9 11 L 17 3 L 1 3 z M 1 5 L 1 13 L 5 9 L 1 5 z M 17 5 L 13 9 L 17 13 L 17 5 z M 6 10 L 1 15 L 17 15 L 12 10 L 9 13 L 6 10 z \\\"/>\\n\\t</g>\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n\\t\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"catching/message.png\",\n" +
            "        \"groups\" : [ \"边界事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"messagerefpackage\", \"cancelactivitypackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"BoundaryEventsMorph\", \"IntermediateEventOnActivityBoundary\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"BoundaryCancelEvent\",\n" +
            "        \"title\" : \"边界取消事件\",\n" +
            "        \"description\" : \"一个边界取消事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n  \\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    \\n    <path\\n       d=\\\"M 7.2839105,10.27369 L 10.151395,7.4062062 L 15.886362,13.141174 L 21.621331,7.4062056 L 24.488814,10.273689 L 18.753846,16.008657 L 24.488815,21.743626 L 21.621331,24.611111 L 15.886362,18.876142 L 10.151394,24.611109 L 7.283911,21.743625 L 13.018878,16.008658 L 7.2839105,10.27369 z\\\"\\n       id=\\\"cancelCross\\\" fill=\\\"none\\\" stroke=\\\"#585858\\\" stroke-width=\\\"1.7\\\" />\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"catching/cancel.png\",\n" +
            "        \"groups\" : [ \"边界事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"BoundaryEventsMorph\", \"IntermediateEventOnActivityBoundary\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"BoundaryCompensationEvent\",\n" +
            "        \"title\" : \"边界修正事件\",\n" +
            "        \"description\" : \"一个边界修正事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n\\t\\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"1\\\"/>\\n    \\n    <polygon id=\\\"poly1\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1.4\\\" points=\\\"15,9 15,23 8,16\\\" stroke-linecap=\\\"butt\\\" stroke-linejoin=\\\"miter\\\" stroke-miterlimit=\\\"10\\\" />\\n    <polygon id=\\\"poly2\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1.4\\\" points=\\\"22,9 22,23 15,16\\\" stroke-linecap=\\\"butt\\\" stroke-linejoin=\\\"miter\\\" stroke-miterlimit=\\\"10\\\" />\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n </g>\\n</svg>\",\n" +
            "        \"icon\" : \"catching/compensation.png\",\n" +
            "        \"groups\" : [ \"边界事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"BoundaryEventsMorph\", \"IntermediateEventOnActivityBoundary\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"CatchTimerEvent\",\n" +
            "        \"title\" : \"中间定时器捕获事件\",\n" +
            "        \"description\" : \"定时器触发的中间捕获事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n    <circle \\n    \\tid=\\\"bg_frame\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"15\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"#ffffff\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 5.5, 3\\\" />\\n    \\t\\n    <circle \\n    \\tid=\\\"frame2_non_interrupting\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"12\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"none\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 4.5, 3\\\" />\\n    \\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame2\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    \\n    <path id=\\\"path1\\\" transform=\\\"translate(6,6)\\\"\\n    \\td=\\\"M 10 0 C 4.4771525 0 0 4.4771525 0 10 C 0 15.522847 4.4771525 20 10 20 C 15.522847 20 20 15.522847 20 10 C 20 4.4771525 15.522847 1.1842379e-15 10 0 z M 9.09375 1.03125 C 9.2292164 1.0174926 9.362825 1.0389311 9.5 1.03125 L 9.5 3.5 L 10.5 3.5 L 10.5 1.03125 C 15.063526 1.2867831 18.713217 4.9364738 18.96875 9.5 L 16.5 9.5 L 16.5 10.5 L 18.96875 10.5 C 18.713217 15.063526 15.063526 18.713217 10.5 18.96875 L 10.5 16.5 L 9.5 16.5 L 9.5 18.96875 C 4.9364738 18.713217 1.2867831 15.063526 1.03125 10.5 L 3.5 10.5 L 3.5 9.5 L 1.03125 9.5 C 1.279102 5.0736488 4.7225326 1.4751713 9.09375 1.03125 z M 9.5 5 L 9.5 8.0625 C 8.6373007 8.2844627 8 9.0680195 8 10 C 8 11.104569 8.8954305 12 10 12 C 10.931981 12 11.715537 11.362699 11.9375 10.5 L 14 10.5 L 14 9.5 L 11.9375 9.5 C 11.756642 8.7970599 11.20294 8.2433585 10.5 8.0625 L 10.5 5 L 9.5 5 z \\\"  \\n    \\tfill=\\\"#585858\\\" stroke=\\\"none\\\" />\\n    \\t\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"catching/timer.png\",\n" +
            "        \"groups\" : [ \"中间捕获事件列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"timercycledefinitionpackage\", \"timerdatedefinitionpackage\", \"timerdurationdefinitionpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"sequence_end\", \"CatchEventsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"CatchSignalEvent\",\n" +
            "        \"title\" : \"中间信号捕获事件\",\n" +
            "        \"description\" : \"信号触发的捕获事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n    <circle \\n    \\tid=\\\"bg_frame\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"15\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"#ffffff\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 5.5, 3\\\" />\\n    \\t\\n    <circle \\n    \\tid=\\\"frame2_non_interrupting\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"12\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"none\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 4.5, 3\\\" />\\n    \\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame2\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n\\t<path\\n\\t   id=\\\"signalCatching\\\"\\n\\t   stroke=\\\"#585858\\\"\\n       d=\\\"M 8.7124971,21.247342 L 23.333334,21.247342 L 16.022915,8.5759512 L 8.7124971,21.247342 z\\\"\\n       style=\\\"fill:none;stroke-width:1.4;stroke-miterlimit:4;stroke-dasharray:none\\\" />\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"catching/signal.png\",\n" +
            "        \"groups\" : [ \"中间捕获事件列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"signalrefpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"sequence_end\", \"CatchEventsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"CatchMessageEvent\",\n" +
            "        \"title\" : \"中间消息捕获事件\",\n" +
            "        \"description\" : \"一个消息触发的中间捕获事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n    <circle \\n    \\tid=\\\"bg_frame\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"15\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"#ffffff\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 5.5, 3\\\" />\\n    \\t\\n    <circle \\n    \\tid=\\\"frame2_non_interrupting\\\" \\n    \\tcx=\\\"16\\\" \\n    \\tcy=\\\"16\\\" \\n    \\tr=\\\"12\\\" \\n    \\tstroke=\\\"#585858\\\" \\n    \\tfill=\\\"none\\\" \\n    \\tstroke-width=\\\"1\\\"\\n    \\tstyle=\\\"stroke-dasharray: 4.5, 3\\\" />\\n    \\t\\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame2\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    \\n\\t<g id=\\\"messageCatching\\\">\\n\\t\\t<path transform=\\\"translate(7,7)\\\" id=\\\"path1\\\" stroke=\\\"none\\\" fill=\\\"#585858\\\" stroke-width=\\\"1\\\" d=\\\"M 1 3 L 9 11 L 17 3 L 1 3 z M 1 5 L 1 13 L 5 9 L 1 5 z M 17 5 L 13 9 L 17 13 L 17 5 z M 6 10 L 1 15 L 17 15 L 12 10 L 9 13 L 6 10 z \\\"/>\\n\\t</g>\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n\\t\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"catching/message.png\",\n" +
            "        \"groups\" : [ \"中间捕获事件列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"messagerefpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"sequence_start\", \"sequence_end\", \"CatchEventsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"ThrowNoneEvent\",\n" +
            "        \"title\" : \"中间抛出事件\",\n" +
            "        \"description\" : \"无触发器的中间抛出事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n  \\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"throwing/none.png\",\n" +
            "        \"groups\" : [ \"中间抛出事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"ThrowEventsMorph\", \"sequence_start\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"ThrowSignalEvent\",\n" +
            "        \"title\" : \"信号中间抛出事件\",\n" +
            "        \"description\" : \"一个信号触发的中间抛出事件\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"15\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"1\\\"/>\\n    <circle id=\\\"frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"12\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"1\\\"/>\\n    <path\\n\\t   id=\\\"signalThrowing\\\"\\n       d=\\\"M 8.7124971,21.247342 L 23.333334,21.247342 L 16.022915,8.5759512 L 8.7124971,21.247342 z\\\"\\n       fill=\\\"#585858\\\"\\n       stroke=\\\"#585858\\\"\\n       style=\\\"stroke-width:1.4;stroke-miterlimit:4;stroke-dasharray:none\\\" />\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"33\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"throwing/signal.png\",\n" +
            "        \"groups\" : [ \"中间抛出事件\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"signalrefpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"ThrowEventsMorph\", \"sequence_start\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"EndNoneEvent\",\n" +
            "        \"title\" : \"结束任务\",\n" +
            "        \"description\" : \"一个无触发器的结束任务\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\">\\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"14\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"3\\\"/>\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"32\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"endevent/none.png\",\n" +
            "        \"groups\" : [ \"结束任务列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"EndEventsMorph\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"EndErrorEvent\",\n" +
            "        \"title\" : \"结束错误任务\",\n" +
            "        \"description\" : \"An end event that throws an error event\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <oryx:docker oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" />\\n  <g pointer-events=\\\"fill\\\">\\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"14\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"3\\\"/>\\n    \\n    <path\\n         fill=\\\"#585858\\\"\\n         stroke=\\\"#585858\\\"\\n         style=\\\"stroke-width:1.5;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:10\\\"\\n         d=\\\"M 22.820839,11.171502 L 19.36734,24.58992 L 13.54138,14.281819 L 9.3386512,20.071607 L 13.048949,6.8323057 L 18.996148,16.132659 L 22.820839,11.171502 z\\\"\\n         id=\\\"errorPolygon\\\" />\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"32\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"endevent/error.png\",\n" +
            "        \"groups\" : [ \"结束任务列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"errorrefpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"EndEventsMorph\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"EndCancelEvent\",\n" +
            "        \"title\" : \"结束取消任务\",\n" +
            "        \"description\" : \"A cancel end event\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\">\\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"14\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"3\\\"/>\\n    \\n    <path id=\\\"path1\\\" d=\\\"M 9 9 L 23 23 M 9 23 L 23 9\\\" fill=\\\"none\\\" stroke=\\\"#585858\\\" stroke-width=\\\"5\\\" />\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"32\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"endevent/cancel.png\",\n" +
            "        \"groups\" : [ \"结束任务列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"EndEventsMorph\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"EndTerminateEvent\",\n" +
            "        \"title\" : \"终结任务\",\n" +
            "        \"description\" : \"A terminate end event\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   width=\\\"40\\\"\\n   height=\\\"40\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"16\\\" oryx:cy=\\\"16\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"fill\\\">\\n    <circle id=\\\"bg_frame\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"14\\\" stroke=\\\"#585858\\\" fill=\\\"#ffffff\\\" stroke-width=\\\"3\\\"/>\\n    \\n    <circle id=\\\"circle1\\\" cx=\\\"16\\\" cy=\\\"16\\\" r=\\\"9\\\" stroke=\\\"#585858\\\" fill=\\\"#585858\\\" stroke-width=\\\"1\\\"/>\\n\\t<text font-size=\\\"11\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\tx=\\\"16\\\" y=\\\"32\\\" \\n\\t\\toryx:align=\\\"top center\\\" \\n\\t\\tstroke=\\\"#373e48\\\"\\n\\t></text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"endevent/terminate.png\",\n" +
            "        \"groups\" : [ \"结束任务列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"executionlistenerspackage\", \"terminateAllpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"EndEventsMorph\", \"sequence_end\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"Pool\",\n" +
            "        \"title\" : \"池\",\n" +
            "        \"description\" : \"A pool to stucture the process definition\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n   width=\\\"600\\\"\\n   height=\\\"250\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"0\\\" oryx:cy=\\\"124\\\" oryx:anchors=\\\"left\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"299\\\" oryx:cy=\\\"249\\\" oryx:anchors=\\\"bottom\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"599\\\" oryx:cy=\\\"124\\\" oryx:anchors=\\\"right\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"299\\\" oryx:cy=\\\"0\\\" oryx:anchors=\\\"top\\\" />\\n  \\t<oryx:magnet oryx:cx=\\\"299\\\" oryx:cy=\\\"124\\\" oryx:default=\\\"yes\\\" />\\n  </oryx:magnets>\\n  <g pointer-events=\\\"none\\\" >\\n    <defs>\\n\\t\\t<radialGradient id=\\\"background\\\" cx=\\\"0%\\\" cy=\\\"10%\\\" r=\\\"100%\\\" fx=\\\"20%\\\" fy=\\\"10%\\\">\\n\\t\\t\\t<stop offset=\\\"0%\\\" stop-color=\\\"#ffffff\\\" stop-opacity=\\\"1\\\"/>\\n\\t\\t\\t<stop id=\\\"fill_el\\\" offset=\\\"100%\\\" stop-color=\\\"#ffffff\\\" stop-opacity=\\\"1\\\"/>\\n\\t\\t</radialGradient>\\n\\t</defs>\\n\\t  \\t\\n  \\t<rect\\n  \\t\\tid=\\\"border\\\"\\n  \\t\\tclass=\\\"stripable-element-force\\\"\\n  \\t\\toryx:resize=\\\"vertical horizontal\\\"\\n  \\t\\tx=\\\"0\\\"\\n  \\t\\ty=\\\"0\\\"\\n  \\t\\twidth=\\\"600\\\"\\n  \\t\\theight=\\\"250\\\"\\n  \\t\\tfill=\\\"none\\\"\\n  \\t\\tstroke-width=\\\"9\\\"\\n  \\t\\tstroke=\\\"none\\\"\\n  \\t\\tvisibility=\\\"visible\\\"\\n  \\t\\tpointer-events=\\\"stroke\\\"\\n  \\t/>\\n    <rect\\n    \\tid=\\\"c\\\"\\n    \\toryx:resize=\\\"vertical horizontal\\\"\\n    \\tx=\\\"0\\\"\\n    \\ty=\\\"0\\\"\\n    \\twidth=\\\"600\\\" \\n    \\theight=\\\"250\\\" \\n    \\tstroke=\\\"black\\\" \\n    \\tfill=\\\"url(#background) white\\\"\\n    \\tfill-opacity=\\\"0.3\\\" \\n    />\\n    \\n\\t<rect \\n\\t\\tid=\\\"caption\\\"\\n\\t\\toryx:anchors=\\\"left top bottom\\\"\\n\\t\\tx=\\\"0\\\"\\n\\t\\ty=\\\"0\\\"\\n\\t\\twidth=\\\"30\\\"\\n\\t\\theight=\\\"250\\\"\\n\\t\\tstroke=\\\"black\\\"\\n\\t\\tstroke-width=\\\"1\\\"\\n\\t\\tfill=\\\"url(#background) white\\\"\\n\\t\\tpointer-events=\\\"all\\\"\\n\\t/>\\n\\t\\n\\t<rect \\n\\t\\tid=\\\"captionDisableAntialiasing\\\"\\n\\t\\toryx:anchors=\\\"left top bottom\\\"\\n\\t\\tx=\\\"0\\\"\\n\\t\\ty=\\\"0\\\"\\n\\t\\twidth=\\\"30\\\"\\n\\t\\theight=\\\"250\\\"\\n\\t\\tstroke=\\\"black\\\"\\n\\t\\tstroke-width=\\\"1\\\"\\n\\t\\tfill=\\\"url(#background) white\\\"\\n\\t\\tpointer-events=\\\"all\\\"\\n\\t/>\\n\\t\\n    <text x=\\\"13\\\" y=\\\"125\\\" font-size=\\\"12\\\" id=\\\"text_name\\\" oryx:fittoelem=\\\"caption\\\" oryx:align=\\\"middle center\\\" oryx:anchors=\\\"left\\\" oryx:rotate=\\\"270\\\" fill=\\\"black\\\" stroke=\\\"black\\\"></text>\\n    \\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"swimlane/pool.png\",\n" +
            "        \"groups\" : [ \"泳道列表\" ],\n" +
            "        \"layout\" : [ {\n" +
            "            \"type\" : \"layout.bpmn2_0.pool\"\n" +
            "        } ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"process_idpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"canContainArtifacts\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"Lane\",\n" +
            "        \"title\" : \"泳道\",\n" +
            "        \"description\" : \"A lane to stucture the process definition\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n   width=\\\"600\\\"\\n   height=\\\"250\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <g pointer-events=\\\"none\\\" >\\n  \\n     <defs>\\n\\t\\t<radialGradient id=\\\"background\\\" cx=\\\"0%\\\" cy=\\\"10%\\\" r=\\\"200%\\\" fx=\\\"20%\\\" fy=\\\"10%\\\">\\n\\t\\t\\t<stop offset=\\\"0%\\\" stop-color=\\\"#ffffff\\\" stop-opacity=\\\"1\\\"/>\\n\\t\\t\\t<stop id=\\\"fill_el\\\" offset=\\\"100%\\\" stop-color=\\\"#ffffff\\\" stop-opacity=\\\"0\\\"/>\\n\\t\\t</radialGradient>\\n\\t</defs>\\n\\t\\n  \\t<rect id=\\\"border_invisible\\\" class=\\\"stripable-element-force\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"600\\\" height=\\\"250\\\" fill=\\\"none\\\" stroke-width=\\\"10\\\" stroke=\\\"white\\\" visibility=\\\"hidden\\\" pointer-events=\\\"stroke\\\"/>\\t\\t\\n\\t<rect id=\\\"border\\\" oryx:resize=\\\"vertical horizontal\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"600\\\" height=\\\"250\\\" stroke=\\\"black\\\" stroke-width=\\\"1\\\" fill=\\\"url(#background) white\\\" pointer-events=\\\"none\\\" />\\n\\t\\n\\t\\n\\t<rect \\n\\t\\tid=\\\"caption\\\"\\n\\t\\toryx:anchors=\\\"left top bottom\\\"\\n\\t\\tx=\\\"0\\\"\\n\\t\\ty=\\\"1\\\"\\n\\t\\twidth=\\\"30\\\"\\n\\t\\theight=\\\"248\\\"\\n\\t\\tstroke=\\\"black\\\"\\n\\t\\tstroke-width=\\\"0\\\"\\n\\t\\tfill=\\\"white\\\"\\n\\t\\tvisibility=\\\"hidden\\\"\\n\\t\\tclass=\\\"stripable-element-force\\\"\\n\\t\\tpointer-events=\\\"all\\\"\\n\\t/>\\n\\t\\n\\t<path\\n\\t\\tstroke=\\\"black\\\"\\n\\t\\tstroke-width=\\\"1\\\"\\n\\t\\tfill=\\\"none\\\"\\n\\t\\td=\\\"M 0,0 L 0,250\\\"\\n        oryx:anchors=\\\"left top bottom\\\"\\n        id=\\\"captionDisableAntialiasing\\\"\\n    />\\n\\t\\n\\t<!--rect \\n\\t\\tid=\\\"captionDisableAntialiasing\\\"\\n\\t\\toryx:anchors=\\\"left top bottom\\\"\\n\\t\\tx=\\\"0\\\"\\n\\t\\ty=\\\"0\\\"\\n\\t\\twidth=\\\"30\\\"\\n\\t\\theight=\\\"250\\\"\\n\\t\\tstroke=\\\"black\\\"\\n\\t\\tstroke-width=\\\"1\\\"\\n\\t\\tfill=\\\"url(#background) white\\\"\\n\\t/-->\\n\\t\\n    <text \\n\\t\\tx=\\\"13\\\"\\n\\t\\ty=\\\"125\\\"\\n\\t\\toryx:rotate=\\\"270\\\" \\n\\t\\tfont-size=\\\"12\\\" \\n\\t\\tid=\\\"text_name\\\" \\n\\t\\toryx:align=\\\"middle center\\\" \\n\\t\\toryx:anchors=\\\"left\\\"\\n\\t\\toryx:fittoelem=\\\"caption\\\"\\n\\t\\tfill=\\\"black\\\" \\n\\t\\tstroke=\\\"black\\\">\\n\\t</text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"swimlane/lane.png\",\n" +
            "        \"groups\" : [ \"泳道列表\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"PoolChild\", \"canContainArtifacts\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"edge\",\n" +
            "        \"id\" : \"SequenceFlow\",\n" +
            "        \"title\" : \"顺序流\",\n" +
            "        \"description\" : \"顺序流定义活动的执行顺序\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\r\\n<svg\\r\\n\\txmlns=\\\"http://www.w3.org/2000/svg\\\"\\r\\n\\txmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\r\\n\\tversion=\\\"1.0\\\"\\r\\n\\toryx:edge=\\\"edge\\\" >\\r\\n\\t<defs>\\r\\n\\t  \\t<marker id=\\\"start\\\" refX=\\\"1\\\" refY=\\\"5\\\" markerUnits=\\\"userSpaceOnUse\\\" markerWidth=\\\"17\\\" markerHeight=\\\"11\\\" orient=\\\"auto\\\">\\r\\n\\t  \\t\\t<!-- <path id=\\\"conditional\\\"   d=\\\"M 0 6 L 8 1 L 15 5 L 8 9 L 1 5\\\" fill=\\\"white\\\" stroke=\\\"black\\\" stroke-width=\\\"1\\\" /> -->\\r\\n\\t\\t\\t<path id=\\\"default\\\" d=\\\"M 5 0 L 11 10\\\" fill=\\\"white\\\" stroke=\\\"#585858\\\" stroke-width=\\\"1\\\" />\\r\\n\\t  \\t</marker>\\r\\n\\t  \\t<marker id=\\\"end\\\" refX=\\\"15\\\" refY=\\\"6\\\" markerUnits=\\\"userSpaceOnUse\\\" markerWidth=\\\"15\\\" markerHeight=\\\"12\\\" orient=\\\"auto\\\">\\r\\n\\t  \\t\\t<path id=\\\"arrowhead\\\" d=\\\"M 0 1 L 15 6 L 0 11z\\\" fill=\\\"#585858\\\" stroke=\\\"#585858\\\" stroke-linejoin=\\\"round\\\" stroke-width=\\\"2\\\" />\\r\\n\\t  \\t</marker>\\r\\n\\t</defs>\\r\\n\\t<g id=\\\"edge\\\">\\r\\n\\t\\t<path id=\\\"bg_frame\\\" d=\\\"M10 50 L210 50\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"2\\\" stroke-linecap=\\\"round\\\" stroke-linejoin=\\\"round\\\" marker-start=\\\"url(#start)\\\" marker-end=\\\"url(#end)\\\" />\\r\\n\\t\\t<text id=\\\"text_name\\\" x=\\\"0\\\" y=\\\"0\\\" oryx:edgePosition=\\\"startTop\\\"/>\\r\\n\\t</g>\\r\\n</svg>\",\n" +
            "        \"icon\" : \"connector/sequenceflow.png\",\n" +
            "        \"groups\" : [ \"连接对象\" ],\n" +
            "        \"layout\" : [ {\n" +
            "            \"type\" : \"layout.bpmn2_0.sequenceflow\"\n" +
            "        } ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"conditionsequenceflowpackage\", \"executionlistenerspackage\", \"defaultflowpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"ConnectingObjectsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"edge\",\n" +
            "        \"id\" : \"MessageFlow\",\n" +
            "        \"title\" : \"消息流\",\n" +
            "        \"description\" : \"Message flow to connect elements in different pools.\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\r\\n<svg\\r\\n\\txmlns=\\\"http://www.w3.org/2000/svg\\\"\\r\\n\\txmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\r\\n\\tversion=\\\"1.0\\\"\\r\\n\\toryx:edge=\\\"edge\\\" >\\r\\n\\t<defs>\\r\\n\\t\\t<marker id=\\\"start\\\" oryx:optional=\\\"yes\\\" oryx:enabled=\\\"yes\\\" refX=\\\"5\\\" refY=\\\"5\\\" markerUnits=\\\"userSpaceOnUse\\\" markerWidth=\\\"10\\\" markerHeight=\\\"10\\\" orient=\\\"auto\\\">\\r\\n\\t  \\t\\t<!-- <path d=\\\"M 10 10 L 0 5 L 10 0\\\" fill=\\\"none\\\" stroke=\\\"#585858\\\" /> -->\\r\\n\\t  \\t\\t<circle id=\\\"arrowhead\\\" cx=\\\"5\\\" cy=\\\"5\\\" r=\\\"5\\\" fill=\\\"white\\\" stroke=\\\"black\\\" />\\r\\n\\t  \\t</marker>\\r\\n\\r\\n\\t  \\t<marker id=\\\"end\\\" refX=\\\"10\\\" refY=\\\"5\\\" markerUnits=\\\"userSpaceOnUse\\\" markerWidth=\\\"10\\\" markerHeight=\\\"10\\\" orient=\\\"auto\\\">\\r\\n\\t  \\t\\t<path id=\\\"arrowhead2\\\" d=\\\"M 0 0 L 10 5 L 0 10 L 0 0\\\" fill=\\\"white\\\" stroke=\\\"#585858\\\" />\\r\\n\\t  \\t</marker>\\r\\n\\t</defs>\\r\\n\\t<g id=\\\"edge\\\">\\r\\n\\t    <path id=\\\"bg_frame\\\" d=\\\"M10 50 L210 50\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"2\\\" stroke-dasharray=\\\"3, 4\\\" marker-start=\\\"url(#start)\\\" marker-end=\\\"url(#end)\\\" />\\r\\n\\t\\t<text id=\\\"text_name\\\" x=\\\"0\\\" y=\\\"0\\\" oryx:edgePosition=\\\"midTop\\\"/>\\r\\n\\t</g>\\r\\n</svg>\",\n" +
            "        \"icon\" : \"connector/messageflow.png\",\n" +
            "        \"groups\" : [ \"连接对象\" ],\n" +
            "        \"layout\" : [ {\n" +
            "            \"type\" : \"layout.bpmn2_0.sequenceflow\"\n" +
            "        } ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"ConnectingObjectsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"edge\",\n" +
            "        \"id\" : \"Association\",\n" +
            "        \"title\" : \"注释\",\n" +
            "        \"description\" : \"连接一个注释到指定元素\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\r\\n<svg\\r\\n\\txmlns=\\\"http://www.w3.org/2000/svg\\\"\\r\\n\\txmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\r\\n\\tversion=\\\"1.0\\\"\\r\\n\\toryx:edge=\\\"edge\\\" >\\r\\n\\t<g id=\\\"edge\\\">\\r\\n\\t    <path id=\\\"bg_frame\\\" d=\\\"M10 50 L210 50\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"2\\\" stroke-dasharray=\\\"3, 4\\\" />\\r\\n\\t\\t<text id=\\\"name\\\" x=\\\"0\\\" y=\\\"0\\\" oryx:edgePosition=\\\"midTop\\\" oryx:offsetTop=\\\"6\\\" style=\\\"font-size:9px;\\\"/>\\r\\n\\t</g>\\r\\n</svg>\",\n" +
            "        \"icon\" : \"connector/association.undirected.png\",\n" +
            "        \"groups\" : [ \"连接对象\" ],\n" +
            "        \"layout\" : [ {\n" +
            "            \"type\" : \"layout.bpmn2_0.sequenceflow\"\n" +
            "        } ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"ConnectingObjectsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"edge\",\n" +
            "        \"id\" : \"DataAssociation\",\n" +
            "        \"title\" : \"日期注释\",\n" +
            "        \"description\" : \"连接一个日期注释到指定元素\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\r\\n<svg\\r\\n\\txmlns=\\\"http://www.w3.org/2000/svg\\\"\\r\\n\\txmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\r\\n\\tversion=\\\"1.0\\\"\\r\\n\\toryx:edge=\\\"edge\\\" >\\r\\n\\t<defs>\\r\\n\\t  \\t<marker id=\\\"end\\\" refX=\\\"10\\\" refY=\\\"5\\\" markerUnits=\\\"userSpaceOnUse\\\" markerWidth=\\\"10\\\" markerHeight=\\\"10\\\" orient=\\\"auto\\\">\\r\\n\\t  \\t\\t<path id=\\\"arrowhead\\\" d=\\\"M 0 0 L 10 5 L 0 10\\\" fill=\\\"none\\\" stroke=\\\"#585858\\\" />\\r\\n\\t  \\t</marker>\\r\\n\\t</defs>\\r\\n\\t<g id=\\\"edge\\\">\\r\\n\\t    <path id=\\\"bg_frame\\\" d=\\\"M10 50 L210 50\\\" stroke=\\\"#585858\\\" fill=\\\"none\\\" stroke-width=\\\"2\\\" stroke-dasharray=\\\"3, 4\\\" marker-end=\\\"url(#end)\\\" />\\r\\n\\t\\t<text id=\\\"name\\\" x=\\\"0\\\" y=\\\"0\\\" oryx:edgePosition=\\\"midTop\\\" oryx:offsetTop=\\\"6\\\" style=\\\"font-size:9px;\\\"/>\\r\\n\\t</g>\\r\\n</svg>\",\n" +
            "        \"icon\" : \"connector/association.unidirectional.png\",\n" +
            "        \"groups\" : [ \"连接对象\" ],\n" +
            "        \"layout\" : [ {\n" +
            "            \"type\" : \"layout.bpmn2_0.sequenceflow\"\n" +
            "        } ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"ConnectingObjectsMorph\", \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"TextAnnotation\",\n" +
            "        \"title\" : \"文本注释\",\n" +
            "        \"description\" : \"连接一个文本注释到指定元素\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n<svg\\n   xmlns=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\n   xmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\n   xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\n   width=\\\"102\\\"\\n   height=\\\"51\\\"\\n   version=\\\"1.0\\\">\\n  <defs></defs>\\n  <oryx:magnets>\\n  \\t<oryx:magnet oryx:cx=\\\"2\\\" oryx:cy=\\\"25\\\" oryx:anchors=\\\"left\\\" oryx:default=\\\"yes\\\"/>\\n  </oryx:magnets>\\n  <g pointer-events=\\\"all\\\" oryx:minimumSize=\\\"10 20\\\" oryx:maximumSize=\\\"\\\" >\\n  <rect \\n\\tid=\\\"textannotationrect\\\"\\n\\toryx:resize=\\\"vertical horizontal\\\"\\n\\tx=\\\"1\\\" \\n\\ty=\\\"1\\\"\\n\\twidth=\\\"100\\\"\\n\\theight=\\\"50\\\"\\n\\tstroke=\\\"none\\\"\\n\\tfill=\\\"none\\\" />\\n  <path \\n  \\tid = \\\"frame\\\"\\n\\td=\\\"M20,1 L1,1 L1,50 L20,50\\\" \\n\\toryx:anchors=\\\"top bottom left\\\" \\n\\tstroke=\\\"#585858\\\" \\n\\tfill=\\\"none\\\" \\n\\tstroke-width=\\\"1\\\" />\\n    \\n    <text \\n\\t\\tfont-size=\\\"12\\\" \\n\\t\\tid=\\\"text\\\" \\n\\t\\tx=\\\"5\\\" \\n\\t\\ty=\\\"25\\\" \\n\\t\\toryx:align=\\\"middle left\\\"\\n\\t\\toryx:fittoelem=\\\"textannotationrect\\\"\\n\\t\\toryx:anchors=\\\"left\\\"\\n\\t\\tstroke=\\\"#373e48\\\">\\n\\t</text>\\n  </g>\\n</svg>\",\n" +
            "        \"icon\" : \"artifact/text.annotation.png\",\n" +
            "        \"groups\" : [ \"加工\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\", \"textpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"all\" ]\n" +
            "    }, {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"DataStore\",\n" +
            "        \"title\" : \"Data store\",\n" +
            "        \"description\" : \"Reference to a data store.\",\n" +
            "        \"view\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" standalone=\\\"no\\\" ?>\\r\\n<svg \\r\\n\\txmlns=\\\"http://www.w3.org/2000/svg\\\"\\r\\n\\txmlns:svg=\\\"http://www.w3.org/2000/svg\\\"\\r\\n   \\txmlns:oryx=\\\"http://www.b3mn.org/oryx\\\"\\r\\n   \\txmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\"\\r\\n\\t\\r\\n\\twidth=\\\"63.001px\\\" \\r\\n\\theight=\\\"61.173px\\\"\\r\\n\\tversion=\\\"1.0\\\">\\r\\n\\t<defs></defs>\\r\\n\\t<oryx:magnets>\\r\\n\\t\\t<oryx:magnet oryx:cx=\\\"0\\\" oryx:cy=\\\"30.5865\\\" oryx:anchors=\\\"left\\\" />\\r\\n\\t\\t<oryx:magnet oryx:cx=\\\"31.5005\\\" oryx:cy=\\\"61.173\\\" oryx:anchors=\\\"bottom\\\" />\\r\\n\\t\\t<oryx:magnet oryx:cx=\\\"63.001\\\" oryx:cy=\\\"30.5865\\\" oryx:anchors=\\\"right\\\" />\\r\\n\\t\\t<oryx:magnet oryx:cx=\\\"31.5005\\\" oryx:cy=\\\"0\\\" oryx:anchors=\\\"top\\\" />\\r\\n\\t\\t<oryx:magnet oryx:cx=\\\"31.5005\\\" oryx:cy=\\\"30.5865\\\" oryx:default=\\\"yes\\\" />\\r\\n\\t</oryx:magnets>\\r\\n\\t\\r\\n\\t<g>\\r\\n\\t\\t<defs>\\r\\n\\t\\t\\t<radialGradient id=\\\"background\\\" cx=\\\"30%\\\" cy=\\\"30%\\\" r=\\\"50%\\\" fx=\\\"0%\\\" fy=\\\"0%\\\">\\r\\n\\t\\t\\t\\t<stop offset=\\\"0%\\\" stop-color=\\\"#ffffff\\\" stop-opacity=\\\"1\\\"></stop>\\r\\n\\t\\t\\t\\t<stop offset=\\\"100%\\\" stop-color=\\\"#ffffff\\\" stop-opacity=\\\"1\\\" id=\\\"fill_el\\\"></stop>\\r\\n\\t\\t\\t</radialGradient>\\r\\n\\t\\t</defs>\\r\\n\\t\\t\\r\\n\\t\\t<path id=\\\"bg_frame\\\" fill=\\\"url(#background) #ffffff\\\" stroke=\\\"#000000\\\" d=\\\"M31.634,0.662c20.013,0,31.292,3.05,31.292,5.729c0,2.678,0,45.096,0,48.244\\r\\n\\t\\t\\tc0,3.148-16.42,6.2-31.388,6.2c-14.968,0-30.613-2.955-30.613-6.298c0-3.342,0-45.728,0-48.05\\r\\n\\t\\t\\tC0.925,4.165,11.622,0.662,31.634,0.662z\\\"/>\\r\\n\\t\\t<path id=\\\"bg_frame2\\\" fill=\\\"none\\\" stroke=\\\"#000000\\\" d=\\\"\\r\\n\\t\\t\\tM62.926,15.69c0,1.986-3.62,6.551-31.267,6.551c-27.646,0-30.734-4.686-30.734-6.454 M0.925,11.137\\r\\n\\t\\t\\tc0,1.769,3.088,6.455,30.734,6.455c27.647,0,31.267-4.565,31.267-6.551 M0.925,6.487c0,2.35,3.088,6.455,30.734,6.455\\r\\n\\t\\t\\tc27.647,0,31.267-3.912,31.267-6.552 M62.926,6.391v4.844 M0.949,6.391v4.844 M62.926,11.041v4.844 M0.949,11.041v4.844\\\"/>\\r\\n\\t\\t\\t \\t\\r\\n\\t\\t<text font-size=\\\"12\\\" id=\\\"text_name\\\" x=\\\"31\\\" y=\\\"66\\\" oryx:align=\\\"center top\\\" stroke=\\\"black\\\" />\\r\\n\\t\\t\\t \\r\\n\\t</g>\\r\\n</svg>\\r\\n\",\n" +
            "        \"icon\" : \"dataobject/data.store.png\",\n" +
            "        \"groups\" : [ \"Artifacts\" ],\n" +
            "        \"propertyPackages\" : [ \"overrideidpackage\", \"namepackage\", \"documentationpackage\" ],\n" +
            "        \"hiddenPropertyPackages\" : [ ],\n" +
            "        \"roles\" : [ \"all\" ]\n" +
            "    } ],\n" +
            "    \"rules\" : {\n" +
            "        \"cardinalityRules\" : [ {\n" +
            "            \"role\" : \"Startevents_all\",\n" +
            "            \"incomingEdges\" : [ {\n" +
            "                \"role\" : \"SequenceFlow\",\n" +
            "                \"maximum\" : 0\n" +
            "            } ]\n" +
            "        }, {\n" +
            "            \"role\" : \"Endevents_all\",\n" +
            "            \"outgoingEdges\" : [ {\n" +
            "                \"role\" : \"SequenceFlow\",\n" +
            "                \"maximum\" : 0\n" +
            "            } ]\n" +
            "        } ],\n" +
            "        \"connectionRules\" : [ {\n" +
            "            \"role\" : \"SequenceFlow\",\n" +
            "            \"connects\" : [ {\n" +
            "                \"from\" : \"sequence_start\",\n" +
            "                \"to\" : [ \"sequence_end\" ]\n" +
            "            } ]\n" +
            "        }, {\n" +
            "            \"role\" : \"Association\",\n" +
            "            \"connects\" : [ {\n" +
            "                \"from\" : \"sequence_start\",\n" +
            "                \"to\" : [ \"TextAnnotation\" ]\n" +
            "            }, {\n" +
            "                \"from\" : \"sequence_end\",\n" +
            "                \"to\" : [ \"TextAnnotation\" ]\n" +
            "            }, {\n" +
            "                \"from\" : \"TextAnnotation\",\n" +
            "                \"to\" : [ \"sequence_end\" ]\n" +
            "            }, {\n" +
            "                \"from\" : \"BoundaryCompensationEvent\",\n" +
            "                \"to\" : [ \"sequence_end\" ]\n" +
            "            }, {\n" +
            "                \"from\" : \"TextAnnotation\",\n" +
            "                \"to\" : [ \"sequence_start\" ]\n" +
            "            }, {\n" +
            "                \"from\" : \"BoundaryCompensationEvent\",\n" +
            "                \"to\" : [ \"sequence_start\" ]\n" +
            "            } ]\n" +
            "        }, {\n" +
            "            \"role\" : \"DataAssociation\",\n" +
            "            \"connects\" : [ {\n" +
            "                \"from\" : \"sequence_start\",\n" +
            "                \"to\" : [ \"DataStore\" ]\n" +
            "            }, {\n" +
            "                \"from\" : \"sequence_end\",\n" +
            "                \"to\" : [ \"DataStore\" ]\n" +
            "            }, {\n" +
            "                \"from\" : \"DataStore\",\n" +
            "                \"to\" : [ \"sequence_end\" ]\n" +
            "            }, {\n" +
            "                \"from\" : \"DataStore\",\n" +
            "                \"to\" : [ \"sequence_start\" ]\n" +
            "            } ]\n" +
            "        }, {\n" +
            "            \"role\" : \"IntermediateEventOnActivityBoundary\",\n" +
            "            \"connects\" : [ {\n" +
            "                \"from\" : \"Activity\",\n" +
            "                \"to\" : [ \"IntermediateEventOnActivityBoundary\" ]\n" +
            "            } ]\n" +
            "        } ],\n" +
            "        \"containmentRules\" : [ {\n" +
            "            \"role\" : \"BPMNDiagram\",\n" +
            "            \"contains\" : [ \"all\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"SubProcess\",\n" +
            "            \"contains\" : [ \"sequence_start\", \"sequence_end\", \"from_task_event\", \"to_task_event\", \"EventSubProcess\", \"TextAnnotation\", \"DataStore\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"EventSubProcess\",\n" +
            "            \"contains\" : [ \"sequence_start\", \"sequence_end\", \"from_task_event\", \"to_task_event\", \"TextAnnotation\", \"DataStore\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"Pool\",\n" +
            "            \"contains\" : [ \"Lane\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"Lane\",\n" +
            "            \"contains\" : [ \"sequence_start\", \"sequence_end\", \"EventSubProcess\", \"TextAnnotation\", \"DataStore\" ]\n" +
            "        } ],\n" +
            "        \"morphingRules\" : [ {\n" +
            "            \"role\" : \"ActivitiesMorph\",\n" +
            "            \"baseMorphs\" : [ \"UserTask\" ],\n" +
            "            \"preserveBounds\" : true\n" +
            "        }, {\n" +
            "            \"role\" : \"GatewaysMorph\",\n" +
            "            \"baseMorphs\" : [ \"ExclusiveGateway\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"StartEventsMorph\",\n" +
            "            \"baseMorphs\" : [ \"StartNoneEvent\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"EndEventsMorph\",\n" +
            "            \"baseMorphs\" : [ \"StartNoneEvent\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"CatchEventsMorph\",\n" +
            "            \"baseMorphs\" : [ \"CatchTimerEvent\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"ThrowEventsMorph\",\n" +
            "            \"baseMorphs\" : [ \"ThrowNoneEvent\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"BoundaryEventsMorph\",\n" +
            "            \"baseMorphs\" : [ \"ThrowNoneEvent\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"BoundaryCompensationEvent\",\n" +
            "            \"baseMorphs\" : [ \"BoundaryCompensationEvent\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"TextAnnotation\",\n" +
            "            \"baseMorphs\" : [ \"TextAnnotation\" ]\n" +
            "        }, {\n" +
            "            \"role\" : \"DataStore\",\n" +
            "            \"baseMorphs\" : [ \"DataStore\" ]\n" +
            "        } ]\n" +
            "    }\n" +
            "}\n";
}
