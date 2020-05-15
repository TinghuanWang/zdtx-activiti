### 一、环境搭建错误记录
1.Can't convert the date-like value to string because

- 问题描述：
    不能将Date类型直接转化为String类型，针对于freemarker模板
    
- 处理方法：将日期类型后面添加如下：

    （1）、原来：生日：${student.studentBirthday}

    （2）、修改后：生日：${(student.studentBirthday)?string("yyyy-MM-dd")}
    

