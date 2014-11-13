
package net.sonodas.j2g3.config;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.ServletModule;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ExtendedUriInfo;

public abstract class JerseyGuiceServletModule extends ServletModule {

	@Override
	protected final void configureServlets() {
		MutableServiceLocatorProvider serviceLocatorProvider = new MutableServiceLocatorProvider();
		bind(MutableServiceLocatorProvider.class).toInstance(serviceLocatorProvider);
		bind(ServiceLocator.class).toProvider(serviceLocatorProvider);
		bind(ExtendedUriInfo.class).toProvider(ExtendedUriInfoProvider.class);
		configureJerseyServlet();
	}

	protected abstract void configureJerseyServlet();

	private static class ExtendedUriInfoProvider implements Provider<ExtendedUriInfo> {
		private final ServiceLocator locator;

		@Inject
		public ExtendedUriInfoProvider(ServiceLocator locator) {
			this.locator = locator;
		}


		@Override
		public ExtendedUriInfo get() {
			if(locator != null) {
				return locator.getService(ExtendedUriInfo.class);
			} else {
				return null;
			}
		}
	}
}
