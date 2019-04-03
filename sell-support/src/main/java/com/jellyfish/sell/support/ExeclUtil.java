package com.jellyfish.sell.support;

import com.jellyfish.sell.support.annotation.IsNeeded;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * ImportExeclUtil
 *
 * @author Driss
 * @time 2018/11/22 3:05 PM
 * @email tt.ckuiry@foxmail.com
 */
public class ExeclUtil {
    // 总行数
    private static int totalRows = 0;
    // 总列数
    private static int totalCells = 0;
    // 错误信息
    private static String errorInfo;

    /**
     * 无参构造方法
     */
    public ExeclUtil() {
    }

    public static int getTotalRows() {
        return totalRows;
    }

    public static int getTotalCells() {
        return totalCells;
    }

    public static String getErrorInfo() {
        return errorInfo;
    }


    /**
     * 根据流读取Excel文件
     *
     * @param inputStream
     * @param isExcel2003
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<List<String>> read(InputStream inputStream, boolean isExcel2003) throws IOException {

        List<List<String>> dataLst = null;

        /** 根据版本选择创建Workbook的方式 */
        Workbook wb = null;

        if (isExcel2003) {
            wb = new HSSFWorkbook(inputStream);
        } else {
            wb = new XSSFWorkbook(inputStream);
        }
        dataLst = readDate(wb);

