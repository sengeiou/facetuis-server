package com.facetuis.server.app.web;

import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

//联系我们CantactUs__xiute
@RestController
@RequestMapping("/1.0/version")
public class VersionController extends FacetuisController {
    public class Version
    {
        private String  name;
        private String  updateLog;
        private String  vid;//版本号

        public void setName(String name){this.name=name;}
        public void setUpLog(String log){this.updateLog=log;}
        public void setVid(String vid){this.vid=vid;}

        public String getName(){return name;}
        public String getUpdateLog(){return updateLog;}
        public String getVid(){return vid;}
    }
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse get()
    {
        Version v = new Version();
        v.setName("脸推App");
        //读取指定路径下的更新log
       String logTxt= readFileForOneLine("123");
        v.setUpLog(logTxt);
        v.setVid("0.0.1");

        return successResult(v);

    }
    /**
     * 读取文件内容
     *
     * @param path 文件详细路径
     *             如: D://my.txt
     * @return String
     * @throws IOException java.IO异常
     */
    private  String readFileForOneLine(String path) {

        File targetFile = new File(path);
        StringBuilder fileContent = new StringBuilder();

        if (targetFile.exists()) {
            try{
                RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "r");//赋予文件读取权限
                String eachLine;
                while (null != (eachLine = randomAccessFile.readLine())) {
                    fileContent.append(eachLine + "\n");
                }
            }catch(Exception e)
            {

            }

        }

        return fileContent.toString();

    }



}
