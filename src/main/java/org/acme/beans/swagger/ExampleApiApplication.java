package org.acme.beans.swagger;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
  tags = {

  },
  info = @Info(
    title = "Example API", //mandatory property
    version = "1.0.1", // mandatory property
    contact = @Contact(
      name="Example API Support",
      url = "http://exampleurl.com/contact",
      email = "techsupport@example.com"
    ),
    license = @License(
      name = "Apache 2.0",
      url = "https://www.apache.org/licenses/LICENSE-2.0.html"
    )
  )
)
public class ExampleApiApplication extends Application {
}
