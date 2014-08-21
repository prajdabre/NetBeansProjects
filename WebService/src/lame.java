
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class lame {

    public static void main(String args[]) throws IOException, InterruptedException {
        Runtime r = Runtime.getRuntime();
        String[] cmd = {
            "/bin/sh",
            "-c",
            "echo \"प्रत्येक  माणासा ला जीवन त सुख  समाधान  आणि  शांती  प्राप्त  हो वी म्हणून  देवपूजा  हे  एक  उत्तम  साधन  अस  .\" | /bin/nc localhost 12345"
        };
        Process p = r.exec(cmd);
        p.waitFor();
        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";

        while ((line = b.readLine()) != null) {
            System.out.println(line);
        }
        
        String[] cmd1 = {
            "/bin/sh",
            "-c",
            "echo \"प्रत्येक माणासाला जीवनात सुख समाधान आणि शांती प्राप्त व्हावी म्हणून देवपूजा हे एक उत्तम साधन आहे .\" | /bin/nc localhost 12346"
        };
        p = r.exec(cmd1);
        p.waitFor();
        b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        line = "";

        while ((line = b.readLine()) != null) {
            System.out.println(line);
        }
    }
}