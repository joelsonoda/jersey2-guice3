package net.sonodas.j2g3.config;

import com.google.inject.Provider;
import org.glassfish.hk2.api.ServiceLocator;

class MutableServiceLocatorProvider implements Provider<ServiceLocator> {
	private ServiceLocator serviceLocator;

	void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	@Override
	public ServiceLocator get() {
		return serviceLocator;
	}
}