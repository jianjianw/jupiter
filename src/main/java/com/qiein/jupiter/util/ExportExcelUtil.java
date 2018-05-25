package com.qiein.jupiter.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * 导出表格
 */
public class ExportExcelUtil {

    /**
     * 将一个集合导出为Excle并转换问文件流下载
     *
     * @param fileName
     * @param list
     * @param pojoClass
     * @return
     */
    public static ResponseEntity<Resource> downloadFile(String fileName, List<?> list, Class<?> pojoClass) {
        ByteArrayOutputStream bos = null;
        try {
            Workbook workbook = ExportExcelUtil.createExcel(fileName, list, pojoClass);
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            fileName = fileName + ".xlsx";
            // 设置下载文件名
            fileName = URLEncoder.encode(fileName, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));
            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 导出Excel workbook
     *
     * @param title
     * @param list
     * @param pojoClass
     * @return
     */
    private static Workbook createExcel(String title, List<?> list, Class<?> pojoClass) {
        Workbook workBook;
        ExportParams params = new ExportParams(null, title);
        workBook = ExcelExportUtil.exportBigExcel(params, pojoClass, list);
        ExcelExportUtil.closeExportBigExcel();
        return workBook;
    }

    /**
     * 导出文件流
     */
    public static void export(HttpServletResponse response, String fileName, List<?> list, Class<?> pojoClass)
            throws Exception {
        String fileNameTemp;
        fileNameTemp = URLEncoder.encode(fileName + ".xlsx", "UTF-8");
        response.setContentType("application/x-msdownload;charset=gbk");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        //编码格式
        response.setHeader("charset", "utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileNameTemp);
        //文件名称
        response.setHeader("file-name", fileNameTemp);
        //设置前端允许访问的Header，不设置前端获取不到文件名称
        response.setHeader("Access-Control-Expose-Headers", "file-name");
        OutputStream os = response.getOutputStream();
        Workbook excel = createExcel(fileName, list, pojoClass);
        excel.write(os);
        os.flush();
        os.close();
    }

}
