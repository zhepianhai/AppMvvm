package com.lzy.imagepicker.util;

import android.net.Uri;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * date 2019/4/12
 * @version V1.0
 */
public class FileUtil {

    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值


    public static byte[] readFile(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];
        DataInputStream input = new DataInputStream(new FileInputStream(file));
        try {
            input.readFully(bytes);
        } finally {
            input.close();
        }
        return bytes;
    }

    public static byte[] readFile(String filePath) throws IOException {

        return readFile(new File(filePath));
    }




    /**
     * 把文件转化成uri
     *
     * @param file 文件对象
     * @return uri结果
     */
    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    /**
     * 把uri转化成文件
     *
     * @param uri
     * @return file对象
     */
    public static File getFile(Uri uri) {
        if (uri != null) {
            String filepath = uri.getPath();
            if (filepath != null) {
                return new File(filepath);
            }
        }
        return null;
    }

    /**
     * 得到文件的路径，不包含名字
     *
     * @return 文件名
     */
    public static File getPathWithoutFileName(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                // no file to be split off. Return everything
                return file;
            } else {
                String filename = file.getName();
                String filepath = file.getAbsolutePath();

                // Construct path without file name.
                String pathwithoutname = filepath.substring(0,
                        filepath.length() - filename.length());
                if (pathwithoutname.endsWith(File.separator)) {
                    pathwithoutname = pathwithoutname.substring(0,
                            pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }




    /**
     * 该文件是否存在
     *
     * @param name 文件路径
     * @return true存在 false不存在
     */
    public static boolean isExistFile(String name) {
        return new File(name).exists();
    }


    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @return double值的大小
     */
    public static long getFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return blockSize;
    }


    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {

                fis = new FileInputStream(file);
                size = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }
        return size;
    }

    /**
     * 获取文件有几mb 兆
     * @param file
     * @return
     */
    public static double getMbFileSize(File file){
        long filesize=getFileSize(file);
        return (double)filesize / 1048576;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }



    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    public static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.parseDouble(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.parseDouble(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.parseDouble(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.parseDouble(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 删除文件
     *
     * @param path
     * @param name
     * @return
     */
    public static boolean deleteFile(String path, String name) {
        int len = path.length();

        if (len < 1 || len < 1)
            return false;

        if (path.charAt(len - 1) != '/')
            path +=File.separator;
        File temFile = new File(path + name);

        if (temFile.exists()) {
            return temFile.delete();
        }

        return true;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        int len = path.length();

        if (len < 1 || len < 1)
            return false;

        File temFile = new File(path);

        if (temFile.exists()) {
            return temFile.delete();
        }
        return true;
    }

    /**
     * 删除文件或文件夹（包括文件夹下所有文件）
     *
     * @param path 文件的路径
     * @return
     */
    public static boolean deleteFileOrDir(String path) {
        if (path == null)
            return false;
        int len = path.length();

        if (len < 1 || len < 1)
            return false;

        return deleteFileOrDir(new File(path));

    }

    /**
     * 删除文件或文件夹（包括文件夹下所有文件）
     *
     * @param temFile 文件对象
     * @return
     */
    public static boolean deleteFileOrDir(File temFile) {

        if (temFile == null) {
            return false;
        }

        if (!temFile.exists()) {
            return false;
        }
        if (temFile.isDirectory()) {
            File[] fileList = temFile.listFiles();
            int count = fileList.length;
            for (int i = 0; i < count; i++) {
                deleteFileOrDir(fileList[i]);
            }
            temFile.delete();
        } else if (temFile.isFile()) {
            temFile.delete();
        }
        return true;
    }

    /**
     * 删除文件或文件夹（包括文件夹下所有文件）
     *
     * @param path 文件的路径
     * @return
     */
    public static boolean deleteDirContainFile(String path) {
        if (path == null) {
            return false;
        }
        return deleteDirContainFile(new File(path));
    }

    /**
     * 删除文件或文件夹（文件夹下所有文件）
     *
     * @return
     */
    public static boolean deleteDirContainFile(File file) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return false;
        }

        File[] fileList = file.listFiles();
        int count = fileList.length;
        boolean flag = true;
        for (int i = 0; i < count; i++) {
            flag = flag && deleteFileOrDir(fileList[i]);
        }
        return flag;
    }

    /**
     * 复制文件
     *
     * @param source 输入文件
     * @param target 输出文件
     */
    public static void copy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void create(String filePath) {
        File file = new File(filePath);
        if (filePath.indexOf(".") != -1) {
            // 说明包含，即使创建文件, 返回值为-1就说明不包含.,即使文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("创建了文件");
        } else {
            // 创建文件夹
            file.mkdir();
            System.out.println("创建了文件夹");
        }
    }
}
