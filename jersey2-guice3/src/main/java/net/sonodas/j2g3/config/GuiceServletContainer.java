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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.glassfish.jersey.servlet.ServletContainer;

@Singleton
public class GuiceServletContainer extends ServletContainer {

	@Inject
	public GuiceServletContainer(JerseyGuiceResourceConfig resourceConfig) {
		super(resourceConfig);
	}
}
