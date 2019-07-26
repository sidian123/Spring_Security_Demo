package top.sidian123.spring_security_demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootApplication
public class SpringSecurityDemoApplication {




    public static void main(String[] args) {

        SpringApplication.run(SpringSecurityDemoApplication.class, args);
    }

}
