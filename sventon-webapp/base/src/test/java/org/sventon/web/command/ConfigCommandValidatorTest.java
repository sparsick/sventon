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
package org.sventon.web.command;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.validation.BindException;
import org.sventon.TestUtils;
import org.sventon.appl.Application;
import org.sventon.appl.ConfigDirectory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.sventon.web.command.ConfigCommand.AccessMethod.USER;

public class ConfigCommandValidatorTest {

  @Test
  public void testSupports() {
    final ConfigCommandValidator validator = new ConfigCommandValidator(false);
    assertTrue(validator.supports(ConfigCommand.class));
  }

  @Test
  public void testValidate() throws Exception {
    final ConfigDirectory configDirectory = TestUtils.getTestConfigDirectory();
    configDirectory.setCreateDirectories(false);
    final MockServletContext servletContext = new MockServletContext();
    servletContext.setContextPath("sventon-test");
    configDirectory.setServletContext(servletContext);
    final Application application = new Application(configDirectory);

    final ConfigCommandValidator validator = new ConfigCommandValidator(false);
    validator.setApplication(application);

    final ConfigCommand command = new ConfigCommand();

    BindException exception = new BindException(command, "test");
    validator.validate(command, exception);

    // An empty command is valid
    assertEquals(0, exception.getAllErrors().size());

    // Invalid repository name
    command.setName("Illegal whitespace in name");
    command.setRepositoryUrl("svn://domain.com/svn/");
    command.setUserPassword("");
    command.setUserName("");
    validator.validate(command, exception);
    assertEquals(1, exception.getAllErrors().size());
    assertEquals("config.error.illegal-name", exception.getFieldError("name").getCode());
    exception = new BindException(command, "test");

    // Empty name is not ok
    command.setName("");
    command.setRepositoryUrl("svn://domain.com/svn/");
    command.setUserPassword("");
    command.setUserName("");
    validator.validate(command, exception);
    assertEquals(1, exception.getAllErrors().size());
    assertEquals("config.error.illegal-name", exception.getFieldError("name").getCode());
    exception = new BindException(command, "test");

    // Valid (typical) input
    command.setRepositoryUrl("svn://domain.com/svn/");
    command.setName("default");
    command.setUserPassword("");
    command.setUserName("");
    command.setCacheUserName("");
    command.setCacheUserPassword("");
    validator.validate(command, exception);
    assertEquals(0, exception.getAllErrors().size());

    // Valid input, spaces will be trimmed
    command.setRepositoryUrl(" svn://domain.com/svn/ ");
    command.setUserPassword("");
    command.setUserName("");
    validator.validate(command, exception);
    assertEquals(0, exception.getAllErrors().size());

    command.setUserPassword(null);
    command.setUserName(null);
    validator.validate(command, exception);
    assertEquals(0, exception.getAllErrors().size());

    command.setRepositoryUrl("");
    validator.validate(command, exception);
    assertEquals(1, exception.getAllErrors().size());
    assertEquals("config.error.illegal-url", exception.getFieldError("repositoryUrl").getCode());

    exception = new BindException(command, "test");
    command.setRepositoryUrl("notavalidurl");
    validator.validate(command, exception);
    assertEquals(1, exception.getAllErrors().size());
    assertEquals("config.error.illegal-url", exception.getFieldError("repositoryUrl").getCode());

    exception = new BindException(command, "test");
    command.setRepositoryUrl("svn://domain.com/svn/");
    command.setUserPassword("");
    command.setUserName("");
    validator.validate(command, exception);
    assertEquals(0, exception.getAllErrors().size());

    //if user based access is used, test connection uid and pwd can be supplied
    command.setRepositoryUrl("svn://domain.com/svn/");
    command.setName("default");
    command.setUserPassword("admin");
    command.setUserName("super-secret-pwd123");
    command.setAccessMethod(USER);
    command.setCacheUserName("");
    command.setCacheUserPassword("");
    validator.validate(command, exception);
    assertEquals(0, exception.getAllErrors().size());
  }

}
