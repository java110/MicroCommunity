package com.java110.code;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName DealHtml
 * @Description TODO
 * @Author wuxw
 * @Date 2022/4/20 22:18
 * @Version 1.0
 * add by wuxw 2022/4/20
 **/
public class DealHtml {

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\Administrator\\Documents\\project\\hc\\MicroCommunityWeb\\public\\components");
        JSONObject js = new JSONObject();
        listFiles(file, js);
        System.out.println("js = " + js.toJSONString());
    }

//    public static void main(String[] args) throws Exception {
//       String js = "<div id=\"addContractModel\" class=\"modal fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\"\n" +
//               "  aria-hidden=\"true\">\n" +
//               "  <div class=\"modal-dialog modal-lg\">\n" +
//               "    <div class=\"modal-content\">\n" +
//               "      <div class=\"modal-body\">\n" +
//               "        <h3 class=\"m-t-none m-b \">添加合同</h3>\n" +
//               "        <div class=\"ibox-content\">\n" +
//               "          <div>\n" +addPrivilege
//               "            <div>\n" +
//               "              <div class=\"row\">\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">合同名称</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.contractName\" type=\"text\" placeholder=\"必填，请填写合同名称\"\n" +
//               "                        class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">合同编号</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.contractCode\" type=\"text\" placeholder=\"必填，请填写合同编号\"\n" +
//               "                        class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">合同类型</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <select class=\"custom-select\" v-model=\"addContractInfo.contractType\"\n" +
//               "                        @change=\"_changeContractType()\">\n" +
//               "                        <option selected disabled value=\"\">必填，请选择合同类型</option>\n" +
//               "                        <option v-for=\"(item,index) in addContractInfo.contractTypes\" :key=\"index\"\n" +
//               "                          :value=\"item.contractTypeId\">{{item.typeName}}</option>\n" +
//               "                      </select>\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "              </div>\n" +
//               "              <div class=\"row\">\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">甲方</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.partyA\" type=\"text\" placeholder=\"必填，请填写甲方\" class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">甲方联系人</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.aContacts\" type=\"text\" placeholder=\"必填，请填写甲方联系人\"\n" +
//               "                        class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">甲方联系电话</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.aLink\" type=\"text\" placeholder=\"必填，请填写甲方联系电话\"\n" +
//               "                        class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "              </div>\n" +
//               "              <div class=\"row\">\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">乙方</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.partyB\" type=\"text\" placeholder=\"必填，请填写乙方\" class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">乙方联系人</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.bContacts\" type=\"text\" placeholder=\"必填，请填写乙方联系人\"\n" +
//               "                        class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">乙方联系电话</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.bLink\" type=\"text\" placeholder=\"必填，请填写乙方联系电话\"\n" +
//               "                        class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "              </div>\n" +
//               "              <div class=\"row\">\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">经办人</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.operator\" type=\"text\" placeholder=\"必填，请填写经办人\"\n" +
//               "                        class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">联系电话</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.operatorLink\" type=\"text\" placeholder=\"必填，请填写联系电话\"\n" +
//               "                        class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">合同金额</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.amount\" type=\"text\" placeholder=\"选填，请填写合同金额\" class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "              </div>\n" +
//               "              <div class=\"row\">\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">开始时间</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.startTime\" type=\"text\" placeholder=\"必填，请填写开始时间\"\n" +
//               "                        class=\"form-control addStartTime\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">结束时间</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.endTime\" type=\"text\" placeholder=\"必填，请填写结束时间\"\n" +
//               "                        class=\"form-control addEndTime\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">签订时间</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.signingTime\" type=\"text\" placeholder=\"必填，请填写签订时间\"\n" +
//               "                        class=\"form-control addSigningTime\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "              </div>\n" +
//               "              <div class=\"row\">\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">楼栋</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.allNum\" type=\"text\" placeholder=\"选填，请填写 楼栋编码\" class=\"form-control\" @blur=\"_queryRoom\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">业主</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.ownerName\" disabled=\"disabled\" type=\"text\" placeholder=\"必填，请填写业主\"\n" +
//               "                        class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "                <div class=\"col-sm-4\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">业主电话</label>\n" +
//               "                    <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                      <input v-model=\"addContractInfo.link\" disabled=\"disabled\" type=\"text\" placeholder=\"必填，请填写业主电话\"\n" +
//               "                        class=\"form-control\">\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "              </div>\n" +
//               "              <div v-for=\"(item,index) in addContractInfo.contractTypeSpecs\">\n" +
//               "                <div class=\"row\" v-if=\"index % 3 == 0\">\n" +
//               "                  <div class=\"col-sm-4\">\n" +
//               "                    <div class=\"form-group row\">\n" +
//               "                      <label class=\"col-sm-3 col-form-label padding-lr-sm text-right\">{{item.specName}}</label>\n" +
//               "                      <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                        <input v-model=\"item.value\" type=\"text\" :placeholder=\"item.specHoldplace\" class=\"form-control\">\n" +
//               "                      </div>\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                  <div class=\"col-sm-4\" v-if=\"index < addContractInfo.contractTypeSpecs.length-1\">\n" +
//               "                    <div class=\"form-group row\">\n" +
//               "                      <label\n" +
//               "                        class=\"col-sm-3 col-form-label padding-lr-sm text-right\">{{addContractInfo.contractTypeSpecs[index+1].specName}}</label>\n" +
//               "                      <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                        <input v-model=\"addContractInfo.contractTypeSpecs[index+1].value\" type=\"text\"\n" +
//               "                          :placeholder=\"addContractInfo.contractTypeSpecs[index+1].specHoldplace\" class=\"form-control\">\n" +
//               "                      </div>\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                  <div class=\"col-sm-4\" v-if=\"index < addContractInfo.contractTypeSpecs.length-2\">\n" +
//               "                    <div class=\"form-group row\">\n" +
//               "                      <label\n" +
//               "                        class=\"col-sm-3 col-form-label padding-lr-sm text-right\">{{addContractInfo.contractTypeSpecs[index+2].specName}}</label>\n" +
//               "                      <div class=\"col-sm-9 padding-lr-xs\">\n" +
//               "                        <input v-model=\"addContractInfo.contractTypeSpecs[index+2].value\" type=\"text\"\n" +
//               "                          :placeholder=\"addContractInfo.contractTypeSpecs[index+2].specHoldplace\" class=\"form-control\">\n" +
//               "                      </div>\n" +
//               "                    </div>\n" +
//               "                  </div>\n" +
//               "                </div>\n" +
//               "              </div>\n" +
//               "  \n" +
//               "              <div class=\"row\">\n" +
//               "                <div class=\"col-sm-10\">\n" +
//               "                  <div class=\"form-group row\">\n" +
//               "                    <label class=\"col-sm-2 col-form-label\">合同附件</label>\n" +
//               "                    <div class=\"col-sm-10\">\n" +
//               "                        <div class=\"row \" style=\"margin-left: 0px;\">\n" +
//               "                            <button type=\"button\" class=\"btn btn-primary\" @click=\"addFileStep()\">添加附件</button>\n" +
//               "                        </div>\n" +
//               "                        <div v-for=\"(item,index) in addContractInfo.contractFilePo\">\n" +
//               "                            <div class=\"row margin-0 margin-top\">\n" +
//               "                                <div  class=\"col-sm-1 text-center\">\n" +
//               "                                    <label class=\"col-form-label\">第{{index+1}}个</label>\n" +
//               "                                </div>\n" +
//               "                                <input type=\"file\"\n" +
//               "                                class=\"custom-file-input form-control\" name=\"excelTemplate\" style=\"width: 11%;opacity: 1;\"\n" +
//               "                                v-on:change=\"getFile($event,index)\" accept=\".png,.pdf,.jpg\">\n" +
//               "                                <label  class=\"col-sm-5 col-form-label padding-lr-sm text-right\">{{item.fileRealName}}</label>\n" +
//               "                                <div>\n" +
//               "                                    <button type=\"button\" class=\"btn btn-link\" @click=\"deleteStep(item)\">删除附件</button>\n" +
//               "                                </div>\n" +
//               "                              \n" +
//               "                            </div>\n" +
//               "                           \n" +
//               "                        </div>\n" +
//               "                    </div>\n" +
//               "                </div>\n" +
//               "                </div>\n" +
//               "              </div>\n" +
//               "\n" +
//               "              <div class=\"ibox-content\">\n" +
//               "                <button class=\"btn btn-primary float-right\" type=\"button\" v-on:click=\"saveContractInfo()\"><i\n" +
//               "                    class=\"fa fa-check\"></i>&nbsp;保存</button>\n" +
//               "                <button type=\"button\" class=\"btn btn-warning float-right\" style=\"margin-right:20px;\"\n" +
//               "                  data-dismiss=\"modal\">取消</button>\n" +
//               "              </div>\n" +
//               "            </div>\n" +
//               "          </div>\n" +
//               "        </div>\n" +
//               "      </div>\n" +
//               "    </div>\n" +
//               "  </div>\n" +
//               "</div>";
//        Pattern p = Pattern.compile("=\"([\u4e00-\u9fa5]|，|,| )+");
//
//        Matcher m = p.matcher(js);
//
//        while (m.find()) {
//            String chinese = m.group();//匹配出的中文
//            chinese = chinese.replace("=\"", "");
//            System.out.println(chinese);
//        }
//    }

    public static void listFiles(File file, JSONObject js) throws Exception {
        if (file.isFile()) {
            if (file.getName().endsWith(".html")) {
                doDealHtml(file, js);

            }
            return;
        }

        File[] files = file.listFiles();

        for (File tmpFile : files) {
            listFiles(tmpFile, js);
        }
    }

    private static void doDealHtml(File tmpFile, JSONObject js) throws Exception {

        String fileName = tmpFile.getName().replace(".html", "");
        System.out.println("fileName=" + fileName + ",dir=" + tmpFile.getPath());
        BufferedReader in = new BufferedReader(new FileReader(tmpFile));
        String str;
        String context = "";
        JSONObject fileNameObj = new JSONObject();
        while ((str = in.readLine()) != null) {
            context += (doDealHtmlNode(str, fileName, fileNameObj) + "\n");
            //doDealHtmlNode(str,fileName);
        }
        js.put(fileName, fileNameObj);
        System.out.println(context);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmpFile));
        bufferedWriter.write(context);
        bufferedWriter.close();


    }

    private static String doDealHtmlNode(String str, String fileName, JSONObject fileNameObj) {
//        Pattern p = Pattern.compile("=\"([\u4e00-\u9fa5]|，)+");
//
//        Matcher m = p.matcher(str);
        Pattern p = Pattern.compile("=\"([\u4e00-\u9fa5]|，|,| )+");

        Matcher m = p.matcher(str);

        while (m.find()) {
            String chinese = m.group();//匹配出的中文
            chinese = chinese.replace("=\"", "");
            fileNameObj.put(chinese, chinese);
            //m.appendReplacement(buf, "><vc:i18n name=\"" + chinese + "\" namespace=\"" + fileName + "\"></vc:i18n>");
            str = str.replace(chinese, "vc.i18n('" + chinese + "','" + fileName + "')");
            str = str.replace("placeholder", ":placeholder");
        }

        p = Pattern.compile(">[\u4e00-\u9fa5]+");

        m = p.matcher(str);

        while (m.find()) {
            String chinese = m.group();//匹配出的中文
            chinese = chinese.replace(">", "");
            fileNameObj.put(chinese, chinese);
            //m.appendReplacement(buf, "><vc:i18n name=\"" + chinese + "\" namespace=\"" + fileName + "\"></vc:i18n>");
            str = str.replace(chinese, "<vc:i18n name=\"" + chinese + "\" namespace=\"" + fileName + "\"></vc:i18n>");
        }

        p = Pattern.compile("}}[\u4e00-\u9fa5]+");

        m = p.matcher(str);

        while (m.find()) {
            String chinese = m.group();//匹配出的中文
            chinese = chinese.replace("}}", "");
            fileNameObj.put(chinese, chinese);
            //m.appendReplacement(buf, "><vc:i18n name=\"" + chinese + "\" namespace=\"" + fileName + "\"></vc:i18n>");
            str = str.replace(chinese, "<vc:i18n name=\"" + chinese + "\" namespace=\"" + fileName + "\"></vc:i18n>");
        }

        p = Pattern.compile("> [\u4e00-\u9fa5]+");

        m = p.matcher(str);

        while (m.find()) {
            String chinese = m.group();//匹配出的中文
            chinese = chinese.replace("> ", "");
            fileNameObj.put(chinese, chinese);
            //m.appendReplacement(buf, "><vc:i18n name=\"" + chinese + "\" namespace=\"" + fileName + "\"></vc:i18n>");
            str = str.replace(chinese, "<vc:i18n name=\"" + chinese + "\" namespace=\"" + fileName + "\"></vc:i18n>");
        }


        return str;
    }
}
