package generation.invoices;

import org.dalesbred.Database;

import generation.invoices.resources.InvoiceResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;


public class invoicesApplication extends Application<invoicesConfiguration> {

    public static void main(final String[] args) throws Exception {
        new invoicesApplication().run(args);
    }

    @Override
    public String getName() {
        return "invoices";
    }

    @Override
    public void initialize(final Bootstrap<invoicesConfiguration> bootstrap) {
        
    }

    @Override
    public void run(final invoicesConfiguration configuration,
                    final Environment environment) {
    	DataSourceFactory config = configuration.getDataSourceFactory();
		final Database database = Database.forUrlAndCredentials(config.getUrl(), config.getUser(),
				config.getPassword());
		environment.jersey().register(new InvoiceResource(database));
    }

}
