package org.c.lins.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;


/**
 * Created by lins on 16-3-28.
 */
@Controller
public class UploadController {

    @RequestMapping(
            value = {"/uploadFlights"},
            method = {RequestMethod.POST}
    )
    public void handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if(!file.isEmpty()) {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX");
            long t1 = System.currentTimeMillis();

                String e = (new java.util.Date()).getTime() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(92) + 1);
                Path path = Paths.get("/media/lins/work/upload/" + e, new String[0]);
                File target = path.toFile();
                if(!target.exists()) {
                    Files.createDirectories(path.getParent(), new FileAttribute[0]);
                    Files.createFile(path, new FileAttribute[0]);
                }

                FileOutputStream os = new FileOutputStream(target);
                FileInputStream in = (FileInputStream)file.getInputStream();
                boolean b = false;

                int b1;
                while((b1 = in.read()) != -1) {
                    os.write(b1);
                }

                os.flush();
                os.close();
                in.close();
        }else {
            System.out.println("YYYYYYYYYYYYYYYYYYYYYYYY");
        }

    }

    @RequestMapping("/index")
    @ResponseBody
    public String handleFileUpload() throws IOException {
        return "ddddd";

    }
}
