package hcm.ess.test.spring;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.SimpleThreadScope;

public class TestBase {

	protected AnnotationConfigApplicationContext ctx;

	public TestBase() {
		setup();
	}

	private void setup() {
		// init test Spring context
		ctx = new AnnotationConfigApplicationContext();
		// register context beans
		ctx.register(TestSpringContext.class);
		// register session scopes for some beans who have Scope annotation
		ConfigurableListableBeanFactory beanFactory = ctx.getBeanFactory();
		Scope sessionScope = new SimpleThreadScope();
		beanFactory.registerScope("session", sessionScope);
		// refresh above test Spring configuration
		ctx.refresh();

		// get beans for testing from test Spring context by using:
		// yourBean = ctx.getBean(yourBean.class);
	}
}
