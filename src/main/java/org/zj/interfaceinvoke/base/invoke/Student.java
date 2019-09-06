package org.zj.interfaceinvoke.base.invoke;

/**
 * @ProjectName: interface-invoke
 * @ClassName: Student
 * @Description: 学生
 * @Author: ZhangJun
 * @Date: 2019/9/6 22:42
 */
public class Student {
    private Student stu;
    private String name;

    public Student getStu() {
        return stu;
    }

    public Student setStu(Student stu) {
        this.stu = stu;
        return this;
    }

    public String getName() {
        return name;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }
}
