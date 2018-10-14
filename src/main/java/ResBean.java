import java.util.List;

public class ResBean {

    private int errno;
    private String guid_info;
    private List<InfoBean> info;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getGuid_info() {
        return guid_info;
    }

    public void setGuid_info(String guid_info) {
        this.guid_info = guid_info;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfoBeans(List<InfoBean> info) {
        this.info = info;
    }
    public static class InfoBean {
        public int getServer_mtime() {
            return server_mtime;
        }

        public void setServer_mtime(int server_mtime) {
            this.server_mtime = server_mtime;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public long getFs_id() {
            return fs_id;
        }

        public void setFs_id(long fs_id) {
            this.fs_id = fs_id;
        }

        public long getServer_ctime() {
            return server_ctime;
        }

        public void setServer_ctime(long server_ctime) {
            this.server_ctime = server_ctime;
        }

        public int getIsdir() {
            return isdir;
        }

        public void setIsdir(int isdir) {
            this.isdir = isdir;
        }

        public long getLocal_mtime() {
            return local_mtime;
        }

        public void setLocal_mtime(long local_mtime) {
            this.local_mtime = local_mtime;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public long getLocal_ctime() {
            return local_ctime;
        }

        public void setLocal_ctime(long local_ctime) {
            this.local_ctime = local_ctime;
        }

        public String getObject_key() {
            return object_key;
        }

        public void setObject_key(String object_key) {
            this.object_key = object_key;
        }

        public String getServer_filename() {
            return server_filename;
        }

        public void setServer_filename(String server_filename) {
            this.server_filename = server_filename;
        }

        private int server_mtime;
        private int category;
        private long fs_id;
        private long server_ctime;
        private int isdir;
        private long local_mtime;
        private long size;
        private String md5;
        private String path;
        private long local_ctime;
        private String object_key;
        private String server_filename;
    }

    public static class NewInfoBean {
        public int getServer_mtime() {
            return server_mtime;
        }

        public void setServer_mtime(int server_mtime) {
            this.server_mtime = server_mtime;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public long getFs_id() {
            return fs_id;
        }

        public void setFs_id(long fs_id) {
            this.fs_id = fs_id;
        }

        public long getServer_ctime() {
            return server_ctime;
        }

        public void setServer_ctime(long server_ctime) {
            this.server_ctime = server_ctime;
        }

        public int getIsdir() {
            return isdir;
        }

        public void setIsdir(int isdir) {
            this.isdir = isdir;
        }

        public long getLocal_mtime() {
            return local_mtime;
        }

        public void setLocal_mtime(long local_mtime) {
            this.local_mtime = local_mtime;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public long getLocal_ctime() {
            return local_ctime;
        }

        public void setLocal_ctime(long local_ctime) {
            this.local_ctime = local_ctime;
        }

        public String getObject_key() {
            return object_key;
        }

        public void setObject_key(String object_key) {
            this.object_key = object_key;
        }

        public String getServer_filename() {
            return server_filename;
        }

        public void setServer_filename(String server_filename) {
            this.server_filename = server_filename;
        }

        public String getSizeStr() {
            return sizeStr;
        }

        public void setSizeStr(String sizeStr) {
            this.sizeStr = sizeStr;
        }
        private int server_mtime;
        private int category;
        private long fs_id;
        private long server_ctime;
        private int isdir;
        private long local_mtime;

        private long size;

        private String sizeStr;
        private String md5;
        private String path;
        private long local_ctime;
        private String object_key;
        private String server_filename;
    }
}
