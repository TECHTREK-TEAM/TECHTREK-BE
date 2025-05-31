package techtrek.domain.sessionInfo.entity.status;

import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

public enum EnterpriseType {
    빅테크,
    핀테크,
    게임,
    전자_제조,
    이커머스,
    스타트업;

    // String을 enum객체로 변환
    public static EnterpriseType fromString(String value) {
        try {
            return EnterpriseType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException(ErrorCode.ENTERPRISE_TYPE_NOT_FOUND);
        }
    }
}
