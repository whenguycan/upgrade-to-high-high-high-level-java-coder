//package com.example.agent;
//
//import com.sun.tools.attach.VirtualMachine;
//import com.sun.tools.attach.VirtualMachineDescriptor;
//
//import java.util.List;
//
///**
// * @Auther: tangwei
// * @Date: 2023/5/9 1:48 PM
// * @Description: 类描述信息
// */
//public class VMAttach {
//
//    public static void main(String[] args) throws Exception{
//        String agent = "/Users/tangwei/javaProject/demo-agent/target/demo-agent-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
//        System.out.println("Agent Path:" + agent);
//
//        List<VirtualMachineDescriptor> vmds =  VirtualMachine.list();
//
//        for (VirtualMachineDescriptor vmd: vmds){
//            if(vmd.displayName().equals("agent-springboot-0.0.1-SNAPSHOT.jar")){
//                System.out.println(vmd.id());
//                VirtualMachine vm = VirtualMachine.attach(vmd.id());
//                System.out.println("Load Agent");
//                vm.loadAgent(agent);
//                System.out.println("Detach");
//                vm.detach();
//            }
//
//        }
//    }
//
//}
