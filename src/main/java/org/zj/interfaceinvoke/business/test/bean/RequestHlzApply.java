package org.zj.interfaceinvoke.business.test.bean;

import java.util.Map;

/**
 * @BelongsProject: appService
 * @BelongsPackage: com.zhongjin.application.operate.riskcheckhlz.entity
 * @Author: ZhangJun
 * @CreateTime: 2019-07-16 11:31
 * @Description: 行列秩决策请求的参数
 */
public class RequestHlzApply {


    /**
     * version : 1.0.0
     * memberId : 12345678
     * reqNo : 123456
     * reqTime : 2019-05-22 13:44:44
     * reqBody : {"baseUser":{"bankCardNo":"6222600260001072444","idCard":"310102197010102033","idName":"张三","ip":"127.0.0.1","phone":"18516522943"},"customParams":{"isReloan":"1"},"memberId":"12345678","transId":"2141","workId":"201"}
     */

    private String version;
    private String memberId;
    private String reqNo;
    private String reqTime;
    private String reqBody;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
        this.reqBody = reqBody;
    }

    public static class ReqBodyBean {
        /**
         * baseUser : {"bankCardNo":"6222600260001072444","idCard":"310102197010102033","idName":"张三","ip":"127.0.0.1","phone":"18516522943"}
         * customParams : {"isReloan":"1"}
         * memberId : 12345678
         * transId : 2141
         * workId : 201
         */

        private BaseUserBean baseUser;
        private Map<String,String> customParams;
        private String memberId;
        private String transId;
        private String workId;

        public BaseUserBean getBaseUser() {
            return baseUser;
        }

        public void setBaseUser(BaseUserBean baseUser) {
            this.baseUser = baseUser;
        }

        public Map<String,String>  getCustomParams() {
            return customParams;
        }

        public void setCustomParams(Map<String,String>  customParams) {
            this.customParams = customParams;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getTransId() {
            return transId;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

        public String getWorkId() {
            return workId;
        }

        public void setWorkId(String workId) {
            this.workId = workId;
        }

        public static class BaseUserBean {
            /**
             * bankCardNo : 6222600260001072444
             * idCard : 310102197010102033
             * idName : 张三
             * ip : 127.0.0.1
             * phone : 18516522943
             */

            private String bankCardNo;
            private String idCard;
            private String idName;
            private String ip;
            private String phone;

            public String getBankCardNo() {
                return bankCardNo;
            }

            public void setBankCardNo(String bankCardNo) {
                this.bankCardNo = bankCardNo;
            }

            public String getIdCard() {
                return idCard;
            }

            public void setIdCard(String idCard) {
                this.idCard = idCard;
            }

            public String getIdName() {
                return idName;
            }

            public void setIdName(String idName) {
                this.idName = idName;
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }
    }
}
