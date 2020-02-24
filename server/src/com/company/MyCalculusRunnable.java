package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

class MyCalculusRunnable implements Runnable {
  private final Socket sock;

  public MyCalculusRunnable(Socket s) {
    sock = s;
  }

  @Override
  public void run() {

    try {
      DataInputStream dis = new DataInputStream(sock.getInputStream());
      DataOutputStream dos = new DataOutputStream(sock.getOutputStream());

      // read op1, op2 and the opreation to make

      char op1 = dis.readChar();
      char op = dis.readChar();
      char op2 = dis.readChar();

      int res = CalculusServer.doOp(Integer.parseInt(String.valueOf(op1)),
          Integer.parseInt(String.valueOf(op2)),
          op);

      // send back result
      System.out.println(res);
      dos.writeInt(res);

      dis.close();
      dos.close();
      sock.close();

    } catch (Exception e) {
      System.out.println(e);
    }

  }

}

