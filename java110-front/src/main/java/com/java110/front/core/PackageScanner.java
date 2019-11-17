
package com.java110.front.core;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by wuxw on 2019/3/18.
 */

public abstract class PackageScanner {

    public void packageScanner(Class<?> klass, String suffix) {
        packageScanner(klass.getPackage().getName(), suffix);
    }

    public void packageScanner(String packageName, String suffix) {
        String packagePath = packageName.replace(".", File.separator);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (url.getProtocol().equals("jar")) {
                    scanPackage(packageName, url, suffix);
                } else {
                    File file = new File(url.toURI());
                    if (!file.exists()) {
                        continue;
                    }
                    scanPackage(packageName, file, suffix);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void scanPackage(String packageName, URL url, String suffix) throws IOException {
        JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarUrlConnection.getJarFile();
        Enumeration<JarEntry> jarEntries = jarFile.entries();


        while (jarEntries.hasMoreElements()) {

            JarEntry jarEntry = jarEntries.nextElement();
            String jarName = jarEntry.getName();
            if (jarEntry.isDirectory() || !jarName.endsWith(suffix) || !jarName.startsWith(packageName)) {
                continue;
            }
            //String className = jarName.replace(suffix, "");

            handleResource(jarName);
        }
    }

    private void scanPackage(String packageName, final File currentfile, final String suffix) {
        File[] files = currentfile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (currentfile.isDirectory()) {
                    return true;
                }
                return pathname.getName().endsWith(suffix);
            }
        });
        for (File file : files) {
            if (file.isDirectory()) {
                scanPackage(packageName + "." + file.getName(), file, suffix);
            } else {
                packageName = packageName.replace(".", File.separator);
                String fileName = packageName + File.separator + file.getName();
                if (StringUtils.isEmpty(fileName) || !fileName.endsWith(suffix)) {
                    continue;
                }
                handleResource(fileName);
            }
        }

    }

    protected abstract void handleResource(String filePath);

    public static void main(String[] args) {
        new PackageScanner() {
            @Override
            protected void handleResource(String filePath) {

            }
        }.packageScanner("components", "js");
    }

}