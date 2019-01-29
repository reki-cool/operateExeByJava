import com.pty4j.PtyProcess;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Pty4j_1 {

    // 运行EXE文件
    public static void doExe(String[] command, final String charset, String directory) {
        try {
            PtyProcess pty = PtyProcess.exec(command, new HashMap<>(System.getenv()), directory);

            final InputStream is = pty.getInputStream();
            final OutputStream os = pty.getOutputStream();

            // 用于获取exe执行结果
            new Thread() {
                public void run() {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(is, charset))){
//                        int temp = -1;
//                        while ((temp = br.read()) != -1) {
//                            System.out.print((char) temp);
//                        }
                        String temp = null;
                        while((temp = br.readLine()) != null) {
                            System.out.println(temp);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();


            new Thread() {
                public void run() {
                    try (Scanner sc = new Scanner(System.in);
                         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, charset))) {
                        String line = null;
                        while ((line = sc.nextLine()) != null) {
                            bw.write(line);
                            bw.newLine();
                            // 若不使用flush方法，则待执行的命令将会被存储在缓存区内，无法及时传入到目标程序内部
                            bw.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        // 运行cmd.exe程序
//        doExe(new String[]{"cmd.exe"}, "UTF-8", "C:/Program Files");
        // 运行TerrariaServer.exe程序
        doExe(new String[]{"tshock_4.3.25/TerrariaServer.exe"}, "UTF-8", "tshock_4.3.25");
    }
}
