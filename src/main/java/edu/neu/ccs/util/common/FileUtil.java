package edu.neu.ccs.util.common;

/**
 * Created by Administrator on 2018/3/2.
 */

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil extends FileUtils {
    private static final int BUFF_SIZE = 1024;
    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    public FileUtil() {
    }

    public static void copyFile(String src, String target) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new FileInputStream(src));
            out = new BufferedOutputStream(new FileOutputStream(target));
            byte[] e = new byte[1024];

            int len;
            while((len = in.read(e, 0, 1024)) > 0) {
                out.write(e, 0, len);
                out.flush();
            }
        } catch (IOException var9) {
            var9.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }

    }

    public static List<String> readAsStringList(String fileName) {
        ArrayList list = new ArrayList();
        BufferedReader reader = null;
        FileInputStream fis = null;

        try {
            File e = new File(fileName);
            if(e.isFile() && e.exists()) {
                fis = new FileInputStream(e);
                reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

                String line;
                while((line = reader.readLine()) != null) {
                    if(!"".equals(line)) {
                        list.add(line);
                    }
                }
            }
        } catch (Exception var18) {
            log.error("readFile", var18);
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException var17) {
                log.error("InputStream关闭异常", var17);
            }

            try {
                if(fis != null) {
                    fis.close();
                }
            } catch (IOException var16) {
                log.error("FileInputStream关闭异常", var16);
            }

        }

        return list;
    }

    public static byte[] readFile(File file) {
        byte[] bytes = null;

        try {
            bytes = IOUtils.readFully(new FileInputStream(file));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return bytes;
    }

    public static byte[] readFile(String fileName) {
        return readFile(new File(fileName));
    }

    public static void writeFile(byte[] bytes, String outputFile) {
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(outputFile);
            os.write(bytes);
        } catch (Exception var7) {
            var7.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }

    }

    public static File mkdir(String folderPath) {
        File file = new File(folderPath);
        if(!file.exists() || !file.isDirectory()) {
            boolean success = false;

            do {
                success = file.mkdirs();
            } while(success);
        }

        return file;
    }

    public static void delFolder(String folderPath) throws Exception {
        delAllFile(folderPath);
        File myFilePath = new File(folderPath);
        myFilePath.delete();
    }

    public static boolean delAllFile(String path) throws Exception {
        boolean flag = false;
        File file = new File(path);
        if(!file.exists()) {
            return flag;
        } else if(!file.isDirectory()) {
            return flag;
        } else {
            String[] tempList = file.list();
            File temp = null;

            for(int i = 0; i < tempList.length; ++i) {
                if(path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }

                if(temp.isFile()) {
                    temp.delete();
                }

                if(temp.isDirectory()) {
                    delAllFile(path + "/" + tempList[i]);
                    delFolder(path + "/" + tempList[i]);
                    flag = true;
                }
            }

            return flag;
        }
    }

    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if(!file.exists()) {
            log.info(fileName + " 文件不存在!");
            return true;
        } else {
            return file.isFile()?deleteFile(fileName):deleteDirectory(fileName);
        }
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if(file.exists() && file.isFile()) {
            if(file.delete()) {
                log.info("删除单个文件 " + fileName + " 成功!");
                return true;
            } else {
                log.info("删除单个文件 " + fileName + " 失败!");
                return false;
            }
        } else {
            log.info(fileName + " 文件不存在!");
            return true;
        }
    }

    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if(!dirName.endsWith(File.separator)) {
            dirNames = dirName + File.separator;
        }

        File dirFile = new File(dirNames);
        if(dirFile.exists() && dirFile.isDirectory()) {
            boolean flag = true;
            File[] files = dirFile.listFiles();

            for(int i = 0; i < files.length; ++i) {
                if(files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if(!flag) {
                        break;
                    }
                } else if(files[i].isDirectory()) {
                    flag = deleteDirectory(files[i].getAbsolutePath());
                    if(!flag) {
                        break;
                    }
                }
            }

            if(!flag) {
                log.info("删除目录失败!");
                return false;
            } else if(dirFile.delete()) {
                log.info("删除目录 " + dirName + " 成功!");
                return true;
            } else {
                log.info("删除目录 " + dirName + " 失败!");
                return false;
            }
        } else {
            log.info(dirNames + " 目录不存在!");
            return true;
        }
    }

    public static boolean exists(String path) {
        return (new File(path)).exists();
    }

    public static void checkAndMkdirs(File file) {
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

    }

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public static String getFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
