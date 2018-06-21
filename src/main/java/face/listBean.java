package face;

import java.util.List;

public class listBean {


    /**
     * code : 0
     * data : [{"avatar":"","birthday":null,"come_from":"","company_id":2,"department":"","description":"","email":"zhangwenbin@thinkwin.com.cn","end_time":0,"entry_date":null,"gender":1,"id":3141,"interviewee":"","interviewee_pinyin":"","job_number":"","name":"张文斌","password_reseted":false,"phone":"","photos":[],"pinyin":"zhangwenbin","purpose":0,"remark":"","start_time":0,"subject_type":0,"title":"","visit_notify":false},{"avatar":"","birthday":null,"come_from":"","company_id":2,"department":"","description":"","email":"zhangbo@thinkwin.com.cn","end_time":0,"entry_date":null,"gender":1,"id":3140,"interviewee":"","interviewee_pinyin":"","job_number":"","name":"张博","password_reseted":false,"phone":"","photos":[],"pinyin":"zhangbo","purpose":0,"remark":"","start_time":0,"subject_type":0,"title":"","visit_notify":false},{"avatar":"","birthday":null,"come_from":"","company_id":2,"department":"","description":"","email":"wuhongjie@thinkwin.com.cn","end_time":0,"entry_date":null,"gender":1,"id":3139,"interviewee":"","interviewee_pinyin":"","job_number":"","name":"吴洪杰","password_reseted":false,"phone":"","photos":[],"pinyin":"wuhongjie","purpose":0,"remark":"","start_time":0,"subject_type":0,"title":"","visit_notify":false},{"avatar":"","birthday":null,"come_from":"","company_id":2,"department":"","description":"","email":"wangjinglong@thinkwin.com.cn","end_time":0,"entry_date":null,"gender":1,"id":3138,"interviewee":"","interviewee_pinyin":"","job_number":"","name":"王景隆","password_reseted":false,"phone":"","photos":[],"pinyin":"wangjinglong","purpose":0,"remark":"","start_time":0,"subject_type":0,"title":"","visit_notify":false},{"avatar":"","birthday":null,"come_from":"","company_id":2,"department":"","description":"","email":"liyuyan@thinkwin.com.cn","end_time":0,"entry_date":null,"gender":1,"id":3137,"interviewee":"","interviewee_pinyin":"","job_number":"","name":"李宇颜","password_reseted":false,"phone":"","photos":[],"pinyin":"liyuyan","purpose":0,"remark":"","start_time":0,"subject_type":0,"title":"","visit_notify":false},{"avatar":"","birthday":null,"come_from":"","company_id":2,"department":"","description":"","email":"zjb@thinkwin.com.cn","end_time":0,"entry_date":null,"gender":1,"id":3136,"interviewee":"","interviewee_pinyin":"","job_number":"","name":"赵净彬","password_reseted":false,"phone":"","photos":[],"pinyin":"zhaojingbin","purpose":0,"remark":"","start_time":0,"subject_type":0,"title":"","visit_notify":false},{"avatar":"","birthday":null,"come_from":"","company_id":2,"department":"","description":"","email":"hjc@thinkwin.com.cn","end_time":0,"entry_date":null,"gender":1,"id":3135,"interviewee":"","interviewee_pinyin":"","job_number":"","name":"候江春","password_reseted":false,"phone":"","photos":[],"pinyin":"houjiangchun","purpose":0,"remark":"","start_time":0,"subject_type":0,"title":"","visit_notify":false},{"avatar":"","birthday":null,"come_from":"","company_id":2,"department":"","description":"","email":"wuruikun@thinkwin.com.cn","end_time":0,"entry_date":null,"gender":1,"id":3134,"interviewee":"","interviewee_pinyin":"","job_number":"","name":"武瑞坤","password_reseted":false,"phone":"","photos":[],"pinyin":"wuruikun","purpose":0,"remark":"","start_time":0,"subject_type":0,"title":"","visit_notify":false},{"avatar":"","birthday":null,"come_from":"","company_id":2,"department":"","description":"","email":"wuwenjuan@thinkwin.com.cn","end_time":0,"entry_date":null,"gender":1,"id":3133,"interviewee":"","interviewee_pinyin":"","job_number":"","name":"吴文娟","password_reseted":false,"phone":"","photos":[],"pinyin":"wuwenjuan","purpose":0,"remark":"","start_time":0,"subject_type":0,"title":"","visit_notify":false},{"avatar":"","birthday":null,"come_from":"","company_id":2,"department":"","description":"","email":"wuyuanyuan@thinkwin.com.cn","end_time":0,"entry_date":null,"gender":1,"id":3132,"interviewee":"","interviewee_pinyin":"","job_number":"","name":"吴源源","password_reseted":false,"phone":"","photos":[],"pinyin":"wuyuanyuan","purpose":0,"remark":"","start_time":0,"subject_type":0,"title":"","visit_notify":false}]
     * page : {"count":86,"current":1,"size":10,"total":9}
     */

