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

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.sventon.model.Revision;

/**
 * BaseCommandValidator.
 *
 * @author patrik@sventon.org
 */
public final class BaseCommandValidator implements Validator {

  @Override
  public boolean supports(final Class clazz) {
    return BaseCommand.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    final BaseCommand command = (BaseCommand) target;

    if (Revision.UNDEFINED.equals(command.getRevision())) {
      command.setRevision(Revision.HEAD);
      errors.rejectValue("revision", "browse.error.illegal-revision");
    }
  }
}
