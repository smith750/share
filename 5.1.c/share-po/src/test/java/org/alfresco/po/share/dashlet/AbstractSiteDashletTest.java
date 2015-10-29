package org.alfresco.po.share.dashlet;

import java.io.File;

import org.alfresco.po.share.DashBoardPage;
import org.alfresco.po.share.site.SiteDashboardPage;
import org.alfresco.po.share.site.SiteFinderPage;
import org.alfresco.po.share.site.SitePage;
import org.alfresco.po.share.site.UploadFilePage;
import org.alfresco.po.share.site.document.AbstractDocumentTest;
import org.alfresco.po.share.site.document.DocumentDetailsPage;
import org.alfresco.po.share.site.document.DocumentLibraryPage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract class with common method used in dashlet
 * based test cases.
 * @author Michael Suzuki
 *
 */
public class AbstractSiteDashletTest extends AbstractDocumentTest
{
    private static Log logger = LogFactory.getLog(AbstractSiteDashletTest.class);
    protected String siteName;
    protected String fileName;
    protected SiteDashboardPage siteDashBoard;
    
    /**
     * Creates a site and uploads a document to the site.
     * @throws Exception if error
     */
    protected void uploadDocument()throws Exception
    {
        try
        {
             File file = siteUtil.prepareFile();
             fileName = file.getName();
             loginAs(username, password);
             siteUtil.createSite(driver, username, password,siteName, 
                         "description",
                         "Public");
             SitePage site = resolvePage(driver).render();
             DocumentLibraryPage docPage = site.getSiteNav().selectDocumentLibrary().render();
             //DocumentLibraryPage docPage = getDocumentLibraryPage(siteName).render();
             UploadFilePage upLoadPage = docPage.getNavigation().selectFileUpload().render();
             docPage = upLoadPage.uploadFile(file.getCanonicalPath()).render();
             DocumentDetailsPage dd = docPage.selectFile(fileName).render();
             dd.selectLike();
        }
        catch (Throwable pe)
        {
            saveScreenShot("uploadDodDashlet");
            logger.error("Problem deleting site", pe);
        }
    }
   
    protected void navigateToSiteDashboard()
    {
        if(logger.isTraceEnabled())
        {
            logger.trace("navigate to " + shareUrl);
        }
        driver.navigate().to(shareUrl);
        DashBoardPage boardPage = resolvePage(driver).render();
        SiteFinderPage finderPage = boardPage.getNav().selectSearchForSites().render();
        finderPage = finderPage.searchForSite(siteName).render();
        finderPage = siteUtil.siteSearchRetry(driver, finderPage, siteName);
        siteDashBoard = finderPage.selectSite(siteName).render();
    }
    
    /**
    @AfterClass(alwaysRun=true)
    public void deleteSite()
    {
        try
        {
            if(driver != null && !StringUtils.isEmpty(siteName))
            {
                siteUtil.deleteSite(username, password, siteName);
                closeWebDriver();
            }
        }
        catch (Exception e)
        {
            logger.error("Problem deleting site", e);
        }
    }
    **/
}