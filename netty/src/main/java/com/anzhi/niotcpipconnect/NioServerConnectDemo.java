package com.anzhi.niotcpipconnect;

import com.anzhi.niotcpipconnect.handle.NioServerHandle;

import static com.anzhi.niotcpipconnect.common.Const.DEFAULT_PORT;

public class NioServerConnectDemo {

    private static NioServerHandle nioServerHandle;

    public static void main(String[] args){
        nioServerHandle = new NioServerHandle(DEFAULT_PORT);
        new Thread(nioServerHandle,"Server").start();
    }
}
