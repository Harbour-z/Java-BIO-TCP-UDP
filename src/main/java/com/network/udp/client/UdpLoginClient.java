package com.network.udp.client;

import com.network.common.ProtocolConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UdpLoginClient {
  private final String serverIp;
  private final int serverPort;


  public UdpLoginClient(String serverIp, int serverPort) {
    this.serverIp = serverIp;
    this.serverPort = serverPort;
  }

  public void start() throws IOException {
    try (DatagramSocket clientSocket = new DatagramSocket()) { // UDP客户端无需绑定固定端口
      InetAddress serverAddress = InetAddress.getByName(serverIp);
      Scanner scanner = new Scanner(System.in);
      while (true) {
        // 1. 控制台输入用户名密码
        System.out.print("请输入用户名和密码（格式：用户名:密码，输入exit退出）：");
        String input = scanner.nextLine();
        if ("exit".equals(input)) {
          break;
        }
        // 2. 发送UDP数据包到服务端
        byte[] sendData = input.getBytes(StandardCharsets.UTF_8);
        DatagramPacket sendPacket = new DatagramPacket(
            sendData, sendData.length, serverAddress, serverPort
        );
        clientSocket.send(sendPacket);
        System.out.println("已发送UDP数据包：" + input);
        // 3. 接收服务端响应
        byte[] buffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        clientSocket.receive(receivePacket); // 阻塞等待响应
        String response = new String(
            receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8
        );
        System.out.println("UDP服务端响应：" + response);
      }
    }
  }

  public static void main(String[] args) throws IOException {
    new UdpLoginClient("127.0.0.1", ProtocolConstants.UDP_PORT).start();
  }
}
