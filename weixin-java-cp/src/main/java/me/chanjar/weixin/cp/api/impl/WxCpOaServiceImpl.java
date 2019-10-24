package me.chanjar.weixin.cp.api.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpOaService;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpApprovalDataResult;
import me.chanjar.weixin.cp.bean.WxCpCheckinData;
import me.chanjar.weixin.cp.bean.WxCpCheckinOption;
import me.chanjar.weixin.cp.bean.WxCpDialRecord;
import me.chanjar.weixin.cp.constant.WxCpApiPathConsts;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

import java.util.Date;
import java.util.List;

import static me.chanjar.weixin.cp.constant.WxCpApiPathConsts.Oa.*;

/**
 * @author Element
 * @date 2019-04-06 11:20
 */
@RequiredArgsConstructor
public class WxCpOaServiceImpl implements WxCpOaService {
  private final WxCpService mainService;

  @Override
  public List<WxCpCheckinData> getCheckinData(Integer openCheckinDataType, Date startTime, Date endTime,
                                              List<String> userIdList) throws WxErrorException {
    if (startTime == null || endTime == null) {
      throw new RuntimeException("starttime and endtime can't be null");
    }

    if (userIdList == null || userIdList.size() > 100) {
      throw new RuntimeException("用户列表不能为空，不超过100个，若用户超过100个，请分批获取");
    }

    long endtimestamp = endTime.getTime() / 1000L;
    long starttimestamp = startTime.getTime() / 1000L;

    if (endtimestamp - starttimestamp < 0 || endtimestamp - starttimestamp >= 30 * 24 * 60 * 60) {
      throw new RuntimeException("获取记录时间跨度不超过一个月");
    }

    JsonObject jsonObject = new JsonObject();
    JsonArray jsonArray = new JsonArray();

    jsonObject.addProperty("opencheckindatatype", openCheckinDataType);
    jsonObject.addProperty("starttime", starttimestamp);
    jsonObject.addProperty("endtime", endtimestamp);

    for (String userid : userIdList) {
      jsonArray.add(userid);
    }

    jsonObject.add("useridlist", jsonArray);

    String responseContent = this.mainService.post(this.mainService.getWxCpConfigStorage().getApiUrl(GET_CHECKIN_DATA), jsonObject.toString());
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return WxCpGsonBuilder.create()
      .fromJson(
        tmpJsonElement.getAsJsonObject().get("checkindata"),
        new TypeToken<List<WxCpCheckinData>>() {
        }.getType()
      );
  }

  @Override
  public List<WxCpCheckinOption> getCheckinOption(Date datetime, List<String> userIdList) throws WxErrorException {
    if (datetime == null) {
      throw new RuntimeException("datetime can't be null");
    }

    if (userIdList == null || userIdList.size() > 100) {
      throw new RuntimeException("用户列表不能为空，不超过100个，若用户超过100个，请分批获取");
    }

    JsonArray jsonArray = new JsonArray();
    for (String userid : userIdList) {
      jsonArray.add(userid);
    }

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("datetime", datetime.getTime() / 1000L);
    jsonObject.add("useridlist", jsonArray);

    String responseContent = this.mainService.post(this.mainService.getWxCpConfigStorage().getApiUrl(GET_CHECKIN_OPTION), jsonObject.toString());
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);

    return WxCpGsonBuilder.create()
      .fromJson(
        tmpJsonElement.getAsJsonObject().get("info"),
        new TypeToken<List<WxCpCheckinOption>>() {
        }.getType()
      );
  }

  @Override
  public WxCpApprovalDataResult getApprovalData(Date startTime, Date endTime, Long nextSpnum) throws WxErrorException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("starttime", startTime.getTime() / 1000L);
    jsonObject.addProperty("endtime", endTime.getTime() / 1000L);
    if (nextSpnum != null) {
      jsonObject.addProperty("next_spnum", nextSpnum);
    }

    String responseContent = this.mainService.post(this.mainService.getWxCpConfigStorage().getApiUrl(GET_APPROVAL_DATA), jsonObject.toString());
    return WxCpGsonBuilder.create().fromJson(responseContent, WxCpApprovalDataResult.class);
  }

  @Override
  public List<WxCpDialRecord> getDialRecord(Date startTime, Date endTime, Integer offset, Integer limit) throws WxErrorException {
    JsonObject jsonObject = new JsonObject();

    if (offset == null) {
      offset = 0;
    }

    if (limit == null || limit <= 0) {
      limit = 100;
    }

    jsonObject.addProperty("offset", offset);
    jsonObject.addProperty("limit", limit);

    if (startTime != null && endTime != null) {

      long endtimestamp = endTime.getTime() / 1000L;
      long starttimestamp = startTime.getTime() / 1000L;

      if (endtimestamp - starttimestamp < 0 || endtimestamp - starttimestamp >= 30 * 24 * 60 * 60) {
        throw new RuntimeException("受限于网络传输，起止时间的最大跨度为30天，如超过30天，则以结束时间为基准向前取30天进行查询");
      }

      jsonObject.addProperty("start_time", starttimestamp);
      jsonObject.addProperty("end_time", endtimestamp);
    }

    String responseContent = this.mainService.post(this.mainService.getWxCpConfigStorage().getApiUrl(GET_DIAL_RECORD), jsonObject.toString());
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);

    return WxCpGsonBuilder.create()
      .fromJson(
        tmpJsonElement.getAsJsonObject().get("record"),
        new TypeToken<List<WxCpDialRecord>>() {
        }.getType()
      );
  }
}
