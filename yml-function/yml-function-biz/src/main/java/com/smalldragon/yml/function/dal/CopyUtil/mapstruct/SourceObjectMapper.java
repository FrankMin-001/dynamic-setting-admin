package com.smalldragon.yml.function.dal.CopyUtil.mapstruct;

import com.smalldragon.yml.function.dal.CopyUtil.SourceObject;
import com.smalldragon.yml.function.dal.CopyUtil.TargetObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ObjectMapper {

    @Mapping(target = "isActive", source = "active")
    @Mapping(target = "updateTime", expression = "java(source.getUpdateTime() != null ? " +
            "source.getUpdateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null)")
    @Mapping(target = "primaryPhone", source = "phone")
    @Mapping(target = "secondaryPhone", source = "mobile")
    @Mapping(target = "primaryEmail", source = "email")
    @Mapping(target = "mainAddress", source = "fullAddress")
    @Mapping(target = "createdBy", source = "creator")
    @Mapping(target = "lastModifiedBy", source = "modifier")
    @Mapping(target = "clientIp", source = "ipAddress")
    @Mapping(target = "userTags", source = "tags")
    @Mapping(target = "publicNote", source = "remark")
    @Mapping(target = "verificationCode", source = "referenceCode")
    @Mapping(target = "profileImage", source = "logo")
    @Mapping(target = "loyaltyScore", expression = "java(source.getAverageRating() * 10)")
    @Mapping(target = "isPremiumMember", expression = "java(source.getActive() && source.getVisitCount() > 100)")
    @Mapping(target = "needsPasswordReset", constant = "false")
    @Mapping(target = "internalComment", constant = "MapStruct转换")
    @Mapping(target = "extendedProperties", ignore = true)
    TargetObject sourceToTarget(SourceObject source);
}
