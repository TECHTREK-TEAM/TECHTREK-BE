package techtrek.domain.basicQuestion.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.status.CSCategory;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class GetCSCategoryDAOBean {

    // cs 불러오기
    public CSCategory exec(String selectedKeyword) {

        return CSCategory.fromKeyword(selectedKeyword)
                .orElseThrow(() -> new CustomException(ErrorCode.ENUM_CS_KEYWORD_NOT_FOUND));
    }
}
