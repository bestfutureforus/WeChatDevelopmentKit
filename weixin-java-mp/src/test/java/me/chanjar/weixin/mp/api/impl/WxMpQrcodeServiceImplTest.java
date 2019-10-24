package me.chanjar.weixin.mp.api.impl;

import com.google.inject.Inject;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.test.ApiTestModule;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.*;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

/**
 * 测试用户相关的接口
 *
 * @author chanjarster
 */
@Test(groups = "qrCodeAPI")
@Guice(modules = ApiTestModule.class)
public class WxMpQrcodeServiceImplTest {
  @Inject
  protected WxMpService wxService;

  @DataProvider
  public Object[][] sceneIds() {
    return new Object[][]{{-1}, {0}, {1}, {200000}};
  }

  @DataProvider
  public Object[][] sceneStrs() {
    return new Object[][]{{null}, {""}, {"test"}, {RandomStringUtils.randomAlphanumeric(100)}};
  }

  @Test(dataProvider = "sceneIds")
  public void testQrCodeCreateTmpTicket(int sceneId) throws WxErrorException {
    WxMpQrCodeTicket ticket = this.wxService.getQrcodeService().qrCodeCreateTmpTicket(sceneId, null);
    Assert.assertNotNull(ticket.getUrl());
    Assert.assertNotNull(ticket.getTicket());
    Assert.assertTrue(ticket.getExpireSeconds() != -1);
    System.out.println(ticket);
  }


  @Test(dataProvider = "sceneStrs")
  public void testQrCodeCreateTmpTicketWithSceneStr(String sceneStr) throws WxErrorException {
    WxMpQrCodeTicket ticket = this.wxService.getQrcodeService().qrCodeCreateTmpTicket(sceneStr, null);
    Assert.assertNotNull(ticket.getUrl());
    Assert.assertNotNull(ticket.getTicket());
    Assert.assertTrue(ticket.getExpireSeconds() != -1);
    System.out.println(ticket);
  }

  @Test(dataProvider = "sceneIds")
  public void testQrCodeCreateLastTicket(int sceneId) throws WxErrorException {
    WxMpQrCodeTicket ticket = this.wxService.getQrcodeService().qrCodeCreateLastTicket(sceneId);
    Assert.assertNotNull(ticket.getUrl());
    Assert.assertNotNull(ticket.getTicket());
    Assert.assertTrue(ticket.getExpireSeconds() == -1);
    System.out.println(ticket);
  }

  public void testQrCodePicture() throws WxErrorException {
    WxMpQrCodeTicket ticket = this.wxService.getQrcodeService().qrCodeCreateLastTicket(1);
    File file = this.wxService.getQrcodeService().qrCodePicture(ticket);
    Assert.assertNotNull(file);
    System.out.println(file.getAbsolutePath());

    try {
      FileUtils.copyFile(file,new File("d:\\t.jpg"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void testQrCodePictureUrl() throws WxErrorException {
    WxMpQrCodeTicket ticket = this.wxService.getQrcodeService().qrCodeCreateLastTicket(1);
    String url = this.wxService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
    Assert.assertNotNull(url);
    System.out.println(url);
  }

}
