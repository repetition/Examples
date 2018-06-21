package face;

import java.util.List;

public class DiPingXianListBean {


    /**
     * code : 0
     * msg : 员工查询成功
     * data : {"page_num":"1","page_size":"10","total":13,"result":[{"user_id":"3f67793f3f3f3f4d3f343f3f6f3f3f","user_name":"章超越","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"3f6b733f1e403f4b3f1c353f7c3f3f43","user_name":"于丽","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"3f6e3f203f2156483f3f663f753f05","user_name":"尹春磊","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"3f773f3f131338483f3b3f543f163f","user_name":"范亚东","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"403f3a566a113f4f3f30683f3f1f3f","user_name":"代鹏凯","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"413f1c2b3f4e3f423f2258133f71","user_name":"王晓宇","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"413f3f613f613f443f3f163f3f033f","user_name":"穆国来","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"423f3f7c3f0e3f4f3f3f433f3f3f","user_name":"张蕾","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"473f0c3f3f3f744d3f234e5a3f353f49","user_name":"高帅","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"48673f303f3f443f51423f3f45546f","user_name":"杨嶷岍","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""}]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * page_num : 1
         * page_size : 10
         * total : 13
         * result : [{"user_id":"3f67793f3f3f3f4d3f343f3f6f3f3f","user_name":"章超越","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"3f6b733f1e403f4b3f1c353f7c3f3f43","user_name":"于丽","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"3f6e3f203f2156483f3f663f753f05","user_name":"尹春磊","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"3f773f3f131338483f3b3f543f163f","user_name":"范亚东","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"403f3a566a113f4f3f30683f3f1f3f","user_name":"代鹏凯","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"413f1c2b3f4e3f423f2258133f71","user_name":"王晓宇","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"413f3f613f613f443f3f163f3f033f","user_name":"穆国来","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"423f3f7c3f0e3f4f3f3f433f3f3f","user_name":"张蕾","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"473f0c3f3f3f744d3f234e5a3f353f49","user_name":"高帅","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""},{"user_id":"48673f303f3f443f51423f3f45546f","user_name":"杨嶷岍","user_sex":0,"user_avatar":"","user_type":0,"access_from":"","access_to":"","user_dept_disp":""}]
         */

        private String page_num;
        private String page_size;
        private int total;
        private List<ResultBean> result;

        public String getPage_num() {
            return page_num;
        }

        public void setPage_num(String page_num) {
            this.page_num = page_num;
        }

        public String getPage_size() {
            return page_size;
        }

        public void setPage_size(String page_size) {
            this.page_size = page_size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * user_id : 3f67793f3f3f3f4d3f343f3f6f3f3f
             * user_name : 章超越
             * user_sex : 0
             * user_avatar :
             * user_type : 0
             * access_from :
             * access_to :
             * user_dept_disp :
             */

            private String user_id;
            private String user_name;
            private int user_sex;
            private String user_avatar;
            private int user_type;
            private String access_from;
            private String access_to;
            private String user_dept_disp;

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public int getUser_sex() {
                return user_sex;
            }

            public void setUser_sex(int user_sex) {
                this.user_sex = user_sex;
            }

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
            }

            public int getUser_type() {
                return user_type;
            }

            public void setUser_type(int user_type) {
                this.user_type = user_type;
            }

            public String getAccess_from() {
                return access_from;
            }

            public void setAccess_from(String access_from) {
                this.access_from = access_from;
            }

            public String getAccess_to() {
                return access_to;
            }

            public void setAccess_to(String access_to) {
                this.access_to = access_to;
            }

            public String getUser_dept_disp() {
                return user_dept_disp;
            }

            public void setUser_dept_disp(String user_dept_disp) {
                this.user_dept_disp = user_dept_disp;
            }
        }
    }
}
