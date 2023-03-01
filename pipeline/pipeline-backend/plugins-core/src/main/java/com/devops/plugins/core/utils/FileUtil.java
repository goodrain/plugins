/*
 *
 * Copyright 2023 Talkweb Co., Ltd.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * /
 */

package com.devops.plugins.core.utils;

import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.result.ABizCode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author sheep
 * @create 2021-12-29 17:38
 */
public class FileUtil {


    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_IE = "MSIE";


    public static void zipUncompress(File srcFile, String destDirPath) {
        //创建压缩文件对象
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile);
        } catch (IOException e) {
            throw new BusinessRuntimeException(srcFile.getPath() + "所指文件不存在");
        }
        //开始解压
        Enumeration<?> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            // 如果是文件夹，就创建个文件夹
            if (entry.isDirectory()) {
                srcFile.mkdirs();
            } else {
                // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                File targetFile = new File(destDirPath + "/" + entry.getName());
                // 保证这个文件的父文件夹必须要存在
                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdirs();
                }
                try {
                    targetFile.createNewFile();
                } catch (IOException e) {
                    throw new BusinessRuntimeException(srcFile.getPath() + "创建文件失败");
                }
                // 将压缩文件内容写入到这个文件中
                try (InputStream is = zipFile.getInputStream(entry);
                     FileOutputStream fos = new FileOutputStream(targetFile)) {
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                } catch (IOException e) {
                    throw new BusinessRuntimeException(srcFile.getPath() + "写入文件失败");
                }
            }
        }

    }


    /**
     * 通过inputStream流 替换文件的参数 ，将file转换为流渲染参数后写回文件
     *
     * @param file   文件
     * @param params 参数
     */
    public static void replaceReturnFile(File file, Map<String, String> params, Boolean micros) {
        File[] files = file.listFiles();

        // files 可能为 null
        if (files == null) {
            return;
        }

        for (File a : files) {
            if (a.getName().equals(".git") || a.getName().endsWith(".xlsx") || a.getName().equals("java")) {
                continue;
            }
            File newFile = null;
            if(micros) {
                if (a.getName().equals("demo-api")) {
                    String parentPath = a.getParent();
                    newFile = new File(parentPath + File.separator + params.get("{{module}}") + "-api");
                    if (!a.renameTo(newFile)) {
                        throw new BusinessRuntimeException(ABizCode.FAIL, "error.rename.file");
                    }
                }
                if (a.getName().equals("demo-server")) {
                    String parentPath = a.getParent();
                    newFile = new File(parentPath + File.separator + params.get("{{module}}") + "-server");
                    if (!a.renameTo(newFile)) {
                        throw new BusinessRuntimeException(ABizCode.FAIL, "error.rename.file");
                    }
                }
                if (a.getName().equals("demo")) {
                    String parentPath = a.getParent();
                    newFile = new File(parentPath + File.separator + params.get("{{module}}"));
                    if (!a.renameTo(newFile)) {
                        throw new BusinessRuntimeException(ABizCode.FAIL, "error.rename.file");
                    }
                }
            }
            if (newFile == null) {
                fileToInputStream(a, params, micros);
            } else {
                fileToInputStream(newFile, params, micros);
            }
        }
    }


    /**
     * 文件转输入流
     *
     * @param file   来源文件
     * @param params 参数
     */
    public static void fileToInputStream(File file, Map<String, String> params, Boolean micros) {
        if (file.isDirectory()) {
            replaceReturnFile(file, params, micros);
        } else {
            try (InputStream inputStream = new FileInputStream(file)) {
                FileUtils.writeStringToFile(file, replaceReturnString(inputStream, params));
            } catch (IOException e) {
                throw new BusinessRuntimeException(ABizCode.FILE_ERROR, e.getMessage());
            }
        }
    }


    /**
     * 通过inputStream流 替换文件的参数
     *
     * @param inputStream 流
     * @param params      参数
     * @return String
     */
    public static String replaceReturnString(InputStream inputStream, Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            byte[] b = new byte[32768];
            for (int n; (n = inputStream.read(b)) != -1; ) {
                String content = new String(b, 0, n);
                if (params != null) {
                    for (Object o : params.entrySet()) {
                        Map.Entry entry = (Map.Entry) o;
                        content = content.replace(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                    }
                }
                stringBuilder.append(content);
            }
            inputStream.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new BusinessRuntimeException(ABizCode.FILE_ERROR, e.getMessage());
        }
    }


    /**
     * 复制文件夹
     *
     * @param resource 源路径
     * @param target   目标路径
     */
    public static void copyFolder(String resource, String target) throws Exception {

        File resourceFile = new File(resource);
        if (!resourceFile.exists()) {
            throw new Exception("源目标路径：[" + resource + "] 不存在...");
        }
        File targetFile = new File(target);
        if (!targetFile.exists()) {
            targetFile.mkdir();
//            throw new Exception("存放的目标路径：[" + target + "] 不存在...");
        }

        // 获取源文件夹下的文件夹或文件
        File[] resourceFiles = resourceFile.listFiles();

        for (File file : resourceFiles) {
            File file1;
            if (resourceFile.getName().equals("demo")) {
                file1 = new File(targetFile.getAbsolutePath());
            } else {
                file1 = new File(targetFile.getAbsolutePath() + File.separator + resourceFile.getName());
            }
            // 复制文件
            if (file.isFile()) {
                // 在 目标文件夹（B） 中 新建 源文件夹（A），然后将文件复制到 A 中
                // 这样 在 B 中 就存在 A
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File targetFile1 = new File(file1.getAbsolutePath() + File.separator + file.getName());
                fileCopy(file, targetFile1);
            }
            // 复制文件夹
            if (file.isDirectory()) {// 复制源文件夹
                String dir1 = file.getAbsolutePath();
                // 目的文件夹
                String dir2 = file1.getAbsolutePath();
                copyFolder(dir1, dir2);
            }
        }

    }


    public static void setAttachment(String name, HttpServletRequest req, HttpServletResponse resp) {
        String userAgent = req.getHeader(USER_AGENT);
        if (StringUtils.isBlank(userAgent) || StringUtils.containsIgnoreCase(userAgent, USER_AGENT_IE)) {
            // IE浏览器
            try {
                name = URLEncoder.encode(name, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                // ignore
            }
        } else {
            // 谷歌、火狐等现代浏览器
            name = new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"");
    }

    public static void fileCopy(File origin, File dest) {
        try (InputStream in = new FileInputStream(origin);
             OutputStream out = new FileOutputStream(dest, true)) {
            IOUtils.copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
