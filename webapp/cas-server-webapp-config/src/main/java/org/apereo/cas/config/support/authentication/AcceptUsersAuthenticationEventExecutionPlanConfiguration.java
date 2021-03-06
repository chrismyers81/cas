package org.apereo.cas.config.support.authentication;

import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.util.AsciiArtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This is {@link AcceptUsersAuthenticationEventExecutionPlanConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 5.1.0
 */
@Configuration("acceptUsersAuthenticationEventExecutionPlanConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class AcceptUsersAuthenticationEventExecutionPlanConfiguration implements AuthenticationEventExecutionPlanConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptUsersAuthenticationEventExecutionPlanConfiguration.class);
    
    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("personDirectoryPrincipalResolver")
    private PrincipalResolver personDirectoryPrincipalResolver;

    @Autowired
    @Qualifier("acceptUsersAuthenticationHandler")
    private AuthenticationHandler acceptUsersAuthenticationHandler;

    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        if (StringUtils.isNotBlank(this.casProperties.getAuthn().getAccept().getUsers())) {
            final String header =
                    "\nCAS is configured to accept a static list of credentials for authentication. "
                            + "While this is generally useful for demo purposes, it is STRONGLY recommended "
                            + "that you DISABLE this authentication method (by SETTING 'cas.authn.accept.users' "
                            + "to a blank value) and switch to a mode that is more suitable for production.";
            AsciiArtUtils.printAsciiArt(LOGGER, "STOP!", header);
            plan.registerAuthenticationHandlerWithPrincipalResolver(acceptUsersAuthenticationHandler, personDirectoryPrincipalResolver);
        }
    }
}
