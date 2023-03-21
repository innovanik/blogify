package blogify.res.r2dbc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {"blogify.res.r2dbc"})
@EnableR2dbcRepositories
public class ResR2dbcAutoConfiguration {

}
