/*
 * ====================================================================
 * Copyright (c) 2005-2012 sventon project. All rights reserved.
 *
 * This software is licensed as described in the file LICENSE, which
 * you should have received as part of this distribution. The terms
 * are also available at http://www.sventon.org.
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
package org.sventon.web.ctrl.template;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.sventon.SVNConnection;
import org.sventon.SVNConnectionFactory;
import org.sventon.SventonException;
import org.sventon.appl.RepositoryConfiguration;
import org.sventon.model.*;
import org.sventon.web.UserRepositoryContext;
import org.sventon.web.command.BaseCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.sventon.appl.Application;
import org.sventon.appl.ConfigDirectory;
import org.sventon.cache.CacheException;

public class AbstractTemplateControllerTest {

    private static String BASE_URL = null;

    @Test
    public void testParseAndUpdateSortParameters() throws Exception {
        final UserRepositoryContext userRepositoryContext = new UserRepositoryContext();
        final AbstractTemplateController ctrl = new TestController();
        final BaseCommand command = new BaseCommand();

        assertNull(userRepositoryContext.getSortMode());
        assertNull(userRepositoryContext.getSortType());
        ctrl.parseAndUpdateSortParameters(command, userRepositoryContext);
        assertEquals("ASC", userRepositoryContext.getSortMode().toString());
        assertEquals("FULL_NAME", userRepositoryContext.getSortType().toString());

        command.setSortType(DirEntryComparator.SortType.SIZE);
        command.setSortMode(DirEntrySorter.SortMode.DESC);

        ctrl.parseAndUpdateSortParameters(command, userRepositoryContext);
        assertEquals("DESC", userRepositoryContext.getSortMode().toString());
        assertEquals("SIZE", userRepositoryContext.getSortType().toString());
    }

    @Test
    public void testCreateConnection() throws Exception {
        final AbstractTemplateController ctrl = new TestController();
        final MutableBoolean usingSharedAuthSettings = new MutableBoolean(false);

        ctrl.setConnectionFactory(new SVNConnectionFactory() {
            public SVNConnection createConnection(RepositoryName repositoryName, SVNURL svnUrl, Credentials credentials) throws SventonException {
                if ("shared".equals(credentials.getUserName())) {
                    usingSharedAuthSettings.setValue(true);
                } else if ("user".equals(credentials.getUserName())) {
                    usingSharedAuthSettings.setValue(false);
                }
                return null;
            }
        });

        final RepositoryConfiguration configuration = new RepositoryConfiguration("test");
        final UserRepositoryContext context = new UserRepositoryContext();

        assertFalse(usingSharedAuthSettings.booleanValue());

        configuration.setUserCredentials(new Credentials("shared", "pass"));
        configuration.setEnableAccessControl(false);
        ctrl.createConnection(configuration, context);
        assertTrue(usingSharedAuthSettings.booleanValue());

        context.setCredentials(new Credentials("user", "pass"));
        configuration.setEnableAccessControl(true);
        ctrl.createConnection(configuration, context);
        assertFalse(usingSharedAuthSettings.booleanValue());

        ctrl.createConnection(configuration, context);
        assertFalse(usingSharedAuthSettings.booleanValue());
    }

    @Test
    public void testSetAuthenticationActionUrlWithBaseUrl() throws IOException, CacheException {
        BASE_URL = "http://browser.sventon.org/svn";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/svn/list");
        final Map<String, Object> model = new HashMap<String, Object>();

        final AbstractTemplateController ctrl = new TestController();
        ConfigDirectory configDir = new TestConfigDirectory();
        configDir.setServletContext(new MockServletContext());
        ctrl.setApplication(new TestApplication(configDir));
        ModelAndView modelAndView = ctrl.prepareAuthenticationRequiredView(request, model);

        assertEquals("http://browser.sventon.org/svn/list", modelAndView.getModel().get("action"));
    }

    @Test
    public void testSetAuthenticationActionUrlWithoutBaseUrl() throws IOException, CacheException {
        BASE_URL = null;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/svn/list");
        final Map<String, Object> model = new HashMap<String, Object>();

        final AbstractTemplateController ctrl = new TestController();
        ConfigDirectory configDir = new TestConfigDirectory();
        configDir.setServletContext(new MockServletContext());
        ctrl.setApplication(new TestApplication(configDir));
        ModelAndView modelAndView = ctrl.prepareAuthenticationRequiredView(request, model);

        assertEquals("http://localhost:80/svn/list", modelAndView.getModel().get("action"));
    }

    private static class TestController extends AbstractTemplateController {

        protected ModelAndView svnHandle(final SVNConnection connection, final BaseCommand command,
                final long headRevision, final UserRepositoryContext userRepositoryContext,
                final HttpServletRequest request, final HttpServletResponse response,
                final BindException exception) throws Exception {
            return new ModelAndView();
        }

        @Override
        protected Map<String, Object> getApplicationModel(BaseCommand command) {
            final Map<String, Object> applicationModel = new HashMap<String, Object>();
            applicationModel.put("baseURL", BASE_URL);
            applicationModel.put("isUpdating", "true");
            applicationModel.put("repositoryNames", "repo");
            applicationModel.put("isEditableConfig", false);
            applicationModel.put("charsets", "UTF-8");
            applicationModel.put("maxRevisionsCount", 10);
            applicationModel.put("command", command);
            return applicationModel;
        }
    }

    private static class TestApplication extends Application {

        public TestApplication(ConfigDirectory configDirectory) {
            super(configDirectory);
        }

        @Override
        public URL getBaseURL() {
            try {
                return new URL(BASE_URL);
            } catch (MalformedURLException ex) {
                return null;
            }
        }
    }

    private static class TestConfigDirectory extends ConfigDirectory {

        public TestConfigDirectory(File sventonConfigDirectory, String exportDirectoryName, String repositoriesDirectoryName) {
            super(sventonConfigDirectory, exportDirectoryName, repositoriesDirectoryName);
        }

        public TestConfigDirectory() {
            super(new File("test"), "testdir", "testrepodir");
        }

    }
}
