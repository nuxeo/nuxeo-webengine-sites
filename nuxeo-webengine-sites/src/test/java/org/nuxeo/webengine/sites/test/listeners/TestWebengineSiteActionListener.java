/*
 * (C) Copyright 2009 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 */

package org.nuxeo.webengine.sites.test.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.IdUtils;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.TransactionalFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;
import org.nuxeo.webengine.sites.utils.SiteConstants;

/**
 * Unit tests for the SiteActionListener.
 *
 * @author rux
 */
@RunWith(FeaturesRunner.class)
@Features({ TransactionalFeature.class, CoreFeature.class })
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy("org.nuxeo.ecm.platform.webengine.sites.core.contrib")
@LocalDeploy("org.nuxeo.ecm.platform.webengine.sites.tests:OSGI-INF/ecm-types-contrib.xml")
public class TestWebengineSiteActionListener {

    @Inject
    protected CoreSession session;

    @Test
    public void testSiteActionListenerWorkspace() throws Exception {
        final String WorkspaceTitle = "Test Workspace";
        String id = IdUtils.generatePathSegment(WorkspaceTitle);
        DocumentModel workspace = session.createDocumentModel("/", id, "Workspace");
        workspace.setPropertyValue("dc:title", WorkspaceTitle);
        workspace = session.createDocument(workspace);
        workspace = session.saveDocument(workspace);
        session.save();
        // re-read the document model
        workspace = session.getDocument(workspace.getRef());
        String siteName = (String) workspace.getPropertyValue(SiteConstants.WEBCONTAINER_NAME);
        String siteUrl = (String) workspace.getPropertyValue(SiteConstants.WEBCONTAINER_URL);
        String documentTitle = workspace.getTitle();
        String documentName = workspace.getName();
        assertFalse("No name in site?", StringUtils.isBlank(siteName));
        assertFalse("No url in site?", StringUtils.isBlank(siteUrl));
        assertFalse("No name in document?", StringUtils.isBlank(documentName));
        assertFalse("No title in document?", StringUtils.isBlank(documentTitle));
        // name contains the title
        assertEquals("Name not valid for web container: " + siteName, documentTitle, siteName);
        // url contains the name
        assertEquals("URL not valid for web container: " + siteUrl, siteUrl,
                URIUtils.quoteURIPathComponent(documentName, false));
    }

    @Test
    public void testSiteActionListenerWebSite() throws Exception {
        final String webSiteTitle = "Test WebSite";
        String id = IdUtils.generatePathSegment(webSiteTitle);
        DocumentModel webSite = session.createDocumentModel("/", id, "WebSite");
        webSite.setPropertyValue("dc:title", webSiteTitle);
        webSite = session.createDocument(webSite);
        webSite = session.saveDocument(webSite);
        session.save();
        // re-read the document model
        webSite = session.getDocument(webSite.getRef());
        String siteName = (String) webSite.getPropertyValue(SiteConstants.WEBCONTAINER_NAME);
        String siteUrl = (String) webSite.getPropertyValue(SiteConstants.WEBCONTAINER_URL);
        String documentTitle = webSite.getTitle();
        String documentName = webSite.getName();
        assertFalse("No name in site?", StringUtils.isBlank(siteName));
        assertFalse("No url in site?", StringUtils.isBlank(siteUrl));
        assertFalse("No name in document?", StringUtils.isBlank(documentName));
        assertFalse("No title in document?", StringUtils.isBlank(documentTitle));
        // name contains the title
        assertEquals("Name not valid for web container: " + siteName, documentTitle, siteName);
        // url contains the name
        assertEquals("URL not valid for web container: " + siteUrl, siteUrl,
                URIUtils.quoteURIPathComponent(documentName, false));
    }

}
