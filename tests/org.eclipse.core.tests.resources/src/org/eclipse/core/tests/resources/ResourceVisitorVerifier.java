/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alexander Kurtakov <akurtako@redhat.com> - Bug 459343
 *******************************************************************************/
package org.eclipse.core.tests.resources;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.junit.Assert;

public class ResourceVisitorVerifier extends Assert implements IResourceVisitor {
	Set<IResource> expected;
	StringBuilder message;
	boolean success = true;
	boolean verified = false;

	public ResourceVisitorVerifier() {
		super();
		reset();
	}

	public void addExpected(IResource resource) {
		expected.add(resource);
	}

	public void addExpected(IResource[] resources) {
		for (IResource resource : resources) {
			expected.add(resource);
		}
	}

	@Override
	public boolean visit(IResource resource) {
		boolean included = expected.remove(resource);
		if (!included) {
			success = false;
			log(resource.getFullPath() + " was not expected.");
		}
		return true;
	}

	private void log(String text) {
		message.append("\n" + text);
	}

	private void verify() {
		if (verified) {
			return;
		}
		// Add messages for the resources which weren't visited but were expected.
		for (IResource resource : expected) {
			success = false;
			log(resource.getFullPath() + " was not visited.");
		}
		verified = true;
	}

	public boolean isValid() {
		verify();
		return success;
	}

	public String getMessage() {
		return message.toString();
	}

	public void reset() {
		expected = new HashSet<>();
		message = new StringBuilder();
		verified = false;
	}
}
