
package net.sonodas.j2g3.config;

import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ExtendedUriInfo;

public class JerseyGuiceServletModule extends ServletModule {
	
//	@Provides
//	@RequestScoped
//	public ExtendedUriInfo getExtendedUriInfo(Injector injector) {
//		ServiceLocator locator = injector.getInstance(ServiceLocator.class);
//		return locator.getService(ExtendedUriInfo.class);
//	}
}
