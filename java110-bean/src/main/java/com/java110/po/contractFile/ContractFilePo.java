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
package com.java110.po.contractFile;

import java.io.Serializable;
import java.util.Date;

public class ContractFilePo implements Serializable {

    private String fileRealName;
    private String contractFileId;
    private String contractId;
    private String fileSaveName;
    private String statusCd = "0";
    public String getFileRealName() {
        return fileRealName;
    }
    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }
    public String getContractFileId() {
        return contractFileId;
    }
    public void setContractFileId(String contractFileId) {
        this.contractFileId = contractFileId;
    }
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
    public String getFileSaveName() {
        return fileSaveName;
    }
    public void setFileSaveName(String fileSaveName) {
        this.fileSaveName = fileSaveName;
    }
    public String getStatusCd() {
        return statusCd;
    }
    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }



}
