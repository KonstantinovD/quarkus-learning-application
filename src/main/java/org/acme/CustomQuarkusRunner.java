package org.acme;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

// I mark my CustomQuarkusRunner class with @QuarkusMain to flag it to
// the Quarkus runtime as a bootstrap class. The Quarkus team recommends
// against performing any heavy-duty processing directly inside the main
// method.
@QuarkusMain
public class CustomQuarkusRunner {

    public static void main(String[] args) {
        Quarkus.run(args);
    }

    // Instead, I provide a different class that implements the
    //QuarkusApplication interface. It’s inside the run method of this
    //interface I’m expected to do my heavy lifting.
    public static class StartupHandler implements QuarkusApplication {
        @Override
        public int run(String... args) throws Exception {
            customStartupHandler(args);
            // I can then use Quarkus#waitForExit to listen for
            // termination signals from any source.
            Quarkus.waitForExit();
            return 0;
        }

        // It could be something as simple as validating the input
        //supplied as command-line parameters. If things don’t look
        //right, I can call the Quarkus#asyncExit method, which will
        //attempt a graceful shutdown.
        public static void customStartupHandler(String[] args) {
            // handle args or Quarkus.asyncExit();
        }

        // You can get a hold of the command-line parameters anywhere else
        //inside your application with
        /*
        @Inject
        @CommandLineArguments
        String[] commandLineArgs;
        */
    }
}
