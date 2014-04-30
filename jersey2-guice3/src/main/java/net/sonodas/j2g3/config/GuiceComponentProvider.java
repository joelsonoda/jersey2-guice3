/**
 *  This file is part of jersey2-guice3.
 *
 *  jersey2-guice3 is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jersey2-guice3 is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with jersey2-guice3.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sonodas.j2g3.config;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import java.util.Set;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.glassfish.jersey.server.spi.ComponentProvider;
import org.jvnet.hk2.guice.bridge.api.HK2IntoGuiceBridge;

public class GuiceComponentProvider implements ComponentProvider {
	private Injector injector;
	private ServiceLocator locator;
	
	@Override
	public void initialize(final ServiceLocator locator) {
		this.locator = locator;
		if(locator != null) {
			Injector origInjector = locator.getService(Injector.class);
			
			if(origInjector != null) {
				this.injector = origInjector.createChildInjector(
					new HK2IntoGuiceBridge(locator),
					new AbstractModule() {
						@Override
						protected void configure() {
							bind(ExtendedUriInfo.class).toProvider(new Provider<ExtendedUriInfo>() {

								@Override
								public ExtendedUriInfo get() {
									return locator.getService(ExtendedUriInfo.class);
								}
								
							});
						}
					}
				);
			}
		}
	}

	@Override
	public boolean bind(Class<?> component, Set<Class<?>> providerContracts) {
		if(injector != null) {
			Key<?> key = Key.get(component);
			Binding<?> binding = injector.getExistingBinding(key);

			// Does the constructor require Guice Binding?
			if(binding == null) {
				binding = injector.getBinding(key);
			}
			
			DynamicConfiguration c = Injections.getConfiguration(locator);
			ServiceBindingBuilder bb = Injections.newFactoryBinder(new GuiceBindingComponentFactory(binding));
			bb.to(component);
			Injections.addBinding(bb, c);
			c.commit();
			return true;
		}
		
		return false;
	}

	@Override
	public void done() {
	}
	
	private class GuiceBindingComponentFactory implements Factory {
		private final Binding binding;

		public GuiceBindingComponentFactory(Binding binding) {
			this.binding = binding;
		}
		
		@Override
		public Object provide() {
			return binding.getProvider().get();
		}

		@Override
		public void dispose(Object instance) {
		}
		
	}

}
