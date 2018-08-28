package cn.cl.bos.web.action.base;

import cn.cl.bos.domain.base.Area;
import cn.cl.bos.service.base.AreaService;
import cn.cl.bos.utils.PinYin4jUtils;
import cn.cl.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Actions
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {
//    //模型驱动
//    private Area area = new Area();

    @Autowired
    private AreaService areaService;

//    @Override
//    public Area getModel() {
//        return area;
//    }

    //接收上传文件
    private File file;
    //文件类型
    private String fileContentType;
    //文件名
    private String fileFileName;

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setFile(File file) {
        this.file = file;
    }

    //批量数据导入
    @Action(value = "area_batchImport")
    public String batchImport() throws IOException {
        ArrayList<Area> areas = new ArrayList<>();

//        基于文件是xls
        if (fileFileName.substring(fileFileName.lastIndexOf(".") + 1).equals("xls")) {

//        1、加载excel表格
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
//        2、读取一个sheet
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
//        3、读取sheet的每一行
            for (Row row : sheet) {
//            如果是第一行
                if (row.getRowNum() == 0) {
                    continue;
                }
//            跳过空行
                if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                    break;
                }
                Area area = new Area();
                //获得id
                area.setId(row.getCell(0).getStringCellValue());
                //获得省份
                area.setProvince(row.getCell(1).getStringCellValue());
//            获得城市
                area.setCity(row.getCell(2).getStringCellValue());
//            获得地区
                area.setDistrict(row.getCell(3).getStringCellValue());
//            获得邮编
                area.setPostcode(row.getCell(4).getStringCellValue());
//                基于pinyin4j生成城市编码和简码
                //省份简码
                String province = area.getProvince().substring(0, area.getProvince().length() - 1);
                //城市简码
                String city = area.getCity().substring(0, area.getCity().length() - 1);
                //地区简码
                String district = area.getDistrict().substring(0, area.getDistrict().length() - 1);
                //简码 湖北武汉洪山 = [H,B,W,H,H,S]
                String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
                StringBuffer sb = new StringBuffer();
                for (String headStr : headArray) {
                    //简码 湖北武汉洪山 = HBWHHS
                    sb.append(headStr);
                }
                String shortCode = sb.toString();
                //获得简码
                area.setShortcode(shortCode);
//                城市编码
                String cityCode = PinYin4jUtils.hanziToPinyin(city, "");
                //获得城市编码
                area.setCitycode(cityCode);
                areas.add(area);
            }
        }//        基于文件是xlsx
        if (fileFileName.substring(fileFileName.lastIndexOf(".") + 1).equals("xlsx")) {

//        1、加载excel表格
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
//        2、读取一个sheet
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
//        3、读取sheet的每一行
            for (Row row : sheet) {
//            如果是第一行
                if (row.getRowNum() == 0) {
                    continue;
                }
//            跳过空行
                if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                    break;
                }
                Area area = new Area();
                //获得id
                area.setId(row.getCell(0).getStringCellValue());
                //获得省份
                area.setProvince(row.getCell(1).getStringCellValue());
//            获得城市
                area.setCity(row.getCell(2).getStringCellValue());
//            获得地区
                area.setDistrict(row.getCell(3).getStringCellValue());
//            获得邮编
                area.setPostcode(row.getCell(4).getStringCellValue());

//                基于pinyin4j生成城市编码和简码
                //省份简码
                String province = area.getProvince().substring(0, area.getProvince().length() - 1);
                //城市简码
                String city = area.getCity().substring(0, area.getCity().length() - 1);
                //地区简码
                String district = area.getDistrict().substring(0, area.getDistrict().length() - 1);
                //简码 湖北武汉洪山 = [H,B,W,H,H,S]
                String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
                StringBuffer sb = new StringBuffer();
                for (String headStr : headArray) {
                    //简码 湖北武汉洪山 = HBWHHS
                    sb.append(headStr);
                }
                String shortCode = sb.toString();
                //获得简码
                area.setShortcode(shortCode);
//                城市编码
                String cityCode = PinYin4jUtils.hanziToPinyin(city, "");
                //获得城市编码
                area.setCitycode(cityCode);
                //将一行数据添加到集合
                areas.add(area);
            }
        }
        //调用业务层
        areaService.saveBatch(areas);
        return NONE;
    }

    //    //属性驱动
//    private int rows;
//
//    private int page;
//
//    public void setRows(int rows) {
//        this.rows = rows;
//    }
//
//    public void setPage(int page) {
//        this.page = page;
//    }
//    分页查询
    @Action(value = "area_pageQuery", results = @Result(name = SUCCESS, type = "json"))
    public String pageQuery() {
        //获取分页数据
        Pageable pageable = new PageRequest(this.page - 1, rows);
        //根据查询条件 构建条件查询对象
        Specification<Area> specification = new Specification<Area>() {
            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //单表查询
                //快递员工号  精准查询
                List<Predicate> list = new ArrayList<>();
                //添加查询条件

//                根据省份模糊查询
                if (StringUtils.isNotBlank(model.getProvince())) {
                    Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
                    list.add(p1);
                }
//                根据城市模糊查询
                if (StringUtils.isNotBlank(model.getCity())) {
                    Predicate p2 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
                    list.add(p2);
                }
//                根据区域模糊查询
                if (StringUtils.isNotBlank(model.getDistrict())) {
                    Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
                    list.add(p3);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };

        //调用业务层
        Page<Area> pageData = areaService.findPageData(specification, pageable);
        //返回数据到客户端
//        Map<String, Object> result = new HashMap<>();
//        result.put("total", pageData.getTotalElements());
//        result.put("rows", pageData.getContent());
//        //将map转换成json 使用struts2-json-plugin插件
//        ActionContext.getContext().getValueStack().push(result);
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }

    //    保存修改
    @Action(value = "area_save", results = @Result(name = SUCCESS, type = "redirect", location = "pages/base/area.html"))
    public String area_save() {
        areaService.save(model);
        return SUCCESS;
    }

    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    //文件导入
    @Action(value = "area_delBatch", results = @Result(name = SUCCESS, type = "redirect", location = "pages/base/area.html"))
    public String area_delBatch() {
        //获取id数组然后按，分割
        String[] idArray = ids.split(",");
        areaService.del(idArray);
        return SUCCESS;
    }
}
