package com.example.myapplication;

import java.io.File;

/**
 * 获取文件夹下所有的文件 不是图片的文件也会被读取的， 如果你是专门用来存放图片的文件，这种方法可行 否则的话就要判断文件的类型了
 */
public class File_contorller {
        public static int picCount() {
            File file = new File("res/drawable");// 图片存放路径
            File list[] = file.listFiles();
            return list.length;
        }
}
