package blogify.res.r2dbc;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class H2ConsoleManualConfiguration implements ApplicationListener<ApplicationEvent> {

	private final Integer port;

	private Server server;

	public H2ConsoleManualConfiguration() {
		this(null);
	}

	public H2ConsoleManualConfiguration(Integer port) {
		this.port = port;
	}

	private Integer getPort() {
		return port == null || port < 0 ? 0 : port;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if ( event instanceof ContextRefreshedEvent ) {
			try {
				this.server = Server.createWebServer("-webPort", Integer.toString(getPort()), "-tcpAllowOthers")
									.start();
				log.info("H2 Console started on port {}", this.server.getPort());
			} catch (SQLException e) {
				log.warn(e.getMessage(), e);
			}
		} else if ( event instanceof ContextClosedEvent ) {
			if ( this.server != null ) {
				this.server.stop();
			}
		}
	}
}
