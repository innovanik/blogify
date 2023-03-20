package blogify.ext;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {"blogify.ext"})
public class ExtAutoConfiguration {

}
