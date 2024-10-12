package com.qdoner.datarecept.server;

import com.qdoner.datarecept.resolver.equipment.elevator.service.ElevatorService;
import com.qdoner.datarecept.resolver.equipment.tower.service.AnalysisService;
import com.qdoner.datarecept.utils.HexEcodeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Data
@Component
@NoArgsConstructor
public class SocketServer {

    @Autowired
    private AnalysisService analysisService;


    private Integer port = 4456;
    private boolean started;
    private ServerSocket serverSocket;
    // 防止重复创建socket线程链接对象浪费资源
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private InputStream inputStream;

    public void start(){
        start(null);
    }

    public void start(Integer port){
        log.info("port: {}, {}", this.port, port);
        try {
            serverSocket =  new ServerSocket(port == null ? this.port : port);
            started = true;
            log.info("Socket服务已启动，占用端口： {}", serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("端口冲突,异常信息：{}", e);
            System.exit(0);
        }
        try {
            Socket socket=serverSocket.accept();
//            ClientSocket register = SocketHandler.register(socket);
            //SocketHandler.onMessage(register);
            while (true){
//                executorService.submit(register);
                inputStream=socket.getInputStream();
                byte[] bytes = new byte[1024];
                inputStream.read(bytes);
                String str =  HexEcodeUtil.ByteArrayToHexStr(bytes);
                log.info("tcp收到数据",str);
                byte[] bytes1 = Arrays.copyOfRange(bytes,0,2);
                String str1 =  HexEcodeUtil.ByteArrayToHexStr(bytes1);
                log.info("数据1："+str1);
                byte[] bytes2 = Arrays.copyOfRange(bytes,2,3);
                String str2 =  HexEcodeUtil.ByteArrayToHexStr(bytes2);
                log.info("数据2："+str2);
                byte[] bytes3 =  Arrays.copyOfRange(bytes,3,4);
                String str3 =  HexEcodeUtil.ByteArrayToHexStr(bytes3);
                log.info("数据3："+str3);
                switch (str2){
                    case "60":
                        socket.getOutputStream().write(analysisService.register(bytes));
                        break;
                    case "0C"://实时数据
                        analysisService.realTimeData(bytes);
                        break;
                    case "31"://
                        analysisService.basicsInfo(bytes);
                        break;
                    case "3D"://工作循环
                        analysisService.workCycle(bytes);
                        break;
                    case "12"://人员身份信息
                        analysisService.staffInfo(bytes);
                        break;
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
