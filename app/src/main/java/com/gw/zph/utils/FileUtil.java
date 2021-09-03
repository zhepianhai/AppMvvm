package com.gw.zph.utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.gw.zph.base.db.dao.OffLineLatLngInfo;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import timber.log.Timber;

public class FileUtil {

    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
    public static final String localFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + Constants.APP_HOME_PATH;//离线填报保存数据根路径

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
     * 得到文件的扩展名
     *
     * @param uri 文件名或者文件uri
     * @return 返回文件的扩展名，如果文件名是null则返回null 如果文件没有扩展名则返回空
     */
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            return "";
        }
    }

    /**
     * 得到文件的扩展名
     *
     * @param uri 文件名或者文件uri
     * @return 返回文件的扩展名，如果文件名是null则返回null 如果文件没有扩展名则返回空
     */
    public static String getExtensionNoDot(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0 && uri.length() > dot + 1) {
            return uri.substring(dot + 1);
        } else {
            return "";
        }
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
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0,
                            pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }

    /**
     * 得到文件的名字，不包含路径
     *
     * @return 文件名
     */
    public static String getFileName(File file) {
        if (file != null && !file.isDirectory()) {
            String fileName = file.getName();

            String name = fileName.substring(0, fileName.length()
                    - getExtension(fileName).length());
            return name;
        }
        return null;
    }

    /**
     * 得到文件的名字，不包含路径
     *
     * @param name 文件路径
     * @return 文件名
     */
    public static String getFileName(final String name) {
        String trueName = name;
        int index = name.lastIndexOf("/");
        if (index > -1) {
            trueName = trueName.substring(index + 1);
        }
        int extensionSize = getExtension(trueName).length();
        if (extensionSize > 0) {
            trueName = trueName.substring(0, trueName.length() - extensionSize);
        }
        return trueName;
    }

    /**
     * 得到文件的名字，包含路径
     *
     * @param name 文件路径
     * @return 文件名
     */
    public static String getFileNameAndExtend(final String name) {
        String trueName = name;
        int index = name.lastIndexOf("/");
        if (index > -1) {
            trueName = trueName.substring(index + 1);
        }
        return trueName;
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
     *
     * @param file
     * @return
     */
    public static double getMbFileSize(File file) {
        long filesize = getFileSize(file);
        return (double) filesize / 1048576;
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
            path += "/";
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


    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strFileName, String infoMessage) {
        //生成文件夹之后，再生成文件，不然会出错
        String strFilePath = Environment.getExternalStorageDirectory().getPath()
                + File.separator + Constants.APP_HOME_PATH + Constants.OFFLINE_DU_CHA_OBJ_PATH + File.separator + strFileName;
        File txtFile = new File(strFilePath);

        if(txtFile.exists()){
            txtFile.delete();
        }
        create(strFilePath);
        // 每次写入时，都换行写
        String strContent = infoMessage + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    // 将字符串写入到文本文件中
    public static void writeTxtToFile(OffLineLatLngInfo info) {
        //生成文件夹之后，再生成文件，不然会出错
        String strFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + Constants.APP_HOME_PATH + Constants.LOCATION_TEST_FILE_PATH + Constants.LOCATION_TEST_FILE_PATH_NAME;
        create(strFilePath);
        String infoMessage = JSDateUtil.getDataStringByObj("时间:" + info.getOperateTime() + "，" + "经度:" + info.getLon() + "，" + "纬度:" + info.getLat()) + "\r\n";
        infoMessage += JSDateUtil.getDataStringByObj("位置:" + info.getAddress());
        // 每次写入时，都换行写
        String strContent = infoMessage + "\r\n";
        strContent += "==================";
        strContent += "\r\n";

        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     * @param zipFileString 压缩完成的Zip路径
     * @throws Exception
     */
    public static boolean ZipFolder(String srcFileString, String zipFileString){
        //创建ZIP
        try{
            ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
            //创建文件
            File file = new File(srcFileString);
            //压缩
            ZipFiles(file.getParent()+ File.separator, file.getName(), outZip);
            //完成和关闭
            outZip.finish();
            outZip.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        Timber.e("folderString:" + folderString + "\n" +
                "fileString:" + fileString + "\n==========================");
        if (zipOutputSteam == null)
            return;
        File file = new File(folderString + fileString);
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        } else {
            //文件夹
            String fileList[] = file.list();
            //没有子文件和压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString + fileString + "/", fileList[i], zipOutputSteam);
            }
        }
    }


    /** 删除单个文件
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                Log.e("--Method--", "删除单个文件" + filePath$Name + "失败！");
                return false;
            }
        } else {
            Log.e("--Method--", "删除单个文件失败：" + filePath$Name + "不存在！");
            return false;
        }
    }

    /** 删除目录及目录下的文件
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            Log.e("--Method--", "删除目录失败：" + filePath + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            } else if (file.isDirectory()) {// 删除子目录
                flag = deleteDirectory(file
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            Log.e("--Method--", "删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            Log.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
            return true;
        } else {
            Log.e("--Method--", "删除目录：" + filePath + "失败！");
            return false;
        }
    }

}