    private int code;
    private PageBean page;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class PageBean {
        /**
         * count : 86
         * current : 1
         * size : 10
         * total : 9
         */

        private int count;
        private int current;
        private int size;
        private int total;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    public static class DataBean {
        /**
         * avatar :
         * birthday : null
         * come_from :
         * company_id : 2
         * department :
         * description :
         * email : zhangwenbin@thinkwin.com.cn
         * end_time : 0
         * entry_date : null
         * gender : 1
         * id : 3141
         * interviewee :
         * interviewee_pinyin :
         * job_number :
         * name : 张文斌
         * password_reseted : false
         * phone :
         * photos : []
         * pinyin : zhangwenbin
         * purpose : 0
         * remark :
         * start_time : 0
         * subject_type : 0
         * title :
         * visit_notify : false
         */

        private String avatar;
        private Object birthday;
        private String come_from;
        private int company_id;
        private String department;
        private String description;
        private String email;
        private int end_time;
        private Object entry_date;
        private int gender;
        private int id;
        private String interviewee;
        private String interviewee_pinyin;
        private String job_number;
        private String name;
        private boolean password_reseted;
        private String phone;
        private String pinyin;
        private int purpose;
        private String remark;
        private int start_time;
        private int subject_type;
        private String title;
        private boolean visit_notify;
        private List<?> photos;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public String getCome_from() {
            return come_from;
        }

        public void setCome_from(String come_from) {
            this.come_from = come_from;
        }

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }

        public Object getEntry_date() {
            return entry_date;
        }

        public void setEntry_date(Object entry_date) {
            this.entry_date = entry_date;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getInterviewee() {
            return interviewee;
        }

        public void setInterviewee(String interviewee) {
            this.interviewee = interviewee;
        }

        public String getInterviewee_pinyin() {
            return interviewee_pinyin;
        }

        public void setInterviewee_pinyin(String interviewee_pinyin) {
            this.interviewee_pinyin = interviewee_pinyin;
        }

        public String getJob_number() {
            return job_number;
        }

        public void setJob_number(String job_number) {
            this.job_number = job_number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isPassword_reseted() {
            return password_reseted;
        }

        public void setPassword_reseted(boolean password_reseted) {
            this.password_reseted = password_reseted;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public int getPurpose() {
            return purpose;
        }

        public void setPurpose(int purpose) {
            this.purpose = purpose;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getSubject_type() {
            return subject_type;
        }

        public void setSubject_type(int subject_type) {
            this.subject_type = subject_type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isVisit_notify() {
            return visit_notify;
        }

        public void setVisit_notify(boolean visit_notify) {
            this.visit_notify = visit_notify;
        }

        public List<?> getPhotos() {
            return photos;
        }

        public void setPhotos(List<?> photos) {
            this.photos = photos;
        }
    }
}
