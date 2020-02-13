package com.lwp.example.plsql;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlsqlHelper {

    /**
     * 获取建表sql
     * @param plsqExportSqlFile
     * @param encoding 读取文件使用的encoding
     * @param tableNames 方法中会把表名转为大写
     * @return
     * @throws IOException
     */
    public static String getTableDll(String plsqExportSqlFile, String encoding, String ... tableNames) throws IOException {
        Objects.nonNull(tableNames);
//        System.out.println(new File(plsqExportSqlFile).getAbsolutePath());
//        System.out.println(new File(PlsqlHelper.class.getClassLoader().getResource(plsqExportSqlFile).getFile()));
        List<String> contentLines = FileUtils.readLines(new File(PlsqlHelper.class.getClassLoader().getResource(plsqExportSqlFile).getFile()), encoding);
        Map<String, List<String>> tableSqlMap = getTableMap (contentLines);
        // 读取plsql导出的文件
        List<String> allSqlList = new ArrayList<>();
        for (String tableName : tableNames) {
            if (tableSqlMap.get("create table " + tableName.toUpperCase()) != null) {
                allSqlList.addAll(tableSqlMap.get("create table " + tableName.toUpperCase()));
            } else {
                throw new RuntimeException(String.format("表名 %s 的表不存在", tableName));
            }
            // System.out.println(tableName);
        }
        return ListUtils.join(allSqlList, "\n");
    }

    /**
     * tableName 为key 建表语句为value， 其他序列不保存
     * @param contentLines plsql文件读取出来的内容数组，一个元素就是sql表中的一行内容
     * @return
     */
    private static Map<String, List<String>> getTableMap(List<String> contentLines) {
        Map<String,  List<String>> tableMap = new HashMap<>();
        if (contentLines != null) {
            // 存放单个对象sql语句
            List<String> dbObjectTempList = new ArrayList<>();
            for (int i = 0; i < contentLines.size(); i++) {
                String tempStr = contentLines.get(i);
                // 判断是否空行,空行就有一个新的数据库对象
                dbObjectTempList.add(tempStr);
                if (StringUtils.isBlank(tempStr)) {
                    if (dbObjectTempList == null || dbObjectTempList.isEmpty()) {
                        // 多个连续空白行
                        continue;
                    }
                    // 拼当前对象
                    for (int i1 = 0; i1 < dbObjectTempList.size(); i1++) {
                        String sql = dbObjectTempList.get(i1);
                        // create sequence
                        if (sql.contains("create table")) {
                            tableMap.put(sql, dbObjectTempList);
                        }
                    }
                    dbObjectTempList = new ArrayList<>();
                    continue;
                }
            }
        }
        return tableMap;
    }

    public static void main(String[] args) throws IOException {
        String sql = PlsqlHelper.getTableDll("plsql_tables.sql", "GBK","test", "test1");
        System.out.println(sql);
        // 内容输出到文件
        String outFilename = "test.sql";
        File file = new File(outFilename);
        System.out.println(file.getAbsolutePath());
        FileUtils.writeStringToFile(file, sql, "GBK");
    }
}
