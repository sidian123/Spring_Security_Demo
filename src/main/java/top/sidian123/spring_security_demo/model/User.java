package top.sidian123.spring_security_demo.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author sidian
 * @date 19-7-25 下午1:20
 */
@Data
public class User {
    @Value("${path}")
    String path;
}
