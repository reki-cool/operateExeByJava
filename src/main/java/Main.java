import java.io.*;
import java.util.Scanner;

public class Main {

    // 运行EXE文件
    public static void doExe(String[] command, final String charset, String directory) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(directory));
            pb.redirectErrorStream(true);
            Process p = pb.start();

            // 输入流，用于获取  exe执行后的返回结果
            final InputStream is = p.getInputStream();
            // 输出流，用于输入  需要传给exe执行的命令
            final OutputStream os = p.getOutputStream();

            // 用于获取exe执行结果
            new Thread() {
                public void run() {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(is, charset))){
                        int temp = -1;
                        while ((temp = br.read()) != -1) {
                            System.out.print((char) temp);
                        }
//                        String temp = null;
//                        while((temp = br.readLine()) != null) {
//                            System.out.println(temp);
//                        }
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
        doExe(new String[]{"cmd.exe"}, "GBK", "C:/Program Files");
        // 运行TerrariaServer.exe程序
//        doExe(new String[]{"tshock_4.3.25/TerrariaServer.exe"}, "UTF-8", "tshock_4.3.25");
    }
}
