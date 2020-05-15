package com.zdtx.process;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/***
 * 因为activiti-spring-boot-starter-basic中引用了spring-boot-starter-security
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
/***
 * 扫描 Mapper 文件夹
 */
@MapperScan("com.zdtx.process.mapper")
public class ZdtxActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZdtxActivitiApplication.class, args);
    }
}
