package techtrek.domain.basicQuestion.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.status.CsCategory;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class GetCSCategoryDAO {

    // cs 불러오기
    public CsCategory exec(String selectedKeyword) {

        return CsCategory.fromKeyword(selectedKeyword)
                .orElseThrow(() -> new CustomException(ErrorCode.ENUM_CS_KEYWORD_NOT_FOUND));
    }
}
