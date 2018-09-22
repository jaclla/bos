package cn.cl.bos.web.action.report;

import cn.cl.bos.domain.take_delivery.WayBill;
import cn.cl.bos.service.take_delivery.base.WayBillService;
import cn.cl.bos.utils.FileUtils;
import cn.cl.bos.web.action.base.common.BaseAction;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;


@ParentPackage("json-default")
@Namespace("/")
@Actions
@Scope("prototype")
@Controller
public class ReportAction extends BaseAction<WayBill> {
    @Autowired
    private WayBillService wayBillService;

    @Action(value = "report_exportXls")
    public String exportXls() throws IOException {
        //查询数据
        List<WayBill> wayBills = wayBillService.findWayBills(model);
//        生成excel
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet sheet = hssfWorkbook.createSheet("运单数据");

        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("运单号");
        row.createCell(1).setCellValue("寄件人");
        row.createCell(2).setCellValue("寄件人电话");
        row.createCell(3).setCellValue("寄件人地址");
        row.createCell(4).setCellValue("收件人");
        row.createCell(5).setCellValue("收件人电话");
        row.createCell(6).setCellValue("收件人地址");
        for (WayBill wayBill : wayBills) {
            HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
            dataRow.createCell(0).setCellValue(wayBill.getWayBillNum());
            dataRow.createCell(1).setCellValue(wayBill.getSendName());
            dataRow.createCell(2).setCellValue(wayBill.getSendMobile());
            dataRow.createCell(3).setCellValue(wayBill.getSendAddress());
            dataRow.createCell(4).setCellValue(wayBill.getRecName());
            dataRow.createCell(5).setCellValue(wayBill.getRecMobile());
            dataRow.createCell(6).setCellValue(wayBill.getRecAddress());

        }

//        导出文件

        ServletActionContext.getResponse().setContentType("application/vnd.ms-excel");
        String fileName = "运单数据.xls";
        String agent = ServletActionContext.getRequest().getHeader("user-agent");
        fileName = FileUtils.encodeDownloadFilename(fileName, agent);

        ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment;filename=" + fileName);

        ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
        hssfWorkbook.write(outputStream);

        hssfWorkbook.close();
        return NONE;
    }
}