        return dataLst;
    }

    /**
     * 读取数据
     *
     * @param wb
     * @return
     * @see [类、类#方法、类#成员]
     */
    private List<List<String>> readDate(Workbook wb) {

        List<List<String>> dataLst = new ArrayList<List<String>>();

        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);

        /** 得到Excel的行数 */
        totalRows = sheet.getPhysicalNumberOfRows();

        /** 得到Excel的列数 */
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        /** 循环Excel的行 */
        for (int r = 0; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }

            List<String> rowLst = new ArrayList<String>();

            /** 循环Excel的列 */
            for (int c = 0; c < getTotalCells(); c++) {

                Cell cell = row.getCell(c);
                String cellValue = "";

                if (null != cell) {
                    // 以下是判断数据的类型
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                            cellValue = cell.getNumericCellValue() + "";
                            break;

                        case HSSFCell.CELL_TYPE_STRING: // 字符串
                            cellValue = cell.getStringCellValue();
                            break;

                        case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                            cellValue = cell.getBooleanCellValue() + "";
                            break;

                        case HSSFCell.CELL_TYPE_FORMULA: // 公式
                            cellValue = cell.getCellFormula() + "";
                            break;

                        case HSSFCell.CELL_TYPE_BLANK: // 空值
                            cellValue = "";
                            break;

                        case HSSFCell.CELL_TYPE_ERROR: // 故障
                            cellValue = "非法字符";
                            break;

                        default:
                            cellValue = "未知类型";
                            break;
                    }
                }

                rowLst.add(cellValue);
            }

            /** 保存第r行的第c列 */
            dataLst.add(rowLst);
        }

        return dataLst;
    }

    /**
     * 按指定坐标读取实体数据
     * <按顺序放入带有注解的实体成员变量中>
     *
     * @param wb       工作簿
     * @param t        实体
     * @param in       输入流
     * @param integers 指定需要解析的坐标
     * @return T 相应实体
     * @throws IOException
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unused")
    public static <T> T readDateT(Workbook wb, T t, InputStream in, Integer[]... integers)
            throws IOException, Exception {
        // 获取该工作表中的第一个工作表
        Sheet sheet = wb.getSheetAt(0);

        // 成员变量的值
        Object entityMemberValue = "";

        // 所有成员变量
        Field[] fields = t.getClass().getDeclaredFields();
        // 列开始下标
        int startCell = 0;

        /** 循环出需要的成员 */
        for (int f = 0; f < fields.length; f++) {

            fields[f].setAccessible(true);
            String fieldName = fields[f].getName();
            boolean fieldHasAnno = fields[f].isAnnotationPresent(IsNeeded.class);
            // 有注解
            if (fieldHasAnno) {
                IsNeeded annotation = fields[f].getAnnotation(IsNeeded.class);
                boolean isNeeded = annotation.isNeeded();

                // Excel需要赋值的列
                if (isNeeded) {

                    // 获取行和列
                    int x = integers[startCell][0] - 1;
                    int y = integers[startCell][1] - 1;

                    Row row = sheet.getRow(x);
                    Cell cell = row.getCell(y);

                    if (row == null) {
                        continue;
                    }

                    // Excel中解析的值
                    String cellValue = getCellValue(cell);
                    // 需要赋给成员变量的值
                    entityMemberValue = getEntityMemberValue(entityMemberValue, fields, f, cellValue);
                    // 赋值
                    PropertyUtils.setProperty(t, fieldName, entityMemberValue);
                    // 列的下标加1
                    startCell++;
                }
            }

        }

        return t;
    }

    /**
     * 读取列表数据
     * <按顺序放入带有注解的实体成员变量中>
     *
     * @param wb        工作簿
     * @param t         实体
     * @param beginLine 开始行数
     * @param totalcut  结束行数减去相应行数
     * @return List<T> 实体列表
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readDateListT(Workbook wb, T t, int beginLine, int totalcut)
            throws Exception {
        List<T> listt = new ArrayList<T>();

        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);

        /** 得到Excel的行数 */
        totalRows = sheet.getPhysicalNumberOfRows();

        /** 得到Excel的列数 */
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        /** 循环Excel的行 */
        for (int r = beginLine - 1; r < totalRows - totalcut; r++) {
            Object newInstance = t.getClass().newInstance();
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }

            // 成员变量的值
            Object entityMemberValue = "";

            // 所有成员变量
            Field[] fields = t.getClass().getDeclaredFields();
            // 列开始下标
            int startCell = 0;

            for (int f = 0; f < fields.length; f++) {

                fields[f].setAccessible(true);
                String fieldName = fields[f].getName();
                boolean fieldHasAnno = fields[f].isAnnotationPresent(IsNeeded.class);
                // 有注解
                if (fieldHasAnno) {
                    IsNeeded annotation = fields[f].getAnnotation(IsNeeded.class);
                    boolean isNeeded = annotation.isNeeded();
                    // Excel需要赋值的列
                    if (isNeeded) {
                        Cell cell = row.getCell(startCell);
                        String cellValue = getCellValue(cell);
                        entityMemberValue = getEntityMemberValue(entityMemberValue, fields, f, cellValue);
                        // 赋值
                        PropertyUtils.setProperty(newInstance, fieldName, entityMemberValue);
                        // 列的下标加1
                        startCell++;
                    }
                }

            }

            listt.add((T) newInstance);
        }

        return listt;
    }

    /**
     * 根据Excel表格中的数据判断类型得到值
     *
     * @param cell
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static String getCellValue(Cell cell) {
        String cellValue = "";

        if (null != cell) {
            // 以下是判断数据的类型
            switch (cell.getCellType()) {
                // 数字
                case HSSFCell.CELL_TYPE_NUMERIC:
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        Date theDate = cell.getDateCellValue();
                        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                        cellValue = dff.format(theDate);
                    } else {
                        DecimalFormat df = new DecimalFormat("0");
                        cellValue = df.format(cell.getNumericCellValue());
                    }
                    break;
                // 字符串
                case HSSFCell.CELL_TYPE_STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                // Boolean
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    cellValue = cell.getBooleanCellValue() + "";
                    break;
                // 公式
                case HSSFCell.CELL_TYPE_FORMULA:
                    cellValue = cell.getCellFormula() + "";
                    break;
                // 空值
                case HSSFCell.CELL_TYPE_BLANK:
                    cellValue = "";
                    break;
                // 故障
                case HSSFCell.CELL_TYPE_ERROR:
                    cellValue = "非法字符";
                    break;

                default:
                    cellValue = "未知类型";
                    break;
            }

        }
        return cellValue;
    }

    /**
     * 根据实体成员变量的类型得到成员变量的值
     *
     * @param realValue
     * @param fields
     * @param f
     * @param cellValue
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static Object getEntityMemberValue(Object realValue, Field[] fields, int f, String cellValue) {
        String type = fields[f].getType().getName();
        switch (type) {
            case "char":
            case "java.lang.Character":
            case "java.lang.String":
                realValue = cellValue;
                break;
            case "java.util.Date":
                realValue = StringUtils.isBlank(cellValue) ? null : DateUtil.strToDate(cellValue, DateUtil.YYYY_MM_DD);
                break;
            case "java.lang.Integer":
                realValue = StringUtils.isBlank(cellValue) ? null : Integer.valueOf(cellValue);
                break;
            case "int":
            case "float":
            case "double":
            case "java.lang.Double":
            case "java.lang.Float":
            case "java.lang.Long":
            case "java.lang.Short":
            case "java.math.BigDecimal":
                realValue = StringUtils.isBlank(cellValue) ? null : new BigDecimal(cellValue);
                break;
            default:
                break;
        }
        return realValue;
    }

    /**
     * 根据路径或文件名选择Excel版本
     *
     * @param filePathOrName
     * @param in
     * @return
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */
    public static Workbook chooseWorkbook(String filePathOrName, InputStream in)
            throws IOException {
        /** 根据版本选择创建Workbook的方式 */
        Workbook wb = null;
        boolean isExcel2003 = ExcelVersionUtil.isExcel2003(filePathOrName);

        if (isExcel2003) {
            wb = new HSSFWorkbook(in);
        } else {
            wb = new XSSFWorkbook(in);
        }

        return wb;
    }

    static class ExcelVersionUtil {

        /**
         * 是否是2003的excel，返回true是2003
         *
         * @param filePath
         * @return
         * @see [类、类#方法、类#成员]
         */
        public static boolean isExcel2003(String filePath) {
            return filePath.matches("^.+\\.(?i)(xls)$");

        }

        /**
         * 是否是2007的excel，返回true是2007
         *
         * @param filePath
         * @return
         * @see [类、类#方法、类#成员]
         */
        public static boolean isExcel2007(String filePath) {
            return filePath.matches("^.+\\.(?i)(xlsx)$");

        }

    }

    public static class DateUtil {

        // ======================日期格式化常量=====================//

        public static final String YYYY_MM_DDHHMMSS = "yyyy-MM-dd HH:mm:ss";

        public static final String YYYY_MM_DD = "yyyy-MM-dd";

        public static final String YYYY_MM = "yyyy-MM";

        public static final String YYYY = "yyyy";

        public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

        public static final String YYYYMMDD = "yyyyMMdd";

        public static final String YYYYMM = "yyyyMM";

        public static final String YYYYMMDDHHMMSS_1 = "yyyy/MM/dd HH:mm:ss";

        public static final String YYYY_MM_DD_1 = "yyyy/MM/dd";

        public static final String YYYY_MM_1 = "yyyy/MM";

        /**
         * 自定义取值，Date类型转为String类型
         *
         * @param date    日期
         * @param pattern 格式化常量
         * @return
         * @see [类、类#方法、类#成员]
         */
        public static String dateToStr(Date date, String pattern) {
            SimpleDateFormat format = null;

            if (null == date) {
                return null;
            }
            format = new SimpleDateFormat(pattern, Locale.getDefault());

            return format.format(date);
        }

        /**
         * 将字符串转换成Date类型的时间
         * <hr>
         *
         * @param s 日期类型的字符串<br>
         *          datePattern :YYYY_MM_DD<br>
         * @return java.util.Date
         */
        public static Date strToDate(String s, String pattern) {
            if (s == null) {
                return null;
            }
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                date = sdf.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
    }


    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {
        // 第一步，创建一个webbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 准备下拉列表数据
        String[] strs = new String[]{"0", "1", "2", "3"};
        // 设置第一列的1-10行为下拉列表
        CellRangeAddressList regions = new CellRangeAddressList(1, 65536, 16, 16);
        // 创建下拉列表数据
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(strs);
        // 绑定
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        dataValidation.setSuppressDropDownArrow(false);
        dataValidation.createPromptBox("输入提示", "请从下拉列表中选择男女");
        dataValidation.setShowPromptBox(true);

        sheet.addValidationData(dataValidation);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);
        //创建单元格，并设置值表头 设置表头居中
        //设置行高
        row.setHeight((short) 500);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //垂直居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //设置背景色
        style.setFillForegroundColor(IndexedColors.PINK.getIndex());
        style.setFillPattern(XSSFCellStyle.FINE_DOTS);

        HSSFCellStyle styleC = wb.createCellStyle();
        styleC.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //垂直居中
        styleC.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        HSSFCell cell = null;
        //创建标题
        for (int i = 0; i < title.length; i++) {
            if (i == 0 || i == 16 || i == 12 || i == 23 || i == 24) {
                sheet.setColumnWidth(i, (short) (35.7 * 300));
            } else {
                sheet.setColumnWidth(i, (short) (35.7 * 100));
            }
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                HSSFCell cell1 = row.createCell(j);
                cell1.setCellValue(values[i][j]);
                cell1.setCellStyle(styleC);
            }
        }

        return wb;
    }

    /**
     * 获取解析文件行数据
     *
     * @param fileName : 文件地址
     * @param isTitle  : 是否过滤第一行解析
     * @return
     * @throws Exception
     */
    public static List<Row> getExcelRead(String fileName, InputStream is, boolean isTitle) throws Exception {
        try {
            //判断其兼容版本 调用了判断版本的方法
            Workbook workbook = getWorkbook(fileName, is);
            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;
            List<Row> list = new ArrayList<Row>();
            for (Row row : sheet) {
                // 跳过第一行的目录
                if (count == 0 && isTitle) {
                    count++;
                    continue;
                }
                list.add(row);
            }
            return list;
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 判断版本的方法
     *
     * @param fileName
     * @param is
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(String fileName, InputStream is) throws Exception {
        Workbook workbook = null;
        try {
            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = true;
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }

            if (isExcel2003) {
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            throw e;
        }
        return workbook;
    }

    /**
     * @param filePath
     * @return
     * @描述：是否是2003的excel，返回true是2003
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * @param filePath
     * @return
     * @描述：是否是2007的excel，返回true是2007
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 验证是否是EXCEL文件
     *
     * @param filePath
     * @return
     */
    public static boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            return false;
        }
        return true;
    }

    /**
     * 得到celL值的方法
     */
    public static String getValue(Cell cell) {
        if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            double value = cell.getNumericCellValue();
            return new BigDecimal(value).toString();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            return String.valueOf(cell.getStringCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }
}
