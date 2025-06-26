package com.network.tcp.server;

import com.network.common.ProtocolConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TcpLoginServer {
  // 存储合法用户
  private static final Map<String, String> VALID_USERS = new ConcurrentHashMap<>();

  static {
    VALID_USERS.put("admin", "123456");
    VALID_USERS.put("test_user", "test1234");
  }

  public void start(int port) throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    System.out.println("TCP登录服务器启动，监听端口" + port);

    while (true){
      // 创建tcp socket
      Socket clientSocket = serverSocket.accept(); //阻塞等待客户端连接
      System.out.println("新客户端连接："+clientSocket.getInetAddress());

      // 创建BIO的线程
      new Thread(() -> handleClient(clientSocket)).start();
    }
  }

  // 处理登录请求
  private void handleClient(Socket clientSocket) {
    try(BufferedReader in = new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream(), ProtocolConstants.CHARSET));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);)
    {
      // 读取客户端发送的用户名密码
      String request = in.readLine();
      System.out.println("收到登录请求：" + request);

      //解析
      String[] userInfo = request.split(ProtocolConstants.SPLITTER);
      if (userInfo.length != 2) {
        System.out.println("格式错误，请使用：username:password");
        return;
      }
      String username = userInfo[0];
      String password = userInfo[1];
      //验证
      String response;
      if(VALID_USERS.containsKey(username) && VALID_USERS.get(username).equals(password)){
        response = "登录成功！欢迎您，" + username + "!";
      }else {
        response = "用户名或密码错误，请重新输入！";
      }
      // 响应返回客户端
      out.println(response);
      System.out.println("响应客户端：" + response);
    } catch (IOException e){
      e.printStackTrace();
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws IOException {
    // 启动TCP登录服务器
    new TcpLoginServer().start(ProtocolConstants.TCP_PORT);
  }

}
