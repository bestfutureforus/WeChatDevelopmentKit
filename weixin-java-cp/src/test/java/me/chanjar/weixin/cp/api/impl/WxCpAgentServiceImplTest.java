package me.chanjar.weixin.cp.api.impl;

import com.google.inject.Inject;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.ApiTestModule;
import me.chanjar.weixin.cp.api.WxCpAgentService;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpAgent;
import me.chanjar.weixin.cp.constant.WxCpApiPathConsts;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;


/**
 * <pre>
 *  管理企业号应用-测试
 *  Created by huansinho on 2018/4/13.
 * </pre>
 *
 * @author <a href="https://github.com/huansinho">huansinho</a>
 */
@Guice(modules = ApiTestModule.class)
public class WxCpAgentServiceImplTest {
  @Inject
  private WxCpService wxCpService;

  @Test
  public void testGet() throws Exception {
    final Integer agentId = this.wxCpService.getWxCpConfigStorage().getAgentId();
    WxCpAgent wxCpAgent = this.wxCpService.getAgentService().get(agentId);

    assertThat(wxCpAgent.getAgentId()).isEqualTo(agentId);

    assertThat(wxCpAgent.getAllowUserInfos().getUsers().toArray()).isNotEmpty();
    assertThat(wxCpAgent.getAllowParties().getPartyIds().toArray()).isNotEmpty();
    assertThat(wxCpAgent.getAllowTags().getTagIds().toArray()).isNotEmpty();
  }

  @Test
  public void testSet() throws WxErrorException {
    final Integer agentId = this.wxCpService.getWxCpConfigStorage().getAgentId();

    this.wxCpService.getAgentService().set(WxCpAgent.builder()
      .description("abcddd")
      .logoMediaId("aaaaaaaaaaaaaa")
      .agentId(agentId)
      .build());
  }

  @Test
  public void testList() throws WxErrorException {
    List<WxCpAgent> list = this.wxCpService.getAgentService().list();

    assertThat(list).isNotEmpty();

    assertThat(list.get(0).getAgentId()).isNotNull();
    assertThat(list.get(0).getName()).isNotEmpty();
    assertThat(list.get(0).getSquareLogoUrl()).isNotEmpty();
  }

  public static class MockTest {
    private WxCpService wxService = mock(WxCpService.class);

    @Test
    public void testGet() throws Exception {
      String returnJson = "{\"errcode\": 0,\"errmsg\": \"ok\",\"agentid\": 9,\"name\": \"测试应用\",\"square_logo_url\": \"http://wx.qlogo.cn/mmhead/alksjf;lasdjf;lasjfuodiuj3rj2o34j/0\",\"description\": \"这是一个企业号应用\",\"allow_userinfos\": {\"user\": [{\"userid\": \"0009854\"}, {\"userid\": \"1723\"}, {\"userid\": \"5625\"}]},\"allow_partys\": {\"partyid\": [42762742]},\"allow_tags\": {\"tagid\": [23, 22, 35, 19, 32, 125, 133, 46, 150, 38, 183, 9, 7]},\"close\": 0,\"redirect_domain\": \"weixin.com.cn\",\"report_location_flag\": 0,\"isreportenter\": 0,\"home_url\": \"\"}";
      when(wxService.get(String.format(wxService.getWxCpConfigStorage().getApiUrl(WxCpApiPathConsts.Agent.AGENT_GET), 9), null)).thenReturn(returnJson);
      when(wxService.getAgentService()).thenReturn(new WxCpAgentServiceImpl(wxService));

      WxCpAgentService wxAgentService = this.wxService.getAgentService();
      WxCpAgent wxCpAgent = wxAgentService.get(9);

      assertEquals(9, wxCpAgent.getAgentId().intValue());

      assertEquals(new Integer[]{42762742}, wxCpAgent.getAllowParties().getPartyIds().toArray());

      assertEquals(new Integer[]{23, 22, 35, 19, 32, 125, 133, 46, 150, 38, 183, 9, 7}, wxCpAgent.getAllowTags().getTagIds().toArray());

    }

  }

}
