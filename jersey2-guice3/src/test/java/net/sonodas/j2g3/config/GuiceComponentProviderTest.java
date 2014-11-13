/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sonodas.j2g3.config;

import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import static org.junit.Assert.*;
import org.junit.Test;

public class GuiceComponentProviderTest extends JerseyTest {

	private static final String messageFromGuice = "Hello World, from Guice";
	private static final String messageFromGuiceAnnotated = "Hello World, I'm annotated";
	private static final String messageFromHK2 = "Hello World, from HK2";

	private static class GuiceInjectable {

		public String getMessage() {
			return messageFromGuice;
		}
	}
	
	private static class HK2Injectable {
		public String getMessage() {
			return messageFromHK2;
		}
	}
	
	private static class AnnotatedGuiceInjectable extends GuiceInjectable {
		
		@Override
		public String getMessage() {
			return messageFromGuiceAnnotated;
		}
	}

	private Injector getInjector() {
		return Guice.createInjector(new JerseyGuiceServletModule(){
			@Override
			protected void configureJerseyServlet() {
				bind(GuiceInjectable.class);
				bind(GuiceInjectable.class)
					.annotatedWith(MyBindingAnnotation.class)
					.to(AnnotatedGuiceInjectable.class);
			}
		});
	}

	@Path("resourceWithGuiceInjectedField")
	public static class ResourceWithGuiceInjectedField {
		
		@Inject
		private GuiceInjectable injectable;

		@GET
		public String getHello() {
			if(injectable != null) {
				return injectable.getMessage();
			} else {
				return "Nope....nope, nope, nope nope";
			}
		}
	}
	
	@Path("resourceWithGuiceInjectedConstructorFromHK2")
	public static class ResourceWithGuiceInjectedConstructorFromHK2 {
		
		private final HK2Injectable injectable;
		
		@Inject
		ResourceWithGuiceInjectedConstructorFromHK2(HK2Injectable injectable) {
			this.injectable = injectable;
		}

		@GET
		public String getHello() {
			return injectable.getMessage();
		}
	}
	
	@Path("resourceWithGuiceInjectedConstructor")
	public static class ResourceWithGuiceInjectedConstructor {
		
		private final GuiceInjectable injectable;

		@Inject
		private ResourceWithGuiceInjectedConstructor(GuiceInjectable injectable) {
			this.injectable = injectable;
		}

		@GET
		public String getHello() {
			return injectable.getMessage();
		}
	}
	
	@Path("resourceWithAnnotatedGuiceInjectedField")
	public static class ResourceWithAnnotatedGuiceInjectedField {
		
		@Inject
		@MyBindingAnnotation
		private GuiceInjectable injectable;

		@GET
		public String getHello() {
			if(injectable != null) {
				return injectable.getMessage();
			} else {
				return "Nope....nope, nope, nope nope";
			}
		}
	}
	
	@BindingAnnotation
	@Target({ FIELD })
	@Retention(RUNTIME)
	public @interface MyBindingAnnotation {}

	public GuiceComponentProviderTest() {
	}

	@Override
	protected Application configure() {
		ResourceConfig rc = new JerseyGuiceResourceConfig(getInjector());
		rc.registerClasses(
				ResourceWithGuiceInjectedConstructorFromHK2.class
				, HK2Injectable.class
				, ResourceWithGuiceInjectedField.class
				, ResourceWithGuiceInjectedConstructor.class
				, ResourceWithAnnotatedGuiceInjectedField.class);
		return rc;
	}
	
	@Test
	public void testResourceWithGuiceInjectedField() {
		String result = target("resourceWithGuiceInjectedField").request().get(String.class);
        assertEquals(messageFromGuice, result);
	}
	
	@Test
	public void testResourceWithGuiceInjectedConstructor() {
		String result = target("resourceWithGuiceInjectedConstructor").request().get(String.class);
        assertEquals(messageFromGuice, result);
	}
	
	@Test
	public void testResourceWithAnnotatedGuiceInjectedField() {
		String result = target("resourceWithAnnotatedGuiceInjectedField").request().get(String.class);
        assertEquals(messageFromGuiceAnnotated, result);
	}
	
	@Test
	public void testResourceWithGuiceInjectedConstructorFromHK2() {
		String result = target("resourceWithGuiceInjectedConstructorFromHK2").request().get(String.class);
        assertEquals(messageFromHK2, result);
	}
	
}
