package com.company;

import java.net.ServerSocket;
import java.net.Socket;

public class CalculusServer {
    public static int doOp(int op1, int op2, char op) throws Exception
    {

        switch (op) {

            case '+':
                return op1 + op2;

            case '-':
                return op1 - op2;

            case'*':
                return op1 * op2;

            case '/':
                if (op2 != 0)
                    return op1 / op2;
                else
                    throw new Exception();

            default:
                throw new Exception();
        }

    }

    public static void main(String[] args) throws Exception {

        // Example of a distant calculator
        ServerSocket ssock = new ServerSocket(9877);

        while (true) { // infinite loop
            Socket comm = ssock.accept();
            System.out.println("connection established");

             new Thread(new MyCalculusRunnable(comm)).start();

        }

    }

}
