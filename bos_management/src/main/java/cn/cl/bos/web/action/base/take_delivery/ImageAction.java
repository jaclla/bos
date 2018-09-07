package cn.cl.bos.web.action.base.take_delivery;

import cn.cl.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@ParentPackage("json-default")
@Namespace("/")
@Actions
@Scope("prototype")
@Controller
public class ImageAction extends BaseAction<Object> {
    private File imgFile;
    private String imgFileFileName;
    private String imgFileContentType;


    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    public void setImgFileContentType(String imgFileContentType) {
        this.imgFileContentType = imgFileContentType;
    }

//    图片上传
    @Action(value = "image_upload", results = @Result(name = SUCCESS, type = "json"))
    public String upload() throws IOException {
        //文件真实路径
        String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
//        保存在服务器的访问路径
        String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";

//        生成图片随机名
        UUID uuid = UUID.randomUUID();
        String ext = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
        String randomFileName = uuid + ext;
//            保存图片
        FileUtils.copyFile(imgFile, new File(savePath + "/" + randomFileName));
//        通知浏览器上传成功
        Map<String, Object> result = new HashMap<>();
        result.put("error", 0);
        result.put("url", saveUrl + randomFileName);//服务器相对路径
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

//    图片查看
    @Action(value = "image_manage", results = @Result(name = SUCCESS, type = "json"))
    public String manage() throws IOException {
        //文件真实路径
        String rootPath = ServletActionContext.getServletContext().getRealPath("/upload/");
//        保存在服务器的访问路径
        String rootUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";

        // 遍历目录取的文件信息
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        // 当前上传目录
        File currentPathFile = new File(rootPath);
        // 图片扩展名
        String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

        if (currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Map<String, Object> hash = new HashMap<String, Object>();
                String fileName = file.getName();
                if (file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if (file.isFile()) {
                    String fileExt = fileName.substring(
                            fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes)
                            .contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file
                                .lastModified()));
                fileList.add(hash);
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("moveup_dir_path", "");
        result.put("current_dir_path", rootPath);
        result.put("current_url", rootUrl);
        result.put("total_count", fileList.size());
        result.put("file_list", fileList);
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }


}
