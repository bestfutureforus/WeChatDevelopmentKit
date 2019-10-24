package me.chanjar.weixin.mp.bean.material;

import java.io.Serializable;

import lombok.Data;
import me.chanjar.weixin.mp.util.json.WxMpGsonBuilder;

@Data
public class WxMpMaterialUploadResult implements Serializable {
  private static final long serialVersionUID = -128818731449449537L;
  private String mediaId;
  private String url;
  private Integer errCode;
  private String errMsg;

  public static WxMpMaterialUploadResult fromJson(String json) {
    return WxMpGsonBuilder.create().fromJson(json, WxMpMaterialUploadResult.class);
  }

  @Override
  public String toString() {
    return WxMpGsonBuilder.create().toJson(this);
  }

}

