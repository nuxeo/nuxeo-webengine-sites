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

package org.nuxeo.webengine.sites.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.IdUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.TransactionalFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.webengine.sites.utils.SiteConstants;
import org.nuxeo.webengine.sites.utils.SiteUtils;

/**
 * Unit tests for the utils methods.
 *
 * @author rux
 */
@RunWith(FeaturesRunner.class)
@Features({ TransactionalFeature.class, CoreFeature.class })
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({ "org.nuxeo.ecm.webengine.base", //
        "org.nuxeo.ecm.platform.webengine.sites.core.contrib", //
        "org.nuxeo.ecm.platform.webengine.sites.tests", //
})
public class TestWebengineSiteUtils {

    @Inject
    protected CoreSession session;

    private final String webSiteTitle = "Test Web Site";

    private DocumentModel webSite;

    protected void entryTest() throws Exception {
        String id = IdUtils.generatePathSegment(webSiteTitle);
        webSite = session.createDocumentModel("/", id, "WebSite");
        webSite.setPropertyValue("dc:title", webSiteTitle);
        webSite.setPropertyValue("webcontainer:isWebContainer", true);
        webSite = session.createDocument(webSite);
        webSite = session.saveDocument(webSite);
        session.save();
        // re-read the document model
        webSite = session.getDocument(webSite.getRef());

    }

    @Test
    public void testContextualLinks() throws Exception {

        entryTest();
        DocumentModel contextualLink1 = session.createDocumentModel(webSite.getPathAsString(), "cl1",
                SiteConstants.CONTEXTUAL_LINK);
        contextualLink1.setPropertyValue("dc:title", "CL1");
        contextualLink1.setPropertyValue("dc:description", "CL1 description");
        contextualLink1.setPropertyValue("clink:link", "http://link1");
        contextualLink1 = session.createDocument(contextualLink1);
        contextualLink1 = session.saveDocument(contextualLink1);
        DocumentModel contextualLink2 = session.createDocumentModel(webSite.getPathAsString(), "cl2",
                SiteConstants.CONTEXTUAL_LINK);
        contextualLink2.setPropertyValue("dc:title", "CL2");
        contextualLink2.setPropertyValue("dc:description", "CL2 description");
        contextualLink2.setPropertyValue("clink:link", "http://link2");
        contextualLink2 = session.createDocument(contextualLink2);
        contextualLink2 = session.saveDocument(contextualLink2);

        session.save();

        DocumentModelList cLinks = session.getChildren(webSite.getRef(), SiteConstants.CONTEXTUAL_LINK);
        assertEquals("Don't have 2 links?", 2, cLinks.size());
        for (DocumentModel linkObject : cLinks) {
            String linkTitle = SiteUtils.getString(linkObject, "dc:title");
            String description = SiteUtils.getString(linkObject, "dc:description");
            String link = SiteUtils.getString(linkObject, "clink:link");

            assertTrue("Title not correct: " + linkTitle, "CL1".equals(linkTitle) || "CL2".equals(linkTitle));
            assertTrue("Description not correct: " + description, "CL1 description".equals(description)
                    || "CL2 description".equals(description));
            assertTrue("Link not correct: " + description, "http://link1".equals(link) || "http://link2".equals(link));
        }
    }

}
