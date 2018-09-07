package cn.cl.bos.web.action.base.take_delivery;

import cn.cl.bos.domain.base.Promotion;
import cn.cl.bos.service.base.PromotionService;
import cn.cl.bos.web.action.base.common.BaseAction;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@ParentPackage("json-default")
@Namespace("/")
@Actions
@Scope("prototype")
@Controller
public class PromotionAction extends BaseAction<Promotion> {

    @Autowired
    private PromotionService promotionService;

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    public void setTitleImgFileContentType(String titleImgFileContentType) {
        this.titleImgFileContentType = titleImgFileContentType;
    }

    private File titleImgFile;
    private String titleImgFileFileName;
    private String titleImgFileContentType;

    @Action(value = "promotion_save",results = @Result(name = SUCCESS,type = "redirect",location = "/pages/take_delivery/promotion.html"))
    public String save() throws IOException {
//        //文件真实路径
        String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
////        保存在服务器的访问路径
        String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
//        生成图片随机名
        UUID uuid = UUID.randomUUID();
        String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
        String randomFileName = uuid + ext;

//        保存图片
        FileUtils.copyFile(titleImgFile, new File(savePath + "/" + randomFileName));

        model.setTitleImg(ServletActionContext.getRequest().getContextPath()+"/upload/"+randomFileName);
        promotionService.save(model);
        return SUCCESS;
    }

    @Action(value = "promotion_pageQuery", results = @Result(name = SUCCESS, type = "json"))
    public String pageQuery() throws IOException {

        Pageable pageable = new PageRequest(page - 1, rows);

        Page<Promotion> pageData= promotionService.findPageDate(pageable);

        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }
}
