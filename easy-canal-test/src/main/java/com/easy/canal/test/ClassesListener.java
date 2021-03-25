//package com.easy.canal.test;
//
//import com.wine.easy.canal.annotation.Table;
//import com.wine.easy.canal.interfaces.ProcessListener;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * @Project easy-canal-parent
// * @PackageName com.easy.canal.test
// * @ClassName ClassesListener
// * @Author qiang.li
// * @Date 2021/3/25 3:44 下午
// * @Description TODO
// */
//@Table(name = "canal.classes")
//@Component
//public class ClassesListener implements ProcessListener<String> {
//
//    @Override
//    public boolean update(String after, String before, List<String> updateFiled) {
//        return false;
//    }
//
//    @Override
//    public boolean insert(String s) {
//        return false;
//    }
//
//    @Override
//    public boolean delete(String s) {
//        return false;
//    }
//}
