package com.muxistudio.appcommon.data;

public class CourseAddedResponse {

    /**
     * code : 0
     * message : OK
     * data : {"Id":"5e46ec2815f80b8eb021e9e0"}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * Id : 5e46ec2815f80b8eb021e9e0
         */

        private String Id;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }
    }
}
