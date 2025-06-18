package com.distribuitedai;

import com.distribuitedai.server.WorkerTCPServer;

public class Main {
    public static void main(String[] args) {
        int port = 5000; // default
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new WorkerTCPServer(port).start();
    }
}