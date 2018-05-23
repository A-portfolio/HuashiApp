package com.muxistudio.appcommon.data;

//从企业号拿过来的数据格式
//card data enterprise
public class CardDataEtp {

    /**
     * result : true
     * msg :
     * model : {"userId":"2016210942","name":"张可","img":"http://p.qlogo.cn/bizmail/2qVD3K0yqAEZKmZdYMFSb82VlUtOyVBEISsGibUzV9iaMvTa6L0Znxkw/","deptName":"计算机学院","balance":"2.08","smtDealdatetimeTxt":"2018-05-15 17:24:13","dataSum":"58","consumeTotal":"326"}
     */

    private boolean result;
    private String msg;
    private ModelBean model;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ModelBean getModel() {
        return model;
    }

    public void setModel(ModelBean model) {
        this.model = model;
    }

    public static class ModelBean {
        /**
         * userId : 2016210942
         * name : 张可
         * img : http://p.qlogo.cn/bizmail/2qVD3K0yqAEZKmZdYMFSb82VlUtOyVBEISsGibUzV9iaMvTa6L0Znxkw/
         * deptName : 计算机学院
         * balance : 2.08
         * smtDealdatetimeTxt : 2018-05-15 17:24:13
         * dataSum : 58
         * consumeTotal : 326
         */

        private String userId;
        private String name;
        private String img;
        private String deptName;
        private String balance;
        //最后一次消费
        private String smtDealdatetimeTxt;
        private String dataSum;
        private String consumeTotal;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getSmtDealdatetimeTxt() {
            return smtDealdatetimeTxt;
        }

        public void setSmtDealdatetimeTxt(String smtDealdatetimeTxt) {
            this.smtDealdatetimeTxt = smtDealdatetimeTxt;
        }

        public String getDataSum() {
            return dataSum;
        }

        public void setDataSum(String dataSum) {
            this.dataSum = dataSum;
        }

        public String getConsumeTotal() {
            return consumeTotal;
        }

        public void setConsumeTotal(String consumeTotal) {
            this.consumeTotal = consumeTotal;
        }
    }
}
