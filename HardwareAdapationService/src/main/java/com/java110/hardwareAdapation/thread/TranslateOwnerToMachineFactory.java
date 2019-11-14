package com.java110.hardwareAdapation.thread;

public class TranslateOwnerToMachineFactory {

    public static void startOwnerToMachine(){
        //启动业主信息同步 线程 变更业主的场景
        TranslateOwnerToMachine ownerMachine = new TranslateOwnerToMachine(true);
        Thread ownerToMachineThread = new Thread(ownerMachine,"TranslateOwnerToMachineThread");
        ownerToMachineThread.start();

        // 变更 门禁的场景
        TranslateOwnerToMachineChangeMachine machine = new TranslateOwnerToMachineChangeMachine(true);
        Thread ownerToMachineChangeThread = new Thread(machine,"TranslateOwnerToMachineChangeMachineThread");
        ownerToMachineChangeThread.start();
    }
}
