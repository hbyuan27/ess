package hcm.ess.test.spring;

//import static org.mockito.Mockito.mock;
//import javax.persistence.EntityManagerFactory;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// @ComponentScan(basePackageClasses = { xxx.class, xxx.class})
@ComponentScan(basePackages = { "hcm.ess" }/*
											 * , excludeFilters = {
											 * 
											 * @ComponentScan.Filter(value =
											 * Controller.class, type =
											 * FilterType.ANNOTATION),
											 * 
											 * @ComponentScan.Filter(value =
											 * Authentication.class, type =
											 * FilterType.ASSIGNABLE_TYPE) }
											 */)
public class TestSpringContext {
	// @Bean(name = "entityManagerFactory")
	// public EntityManagerFactory entityManagerFactory() {
	// return mock(EntityManagerFactory.class);
	// }
}
