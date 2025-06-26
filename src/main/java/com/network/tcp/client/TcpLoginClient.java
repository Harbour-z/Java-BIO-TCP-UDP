package com.network.tcp.client;

import com.network.common.ProtocolConstants;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TcpLoginClient {
  private final String serverIp;
  private final int serverPort;

  public TcpLoginClient(String serverIp, int serverPort) {
    this.serverIp = serverIp;
    this.serverPort = serverPort;
  }

  public void start() throws IOException {
    // 连接服务器
    Socket socket = new Socket(serverIp, serverPort);
    System.out.println("已连接TCP服务器" + serverIp + ":" + serverPort);
    try (BufferedReader in = new BufferedReader(
        new InputStreamReader(socket.getInputStream(), ProtocolConstants.CHARSET));
         PrintWriter out = new PrintWriter(
             new OutputStreamWriter(socket.getOutputStream(), ProtocolConstants.CHARSET), true);
         Scanner scanner = new Scanner(System.in)
    )
    {
      // 2. 循环输入用户名密码并发送
      while (true) {
        System.out.print("请输入用户名和密码（格式：username:password，输入exit退出）：");
        String input = scanner.nextLine();
        if ("exit".equals(input)) {
          break;
        }
        // 3. 发送数据（格式："username:password"）
        out.println(input);
        // 4. 接收服务器响应
        String response = in.readLine();
        System.out.println("服务器响应：" + response);
      }
    } finally {
      socket.close();
    }
  }

  public static void main(String[] args) throws IOException {
    new TcpLoginClient(serverIp, 8990).start();
  }
}
