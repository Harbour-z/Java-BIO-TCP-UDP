package com.network.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
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
    System.out.println("TCP登录服务器启动，监听端口");
  }

}
