package com.example;

/**
 * @Auther: tangwei
 * @Date: 2023/5/17 10:37 AM
 * @Description: 类描述信息
 */
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileUtils {
    public static final int BUFFER_SIZE = 16 * 1024;

    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    public static String getFilePath(String relativePath) {
        String dir = Objects.requireNonNull(FileUtils.class.getResource("/")).getPath();
        String filepath = dir + relativePath;
        if (filepath.contains(":")) {
            return filepath.substring(1);
        }
        return filepath;
    }

    public static String getFilePath(Class<?> clazz, String className) {
        String path = Objects.requireNonNull(clazz.getResource("/")).getPath();
        return String.format("%s%s.class", path, className.replace('.', File.separatorChar));
    }

    public static byte[] readClassBytes(Class<?> clazz) throws IOException {
        String resource = clazz.getSimpleName() + ".class";
        InputStream in = clazz.getResourceAsStream(resource);
        assert in != null;

        int available = in.available();
        byte[] bytes = new byte[available];
        int count = in.read(bytes);
        assert count != -1;
        return bytes;
    }

    public static byte[] readBytes(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File Not Exist: " + filepath);
        }

        try (
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis)
        ) {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            byte[] buf = new byte[BUFFER_SIZE];
            int len;
            while ((len = bis.read(buf)) != -1) {
                bao.write(buf, 0, len);
            }
            return bao.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void writeBytes(String filepath, byte[] bytes) {
        File file = new File(filepath);
        File dirFile = file.getParentFile();
        mkdirs(dirFile);

        try (OutputStream out = new FileOutputStream(filepath);
             BufferedOutputStream buff = new BufferedOutputStream(out)) {
            buff.write(bytes);
            buff.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readLines(String filepath) {
        return readLines(filepath, "UTF8");
    }

    public static List<String> readLines(String filepath, String charsetName) {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File Not Exist: " + filepath);
        }

        try (
                InputStream in = new FileInputStream(file);
                Reader reader = new InputStreamReader(in, charsetName);
                BufferedReader bufferReader = new BufferedReader(reader)
        ) {
            List<String> list = new ArrayList<>();
            String line;
            while ((line = bufferReader.readLine()) != null) {
                list.add(line);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeLines(String filepath, List<String> lines) {
        if (lines == null || lines.size() < 1) return;

        File file = new File(filepath);
        File dirFile = file.getParentFile();
        mkdirs(dirFile);

        try (
                OutputStream out = new FileOutputStream(file);
                Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                BufferedWriter bufferedWriter = new BufferedWriter(writer)
        ) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mkdirs(File dirFile) {
        boolean file_exists = dirFile.exists();

        if (file_exists && dirFile.isDirectory()) {
            return;
        }

        if (file_exists && dirFile.isFile()) {
            throw new RuntimeException("Not A Directory: " + dirFile);
        }

        if (!file_exists) {
            boolean flag = dirFile.mkdirs();
            if (!flag) {
                throw new RuntimeException("Create Directory Failed: " + dirFile.getAbsolutePath());
            }
        }
    }

    public static void copy(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            throw new IllegalArgumentException("srcPath is not valid: " + srcPath);
        }

        File destFile = new File(destPath);
        File dirFile = destFile.getParentFile();
        mkdirs(dirFile);

        try (
                FileInputStream fis = new FileInputStream(srcFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos)
        ) {
            byte[] buf = new byte[BUFFER_SIZE];
            int len;
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static byte[] readClassBytes(String className) {
        InputStream in = getInputStream(className);
        return readStream(in);
    }

    public static byte[] readStream(final InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("inputStream is null!!!");
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            return out.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                //
            }
        }
        return null;
    }

    public static InputStream getInputStream(String className) {
        return ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class");
    }
}