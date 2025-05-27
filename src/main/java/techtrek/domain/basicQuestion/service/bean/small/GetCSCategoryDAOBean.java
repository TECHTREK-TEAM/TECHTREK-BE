package techtrek.domain.basicQuestion.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.status.CSCategory;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class GetCSCategoryDAOBean {

    public CSCategory exec(List<String> keywords) {
        if (keywords.isEmpty()) {
            throw new CustomException(ErrorCode.ENUM_ENTERPRISE_KEYWORD_NOT_FOUND);
        }

        String selectedKeyword = keywords.get(new Random().nextInt(keywords.size()));

        return CSCategory.fromKeyword(selectedKeyword)
                .orElseThrow(() -> new CustomException(ErrorCode.ENUM_CS_KEYWORD_NOT_FOUND));
    }
}
