package com.distribuitedai;

import com.distribuitedai.server.WorkerTCPServer;

public class Main {
    public static void main(String[] args) {
        WorkerTCPServer server = new WorkerTCPServer();
        server.start();
    }
}