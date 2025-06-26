package com.network.udp.server;

import com.network.common.ProtocolConstants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UdpLoginServer {
  // 合法用户列表
  private static final Map<String, String> VALID_USERS = new ConcurrentHashMap<>();
  static {
    VALID_USERS.put("admin", "123456");
    VALID_USERS.put("test", "test1234");
  }

  public void start(int port) throws Exception {
    // UDP的Socket
    DatagramSocket serverSocket = new DatagramSocket(port);
    System.out.println("UDP登录服务器启动，监听端口：" + port);

    byte[] buffer = new byte[1024]; // 接收缓冲区

    while (true) {
      // 2. 接收客户端UDP数据包（阻塞等待）
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      serverSocket.receive(packet); // 阻塞接收数据
      // 3. 解析客户端数据（格式："username:password"）
      String request = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
      System.out.println("UDP客户端[" + packet.getAddress() + "]发送：" + request);
      // 4. 验证用户名密码
      String[] userInfo = request.split(ProtocolConstants.SPLITTER);
      String response;
      if (userInfo.length != 2) {
        response = "格式错误，请使用：username:password";
      } else {
        String username = userInfo[0];
        String password = userInfo[1];
        if (VALID_USERS.containsKey(username) && VALID_USERS.get(username).equals(password)) {
          response = "登录成功！欢迎您，" + username + "！";
        } else {
          response = "用户名或密码错误，请重新输入！";
        }
      }
      // 5. 发送响应给客户端
      byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
      serverSocket.send(new DatagramPacket(
          responseBytes,
          responseBytes.length,
          packet.getAddress(), // 客户端IP
          packet.getPort()     // 客户端端口
      ));
    }
  }

  public static void main(String[] args) throws Exception {
    new UdpLoginServer().start(ProtocolConstants.UDP_PORT);
  }
}
