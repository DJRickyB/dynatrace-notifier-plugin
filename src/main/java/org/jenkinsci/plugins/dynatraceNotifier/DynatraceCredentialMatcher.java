package org.jenkinsci.plugins.dynatraceNotifier;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsMatcher;
import com.cloudbees.plugins.credentials.common.CertificateCredentials;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;

/**
 * A very simple matcher to ensure we only show username/password or certificate credentials
 */
public class DynatraceCredentialMatcher implements CredentialsMatcher {
    public boolean matches(Credentials credentials) {
        return (credentials instanceof CertificateCredentials) || (credentials instanceof UsernamePasswordCredentials);
    }
}
