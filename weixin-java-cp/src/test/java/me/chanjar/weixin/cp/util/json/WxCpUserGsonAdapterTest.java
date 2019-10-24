package me.chanjar.weixin.cp.util.json;

import org.testng.annotations.*;

import me.chanjar.weixin.cp.bean.WxCpUser;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <pre>
 *
 * Created by Binary Wang on 2018/9/16.
 * </pre>
 *
 * @author BinaryWang
 */
public class WxCpUserGsonAdapterTest {

  @Test
  public void testDeserialize() {
    final String userJson = "{\n" +
      "    \"errcode\": 0,\n" +
      "    \"errmsg\": \"ok\",\n" +
      "    \"userid\": \"zhangsan\",\n" +
      "    \"name\": \"李四\",\n" +
      "    \"department\": [1, 2],\n" +
      "    \"order\": [1, 2],\n" +
      "    \"position\": \"后台工程师\",\n" +
      "    \"mobile\": \"15913215421\",\n" +
      "    \"gender\": \"1\",\n" +
      "    \"email\": \"zhangsan@gzdev.com\",\n" +
      "    \"isleader\": 1,\n" +
      "    \"avatar\": \"http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0\",\n" +
      "    \"telephone\": \"020-123456\",\n" +
      "    \"enable\": 1,\n" +
      "    \"alias\": \"jackzhang\",\n" +
      "    \"extattr\": {\n" +
      "        \"attrs\": [{\n" +
      "            \"name\": \"爱好\",\n" +
      "            \"value\": \"旅游\"\n" +
      "        }, {\n" +
      "            \"name\": \"卡号\",\n" +
      "            \"value\": \"1234567234\"\n" +
      "        }]\n" +
      "    },\n" +
      "    \"status\": 1,\n" +
      "    \"qr_code\": \"https://open.work.weixin.qq.com/wwopen/userQRCode?vcode=xxx\",\n" +
      "    \"external_profile\": {\n" +
      "        \"external_attr\": [{\n" +
      "                \"type\": 0,\n" +
      "                \"name\": \"文本名称\",\n" +
      "                \"text\": {\n" +
      "                    \"value\": \"文本\"\n" +
      "                }\n" +
      "            },\n" +
      "            {\n" +
      "                \"type\": 1,\n" +
      "                \"name\": \"网页名称\",\n" +
      "                \"web\": {\n" +
      "                    \"url\": \"http://www.test.com\",\n" +
      "                    \"title\": \"标题\"\n" +
      "                }\n" +
      "            },\n" +
      "            {\n" +
      "                \"type\": 2,\n" +
      "                \"name\": \"测试app\",\n" +
      "                \"miniprogram\": {\n" +
      "                    \"appid\": \"wx8bd80126147df384\",\n" +
      "                    \"pagepath\": \"/index\",\n" +
      "                    \"title\": \"my miniprogram\"\n" +
      "                }\n" +
      "            }\n" +
      "        ]\n" +
      "    }\n" +
      "}";

    final WxCpUser user = WxCpUser.fromJson(userJson);
    assertThat(user).isNotNull();

    assertThat(user.getOrders()).isNotEmpty();
    assertThat(user.getOrders().length).isEqualTo(2);
    assertThat(user.getOrders()[0]).isEqualTo(1);
    assertThat(user.getOrders()[1]).isEqualTo(2);


    assertThat(user.getExternalAttrs()).isNotEmpty();

    final WxCpUser.ExternalAttribute externalAttr1 = user.getExternalAttrs().get(0);
    assertThat(externalAttr1.getType()).isEqualTo(0);
    assertThat(externalAttr1.getName()).isEqualTo("文本名称");
    assertThat(externalAttr1.getValue()).isEqualTo("文本");

    final WxCpUser.ExternalAttribute externalAttr2 = user.getExternalAttrs().get(1);
    assertThat(externalAttr2.getType()).isEqualTo(1);
    assertThat(externalAttr2.getName()).isEqualTo("网页名称");
    assertThat(externalAttr2.getUrl()).isEqualTo("http://www.test.com");
    assertThat(externalAttr2.getTitle()).isEqualTo("标题");

    final WxCpUser.ExternalAttribute externalAttr3 = user.getExternalAttrs().get(2);
    assertThat(externalAttr3.getType()).isEqualTo(2);
    assertThat(externalAttr3.getName()).isEqualTo("测试app");
    assertThat(externalAttr3.getAppid()).isEqualTo("wx8bd80126147df384");
    assertThat(externalAttr3.getPagePath()).isEqualTo("/index");
    assertThat(externalAttr3.getTitle()).isEqualTo("my miniprogram");
  }

  @Test
  public void testSerialize() {
    WxCpUser user = new WxCpUser();
    user.setOrders(new Integer[]{1, 2});
    user.addExternalAttr(WxCpUser.ExternalAttribute.builder()
      .type(0)
      .name("文本名称")
      .value("文本")
      .build());
    user.addExternalAttr(WxCpUser.ExternalAttribute.builder()
      .type(1)
      .name("网页名称")
      .url("http://www.test.com")
      .title("标题")
      .build());
    user.addExternalAttr(WxCpUser.ExternalAttribute.builder()
      .type(2)
      .name("测试app")
      .appid("wx8bd80126147df384")
      .pagePath("/index")
      .title("my miniprogram")
      .build());

    assertThat(user.toJson()).isEqualTo("{\"order\":[1,2],\"external_profile\":{\"external_attr\":" +
      "[{\"type\":0,\"name\":\"文本名称\",\"text\":{\"value\":\"文本\"}}," +
      "{\"type\":1,\"name\":\"网页名称\",\"web\":{\"url\":\"http://www.test.com\",\"title\":\"标题\"}}," +
      "{\"type\":2,\"name\":\"测试app\"," +
      "\"miniprogram\":{\"appid\":\"wx8bd80126147df384\",\"pagepath\":\"/index\",\"title\":\"my miniprogram\"}}]}}");
  }
}
