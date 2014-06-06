/*
 * Copyright 2014 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stormpath.sdk.oauth.authc;

import com.stormpath.sdk.oauth.authz.ScopeFactory;

/**
 * An OAuth-specific {@code ApiRequestAuthenticator} that authenticates an API Request for a client requesting a new
 * OAuth Access Token.  This request is usually submitted to the application's OAuth token endpoint
 * (e.g. {@code /oauth/token}).  The request is authenticated using the
 * <a href="http://tools.ietf.org/html/rfc6749#section-4.4">OAuth 2 Client Credentials Grant Type</a>, and the
 * new token will be available in the returned {@link TokenOauthAuthenticationResult}.
 * <p>
 * This interface reflects the <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder design pattern</a> so
 * that the request authentication process may be customized.
 * </p>
 *
 * <h3>Usage Example</h3>
 * <pre>
 * //assume a POST request to, say, https://api.mycompany.com/oauth/token:
 *
 * public void processOauthTokenRequest(HttpServletRequest request, HttpServletResponse response) {
 *
 *    Application application = client.getResource(myApplicationRestUrl, Application.class);
 *
 *    //if you want to control the OAuth scope (aka application-specific permissions) that should be granted to the
 *    //generated token (and will be retained as long as the token is alive):
 *    <b>ScopeFactory scopeFactory = new MyScopeFactory();</b> //create the 'MyScopeFactory' class yourself
 *
 *    TokenOAuthAuthenticationResult result = (TokenOAuthAuthenticationResult)application.authenticateOauthRequest(request)
 *        <b>{@link #using(com.stormpath.sdk.oauth.authz.ScopeFactory) .using(scopeFactory)}
 *        {@link #withTtl(long) .withTtl(3600)}
 *        .execute()</b>;
 *
 *    TokenResponse token = result.getTokenResponse();
 *
 *    response.setStatus(HttpServletResponse.SC_OK);
 *    response.setContentType("application/json");
 *    response.getWriter().print(token.toJson());
 *
 *    response.getWriter().flush();
 * }
 * </pre>
 *
 * @see com.stormpath.sdk.application.Application#authenticateOauthRequest(Object)
 * @see #using(com.stormpath.sdk.oauth.authz.ScopeFactory)
 * @see #withTtl(long)
 * @see #execute()
 * @since 1.0.RC
 */
public interface TokenOauthRequestAuthenticator {

    /**
     * Specifies the {@link ScopeFactory} to be used for this authentication request.
     *
     * If not provided an empty scope will be returned.
     *
     * @param scopeFactory the {@link ScopeFactory} to be used for this authentication request.
     * @return this instance for method chaining.
     */
    public TokenOauthRequestAuthenticator using(ScopeFactory scopeFactory);

    /**
     * Specifies the <a href="http://en.wikipedia.org/wiki/Time_to_live">time to live</a> of this authentication request
     * in seconds.
     *
     * If not provided the default ttl (3600 seconds) will be used.
     *
     * @param ttl the time to live (in seconds) of this authentication request.
     * @return this instance for method chaining.
     */
    public TokenOauthRequestAuthenticator withTtl(long ttl);

    /**
     * Executes this authentication request.
     *
     * @return the result of the authentication request in the form of a {@link TokenOauthAuthenticationResult}.
     */
    public TokenOauthAuthenticationResult execute() throws Exception;
}
